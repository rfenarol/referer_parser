package it.clickmeter.parser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class EmptyRefererCounter {

	private static File fn;
	private static FileWriter fw; 	
	private static CSVWriter resultCsvWriter;
	
	private static int fullRows=0;
	private static int pixelRows=0;
	private static int totalRows=0;
	private static int linkRows=0;

	public static void main(String args[]) throws Exception{
		fn = new File("referer_count.csv");
		fw = new FileWriter(fn, true);
		resultCsvWriter = new CSVWriter(fw, ',');
		readHitsCSV();
		writeReportCSV();
		resultCsvWriter.close();
		fw.close();
	}

	private static void readHitsCSV() throws Exception  {
		String filename="/home/roberto/Scrivania/clickmeter/dataHits_gennaio_febbraio_marzo/HITS03.csv";
		File f = new File(filename);
		FileReader fr = new FileReader(f);
		String [] nextLine;
		CSVReader csvReader = new CSVReader(fr, ',');
		while((nextLine = csvReader.readNext()) != null){
			totalRows+=1;
			String url;
			String hitType;
			url = nextLine[15];
			hitType = nextLine[18];
			if(!url.isEmpty()){
				fullRows+=1;
				if("1".equals(hitType)) pixelRows+=1;
				if("0".equals(hitType)) linkRows+=1;
			}
		}
		csvReader.close();
		fr.close();
	}

	private static void writeReportCSV() throws Exception {

		String[] out = new String[7];
		out[0]=Integer.toString(fullRows);
		out[1]=Integer.toString(linkRows);
		out[2]=Integer.toString(pixelRows);
		out[3]=Integer.toString(totalRows);
		out[4]=Integer.toString((fullRows*100)/totalRows);
		out[5]=Integer.toString((linkRows*100)/fullRows);
		out[6]=Integer.toString((pixelRows*100)/fullRows);
		resultCsvWriter.writeNext(out);
	}
}