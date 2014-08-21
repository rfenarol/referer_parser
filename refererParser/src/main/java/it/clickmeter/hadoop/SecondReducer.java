package it.clickmeter.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SecondReducer extends Reducer<IntWritable, Text, Text, IntWritable> {
	private Text result = new Text();
	
	public void reduce(IntWritable key, Iterable<Text> values, Context context)throws IOException, InterruptedException {
		
		for (Text domain : values) {
			result.set(domain.toString());
			context.write(result, key);
		}
	}

}
