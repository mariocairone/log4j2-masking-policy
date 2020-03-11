package com.mariocairone.log4j2.core.policy.model;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
@Plugin(name = "Converters",
category = Node.CATEGORY,
printObject = true)
public class Converters {

	private List<Converter> converters;
	
	@PluginFactory
	public static Converters createExlusions(
			@PluginElement("Converter") 
			@Required(message ="No Exclusion provided for Exclusions")
			final Converter[] converters
			) {
		return new Converters(Arrays.asList(converters));
	}

	public Converters(List<Converter> converters) {
		super();
		this.converters = converters;
	}

	public List<Converter> getConverters() {
		return converters;
	}

	public void setConverters(List<Converter> converters) {
		this.converters = converters;
	}
	
	


	
}
