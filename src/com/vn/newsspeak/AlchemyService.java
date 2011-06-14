package com.vn.newsspeak;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Dictionary;

public class AlchemyService {
	
	private static final String apiKey = "3dd078fdf99f03a052aaa48579a3db2a4e9520a9";
	private static final String alchemyApiUrl = "http://access.alchemyapi.com/calls/url/";
	
	AlchemyService() {
		
	}
	
	String makeApiCall(String methodName, Dictionary methodArgs) {
		InputStream responseStream = null;
		String result = "";
		
		try {
			URL feedUrl = new URL(alchemyApiUrl + methodName);
			URLConnection conn = feedUrl.openConnection();
			
			// Make the connection and the api call
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(methodArgs);
		    wr.flush();
		    
		    responseStream = conn.getInputStream();
		    result = responseStream.toString();
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
