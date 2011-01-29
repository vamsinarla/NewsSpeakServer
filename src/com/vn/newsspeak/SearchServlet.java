package com.vn.newsspeak;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.log.Log;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

public class SearchServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(SearchServlet.class.getName());
	
	/**
	 * Default UID
	 */
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
		// GET params
		String searchTerm = req.getParameter("searchTerm");
		
		if (searchTerm == null) {
			Log.debug("Invalid GET parameters");
			return;
		}
		// lower case
		searchTerm = searchTerm.toLowerCase();
		
		// Get the Datastore Service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Prepare the query
		Query query = new Query("NewsSource");
		
		// Fetch the results
		PreparedQuery preparedQuery = datastore.prepare(query);
		
		// JSONObject to store the results
		JSONObject results = new JSONObject();
		
		try {
			// Indicate a successful search, not necessarily one with non-zero results.
			results.put("success", true);
			// Insert the results themselves
			JSONArray itemArray = new JSONArray();
			String resultTitle;
			for (Entity singleResult: preparedQuery.asIterable()) {
				resultTitle = (String) singleResult.getProperty("title");
				// Actual matching is done here
				if (resultTitle.toLowerCase().contains(searchTerm)) {
					itemArray.put(resultTitle);
				}
			}
			// Put the number of returned results and the results
			results.put("resultsSize", itemArray.length());
			results.put("results", itemArray);
		} catch (JSONException e) {
			e.printStackTrace();
			log.severe("Failure searching for " + searchTerm);
			return;
		}
		
		// Return the results to the client
		PrintWriter respWriter = resp.getWriter();
		respWriter.write(results.toString());
	}
}