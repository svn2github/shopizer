package com.salesmanager.core.utils;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class CoreConfiguration {
	

	public Properties properties = new Properties();
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public CoreConfiguration() {}
	
	public String getProperty(String propertyKey) {
		
		return properties.getProperty(propertyKey);
		
		
	}

}
