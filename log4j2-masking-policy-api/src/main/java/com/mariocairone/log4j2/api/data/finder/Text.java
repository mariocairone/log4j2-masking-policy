package com.mariocairone.log4j2.api.data.finder;

import com.mariocairone.log4j2.api.data.finder.Text;

public class Text {

	private char[] chars;
	
	private int start;
	
	private int end;
	
	private int length;
	
	private Text(char[] text, int start) {
		super();
		this.chars = text;
		this.start = start;
		this.end = start + text.length -1;
		this.length = end - start + 1 ; 
	}


	public char[] chars() {
		return chars;
	}	
	
	public int start() {
		return start;
	}

	public int end() {
		return end;
	}

	public int length() {
		return length;
	}
	
	public static Text of(char[] text, int start) {
		
		return new Text(text,  start) ;
	}


	@Override
	public String toString() {
		return chars != null ? new String(chars) : null;
	}
	
	
	
	
	
}
