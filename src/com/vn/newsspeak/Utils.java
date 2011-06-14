package com.vn.newsspeak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
	/**
	 * Get a string from an inputStream
	 * @param inputStream
	 * @return
	 * @throws IOException 
	 */
	public static String getStringFromInputStream(InputStream inputStream) 
	throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder builder = new StringBuilder();
		String oneLine;
		
		while ((oneLine = reader.readLine()) != null) {
			builder.append(oneLine);
		}
		reader.close();
		
		return builder.toString();
	}
}
