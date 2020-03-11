package com.mariocairone.log4j2.core.data.utils;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TestUtils {

	  public static String getString(String tagName, Element element) {
	        NodeList list = element.getElementsByTagName(tagName);
	        if (list != null && list.getLength() > 0) {
	            NodeList subList = list.item(0).getChildNodes();

	            if (subList != null && subList.getLength() > 0) {
	                return subList.item(0).getNodeValue();
	            }
	        }

	        return null;
	    }
	  
	  public static String getResourceFileAsString(String resourceFileName) {
		    InputStream is = TestUtils.class.getClassLoader().getResourceAsStream(resourceFileName);
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    return reader.lines().collect(Collectors.joining("\n"));
		}
	  
	  public static  Document toXmlDocument(char[] str)
	            throws ParserConfigurationException, SAXException, IOException {
	 
	        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
	                .newInstance();
	        docBuilderFactory.setNamespaceAware(true);
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document document = docBuilder.parse(new InputSource(new CharArrayReader(
	                str)));
	 
	        return document;
	    }
	  
		public static byte[] toByteArray(char[] chars) {
		
		CharBuffer charBuffer = CharBuffer.wrap(chars);
		Charset charset = Charset.forName("UTF-8");
		ByteBuffer byteBuffer = charset.encode(charBuffer);
		return byteBuffer.array();
		
	}
	
	public static char[] toCharArray(byte[] chars) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(chars);
		Charset charset = Charset.forName("UTF-8");
		CharBuffer charBuffer = charset.decode(byteBuffer);
		return charBuffer.array();
		
	}	
	
}