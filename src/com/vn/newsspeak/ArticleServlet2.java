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
			String source = req.getParameter("source");
			String type = req.getParameter("type");
			
			// Since this parameter is optional and some versions of NewsSpeak may not use it. Check this.
			if (type == null) {
				type = "";
			}
			
			if (source == null) {
				source = "";
			}
			
			ArticleParser parser = factory.getParser(source);
			String content = parser.getContent(articleUrl, type);
			PrintWriter writer = resp.getWriter();
			writer.write(content);
			
		} catch(Exception exception) {
			log.severe(exception.getMessage());
			exception.printStackTrace();
		}
	}
}

