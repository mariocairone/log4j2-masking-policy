package com.mariocairone.log4j2.core.data.finder;

import java.io.CharArrayReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.mariocairone.log4j2.core.data.finder.AbstractFinder;

public class XmlFinder extends AbstractFinder {

	public static final char START_XML_DELIMITER = '<';
	public static final char END_XML_DELIMITER = '>';
	private static final XMLInputFactory factory = XMLInputFactory.newInstance();
	
	@Override
	protected boolean isStartDelimiter(char character) {
		return character == START_XML_DELIMITER ;
	}

	@Override
	protected boolean found(char[] text) {
	   
	    try {
			XMLStreamReader streamReader = 
						factory.createXMLStreamReader(new CharArrayReader(text));
		      while (streamReader.hasNext()) {
		    	  streamReader.next();
		      }
			 return true;
		} catch (XMLStreamException e) {
			return false;
		}
	    
	    

	}

	@Override
	protected char getEndDelimiter(char startDelimiter) {
		return END_XML_DELIMITER;
	}

}
