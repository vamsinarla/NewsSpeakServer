package com.vn.newsspeak;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FeedDataStorePopulator extends HttpServlet {
    /**
	 * Default UID
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FeedDataStorePopulator.class.getName());
	
	/**
	 * Vars
	 */
	private ArrayList<NewsSource> sources;
	private NewsSource source;
	
	 /**
     * Processes a GET request contained in the request and sends response
     * back to the client.
     * @param req the GET request the client has made of the servlet
     * @param resp the response the servlet sends to the client
     */
	@Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
    	
		/*ArrayList<String> categories = new ArrayList<String>(
											Arrays.asList("US", 
													"World", 
													"Business", 
													"Sports", 
													"Politics",
													"Technology", 
													"Opinion", 
													"Health"));
		
		ArrayList<String> categoryURLs = new ArrayList<String>(
												Arrays.asList("http://feeds.nytimes.com/nyt/rss/HomePage",
														"http://feeds.nytimes.com/nyt/rss/World",
														"http://feeds.nytimes.com/nyt/rss/Business",
														"http://feeds1.nytimes.com/nyt/rss/Sports",
														"http://feeds.nytimes.com/nyt/rss/Politics",
														"http://feeds.nytimes.com/nyt/rss/Technology",
														"http://feeds.nytimes.com/nyt/rss/Opinion",
														"http://feeds.nytimes.com/nyt/rss/Health"));
														
		
		// Put in a NewsSource object
		NewsSource newsSource = new NewsSource("New York Times",
												true,
												categories,
												categoryURLs,
												"http://feeds.nytimes.com/nyt/rss/HomePage"
												);*/
		
		
		// Add the {@code NewsSource} object to the persistent store
        PersistenceManager pm = PMF.get().getPersistenceManager();
        PrintWriter out = resp.getWriter();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        sources = new ArrayList<NewsSource>();
        
        try {
        	SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new InputSource("./newssources.xml"), new NewsSourceHandler());
            if (pm.makePersistentAll(sources) != null) {
                out.print("success=yes");
            } else {
                out.print("success=no");
            }
        } catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            pm.close();
        }
        
    }
	
	private class NewsSourceHandler extends DefaultHandler {
		
		private StringBuilder builder;
		private ArrayList<String> array;
		
		/** 
		 * Collecting all characters. Use string builder for efficiency
		 */
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
			builder.append(ch, start, length);
		}
		
		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			builder = new StringBuilder();
		}
		
		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, name, attributes);
			if (name.equalsIgnoreCase("source")){
				source = new NewsSource();
			} else if (name.equalsIgnoreCase("categories")) {
				array = new ArrayList<String>();
			}  else if (name.equalsIgnoreCase("categoryUrls")) {
				array = new ArrayList<String>();
			}
			builder.setLength(0);
		}

		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			super.endElement(uri, localName, name);
			
			if (source != null){
				if (name.equalsIgnoreCase("name")) {
					source.setTitle(builder.toString());
				} else if (name.equalsIgnoreCase("type")) {
					source.setType(builder.toString());
				} else if (name.equalsIgnoreCase("preferred")) {
					source.setPreferred(builder.toString().equalsIgnoreCase("true") ? true : false);
				} else if (name.equalsIgnoreCase("hascategories")) {
					source.setHasCategories(builder.toString().equalsIgnoreCase("true") ? true : false);
				} else if (name.equalsIgnoreCase("defaultLink")) {
					source.setDefaultUrl(builder.toString());
				} else if (name.equalsIgnoreCase("title")) {
					array.add(builder.toString());
				} else if (name.equalsIgnoreCase("link")) {
					array.add(builder.toString());
				} else if (name.equalsIgnoreCase("categoryUrls")) {
					source.setCategoryUrls(array);
				} else if (name.equalsIgnoreCase("categories")) {
					source.setCategories(array);
				} else if (name.equalsIgnoreCase("source")) {
					sources.add(source);
				}
				builder.setLength(0);	
			}
		}
	}
}
