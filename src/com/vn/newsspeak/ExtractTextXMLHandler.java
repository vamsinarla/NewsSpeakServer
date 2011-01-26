package com.vn.newsspeak;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Represent the result object
 * @author vamsi
 *
 */
class Result {
	String status;
	String usage;
	String url;
	String text;
}

/**
 * SAX Parser implementation for the XML returned from the Alchemy API 
 * @author vamsi
 *
 */
public class ExtractTextXMLHandler extends DefaultHandler {

	private StringBuilder builder;
	private Result result;

	ExtractTextXMLHandler(Result inResult) {
		result = inResult;
		builder = new StringBuilder();
		builder.setLength(0);
	}
	
	/**
	 * End element override
	 */
	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);
		
		if (result != null) {
			if (name.equalsIgnoreCase("status")) {
				result.status = builder.toString();
			} else if (name.equalsIgnoreCase("url")) {
				result.url = builder.toString();
			} else if (name.equalsIgnoreCase("text")) {
				result.text = builder.toString();
			} else if (name.equalsIgnoreCase("usage")) {
				result.usage = builder.toString();
			}
		}
		
		builder.setLength(0);
	}
	
	/** 
	 * Collecting all characters. Use string builder for efficiency
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		builder.append(ch, start, length);
	}
}
