package com.vn.newsspeak;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * New version of Article Servlet/Handler. 
 * Receives a post with the article URL and uses the Alchemy API 
 * to process the text.
 * @author narla
 *
 */
@SuppressWarnings("serial")
public class ArticleServlet2 extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(FeedServlet.class.getName());
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
		ArticleParserFactory factory = new ArticleParserFactory();
		
		try {
			// POST parameters
			String articleUrl = req.getParameter("link");
			
			// Get the default parser
			ArticleParser parser = factory.getParser("");
			String content = parser.getContent(articleUrl);
			
			// Write the output
			PrintWriter writer = resp.getWriter();
			writer.write(content);
			
		} catch(Exception exception) {
			log.severe(exception.getMessage());
			exception.printStackTrace();
		}
	}
}

