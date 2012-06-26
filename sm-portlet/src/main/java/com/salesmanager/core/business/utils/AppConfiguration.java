package com.salesmanager.core.business.utils;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class AppConfiguration {
	
	
	public Properties properties = new Properties();
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public AppConfiguration() {}
	
	public String getProperty(String propertyKey) {
		
		return properties.getProperty(propertyKey);
		
		
	}

}
