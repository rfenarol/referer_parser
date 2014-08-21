package it.clickmeter.hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class SecondMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
	private IntWritable occurrence;
	private Text domain = new Text();
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		StringTokenizer tokenizer;
		String line = value.toString();
		tokenizer = new StringTokenizer(line);
		
		/* linea del tipo 'interesse 12' */
		if (tokenizer.hasMoreTokens())
			domain.set(tokenizer.nextToken());
		if (tokenizer.hasMoreTokens())
			occurrence = new IntWritable( Integer.parseInt(tokenizer.nextToken()) );
			
		context.write(occurrence, domain);
	}

}
