package com.vn.newsspeak;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.org.json.JSONObject;

public class FetchSourceServlet extends HttpServlet {

	/**
	 * Default UID
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(SearchServlet.class.getName());
	
	/**
	 * GET request handler
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// GET params
		String sourceName = req.getParameter("sourceName");
		
		// Get the Datastore Service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Prepare the query
		Query query = new Query("NewsSource");
		query.addFilter("title", Query.FilterOperator.EQUAL, sourceName);
		
		// Fetch the results
		PreparedQuery preparedQuery = datastore.prepare(query);
		
		JSONObject newsSourceResult = null;
		
		try {
			// We SHOULD ASSERT that we have just 1 result for that sourceName
			for (Entity singleResult : preparedQuery.asIterable()) {
				newsSourceResult = NewsSource.createJSON(singleResult);
				
				// Add only one entry so break now
				break;
			}
			if (newsSourceResult != null) {
				PrintWriter responseWriter = resp.getWriter();
				responseWriter.write(newsSourceResult.toString());
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
