package com.mariocairone.log4j2.core.policy;

import com.amariocairone.log4j2.api.data.exceptions.InvalidInputException;
import com.mariocairone.log4j2.api.data.converters.MessageConverter;
import com.mariocairone.log4j2.core.policy.model.Converter;

public class MessageConverterFactory {

	
	public static MessageConverter createConverter(Converter converter) {
		String className = converter.getClassName();
		try {
			return (MessageConverter) Class.forName(className).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new InvalidInputException("Error instanciating class " + className,e);		
		}
			
	}
}
