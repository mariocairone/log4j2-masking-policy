package com.mariocairone.log4j2.api.data.masker;

public interface MaskingEngine {
	
	
	public String maskNumber(String input);
	
	public String maskString(String input);

}
