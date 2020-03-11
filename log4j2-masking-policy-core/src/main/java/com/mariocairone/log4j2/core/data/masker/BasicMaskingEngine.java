package com.mariocairone.log4j2.core.data.masker;

import java.util.regex.Pattern;

import com.mariocairone.log4j2.api.data.masker.MaskingEngine;

public class BasicMaskingEngine implements MaskingEngine {


	private static final Pattern digits = Pattern.compile("\\d");
	private static final Pattern capitalLetters = Pattern.compile("[A-Z]");
	private static final Pattern nonSpecialCharacters = Pattern.compile("[^X\\s!-/:-@\\[-`{-~]");
	
	public String maskString(String value) {
		String tmpMasked = digits.matcher(value).replaceAll("*");
		tmpMasked = capitalLetters.matcher(tmpMasked).replaceAll("X");
		return nonSpecialCharacters.matcher(tmpMasked).replaceAll("x");
	}

	public String maskNumber(String value) {
		return value.replaceAll("[0-9]", "*");
	}

}
