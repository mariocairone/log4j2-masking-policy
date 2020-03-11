package com.mariocairone.log4j2.core.data.finder;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.mariocairone.log4j2.core.data.finder.AbstractFinder;

public class JsonFinder extends AbstractFinder {

	public static final char START_JSON_OBJECT_DELIMITER = '{';
	public static final char END_JSON_OBJECT_DELIMITER = '}';
	public static final char START_JSON_ARRAY_DELIMITER = '[';
	public static final char END_JSON_ARRAY_DELIMITER = ']';

	private static final JsonFactory jsonFactory = new JsonFactory();


	protected boolean isStartDelimiter(char character) {

		if (character == START_JSON_OBJECT_DELIMITER || character == START_JSON_ARRAY_DELIMITER)
			return true;
		return false;

	}
	
	protected boolean found(char[] text) {
		
		try {
			final JsonParser parser =jsonFactory.createParser(text, 0, text.length);
		      while (parser.nextToken() != null) {
		      }
			 return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	protected char getEndDelimiter(char startDelimiter) {
    	return startDelimiter == START_JSON_OBJECT_DELIMITER ? END_JSON_OBJECT_DELIMITER
				: END_JSON_ARRAY_DELIMITER;
    }


}
