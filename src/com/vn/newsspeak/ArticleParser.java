package com.vn.newsspeak;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;
import org.xml.sax.SAXException;


public class ArticleParser {
	
	protected String url;
	protected Parser parser;
	
	public ArticleParser() {
		parser = new Parser();
	}
	
	protected String getContent(String link, String type) throws ParserException {
		// GET params
		String api_key = "3dd078fdf99f03a052aaa48579a3db2a4e9520a9";

		String alchemyApiUrl = "http://access.alchemyapi.com/calls/url/URLGetText?apikey=" + api_key + "&url=" + link;
		
		return getExtractedText(alchemyApiUrl);
	}
	
	protected String getContent(String link) throws ParserException {
		// GET params
		String api_key = "3dd078fdf99f03a052aaa48579a3db2a4e9520a9";

		String alchemyApiUrl = "http://access.alchemyapi.com/calls/url/URLGetText?apikey=" + api_key + "&url=" + link;
		
		return getExtractedText(alchemyApiUrl);
	}
	
	private String getExtractedText(String alchemyApiUrl) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		Result result = new Result();
		
		try {
			SAXParser parser = factory.newSAXParser();
			
			ExtractTextXMLHandler handler = new ExtractTextXMLHandler(result);
			parser.parse(alchemyApiUrl, handler);
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.text;
	}
	
}

