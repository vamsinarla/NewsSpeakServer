package com.vn.newsspeak;

import it.sauronsoftware.feed4j.FeedParser;
import it.sauronsoftware.feed4j.bean.Feed;
import it.sauronsoftware.feed4j.bean.FeedItem;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FeedServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(FeedServlet.class.getName());
	private HashMap<String, String> feedLinkMap;
	
	public FeedServlet() {
		super();
		
		// Prepare the Hashmap for newspapers -> links
		feedLinkMap = new HashMap<String, String>();
		prepareFeedLinkMap();
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
        
		try {
	        // Get the newspaper name
	        String newspaper = req.getParameter("newspaper");
	        String category = req.getParameter("category");
	        String feedLink = getFeedUrl(newspaper, category);
	       
	        // Set the content type to UTF-8 for the response
	        resp.setContentType("text/html; charset=UTF-8");
	        
	        URL url = new URL(feedLink);
	        PrintWriter writer = resp.getWriter();
	        
	        // Main workhorse
	        parseFeed(url, writer);
	        
		} catch (MalformedURLException exception) {
			log.severe("Malformed URL, probably missing params in GET request.");
			resp.getWriter().print("Please correctly supply the URL or params in the GET request.");
		} catch (Exception exception) {
			log.severe("Something drastic occurred" + exception.getMessage());
			resp.getWriter().print("Please try again later. Error :" + exception.getMessage());
		}
	}

	private void parseFeed(URL url, PrintWriter writer) {

        Feed feed = null;
        try {
            feed = FeedParser.parse(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        writer.println("<items>");
        int items = feed.getItemCount();
        for (int i = 0; i < items; i++) {
            FeedItem item = feed.getItem(i);
            writer.println("<item>");
            
            addTagElement(writer, "title", item.getTitle(), true);
            addTagElement(writer, "description", item.getDescriptionAsText(), true);
            addTagElement(writer, "link", item.getLink().toString(), false);
            writer.println("</item>");
        }
        writer.println("</items>");
	}
	
	private void addTagElement(PrintWriter writer, String tag, String value, boolean writeCData) {
		writer.println("<" + tag + ">");
		
		if (writeCData) {
			writer.print("<![CDATA[");
		}
		
		writer.println(value);
		
		if (writeCData) {
			writer.print("]]>");
		}
		
		writer.println("</" + tag + ">");
	}
	
	private void prepareFeedLinkMap() {

		// For NewsSpeak client
		// For New York Times
		feedLinkMap.put("New York Times", "http://feeds.nytimes.com/nyt/rss/HomePage");
		feedLinkMap.put("New York Times US", "http://feeds.nytimes.com/nyt/rss/HomePage");
		feedLinkMap.put("New York Times World", "http://feeds.nytimes.com/nyt/rss/World");
		feedLinkMap.put("New York Times Business", "http://feeds.nytimes.com/nyt/rss/Business");
		feedLinkMap.put("New York Times Sports", "http://feeds1.nytimes.com/nyt/rss/Sports");
		feedLinkMap.put("New York Times Politics", "http://feeds.nytimes.com/nyt/rss/Politics");
		feedLinkMap.put("New York Times Technology", "http://feeds.nytimes.com/nyt/rss/Technology");
		feedLinkMap.put("New York Times Opinion", "http://feeds.nytimes.com/nyt/rss/Opinion");
		feedLinkMap.put("New York Times Health", "http://feeds.nytimes.com/nyt/rss/Health");
		
		// For Wall Street Journal
		feedLinkMap.put("Wall Street Journal", "http://online.wsj.com/xml/rss/3_7011.xml");
		feedLinkMap.put("Wall Street Journal US", "http://online.wsj.com/xml/rss/3_7011.xml");
		feedLinkMap.put("Wall Street Journal World", "http://online.wsj.com/xml/rss/3_7085.xml");
		feedLinkMap.put("Wall Street Journal Business", "http://online.wsj.com/xml/rss/3_7014.xml");
		feedLinkMap.put("Wall Street Journal Sports", "http://online.wsj.com/xml/rss/3_7204.xml");
		feedLinkMap.put("Wall Street Journal Politics", "http://online.wsj.com/xml/rss/3_7087.xml");
		feedLinkMap.put("Wall Street Journal Technology", "http://online.wsj.com/xml/rss/3_7455.xml");
		feedLinkMap.put("Wall Street Journal Opinion", "http://online.wsj.com/xml/rss/3_7041.xml");
		feedLinkMap.put("Wall Street Journal Health", "http://online.wsj.com/xml/rss/3_7089.xml");
		
		// For USA Today
		feedLinkMap.put("USA Today", "http://rssfeeds.usatoday.com/usatoday-NewsTopStories");
		feedLinkMap.put("USA Today US", "http://rssfeeds.usatoday.com/usatoday-NewsTopStories");
		feedLinkMap.put("USA Today World", "http://rssfeeds.usatoday.com/UsatodaycomWorld-TopStories");
		feedLinkMap.put("USA Today Business", "http://rssfeeds.usatoday.com/UsatodaycomMoney-TopStories");
		feedLinkMap.put("USA Today Sports", "http://rssfeeds.usatoday.com/UsatodaycomSports-TopStories");
		feedLinkMap.put("USA Today Politics", "http://rssfeeds.usatoday.com/TP-OnPolitics");
		feedLinkMap.put("USA Today Technology", "http://rssfeeds.usatoday.com/usatoday-TechTopStories");
		feedLinkMap.put("USA Today Opinion", "http://rssfeeds.usatoday.com/News-Opinion");
		feedLinkMap.put("USA Today Health", "http://rssfeeds.usatoday.com/UsatodaycomHealth-TopStories");
		
		// For LA Times
		feedLinkMap.put("Los Angeles Times", "http://feeds.latimes.com/latimes/news");
		feedLinkMap.put("Los Angeles Times US", "http://feeds.latimes.com/latimes/news");
		feedLinkMap.put("Los Angeles Times World", "http://feeds.latimes.com/latimes/news/nationworld/world");
		feedLinkMap.put("Los Angeles Times Business", "http://feeds.latimes.com/latimes/business");
		feedLinkMap.put("Los Angeles Times Sports", "http://feeds.latimes.com/latimes/sports");
		feedLinkMap.put("Los Angeles Times Politics", "http://feeds.latimes.com/latimes/news/politics/");
		feedLinkMap.put("Los Angeles Times Technology", "http://feeds.latimes.com/latimes/technology");
		feedLinkMap.put("Los Angeles Times Opinion", "http://feeds2.feedburner.com/latimes/news/opinion/commentary/");
		feedLinkMap.put("Los Angeles Times Health", "http://feeds.latimes.com/latimes/features/health/");
		
		// For BBC
		feedLinkMap.put("BBC", "http://feeds.bbci.co.uk/news/rss.xml?edition=uk");
		feedLinkMap.put("BBC UK", "http://feeds.bbci.co.uk/news/uk/rss.xml?edition=uk");
		feedLinkMap.put("BBC World", "http://feeds.bbci.co.uk/news/world/rss.xml?edition=uk");
		feedLinkMap.put("BBC Business", "http://feeds.bbci.co.uk/news/business/rss.xml?edition=uk");
		feedLinkMap.put("BBC Politics", "http://feeds.bbci.co.uk/news/politics/rss.xml?edition=uk");
		feedLinkMap.put("BBC Technology", "http://feeds.bbci.co.uk/news/technology/rss.xml?edition=uk");
		feedLinkMap.put("BBC Entertainment", "http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml?edition=uk");
		feedLinkMap.put("BBC Health", "http://feeds.bbci.co.uk/news/health/rss.xml?edition=uk");
		
		
		// For CNN US
		feedLinkMap.put("CNN US", "http://rss.cnn.com/rss/cnn_us.rss");
		
		feedLinkMap.put("Huffington Post", "http://feeds.huffingtonpost.com/huffingtonpost/raw_feed");
		feedLinkMap.put("TechCrunch", "http://feeds.feedburner.com/Techcrunch");
		
		feedLinkMap.put("engadget", "http://www.engadget.com/rss.xml");
		feedLinkMap.put("Mashable", "http://feeds.mashable.com/Mashable");
		feedLinkMap.put("Washington Post", "http://feeds.washingtonpost.com/wp-dyn/rss/print/index_xml");
		feedLinkMap.put("NY Daily News", "http://feeds.nydailynews.com/nydnrss/news");
		feedLinkMap.put("engadget", "http://www.engadget.com/rss.xml");
		feedLinkMap.put("Gizmodo", "http://feeds.gawker.com/gizmodo/full");
		feedLinkMap.put("Read Write Web", "http://feeds.feedburner.com/readwriteweb");
		feedLinkMap.put("Daily Beast", "http://www.tdbimg.com/ext/rss/cheatsheet/rss_cheatsheet.xml");
		
		// For NewsSpeak India client
		feedLinkMap.put("Times of India", "http://timesofindia.feedsportal.com/c/33039/f/533965/index.rss");
		feedLinkMap.put("India Today", "http://indiatoday.intoday.in/site/rssGenerator.jsp?secId=4&feed=LATEST%20HEADLINES");
		feedLinkMap.put("The Hindu", "http://www.thehindu.com/?service=rss");
		feedLinkMap.put("The Economic Times", "http://economictimes.feedsportal.com/c/33041/f/537910/index.rss");
		
	}
	
	private String getFeedUrl(String newsSource, String category) {
		// Older client or default category
		if (category != null) {
			String source = newsSource + " " + category;
			return feedLinkMap.get(source);
		}	
		return feedLinkMap.get(newsSource);
	}
}
