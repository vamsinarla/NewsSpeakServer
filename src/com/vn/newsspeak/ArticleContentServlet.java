package com.vn.newsspeak;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * New version of Article Servlet/Handler. 
 * Receives a post with the article URL and uses the Alchemy API 
 * to process the text.
 * @author narla
 *
 */
@SuppressWarnings("serial")
public class ArticleContentServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(ArticleContentServlet.class.getName());
	private static final String GET_CONTENT_API= "URLGetText";
	
	private static final String PARAM_URL = "url";
	private static final String PARAM_FORMAT = "format";
	private static final String PARAM_RESPONSE = "response";
	
	private static final String TYPE_HTML = "html";
	private static final String TYPE_TEXT = "text";
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
		
		try {
			// POST parameters
			String articleUrl = req.getParameter(PARAM_URL);
			String format = req.getParameter(PARAM_FORMAT);
			String responseType = req.getParameter(PARAM_RESPONSE);
			
			HashMap<String, String> apiArgs = new HashMap<String, String>();
			apiArgs.put(AlchemyService.OUTPUT_MODE, format);
			apiArgs.put(AlchemyService.URL, articleUrl);
			
			AlchemyService alchemyObj = new AlchemyService();
			resp.setContentType("text/html;charset=UTF-8");
			PrintWriter writer = resp.getWriter();
			
			writer.write(getContent(responseType, alchemyObj.makeApiCall(GET_CONTENT_API, apiArgs)));
		} catch(Exception exception) {
			log.severe(exception.getMessage());
			exception.printStackTrace();
		}
	}
	
	private String getContent(String responseType, String response) throws JSONException {
		JSONObject object = new JSONObject(response);
		String result = object.getString("text");
		
		if (responseType.equalsIgnoreCase(TYPE_HTML)) {
			result = result.replaceAll("(\r\n|\r|\n|\n\r)", "<br><br>");
			result = renderHTML(result);
		}
		
		return result;
	}
	
	private String renderHTML(String content) {
		String result = "<html><head><meta http-equiv=\"Content-Type\"content=\"text/html; charset=utf-8\"/><style type=\"text/css\"> body { font-family: serif } </style></head><body>"; 
		result += content + "<br><br><br><br><br><br></body></html>";
		return result;
	}
}

