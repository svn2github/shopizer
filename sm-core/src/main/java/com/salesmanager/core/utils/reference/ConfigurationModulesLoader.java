package com.salesmanager.core.utils.reference;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;


public class ConfigurationModulesLoader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationModulesLoader.class);
	

	
	public static String toJSONString(Map<String,IntegrationConfiguration> configurations) throws Exception {
		
		StringBuilder jsonModules = new StringBuilder();
		jsonModules.append("[");
		int count = 0;
		for(Object key : configurations.keySet()) {
			
			String k = (String)key;
			IntegrationConfiguration c = (IntegrationConfiguration)configurations.get(k);
			
			String jsonString = c.toJSONString();
			jsonModules.append(jsonString);
			
			count ++;
			if(count<configurations.size()) {
				jsonModules.append(",");
			}
		}
		jsonModules.append("]");
		return jsonModules.toString();
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String,IntegrationConfiguration> loadIntegrationConfigurations(String value) throws Exception {
		
		
		Map<String,IntegrationConfiguration> modules = new HashMap<String,IntegrationConfiguration>();
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			

            Map[] objects = mapper.readValue(value, Map[].class);
            
            for(int i = 0; i < objects.length; i++) {
            	
            	
            	Map object = objects[i];
            	
            	IntegrationConfiguration configuration = new IntegrationConfiguration();
            	
            	String moduleCode = (String)object.get("moduleCode");
            	configuration.setActive((Boolean)object.get("active"));
            	configuration.setEnvironment((String)object.get("environment"));
            	configuration.setModuleCode(moduleCode);
            	
            	modules.put(moduleCode, configuration);

            	
            	Map<String,String> confs = (Map<String,String> )object.get("integrationKeys");
            	configuration.setIntegrationKeys(confs);
            	
            	Map<String,String[]> options = (Map<String,String[]> )object.get("integrationOptions");
            	configuration.setIntegrationOptions(options);

            	
            }
            
            return modules;

  		} catch (Exception e) {
  			throw new ServiceException(e);
  		}
  		

	
	}

}