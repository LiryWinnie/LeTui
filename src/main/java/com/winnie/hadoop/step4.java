package com.winnie.hadoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
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
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/**
* Created by彭文钰
* 2018年5月20日 下午11:59:47
*/
public class step4 {

	//输入文件的相对路径
	private static String inPath = "/Movie/step2_output";
	//输出文件的相对路径
	private static String outPath = "/Movie/step4_output";
	//将step1输出的转置矩阵作为全局缓存
	private static String cache = "/Movie/step3_output/part-r-00000";
	//hdfs地址
	private static String hdfs = "hdfs://192.168.190.148:9000";
	
	public static class Mapper4 extends Mapper<LongWritable, Text, Text, Text>{

		private Text outKey = new Text();
		private Text outValue = new Text();
		
		private List<String> cacheList = new ArrayList<String>();
		
		private DecimalFormat df = new DecimalFormat("0.00");
		//在map方法之前执行，且只执行一次
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);
			//通过输入流将全局缓存变量中的 右侧矩阵 读入List<String>中
			Configuration conf = context.getConfiguration();
		      
			FileSystem fs = FileSystem.get(conf);
			
			FSDataInputStream in = fs.open(new Path("/Movie/step3_output/part-r-00000"));
	        //FileReader fr = new FileReader(itermOccurrenceMatrix);
		
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
			//每一行的格式是： 行 tab 列_值,列_值,列_值,列_值
			String line = null;
			while((line = br.readLine())!=null) {
				cacheList.add(line);
			}
			in.close();
			br.close();
		}
		
		/**
		 *@param key: 行号
		 *@param value: 行 tab 列_值,列_值,列_值,列_值
		 */
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			//行
			String row_matrix1 = value.toString().split("\t")[0];
			//行_值（数组）
			String[] column_value_array_matrix1 = value.toString().split("\t")[1].split(",");
			
			for (String line : cacheList) {
				//右侧矩阵的行 line
				//格式 ：行 tab 列_值,列_值,列_值,列_值
				String row_matrix2 = line.toString().split("\t")[0];
				String[] column_value_array_matrix2 = line.toString().split("\t")[1].split(",");
				
				//矩阵两行相乘得到的结果
				double result = 0;
				//遍历左边矩阵的第一行的每一列
				for (String column_value_matrix1 : column_value_array_matrix1) {
					String column_matrix1 = column_value_matrix1.split("_")[0];
					String value_matrix1 = column_value_matrix1.split("_")[1];
					
					//遍历右边矩阵的每一行的每一列
					for (String column_value_matrix2 : column_value_array_matrix2) {
						if(column_value_matrix2.startsWith(column_matrix1+"_")) {
							String value_matrix2 = column_value_matrix2.split("_")[1];
							//将两列的值相乘，并累加
							result += Double.valueOf(value_matrix1)*Double.valueOf(value_matrix2);
						}
					}
				}
				
				if( result == 0) {
					continue;
				}
				//result是结果矩阵中的某元素，坐标为   行：row_matrix1 值：row_matrix2(因为右矩阵已经转置)；
				outKey.set(row_matrix1);
				outValue.set(row_matrix2+"_"+df.format(result));
				
				//输出格式 key:行  value: 行_值
				context.write(outKey, outValue);
			}
			
			
		}
	}
	
	public static class Reducer4 extends Reducer<Text, Text, Text, Text>{

		private Text outKey = new Text();
		
		private Text outValue = new Text();
		
		//key:列号   value：[行号_值,行号_值,行号_值...]
		@Override
		protected void reduce(Text key, Iterable<Text> values,Context context)
				throws IOException, InterruptedException {
			StringBuilder sb = new StringBuilder();
			for (Text value : values) {
				sb.append(value+",");
			}
			
			String result = null;
			if(sb.toString().endsWith(",")) {
				result = sb.substring(0,sb.length() - 1);
			}
			
			//outKey:行  outValue：列_值,列_值,列_值,列_值
			outKey.set(key);
			outValue.set(result);
			
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
		   Job job = Job.getInstance(conf,"step4");
		   //添加分布式缓存文件夹
		   job.addCacheArchive(new URI(cache+"#itemUserScore2"));
		   
		   //设置Job的主类
		   job.setJarByClass(step4.class);
		   //设置Job的Mapper类和Reducer类
		   job.setMapperClass(Mapper4.class);
		   job.setReducerClass(Reducer4.class);
		   
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
