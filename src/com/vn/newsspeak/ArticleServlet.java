package com.vn.newsspeak;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
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
public class ArticleServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(ArticleServlet.class.getName());
	private static final String GET_CONTENT_API= "URLGetText";
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
		
		try {
			// POST parameters
			String articleUrl = req.getParameter("url");
			String format = req.getParameter("format");
			
			AlchemyService alchemyObj = new AlchemyService();
			PrintWriter writer = resp.getWriter();
			writer.write(alchemyObj.makeApiCall(GET_CONTENT_API, ));
			
		} catch(Exception exception) {
			log.severe(exception.getMessage());
			exception.printStackTrace();
		}
	}
}

