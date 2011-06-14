package com.vn.newsspeak;

import it.sauronsoftware.feed4j.FeedParser;
import it.sauronsoftware.feed4j.bean.Feed;
import it.sauronsoftware.feed4j.bean.FeedItem;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FeedServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(FeedServlet.class.getName());
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
		try {
			// Get the newspaper name
	        String feedLink = req.getParameter("url");
			
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
            addTagElement(writer, "link", item.getLink().toString(), true);
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
}
