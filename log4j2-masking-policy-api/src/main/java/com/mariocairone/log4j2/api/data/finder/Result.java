package com.mariocairone.log4j2.api.data.finder;

import java.util.ArrayList;
import java.util.List;

import com.mariocairone.log4j2.api.data.finder.Text;

public class Result {

	private List<Text> texts;
	
	public Result() {
		super();
		texts = new ArrayList<Text>();
	}

	public Text get(int i) {
		return texts.get(i);
	}

	public List<Text> texts() {
		return texts;
	}
	
	public void add(Text text) {
		texts.add(text);
	}
	
	public boolean isEmpty() {
		return texts.isEmpty();
	}
	
	public int size() {
		return texts.size();
	}
	
	
	
}
