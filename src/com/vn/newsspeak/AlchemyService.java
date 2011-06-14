package com.vn.newsspeak;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AlchemyService {
	
	private static final String apiKey = "3dd078fdf99f03a052aaa48579a3db2a4e9520a9";
	private static final String alchemyApiUrl = "http://access.alchemyapi.com/calls/url/";
	
	public static final String OUTPUT_MODE = "outputMode";
	public static final String URL = "url";
	
	AlchemyService() {
		
	}
	
	String makeApiCall(String methodName, HashMap<String, String> apiArgs) {
		InputStream responseStream = null;
		String result = "";
		
		try {
			URL feedUrl = new URL(alchemyApiUrl + methodName);
			URLConnection conn = feedUrl.openConnection();
			
			// Make the connection and the api call
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(makeApiArgs(apiArgs));
		    wr.flush();
		    
		    responseStream = conn.getInputStream();
		    result = Utils.getStringFromInputStream(responseStream);
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private String makeApiArgs(HashMap<String, String> apiArgs) throws UnsupportedEncodingException {
		String args = "";
		
		apiArgs.put("apikey", apiKey);
		
		// Get a set of the entries
		 Iterator<?> iter = apiArgs.entrySet().iterator();
		// Display elements
		while(iter.hasNext()) {
			Map.Entry me = (Map.Entry)iter.next();
			
			args += URLEncoder.encode((String) me.getKey(), "UTF-8") + "=" 
					+ URLEncoder.encode((String) me.getValue(), "UTF-8") + "&";
		}
  
		return args;
	}
}
