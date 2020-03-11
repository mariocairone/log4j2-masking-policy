package com.mariocairone.log4j2.core.policy.model;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

@Plugin(name = "MaskPolicy",
category = Node.CATEGORY,
printObject = true)
public class MaskPolicy {
	
	private static boolean DEFAULT_ENABLED = true;
	
	private String type;
	
	private boolean enabled;
	
	private Exclusions exlusions;
	
	private MaskPolicy(Builder builder) {
		this.type = builder.type;
		this.enabled = builder.enabled;
		this.exlusions = builder.exlusions;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Exclusions getExlusions() {
		return exlusions;
	}

	public void setExlusions(Exclusions exlusions) {
		this.exlusions = exlusions;
	}


	@PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }

	public static class Builder implements org.apache.logging.log4j.core.util.Builder<MaskPolicy> {
	
		@PluginBuilderAttribute
		@Required(message ="No type provided for MaskPolicy")
		private String type;
		
		@PluginBuilderAttribute
		private boolean enabled = DEFAULT_ENABLED;
		
		@PluginElement(value = "Exclusions")
		private Exclusions exlusions;
		
		@Override
		public MaskPolicy build() {
			// TODO Auto-generated method stub
			return new MaskPolicy(this);
		}
		
	   public Builder setType(String type) {
	        this.type = type;
	        return this;
	   }
	   
	   public String getType() {
		   return type;
	   }
	
	   public boolean isEnabled() {
		   return enabled;
	   }
	
	   public Builder setEnabled(boolean enabled) {
		   this.enabled = enabled;
		   return this;
	   }
	
	   public Exclusions getExlusions() {
		   return exlusions;
	   }
	
	   public Builder setExlusions(Exclusions exlusions) {
		   this.exlusions = exlusions;
		   return this;
	   }
		
	}
	
	    
	    
	
}
