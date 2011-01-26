package com.vn.newsspeak;

import java.util.HashMap;

import com.vn.newsspeak.parsers.CNNParser;
import com.vn.newsspeak.parsers.DailyBeastParser;
import com.vn.newsspeak.parsers.EngadgetParser;
import com.vn.newsspeak.parsers.HuffPostParser;
import com.vn.newsspeak.parsers.LATimesParser;
import com.vn.newsspeak.parsers.MashableParser;
import com.vn.newsspeak.parsers.NYDailyNewsParser;
import com.vn.newsspeak.parsers.NYTimesParser;
import com.vn.newsspeak.parsers.ReadWriteWebParser;
import com.vn.newsspeak.parsers.TOIParser;
import com.vn.newsspeak.parsers.TechCrunchParser;
import com.vn.newsspeak.parsers.TheHinduParser;
import com.vn.newsspeak.parsers.USATodayParser;
import com.vn.newsspeak.parsers.WSJParser;
import com.vn.newsspeak.parsers.WashPostParser;

public class ArticleParserFactory {
	
	private HashMap<String, ArticleParser> articleParserMap;
	
	public ArticleParserFactory() {
		articleParserMap = new HashMap<String, ArticleParser>();
		
		articleParserMap.put("new york times", new NYTimesParser());
		articleParserMap.put("huffington post", new HuffPostParser());
		articleParserMap.put("wall street journal", new WSJParser());
		articleParserMap.put("techcrunch", new TechCrunchParser());
		articleParserMap.put("mashable", new MashableParser());
		articleParserMap.put("usa today", new USATodayParser());
		articleParserMap.put("los angeles times", new LATimesParser());
		articleParserMap.put("washington post", new WashPostParser());
		articleParserMap.put("ny daily news", new NYDailyNewsParser());
		articleParserMap.put("engadget", new EngadgetParser());
		articleParserMap.put("readwriteweb", new ReadWriteWebParser());
		articleParserMap.put("daily beast", new DailyBeastParser());
		articleParserMap.put("cnn us", new CNNParser());
		
		
		articleParserMap.put("times of india", new TOIParser());
		articleParserMap.put("the hindu", new TheHinduParser());
	}
	
	public ArticleParser getParser(String source) {
		ArticleParser parser;
		
		source = source.toLowerCase();
		
		if ((parser = articleParserMap.get(source)) == null) {
			return new ArticleParser();
		} else {
			return parser;
		}
	}
}
