package com.salesmanager.core.business.system.service;

import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.system.dao.ModuleConfigurationDao;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.business.system.model.ModuleConfig;
import com.salesmanager.core.utils.CacheUtils;

@Service("moduleConfigurationService")
public class ModuleConfigurationServiceImpl extends
		SalesManagerEntityServiceImpl<Long, IntegrationModule> implements
		ModuleConfigurationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleConfigurationServiceImpl.class);


	
	private ModuleConfigurationDao integrationModuleDao;
	
	@Autowired
	public ModuleConfigurationServiceImpl(
			ModuleConfigurationDao integrationModuleDao) {
			super(integrationModuleDao);
			this.integrationModuleDao = integrationModuleDao;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<IntegrationModule> getIntegrationModules(String module) {
		
		
		List<IntegrationModule> modules = null;
		try {
			
			CacheUtils cacheUtils = CacheUtils.getInstance();
			modules = (List<IntegrationModule>) cacheUtils.getFromCache("INTEGRATION_M)" + module);
			if(modules==null) {
				modules = integrationModuleDao.getModulesConfiguration(module);
				//set json objects
				for(IntegrationModule mod : modules) {
					
					String regions = mod.getRegions();
					if(regions!=null) {
						Object objRegions=JSONValue.parse(regions); 
						JSONArray arrayRegions=(JSONArray)objRegions;
						Iterator i = arrayRegions.iterator();
						while(i.hasNext()) {
							mod.getRegionsSet().add((String)i.next());
						}
					}
					
					String config = mod.getConfiguration();
					if(config!=null) {
						
						ObjectMapper mapper = new ObjectMapper();
						ModuleConfig conf = mapper.readValue(config, ModuleConfig.class);
						mod.setModuleConfig(conf);
					}


				}
				cacheUtils.putInCache(modules, "INTEGRATION_M)" + module);
			}

		} catch (Exception e) {
			LOGGER.error("getIntegrationModules()", e);
		}
		return modules;
		
		
	}
	

	



}
