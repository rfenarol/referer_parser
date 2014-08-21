package it.clickmeter.parser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class ParseURL {

	private static final String hostPattern = "(?:http|https)://(\\w+([\\.|-]\\w+)*\\.[a-z]+)";
	private static final String enginePattern = "(google|yahoo|bing|ask|aol|baidu|yandex|exalead|duckduckgo|blekko)\\.[a-z]+";
	private static final String classifiedPattern = "(craigslist|kijiji|backpage|classifiedads|ebay|usfreeads|webcosmo|gumtree|oodle|subito|sell)\\.[a-z]+";
	private static final String socialNetworkPattern = "(facebook|plus\\.google|linkedin|youtube|tumblr|pinterest|twitter|^t|instagram|flickr|^vk)\\.[a-z]+";
	private static final String mailServicePattern = "(mail\\.google|mail\\.aol|outlook|mail\\.yahoo|mail\\.live|mail\\.inbox|gmx|mail\\.zoho|mail\\.virgilio|alice|posta\\.libero)\\.[a-z]+";
	private static final String advertisingServicePattern = "(onclickads|doubleclick|adwords\\.google|googleadservices|bingads.microsoft|advertising.yahoo|buysellads|adroll|7search|infolinks|ads.twitter)\\.[a-z]+";
	private static final String adwordsPattern ="[\\p{Punct}]gclid=([^&]*)";
	private static final String keywordsPattern = "[\\p{Punct}](?:wd|q|p|text)=([^&]*)";
	private static final String utmSourcePattern = "[\\p{Punct}]utm_source=([^&]*)";
	private static final String utmMediumPattern = "[\\p{Punct}]utm_medium=([^&]*)";
	private static final String utmCampaignPattern = "[\\p{Punct}]utm_campaign=([^&]*)";

	private static File fn;
	private static FileWriter fw; 	
	private static CSVWriter resultCsvWriter;

	private static Map<String, String> categories;

	public static void main(String args[]) throws Exception{
		populateCategoriesMap();
		fn = new File("report.csv");
		fw = new FileWriter(fn, true);
		resultCsvWriter = new CSVWriter(fw, ',');
		readHitsCSV();
		resultCsvWriter.close();
		fw.close();
	}

	private static void populateCategoriesMap() throws IOException, Exception {
		categories = new HashMap<String, String>();
		String filename="/home/roberto/Scrivania/clickmeter/report_march_hits/classification.csv";
		File f = new File(filename);
		FileReader fr = new FileReader(f);
		String [] nextLine;
		CSVReader csvReader = new CSVReader(fr, ',');
		while((nextLine = csvReader.readNext()) != null){
			String domain;
			String category;
			domain = nextLine[0];
			category = nextLine[1];
			if(!domain.isEmpty() && !category.isEmpty()){
				categories.put(domain, category);
			}
		}
		csvReader.close();
		fr.close();
	}

	private static void analyzerRow(String referer) throws Exception{

		String source = parse(referer, hostPattern);
		String category = "";
		String keywords = "";

		//SOCIAL NETWORKS GROUPING
		String socialNetwork = parse(source, socialNetworkPattern);
		if(!socialNetwork.isEmpty()){
			if("t.co".equals(source)) source = "twitter";
			category = "Social Network";
		}

		//ADVERTISING SOURCE GROUPING
		String adService = parse(source, advertisingServicePattern);
		if(adService.isEmpty()){
			String gclid = parse(referer, adwordsPattern);
			if(!gclid.isEmpty()) adService = "Adwords";
		}
		if(!adService.isEmpty()){ 
			source = adService;
			category = "Advertising";
		}

		//MAIL SERVICES GROUPING
		String mailService = parse(source, mailServicePattern);
		if(mailService.isEmpty()) mailService = parse(source, "(mail\\.ru$)");
		if(!mailService.isEmpty()){ 
			source = mailService;
			category = "E-Mail service";
		}
		
		if(socialNetwork.isEmpty() && mailService.isEmpty() && adService.isEmpty()){
			//SEARCH ENGINE GROUPING
			String searchEngine = parse(source, enginePattern);
			if(!searchEngine.isEmpty()){
				keywords = parse(referer, keywordsPattern);
				keywords = keywords.replaceAll("\\+", " ");
				category = "Search Engine";
				source=searchEngine;
			}else{
				//CLASSIFIED GROUPING
				String classified = parse(source, classifiedPattern);
				if(!classified.isEmpty())
					category = "Classified";
					source = classified;
			}
		}

		if(categories.containsKey(source))
			category=categories.get(source);

		String utmSource = parse(referer, utmSourcePattern);
		String utmMedium = parse(referer, utmMediumPattern);
		String utmCampaign = parse(referer, utmCampaignPattern);

		String[] row = new String[10];
		row[0]=source;
		row[1]=category;
		row[2]=keywords;
		row[3]=utmSource;
		row[4]=utmMedium;
		row[5]=utmCampaign;
		row[6]=referer;

		writeReportCSV(row);
	}

	private static void readHitsCSV() throws Exception  {
		String filename="/home/roberto/Scrivania/clickmeter/marchHits/backup/01/GENERATED-DAILY-HISTORY.csv";
		//		String filename="C:/Users/Roberto/Desktop/ClickMeter/marchHits_referer/backup/31/00-hits_processor_1F5F15DCC378ECF2E1A94EDC15CB52F9.csv";
		File f = new File(filename);
		FileReader fr = new FileReader(f);
		String [] nextLine;
		CSVReader csvReader = new CSVReader(fr, ',');
		int j = 0;
		while((nextLine = csvReader.readNext()) != null){
			String url;
			url = nextLine[15];
			if(!url.isEmpty()){
				for(int i=0;i<3;i++){
					try {
						url = java.net.URLDecoder.decode(url, "UTF-8");
					} catch (Exception e) {
						j++;
						System.out.println(url);
						continue;
					}
				}
				analyzerRow(url);
			}
			//				else{
			//				String[] row = new String[3];
			//				row[0]="Unknown";
			//				row[1]="";
			//				row[2]="Direct";
			//				writeReportCSV(row);
			//			}
		}
		System.out.println(j);
		csvReader.close();
		fr.close();
	}

	private static void writeReportCSV(String[] row) throws Exception {

		String[] out = new String[10];
		for(int i=0; i<row.length; i++)
			out[i]=row[i];
		resultCsvWriter.writeNext(out);
	}


	private static String parse(String url, String pattern) {
		Pattern patt = Pattern.compile(pattern);
		Matcher mat = patt.matcher(url);
		String result = "";
		if( mat.find())
			result = mat.group(1);
		return result;
	}
}