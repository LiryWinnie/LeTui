package com.winnie.hadoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
/**
* Created by彭文钰
* 2018年5月21日 上午12:00:05
*/
public class step5 {
	
	//输入文件的相对路径
	private static String inPath = "/Movie/step4_output";
	//输出文件的相对路径
	private static String outPath = "/Movie/step5_output";
	//将step1输出的转置矩阵作为全局缓存
	private static String cache = "/Movie/step1_output/part-r-00000";
	//hdfs地址
	private static String hdfs = "hdfs://192.168.190.148:9000";

	public static class Mapper5 extends Mapper<LongWritable, Text, Text, Text>{
		
		private Text outKey = new Text();
		private Text outValue = new Text();
		
		private List<String> cacheList = new ArrayList<String>();
		
		//在map方法之前执行，且只执行一次
			@Override
			protected void setup(Context context) throws IOException, InterruptedException {
				super.setup(context);
				//通过输入流将全局缓存变量中的 右侧矩阵 读入List<String>中
				Configuration conf = context.getConfiguration();
			      
			    FileSystem fs = FileSystem.get(conf);
				
				FSDataInputStream in = fs.open(new Path("/Movie/step1_output/part-r-00000"));
				
		        //FileReader fr = new FileReader("itemUserScore");
		       
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
					
				//每一行的格式是： 行 tab 列_值,列_值,列_值,列_值
				String line = null;
				while((line = br.readLine())!=null) {
					cacheList.add(line);
				}
				in.close();
				br.close();
			}
			
			@Override
			protected void map(LongWritable key, Text value, Context context)
					throws IOException, InterruptedException {
				String item_matrix1 = value.toString().split("\t")[0];
				String[] user_score_array_matrix1 = value.toString().split("\t")[1].split(",");
				
				for (String line : cacheList) {
					String item_matrix2 = line.toString().split("\t")[0];
					String[] user_score_array_matrix2 = line.toString().split("\t")[1].split(",");
					
					//如果行首的物品ID相同
					if (item_matrix1.equals(item_matrix2)) {
						//遍历matrix1的列
						for (String user_score_matrix1 : user_score_array_matrix1) {
							String user_matrix1 = user_score_matrix1.split("_")[0];
							String score_matrix1 = user_score_matrix1.split("_")[1];
							
							boolean flag = false;
							//遍历matrix2的列
							for (String user_score_matrix2 : user_score_array_matrix2) {
								String user_matrix2 = user_score_matrix2.split("_")[0];
								if(user_matrix1.equals(user_matrix2)) {
									flag = true;
								}
							}
							
							if(flag == false) {
								outKey.set(user_matrix1);
								outValue.set(item_matrix1 + "_" + score_matrix1);
								
								context.write(outKey, outValue);
							}
						}
					}
				}
			}
	}
	
	public static class Reducer5 extends Reducer<Text, Text, Text, Text>{
		
		private Text outKey = new Text();
		
		private Text outValue = new Text();

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			StringBuilder sb = new StringBuilder();
			for (Text value : values) {
				sb.append(value + ",");
			}
			
			String line = null;
			if(sb.toString().endsWith(",")) {
				line=sb.substring(0, sb.length() - 1);
			}
			outKey.set(key);
			outValue.set(line);
			
			context.write(outKey, outValue);
		}

	}
	
	public int run() {
		try {
		   //创建job配置类
		   Configuration conf = new Configuration();
		   //配置hdfs的地址
		   conf.set("fs.defaultFS", hdfs);
		   //创建一个Job实例
		   Job job = Job.getInstance(conf,"step5");
		   //添加分布式缓存文件夹
		   job.addCacheArchive(new URI(cache+"#itemUserScore3"));
		   
		   //设置Job的主类
		   job.setJarByClass(step5.class);
		   //设置Job的Mapper类和Reducer类
		   job.setMapperClass(Mapper5.class);
		   job.setReducerClass(Reducer5.class);
		   
		   //设置Mapper输出的类型
		   job.setMapOutputKeyClass(Text.class);
		   job.setMapOutputValueClass(Text.class);
		   
		   //设置Reducer输出的类型
		   job.setOutputKeyClass(Text.class);
		   job.setOutputValueClass(Text.class);
		   
		   FileSystem fs = FileSystem.get(conf);
		   //设置输入输出路径
		   Path inputPath = new Path(inPath);
		   if (fs.exists(inputPath)) {
			   FileInputFormat.addInputPath(job,inputPath);
		   }
		   
		   Path outputPath = new Path(outPath);
		   fs.delete(outputPath,true);
		   
		   org.apache.hadoop.mapreduce.lib.output.FileOutputFormat.setOutputPath(job, outputPath);
		   
		   return job.waitForCompletion(true) ? 1 : -1;
		   
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
