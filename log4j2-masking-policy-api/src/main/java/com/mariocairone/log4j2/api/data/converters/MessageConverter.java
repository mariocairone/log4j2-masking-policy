package com.mariocairone.log4j2.api.data.converters;

import org.apache.logging.log4j.message.Message;

public interface MessageConverter {
		
	public Message convertMessage(Message message,String content);
	
	public boolean isSupported(Message message);
	
}
