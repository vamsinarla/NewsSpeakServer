package com.alchemyapi.test;

import com.alchemyapi.api.*;


import org.w3c.dom.Document;
import java.io.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

public class AlchemyAPITest extends TestCase {
	
	private AlchemyAPI alchemyObj;
	
	protected void setUp() throws Exception {
		alchemyObj = AlchemyAPI.GetInstanceFromFile("api_key.txt");
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testURLGetRankedNamedEntitiesString() {
		try {
			AlchemyAPI_NamedEntityParams entityParams = new AlchemyAPI_NamedEntityParams();
			entityParams.setDisambiguate(true);
			String test = getStringFromDocument(alchemyObj.URLGetRankedNamedEntities("http://www.cnn.com/2010/US/06/07/gulf.oil.spill/index.html?hpt=T1", entityParams));
			assertTrue(test.indexOf("disambiguated") != -1);
			
			entityParams = new AlchemyAPI_NamedEntityParams();
			entityParams.setDisambiguate(false);
			test = getStringFromDocument(alchemyObj.URLGetRankedNamedEntities("http://www.cnn.com/2010/US/06/07/gulf.oil.spill/index.html?hpt=T1", entityParams));
			assertTrue(test.indexOf("disambiguated") == -1);
		}
		catch(Throwable t) {
			fail("Exception thrown");
		}
	}

	public void testHTMLGetRankedNamedEntitiesStringStringAlchemyAPI_NamedEntityParams() {
		try {
			String htmlDoc = getFileContents("data/example.html");
			String htmlDoc2 = getFileContents("data/example3.html");
			
			String test = getStringFromDocument(alchemyObj.HTMLGetRankedNamedEntities(htmlDoc2, "http://www.test.com/"));
			assertTrue(test.indexOf("disambiguated") != -1);
			
			AlchemyAPI_NamedEntityParams entityParams = new AlchemyAPI_NamedEntityParams();
			entityParams.setDisambiguate(false);
			test = getStringFromDocument(alchemyObj.HTMLGetRankedNamedEntities(htmlDoc2, "http://www.test.com/", entityParams));
			assertTrue(test.indexOf("disambiguated") == -1);
			
			entityParams = new AlchemyAPI_NamedEntityParams();
			entityParams.setQuotations(true);
			test = getStringFromDocument(alchemyObj.HTMLGetRankedNamedEntities(htmlDoc2, "http://www.test.com/", entityParams));
			assertTrue(test.indexOf("<quotation>") != -1);
			
			entityParams = new AlchemyAPI_NamedEntityParams();
			entityParams.setQuotations(false);
			test = getStringFromDocument(alchemyObj.HTMLGetRankedNamedEntities(htmlDoc2, "http://www.test.com/", entityParams));
			assertTrue(test.indexOf("<quotation>") == -1);
			
			entityParams = new AlchemyAPI_NamedEntityParams();
			test = getStringFromDocument(alchemyObj.HTMLGetRankedNamedEntities(htmlDoc2, "http://www.test.com/", entityParams));
			assertTrue(test.indexOf("<entity>") != -1);
			
			entityParams = new AlchemyAPI_NamedEntityParams();
			entityParams.setXPath("//a");
			test = getStringFromDocument(alchemyObj.HTMLGetRankedNamedEntities(htmlDoc2, "http://www.test.com/", entityParams));
			assert(test.indexOf("<entity>") == -1);
		}
		catch(Throwable t) {
			t.printStackTrace();
			fail("Exception thrown");
		}
		
	}

	public void testTextGetRankedNamedEntitiesStringAlchemyAPI_NamedEntityParams() {
		try {
			String test = getStringFromDocument(alchemyObj.TextGetRankedNamedEntities(
	            "Hello there, my name is Bob Jones.  I live in the United States of America.  " +
	            "Where do you live, Fred?"));
			assertTrue(test.indexOf("disambiguated") != -1);
			
			AlchemyAPI_NamedEntityParams entityParams = new AlchemyAPI_NamedEntityParams();
			entityParams.setDisambiguate(false);
			test = getStringFromDocument(alchemyObj.TextGetRankedNamedEntities(
		            "Hello there, my name is Bob Jones.  I live in the United States of America.  " +
		            "Where do you live, Fred?", entityParams));
			assertTrue(test.indexOf("disambiguated") == -1);
			
			entityParams = new AlchemyAPI_NamedEntityParams();
			entityParams.setCustomParameters("disambiguate","0");
			test = getStringFromDocument(alchemyObj.TextGetRankedNamedEntities(
		            "Hello there, my name is Bob Jones.  I live in the United States of America.  " +
		            "Where do you live, Fred?", entityParams));
			assertTrue(test.indexOf("disambiguated") == -1);
			

			
		}
		catch(Throwable t) {
			t.printStackTrace();
			fail("Exception thrown");
		}
	}
	
	public void testCategory() {
		try {
			String htmlDoc = getFileContents("data/example.html");
			String htmlDoc2 = getFileContents("data/example2.html");
			String htmlDoc2_nolinks = getFileContents("data/example2_nolinks.html");
			
			
			String test = getStringFromDocument(alchemyObj.HTMLGetCategory(htmlDoc2_nolinks, "http://www.test.com/"));
			assertTrue(test.indexOf("category") != -1);
		}
		catch(Throwable t) {
			t.printStackTrace();
			fail("Exception thrown");
		}
	}
	
	public void testKeywords() {
		try {
			String htmlDoc = getFileContents("data/example.html");
			String htmlDoc2 = getFileContents("data/example2.html");
			String htmlDoc2_nolinks = getFileContents("data/example2_nolinks.html");
			
			String test = getStringFromDocument(alchemyObj.HTMLGetRankedKeywords(htmlDoc2_nolinks, "http://www.test.com/"));
			//System.out.print(test);
			//assertTrue(test.indexOf("<text>") == -1);
			
			AlchemyAPI_KeywordParams keywordParams = new AlchemyAPI_KeywordParams();
			keywordParams.setShowSourceText(true);
			test = getStringFromDocument(alchemyObj.HTMLGetRankedKeywords(htmlDoc2_nolinks, "http://www.test.com/", keywordParams));
			assertTrue(test.indexOf("<text>") != -1);
			
		}
		catch(Throwable t) {
			t.printStackTrace();
			fail("Exception thrown");
		}
	}
	
	public void testText() {
		try {
			String htmlDoc = getFileContents("data/example.html");
			String htmlDoc2 = getFileContents("data/example2.html");
			String htmlDoc2_nolinks = getFileContents("data/example2_nolinks.html");
			
			String test = getStringFromDocument(alchemyObj.HTMLGetText(htmlDoc2, "http://www.test.com/"));
			assertTrue(test.indexOf("<a") == -1);
			
			AlchemyAPI_TextParams textParams = new AlchemyAPI_TextParams();
			textParams.setExtractLinks(true);
			test = getStringFromDocument(alchemyObj.HTMLGetText(htmlDoc2, "http://www.test.com/", textParams));
			assertTrue(test.indexOf("<text>") != -1);
			
		}
		catch(Throwable t) {
			t.printStackTrace();
			fail("Exception thrown");
		}
	}
	

    private static String getFileContents(String filename)
            throws IOException, FileNotFoundException
	{
	    File file = new File(filename);
	    StringBuilder contents = new StringBuilder();
	
	    BufferedReader input = new BufferedReader(new FileReader(file));
	
	    try {
	        String line = null;
	
	        while ((line = input.readLine()) != null) {
	            contents.append(line);
	            contents.append(System.getProperty("line.separator"));
	        }
	    } finally {
	        input.close();
	    }
	
	    return contents.toString();
	}

	// utility method
	private static String getStringFromDocument(Document doc) {
	    try {
	        DOMSource domSource = new DOMSource(doc);
	        StringWriter writer = new StringWriter();
	        StreamResult result = new StreamResult(writer);
	
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.transform(domSource, result);
	
	        return writer.toString();
	    } catch (TransformerException ex) {
	        ex.printStackTrace();
	        return null;
	    }
	}

}
