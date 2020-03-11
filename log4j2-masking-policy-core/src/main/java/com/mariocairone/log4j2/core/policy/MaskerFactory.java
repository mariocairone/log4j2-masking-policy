package com.mariocairone.log4j2.core.policy;

import java.util.List;
import java.util.stream.Collectors;

import com.mariocairone.log4j2.api.data.masker.Masker;
import com.mariocairone.log4j2.core.data.masker.JsonMasker;
import com.mariocairone.log4j2.core.data.masker.XmlMasker;
import com.mariocairone.log4j2.core.policy.model.Exclusions;
import com.mariocairone.log4j2.core.policy.model.MaskPolicy;

public class MaskerFactory {

	private static String JSON_TYPE = "json"; 
	private static String XML_TYPE = "xml"; 

	
	public static Masker createMasker(MaskPolicy policy) {
				
		String type = policy.getType();
		boolean enabled = policy.isEnabled();
		Exclusions exclusions = policy.getExlusions(); 
		
		List<String> values = getExclusions(exclusions);
		
		if(type.equalsIgnoreCase(JSON_TYPE)) {			
			return createJsonMasker(values,enabled);	
		} else if(type.equalsIgnoreCase(XML_TYPE)) {
			return createXmlMasker(values,enabled);	
		}
		
			
		throw new IllegalArgumentException("MaskPolicy type not supported");
	}

	private static JsonMasker createJsonMasker(List<String> withelist, boolean enabled) {
						
		return new JsonMasker(withelist,enabled);
	}


	private static XmlMasker createXmlMasker(List<String> withelist, boolean enabled) {
						
		return new XmlMasker(withelist,enabled);		
	}
	
	private static List<String> getExclusions(Exclusions exclusions) {
		List<String> withelist = exclusions.getExclusions()
			.stream()
				.map(exclusion -> exclusion.getValue())
					.collect(Collectors.toList());
		return withelist;
	}
	
}
