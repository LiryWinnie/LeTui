package com.winnie.hadoop;

import java.io.IOException;

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
* 2018年5月20日 下午11:59:39
*/
public class step3 {
	
	//输入文件的相对路径
	private static String inPath = "/Movie/step1_output";
	//输出文件的相对路径
	private static String outPath = "/Movie/step3_output";
	//hdfs地址
	private static String hdfs = "hdfs://192.168.190.137:9000";

	public static class Mapper3 extends Mapper<LongWritable, Text, Text, Text>{

		private Text outKey = new Text();
		
		private Text outValue = new Text();
		
	    /**
		*key:1
		*value:1	1_0,2_3,3_-1,4_2,5_-3 
		*/
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] rowAndline = value.toString().split("\t");
			
			//矩阵的行号
			String row = rowAndline[0];
			String[] lines = rowAndline[1].split(",");
			
			//lines=["1_0","2_3","3_-1","4_2","5_-3"]
			for(int i = 0;i<lines.length;i++) {
				String column = lines[i].split("_")[0];
				String valueStr = lines[i].split("_")[1];
				//key:列号   value：行号_值
				outKey.set(column);
				outValue.set(row+"_"+valueStr);
				
				context.write(outKey, outValue);
			}
		}
	} 
	
	public static class Reduce3 extends Reducer<Text, Text, Text, Text>{

		private Text outKey = new Text();
		
		private Text outValue = new Text();
		
		//key:列号   value：[行号_值,行号_值,行号_值...]
		@Override
		protected void reduce(Text key, Iterable<Text> values,Context context)
				throws IOException, InterruptedException {
			StringBuilder sb = new StringBuilder();
			for(Text text:values) {
				sb.append(text+",");
			}
			String line = null;
			if(sb.toString().endsWith(",")) {
				line=sb.substring(0,sb.length()-1);
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
		   Job job = Job.getInstance(conf,"step3");
		   
		   //设置Job的主类
		   job.setJarByClass(step3.class);
		   //设置Job的Mapper类和Reducer类
		   job.setMapperClass(Mapper3.class);
		   job.setReducerClass(Reduce3.class);
		   
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
