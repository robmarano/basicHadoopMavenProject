package com.thehackerati.hadoop;

import java.io.IOException;
import java.util.*;
 
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

import org.apache.log4j.*;
 
public class WordCount {

  public static Logger logger;

  static {
    logger = Logger.getLogger("WordCount.class");
    logger.setLevel(Level.INFO);
  }
 
  public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
 
    public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
      WordCount.logger.info("map in key = " + key.toString());
      String line = value.toString();
      StringTokenizer tokenizer = new StringTokenizer(line);
      while (tokenizer.hasMoreTokens()) {
        word.set(tokenizer.nextToken());
        output.collect(word, one);
      }
    }
  }
 
  public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
    public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
      int sum = 0;
      while (values.hasNext()) {
        sum += values.next().get();
      }
      output.collect(key, new IntWritable(sum));
    }
  }
 
  public static void main(String[] args) throws Exception {
    WordCount.logger.info("Running WordCount MapReduce Program...");
    JobConf conf = new JobConf(WordCount.class);
    conf.setJobName("wordcount");
 
    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(IntWritable.class);
 
    conf.setMapperClass(Map.class);
    conf.setCombinerClass(Reduce.class);
    conf.setReducerClass(Reduce.class);
 
    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);
 
    FileInputFormat.setInputPaths(conf, new Path(args[0]));
    FileOutputFormat.setOutputPath(conf, new Path(args[1]));
 
    WordCount.logger.info("\tStarting MapReduce job...");
    JobClient.runJob(conf);
    WordCount.logger.info("\tCompleted MapReduce job.");
    WordCount.logger.info("Done.");
  }
}
