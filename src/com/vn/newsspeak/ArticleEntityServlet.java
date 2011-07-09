package com.vn.newsspeak;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class handles returning a set of entities for a given article
 * @author vnarla
 *
 */
public class ArticleEntityServlet extends HttpServlet  {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ArticleContentServlet.class.getName());
	private static final String GET_ENTITIES_API= "URLGetRankedNamedEntities";
	
	private static final String PARAM_MAX_ENTITIES = "maxRetrieve";
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
		
		try {
			// POST parameters
			String articleUrl = req.getParameter("url");
			String format = req.getParameter("format");
			
			HashMap<String, String> apiArgs = new HashMap<String, String>();
			apiArgs.put(AlchemyService.OUTPUT_MODE, format);
			apiArgs.put(AlchemyService.URL, articleUrl);
			
			apiArgs.put(PARAM_MAX_ENTITIES, "10");
			
			AlchemyService alchemyObj = new AlchemyService();
			PrintWriter writer = resp.getWriter();
			writer.write(alchemyObj.makeApiCall(GET_ENTITIES_API, apiArgs));
			
		} catch(Exception exception) {
			log.severe(exception.getMessage());
			exception.printStackTrace();
		}
	}
}