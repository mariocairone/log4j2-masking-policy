package com.mariocairone.log4j2.core.data.utils;

import java.util.Arrays;

public class CharsUtils {
	
	public static char[] slice(char[] characters, int startIndex, int endIndex) {
		return Arrays.copyOfRange(characters, startIndex, endIndex + 1);
	}	
	


	public static char[] replace(char[] original, int start, int end, char[] masked) {
		
		int delta = masked.length - (end -start +1);
		int length = original.length + delta;

		char[] result = new char[length];
		
		System.arraycopy(original, 0, result, 0, start);
		System.arraycopy(masked, 0, result, start, masked.length);
		System.arraycopy(original, end, result, start + masked.length -1, original.length - end );

		
		return result;
	} 	
	
    public static boolean isEmptyJson(char[] text) {
    	
		if(text != null && 
				text.length > 1 && 
						(text[text.length -1] == '}' && text[0] == '{'
							|| text[text.length -1] == ']' && text[0] == '['))  {
			
			for(int i = 1 ; i < text.length - 1; i++ ) {
				if(!Character.isWhitespace(text[i]))
					return false;
			}
			
			return true;
		}
	
	return false;
	
}
	
}
