package com.mariocairone.log4j2.core.policy.model;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

@Plugin(name = "Exclusions",
category = Node.CATEGORY,
printObject = true)
public class Exclusions {

	private List<Exclusion> exclusions;
	
	@PluginFactory
	public static Exclusions createExlusions(
			@PluginElement("Exclusion") 
			@Required(message ="No Exclusion provided for Exclusions")
			final Exclusion[] exclusions
			) {
		return new Exclusions(Arrays.asList(exclusions));
	}

	public Exclusions(List<Exclusion> exclusions) {
		super();
		this.exclusions = exclusions;
	}


	public List<Exclusion> getExclusions() {
		return exclusions;
	}

	public void setExclusions(List<Exclusion> exclusions) {
		this.exclusions = exclusions;
	}
	
	
	
}
