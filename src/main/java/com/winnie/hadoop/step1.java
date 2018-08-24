package com.winnie.hadoop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
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
* 2018年5月20日 下午5:03:46
*/
public class step1 {
	
	//输入文件的相对路径
	private static String inPath = "/Movie/step1_input/hadoop1.txt";
	//输出文件的相对路径
	private static String outPath = "/Movie/step1_output";
	//hdfs地址
	private static String hdfs = "hdfs://192.168.190.148:9000";

	public static class Mapper1 extends Mapper<LongWritable, Text, Text, Text>{

		private Text outKey = new Text();
		
		private Text outValue = new Text();
		
		/**
		 * key:1   2 .....
		 * value: A,1,1  C,3,5 .....
		 */
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] values = value.toString().split(",");
			String userID = values[0];
			String itemID = values[1];
			String score = values[2];
			
			outKey.set(itemID);
			outValue.set(userID+"_"+score);
			
			context.write(outKey, outValue);
		}

	}
	
	public static class Reducer1 extends Reducer<Text, Text, Text, Text>{

		private Text outKey = new Text();
		
		private Text outValue = new Text();
		
		@Override
		protected void reduce(Text key, Iterable<Text> values,Context context)
				throws IOException, InterruptedException {
			String itemID = key.toString();
			//<userID, score>
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for(Text value:values) {
				String userID = value.toString().split("_")[0];
				String score = value.toString().split("_")[1];
				if(map.get(userID)==null) {
					 map.put(userID,Integer.valueOf(score));
				}else {
					Integer preScore = map.get(userID);
					map.put(userID, preScore+Integer.valueOf(score));
				}
			}
			
			StringBuilder sBuilder = new StringBuilder();
			for(Map.Entry<String, Integer> entry : map.entrySet()) {
				String userID = entry.getKey();
				String score = String.valueOf(entry.getValue());
				sBuilder.append(userID + "_" + score + ",");
			}
			
			String line = null;
			//去掉行末的“，”
			if(sBuilder.toString().endsWith(",")) {
				line=sBuilder.substring(0, sBuilder.length()-1);
			}
			
			outKey.set(itemID);
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
		   Job job = Job.getInstance(conf,"step1");
		   
		   //设置Job的主类
		   job.setJarByClass(step1.class);
		   //设置Job的Mapper类和Reducer类
		   job.setMapperClass(Mapper1.class);
		   job.setReducerClass(Reducer1.class);
		   
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
		   
		   return job.waitForCompletion(true)?1:-1;
		   
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
