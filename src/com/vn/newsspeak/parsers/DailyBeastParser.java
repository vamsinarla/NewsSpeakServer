package com.vn.newsspeak.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.vn.newsspeak.ArticleParser;


public class DailyBeastParser extends ArticleParser {

	public DailyBeastParser() {
		super();
	}
	
	@Override
	protected String getContent(String link, String type) throws ParserException {
		String content = "";
		TagNameFilter divFilter = new TagNameFilter("div");
		HasAttributeFilter attrFilter = new HasAttributeFilter("class", "text ");
		AndFilter andFilter = new AndFilter(divFilter, attrFilter);
		
		StringBuilder builder = new StringBuilder();
		URL url;
		
		try {
			url = new URL(link);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
			String oneLine;
			
			while ((oneLine = reader.readLine()) != null) {
				builder.append(oneLine);
			}
			
			reader.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parser.setInputHTML(builder.toString());
		
		NodeList list = parser.parse(andFilter);
		
		if (list.size() == 0) {
			attrFilter = new HasAttributeFilter("class", "text text_w_xtra");
			andFilter = new AndFilter(divFilter, attrFilter);
			
			parser = new Parser();
			parser.setInputHTML(builder.toString());
			list = parser.parse(andFilter);
		}
		
		list = list.extractAllNodesThatMatch(new TagNameFilter("p"), true);
		
		if (type.equalsIgnoreCase("html")) {
			for (int i = 0; i < list.size(); ++i) {
				content += list.elementAt(i).toHtml();
			}
		} else {
			for (int i = 0; i < list.size(); ++i) {
				content += list.elementAt(i).toPlainTextString();
			}
		}
		
		// Remove special characters, such as &nbsp;
        Pattern regex = Pattern.compile("&.*?;");
        Matcher matcher = regex.matcher(content);
        while (matcher.find()) content = matcher.replaceAll("");
        
        // Remove extra whitespaces
        content = content.replaceAll("(\\t|\\n)", "");
        
		return content;
	}
}
