package com.mariocairone.log4j2.core.policy.model;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

@Plugin(name = "Converter",
category = Node.CATEGORY,
printObject = true)
public class Converter {

	private String className;

	@PluginFactory
	public static Converter createConverter(
			@PluginAttribute("className") 
			@Required(message ="No value provided for Exclusion")
			final String className
			) {
		return new Converter(className);
	}
	
	public Converter(String className) {
		super();
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	
	
}
