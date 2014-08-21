package it.clickmeter.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
	private static final String hostPattern = "(?:http|https)://(\\w+([\\.|-]\\w+)*\\.[a-z]+)";
	private static final String enginePattern = "(google|yahoo|bing|ask|aol|baidu|yandex|exalead|duckduckgo|blekko)\\.[a-z]+";
	private static final String classifiedPattern = "(craigslist|kijiji|backpage|classifiedads|ebay|usfreeads|webcosmo|gumtree|oodle|subito|sell)\\.[a-z]+";
	private static final String socialNetworkPattern = "(facebook|plus\\.google|linkedin|youtube|tumblr|pinterest|twitter|^t|instagram|flickr|^vk)\\.[a-z]+";
	private static final String mailServicePattern = "(mail\\.google|mail\\.aol|outlook|mail\\.yahoo|mail\\.live|mail\\.inbox|gmx|mail\\.zoho|mail\\.virgilio|alice|posta\\.libero)\\.[a-z]+";
	private static final String advertisingServicePattern = "(onclickads|doubleclick|adwords\\.google)\\.[a-z]+";
	private static final String adwordsPattern ="[\\p{Punct}]gclid=([^&]*)";
	private static final String keywordsPattern = "[\\p{Punct}](?:wd|q|p|text)=([^&]*)";
	private static final String utmSourcePattern = "[\\p{Punct}]utm_source=([^&]*)";
	private static final String utmMediumPattern = "[\\p{Punct}]utm_medium=([^&]*)";
	private static final String utmCampaignPattern = "[\\p{Punct}]utm_campaign=([^&]*)";

	public static void main( String[] args ){

		//		String referer = "http://www.sportmediaset.mediaset.it/calcio/juventus/2014/articoli/1032169/juve-via-al-countdown-scudetto-160-.shtml";
		//		String referer = "http://www.net-ebook.it/";
		//    	String referer = "http://www.southernkissed.com/honey-nut-muffins/?utm_source=rss&utm_medium=rss&utm_campaign=honey-nut-muffins";
		//		String referer = "http://www.everydayonsales.com/74968/21-31-mar-2014-parkson-mad-about-kids-promotion-for-children-goods?utm_source=EverydayOnSales.com&utm_campaign=4d882d8681-RSS_EMAIL_CAMPAIGN&utm_medium=email&utm_term=0_3b0d009c51-4d882d8681-391699077&goal=0_3b0d009c51-4d882d8681-391699077";
		//		String referer = "http://www.google.com/aclk?sa=l&ai=CDiB12ckRU--PDuijzAG5yYCwDriJsYMDgPb0yjT8n5i_ARACIJmo2R4oCFCWg4W4AWDJ5sSGxKPIF6ABlMev_APIAQGqBChP0JpoNqU_hs012ow6kxYz4aPzBWbLdL7tvWv9ZQEg0FIljGzWgHgggAWQToAH1LjQA5AHAQ&num=5&sig=AOD64_3VZ8bPrdLXVmxQ8JiX3ennTpcJow&rct=j&q=cabin rentals northern arizona&ved=0CLwBENEM&adurl=http://go.clickmeter.com/azcabins/?_kk=rental cabins in arizona&_kt=1d369578-1487-46d2-b86f-87da327abaa2";
		//		String referer = "http://lindsaymendez96.tumblr.com/post/59357092412/inolvidable-by-reik";
		//				String referer = "http://www.google.com/url?q=http://www.gvomail.com/redir.php?msg=c6b04b2b17beb1b092436a3bfac4670d&k=5b4ea45a05b1e659b166c4d27117e53e131e8944b9e9f67b63ec6011f909c251&url=http://onlineincometoday.org/go/access-profits.php&sa=D&sntz=1&usg=AFQjCNHZ85V82A28WOGyEjZ3iwpQeP8-LA";
		//				String referer = "http://portland.craigslist.org/mlt/csr/4350877873.html";
		//				String referer = "http://www.local.com/results.aspx?keyword=amsoil dealer&cid=900&gid=Autos&gclid=CKOr4L228rwCFfM7Ogodxy4AaQ";
		//		String referer= "http://www.ebay.it/itm/MICHAEL-KORS-Uhr-Damenuhr-Armbanduhr-Damenchronograph-MK5055-/310945989465";
		String referer = "http://t.co";
//		String referer = "http://news.mail.ru/inregions/siberian/75/incident/18039705/?frommail=1";

		//		String referer = "https://www.google.com/search?sourceid=navclient&aq=1&oq=php+li&ie=UTF-8&rlz=1T4ADFA_enUS427US433&q=php+tutorial";
		//		String referer = "https://www.bing.com/search?q=amazon+italia&go=&qs=n&form=QBRE&filt=all&pq=amazon+italia&sc=8-8&sp=-1&sk=&cvid=90ec0079b3a9433aa5a28f9fe7902822";
		//				String referer = "https://it.search.yahoo.com/search;_ylt=Agli4pAMbBeGO4jFaOHWxsQbrK5_?p=amazon+italia&toggle=1&cop=mss&ei=UTF-8&fr=yfp-t-909";
		//				String referer = "http://search.aol.com/aol/search?s_it=webmail-searchbox&s_qt=ac&q=torreon real estate";
		//				String referer = "http://www.baidu.com/#wd=amazon italia&rsv_bp=0&tn=baidu&rsv_spt=3&ie=utf-8&rsv_sug3=9&rsv_sug4=542&rsv_sug1=1&rsv_sug2=0&inputT=2242";
		//				String referer = "http://yandex.ru/yandsearch?lr=101497&text=amazon+italia";
		//		String referer = "https://duckduckgo.com/?q=amazon+italia";
		//		String referer = "http://blekko.com/#?q=amazon italia";
		//		String referer = "http://www.exalead.com/search/web/results/?q=amazon italia";



		String source = parse(referer, hostPattern);
		String category = "Other";
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
				if(!classified.isEmpty()){
					category = "Classified";
					source = classified;
				}
			}
		}

		String utmSource = parse(referer, utmSourcePattern);
		String utmMedium = parse(referer, utmMediumPattern);
		String utmCampaign = parse(referer, utmCampaignPattern);

		System.out.println(source);
		System.out.println(category);
		System.out.println(keywords);
		System.out.println(utmSource);
		System.out.println(utmMedium);
		System.out.println(utmCampaign);


	}


	private static String parse(String url, String pattern) {
		Pattern patt = Pattern.compile(pattern);
		Matcher mat = patt.matcher(url);
		String result = "";
		if(mat.find())
			result = mat.group(1);
		return result;
	}
}
