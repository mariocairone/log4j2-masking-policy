package com.mariocairone.log4j2.core.policy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ObjectMessage;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;

import com.mariocairone.log4j2.api.data.converters.MessageConverter;
import com.mariocairone.log4j2.api.data.masker.Masker;
import com.mariocairone.log4j2.core.policy.model.Converters;
import com.mariocairone.log4j2.core.policy.model.MaskPolicy;

/**
 * Rewrites log event masking sensitive date.
 * 
 * @since 1.0.0
 */
@Plugin(name = "MaskPolicies", elementType = "rewritePolicy", printObject = true, category = "Core")
public class MaskingPolicy implements RewritePolicy {

//	private static final Logger LOGGER = LogManager.getLogger(RewritePolicy.class);

	private final List<MessageConverter> messageConverters; 
	
	private final List<Masker> maskers;
	
	@PluginFactory
	public static MaskingPolicy createPolicy(
			@PluginElement("MaskPolicy") 
			@Required(message ="No MaskPolicy provided for MaskPolicies")
			final MaskPolicy[] policies,
			@PluginElement("Converter") 
			final Converters converters
			
			) {

		return new MaskingPolicy(Arrays.asList(policies), converters);
	}


	private MaskingPolicy(final List<MaskPolicy> policies,
						  final Converters converters) {
		super();
		this.maskers = new ArrayList<>();
		for (MaskPolicy policy : policies) { 			
			maskers.add(MaskerFactory.createMasker(policy));		
		}

		this.messageConverters = new ArrayList<>();
		if(converters != null)
			converters.getConverters()
				.stream()
					.forEach( converter -> messageConverters.add(MessageConverterFactory.createConverter(converter)));
		
	}

	@Override
	public LogEvent rewrite(final LogEvent event) {

		
		Message outputMessage = convertAndMaskMessage(event.getMessage());
	              
	        
		return new Log4jLogEvent.Builder(event).setMessage(outputMessage).build();
	
	}	
	

	
	private String mask(final String formattedMessage) {
		
		char[] content = formattedMessage.toCharArray();
		
		for (Masker masker : maskers) { 	
			content = masker.mask(content);
		}
		
		return new String(content);
	} 
	
	@SuppressWarnings("unchecked")
	private Message convertAndMaskMessage(Message message){
		
		String maskedMessage = mask(message.getFormattedMessage());
		for(MessageConverter converter: messageConverters) {
			if(converter.isSupported(message)) {
				return  converter.convertMessage(message, maskedMessage);
			}
		}
		
		if(message instanceof MapMessage) {
			 MapMessage<?,Object> mapMessage = (MapMessage<?,Object>) message; 
			 return ((MapMessage<?,Object>) message).newInstance(mapMessage.getData());
		} else if(message instanceof ObjectMessage ) {
			return new ObjectMessage(maskedMessage);
		} else {
			return ParameterizedMessageFactory.INSTANCE.newMessage(maskedMessage,message.getParameters());
		}
		
	}
	

	
	
}