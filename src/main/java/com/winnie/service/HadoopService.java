package com.winnie.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winnie.domain.Movie;
import com.winnie.domain.MovieList;
import com.winnie.domain.Score;
import com.winnie.hadoop.step1;
import com.winnie.hadoop.step2;
import com.winnie.hadoop.step3;
import com.winnie.hadoop.step4;
import com.winnie.hadoop.step5;
import com.winnie.repository.ScoreRepository;
import com.winnie.domain.SortMovie;

/**
* Created by彭文钰
* 2018年5月20日 下午9:29:56
*/
@Service
public class HadoopService {
	
	
	@Autowired
	ScoreRepository scoreRepository;
	
	@Autowired
	MovieSearch movieSearch;
	
	public List<MovieList> movieRecommend(Integer userId) throws Exception {
		List<Score> scores = new ArrayList<Score>();
		scores = scoreRepository.findAll();
		
		File file = new File("E://stud/hadoopMovie/hadoop1.txt");
		FileWriter fw = new FileWriter(file);
		
		for(int i = 0; i<scores.size();i++) {
			fw.write(scores.get(i).getUserId()+","+scores.get(i).getDid()+","+
		scores.get(i).getScore()+"\n");
		}
		fw.close();
		AddFileToHdfs();
		hadoopJobRun();
		DownloadFileToLocal();
		
		List<String> movieList = ReadFile(userId); /*ReadFileMahout(userId);*/
		List<MovieList> recomMovie = new ArrayList<MovieList>();
		List<SortMovie<MovieList>> reCoMovies = new ArrayList<SortMovie<MovieList>>();
		for (String mstring : movieList) {
			Movie m = new Movie();
			MovieList mList =new MovieList();
			SortMovie<MovieList> mLL = new SortMovie<MovieList>();
			String jsons = movieSearch.doGet("https://api.douban.com/v2/movie/subject/"+mstring.split("_")[0]);
			m = movieSearch.toInfo(jsons);
			mList.setCasts(m.getCasts());
			mList.setDirector(m.getDirector());
			mList.setImg(m.getImg());
			mList.setMovieName(m.getName());
			mList.setDid(m.getDid());
			mList.setGenres(m.getGenres());
			mList.setYear(m.getYear());
			mLL.setRecomMovie(mList);
			mLL.setSimilarity(Float.parseFloat(mstring.split("_")[1]));
			reCoMovies.add(mLL);
		}
		sort(reCoMovies);	
		int leng = 0;
		if(reCoMovies.size()<10) {
			leng = reCoMovies.size();
		}else {
			leng = 10;
		}
		for(int i=0;i<leng;i++) {
			recomMovie.add(reCoMovies.get(i).getRecomMovie());
		}
		return recomMovie;
	}
	
    public void hadoopJobRun() {
		
		final Logger logger = LoggerFactory.getLogger(HadoopService.class);
		
		int status1 = -1;
		int status2 = -1;
		int status3 = -1;
		int status4 = -1;
		int status5 = -1;
		
		status1 = new step1().run();
		if(status1 == 1) {
			logger.info("Step1运行成功，开始运行step2...");
			status2 = new step2().run();
		}else {
			logger.error("Step1运行失败...");
		}
		if(status2 == 1) {
			logger.info("Step2运行成功，开始运行step3...");
			status3 = new step3().run();
		}else {
			logger.error("Step2运行失败...");
		}
		if(status3 == 1) {
			logger.info("Step3运行成功，开始运行step4...");
			status4 = new step4().run();
		}else {
			logger.error("Step3运行失败...");
		}
		if(status4 == 1) {
			logger.info("Step4运行成功，开始运行step5...");
			status5 = new step5().run();
		}else {
			logger.error("Step4运行失败...");
		}
		if (status5 == 1) {
			logger.info("Step5运行成功，程序结束！");
		} else {
			logger.error("Step5运行失败...");
		}
	}
	
	public void AddFileToHdfs() throws Exception {
        // 要上传的文件所在的本地路径
        Path src = new Path("E://stud/hadoopMovie/hadoop1.txt");
        // 要上传到hdfs的目标路径
        Path dst = new Path("hdfs://192.168.190.137:9000/Movie/step1_input/");
        
        Configuration conf = new Configuration();
        FileSystem fs = dst.getFileSystem(conf);
        fs.copyFromLocalFile(src, dst);
        fs.close();
    }

    public void DownloadFileToLocal() throws IllegalArgumentException, IOException {
    	Path dst = new Path("hdfs://192.168.190.137:9000/Movie/step5_output/part-r-00000");
    	Configuration conf = new Configuration();
        FileSystem fs = dst.getFileSystem(conf);
        fs.copyToLocalFile(dst, new Path("E://stud/hadoopMovie/"));
        fs.close();
    }
    
    public List<String> ReadFile(Integer userId) throws IOException{
    	File outFile = new File("E://stud/hadoopMovie/part-r-00000");
    	List<String> movieList = new ArrayList<String>();
		if(outFile.isFile() && outFile.exists()) {
			InputStreamReader read = new InputStreamReader(  new FileInputStream(outFile),"UTF-8");
			BufferedReader br = new BufferedReader(read);
			
			String line =null;
		    List<String> lineList = new ArrayList<String>();
		    
			while((line = br.readLine())!=null) {
				lineList.add(line);
				//System.out.println(line);
			}
			
			for(int i= 0 ;i< lineList.size();i++) {
				if(lineList.get(i).split("\t")[0].equals(String.valueOf(userId))) {
					
					for(String qString : lineList.get(i).split("\t")[1].split(",")) {
						movieList.add(qString);
					}
					break;
				}
			}
			read.close();
			br.close();
		}
		return movieList;
    }
    
    /**
	 *用mahout实现推荐算法
	 */
    public List<String> ReadFileMahout(Integer userId) throws IOException{
    	
    	List<String> movieList = new ArrayList<String>();
    	
    	try {
    		// 1,构建模型
    		DataModel dataModel = new FileDataModel(new File("E://stud/hadoopMovie/hadoop1.txt"));
    		//2,计算相似度
    		UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
    		//3,查找K近邻
    		UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(2, userSimilarity, dataModel);
    		//4,构造推荐引擎
    		Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);
    		//为用户i推荐2个item
    		Long id = new Long((long)userId);
    		List <RecommendedItem> recommendations = recommender.recommend(id,10);
    		System.out.println(recommendations.size());
    		for (RecommendedItem recommendation:recommendations){
    			System.out.println(recommendation.getItemID()+"_"+recommendation.getItemID());
    		     movieList.add(recommendation.getItemID()+"_"+recommendation.getValue());
    		}
    		return movieList;
    	}catch(IOException e){
    		 e.printStackTrace();
    	}catch(TasteException e){
    		 e.printStackTrace();
    	}
		return movieList;	
    }
    
        /**
         * 希尔排序 针对有序序列在插入时采用交换法
         * @param arr
         */
     public static void sort(List<SortMovie<MovieList>> reCoMovies){
         //增量gap，并逐步缩小增量
        for(int gap=reCoMovies.size()/2;gap>0;gap/=2){
            //从第gap个元素，逐个对其所在组进行直接插入排序操作
            for(int i=gap;i<reCoMovies.size();i++){
                int j = i;
                while(j-gap>=0 && reCoMovies.get(j).getSimilarity()>reCoMovies.get(j-gap).getSimilarity()){
                    //插入排序采用交换法
                	SortMovie<MovieList> m1 = new SortMovie<MovieList>();
                	SortMovie<MovieList> m2 = new SortMovie<MovieList>();
                	m1 = reCoMovies.get(j);
                	m2 = reCoMovies.get(j-gap);
                	reCoMovies.set(j-gap, m1);
                	reCoMovies.set(j, m2);
                    j-=gap;
                }
            }
        }
     }
}
