package com.shopizer.search.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {
	
	private static Config configurations = null;
	private static PropertiesConfiguration config = null;
	
	static {
		
		try {
			
			config = new PropertiesConfiguration("personalization.properties");
			//config.setReloadingStrategy(new FileChangedReloadingStrategy());
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public static Config getInstance() {
		if(configurations==null) {
			initConfig();
		}
		return configurations;
	}
	
	private static synchronized void initConfig() {
		
		configurations = new Config();
	}
	
	public Configuration getConfiguration() {
		return config;
	}

}
