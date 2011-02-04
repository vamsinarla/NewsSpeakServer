package com.vn.newsspeak;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

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
		String api_key = "3dd078fdf99f03a052aaa48579a3db2a4e9520a9";
		
		String postData = URLEncoder.encode("apikey") + "=" + api_key + "&";
		postData += URLEncoder.encode("url") + "=" + link;

		String alchemyApiUrl = "http://access.alchemyapi.com/calls/url/URLGetText";
		InputStream responseStream = null;
		Result result = new Result();
		
		try {
			URL feedUrl = new URL(alchemyApiUrl);
			URLConnection conn = feedUrl.openConnection();
			
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(postData);
		    wr.flush();
		    
		    responseStream = conn.getInputStream();
		    getExtractedText(responseStream, result, type);
		   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.text;
	}
	
	private void getExtractedText(InputStream responseStream, Result result, String type) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		try {
			SAXParser parser = factory.newSAXParser();
			
			ExtractTextXMLHandler handler = new ExtractTextXMLHandler(result);
			parser.parse(responseStream, handler);
		
			// For text only version
			if (type.equalsIgnoreCase("html")) {
				result.text = result.text.replaceAll("(\r\n|\r|\n|\n\r)", "<br><br>");
			} else { // For reading out
				
			}
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
	}
	
}