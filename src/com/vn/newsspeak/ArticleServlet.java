package com.vn.newsspeak;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class ArticleServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(FeedServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
		ArticleParserFactory factory = new ArticleParserFactory();
		
		try {
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

