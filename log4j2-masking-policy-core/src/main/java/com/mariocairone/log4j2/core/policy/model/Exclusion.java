package com.mariocairone.log4j2.core.policy.model;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

@Plugin(name = "Exclusion",
category = Node.CATEGORY,
printObject = true)
public class Exclusion {

	private String value;

	@PluginFactory
	public static Exclusion createExlusion(
			@PluginAttribute("value") 
			@Required(message ="No value provided for Exclusion")
			final String value
			) {
		return new Exclusion(value);
	}
	
	public Exclusion(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
	
}
