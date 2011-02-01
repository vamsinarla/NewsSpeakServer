package com.vn.newsspeak;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONObject;

public class FeaturedSourcesServlet extends HttpServlet {

	/**
	 * default id
	 */
	private static final long serialVersionUID = 1L;
	private static final int MIN_LIMIT = 10;
	private static final int FETCH_LIMIT = 20;

	@SuppressWarnings("unused")
	public void doGet (HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
		// GET the parameters
		String country = req.getParameter("country");
		String language = req.getParameter("language");
		
		// upper case
		country = country.toUpperCase();
		
		// Make the datastore call to get the sources for this country
		// Get the Datastore Service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		ArrayList<Entity> resultsList = new ArrayList<Entity>();
		
		// Populate the user with the data sources for his country
		// Prepare the query for country
		Query query = new Query("NewsSource");
		query.addFilter("country", Query.FilterOperator.EQUAL, country);
		
		// Fetch the results
		PreparedQuery countryQuery = datastore.prepare(query);
		resultsList = (ArrayList<Entity>) countryQuery.asList(withLimit(FETCH_LIMIT));
		
		// Populate the user with the preferred data sources
		// Fetch preferred source
		query = new Query("NewsSource");
		query.addFilter("preferred", Query.FilterOperator.EQUAL, true);
		
		PreparedQuery preferredQuery = datastore.prepare(query);
		resultsList.addAll(preferredQuery.asList(withLimit(FETCH_LIMIT)));
		
		// If there aren't as many items then populate the rest with US data sources
		if (resultsList.size() < MIN_LIMIT) {
			// Prepare the query for US
			query = new Query("NewsSource");
			query.addFilter("country", Query.FilterOperator.EQUAL, "US");
			
			preferredQuery = datastore.prepare(query);
			resultsList.addAll(preferredQuery.asList(withLimit(FETCH_LIMIT)));
		}
		
		JSONObject sources = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject newsSourceResult = new JSONObject();
		
		try {
			for (int index = 0; index < resultsList.size(); ++index) {
				newsSourceResult = NewsSource.createJSON(resultsList.get(index));
				array.put(newsSourceResult);
			}
			sources.put("sources", array);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		PrintWriter responseWriter = resp.getWriter();
		responseWriter.write(sources.toString());
	}
}
