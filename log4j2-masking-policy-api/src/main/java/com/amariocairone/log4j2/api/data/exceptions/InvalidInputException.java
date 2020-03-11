package com.amariocairone.log4j2.api.data.exceptions;

public class InvalidInputException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidInputException() {
		super();
		
	}

	public InvalidInputException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public InvalidInputException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public InvalidInputException(String message) {
		super(message);
		
	}

	public InvalidInputException(Throwable cause) {
		super(cause);
		
	}

	
	
	
}
