package com.salesmanager.core.business.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.system.dao.MerchantConfigurationDao;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.model.Module;

@Service("merchantConfigurationService")
public class MerchantConfigurationServiceImpl extends
		SalesManagerEntityServiceImpl<Long, MerchantConfiguration> implements
		MerchantConfigurationService {

	
	private MerchantConfigurationDao merchantConfigurationDao;
	
	@Autowired
	public MerchantConfigurationServiceImpl(
			MerchantConfigurationDao merchantConfigurationDao) {
			super(merchantConfigurationDao);
			this.merchantConfigurationDao = merchantConfigurationDao;
	}
	

	public MerchantConfiguration getMerchantConfiguration(String key, MerchantStore store) throws ServiceException {
		
		return merchantConfigurationDao.getMerchantConfiguration(key, store);
		
	}
	
	public Map<String,IntegrationConfiguration> getMerchantIntegrationConfigurations(MerchantStore store, Module module) throws ServiceException {
		
		MerchantConfiguration configuration = getMerchantConfiguration(module.name(), store);
		Map<String,IntegrationConfiguration> integrations = null;
		if(configuration != null) {
			
			String value = configuration.getValue();
			if(!StringUtils.isBlank(value)) {
				try {
					integrations = getMerchantIntegrationConfigurations(value);
				
				} catch (Exception e) {
					throw new ServiceException(e);
				}
			}
			
		}
		
		return integrations;
		
	}
	

	@SuppressWarnings("null")
	public IntegrationConfiguration getMerchantIntegrationConfiguration(MerchantStore store, Module module, String moduleName) throws ServiceException {
		
		Map<String,IntegrationConfiguration> integrations = getMerchantIntegrationConfigurations(store,module);
		
		IntegrationConfiguration config = null;
		if(integrations ==null) {
			
			 config = integrations.get(moduleName);
			
		}
		
		return config;
	}
	
	public void addMerchantIntegrationConfiguration(MerchantStore store, Module module, IntegrationConfiguration configuration) throws ServiceException {
		
		try {
			

			//get current merchant configuration
			MerchantConfiguration config = getMerchantConfiguration(module.name(), store);
			if(config==null) {
				config = new MerchantConfiguration();
				config.setMerchantSore(store);
				config.setKey(module.name());
			}
			
			String value = config.getValue();
			
			Map<String,IntegrationConfiguration> integrations = null;
			if(!StringUtils.isBlank(value)) {
				integrations = getMerchantIntegrationConfigurations(value);
			}
			if(integrations==null){
				integrations = new HashMap<String,IntegrationConfiguration>();
			}
			
			integrations.put(configuration.getModuleCode(), configuration);
			String encoded = encodeIntegrationConfiguration(integrations);
			
			config.setValue(encoded);
			
			if(config.getId()==null || config.getId()==0){
				merchantConfigurationDao.save(config);
			} else {
				merchantConfigurationDao.update(config);
			}
			
		
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	private Map<String,IntegrationConfiguration> getMerchantIntegrationConfigurations(String value) throws Exception {
		
		
		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		List<IntegrationConfiguration> entries = (List<IntegrationConfiguration>) mapper.readValue(value,IntegrationConfiguration.class);
		Map<String,IntegrationConfiguration> integrations = new HashMap<String,IntegrationConfiguration>();
		for(IntegrationConfiguration integration : entries) {
			
			integrations.put(integration.getModuleCode(),integration);
			
		}
		
		
		return integrations;
		
	}
	
	private String encodeIntegrationConfiguration(Map<String,IntegrationConfiguration> integrations ) throws Exception {
		
		
		StringBuilder configs = new StringBuilder();
		configs.append("[");
		int count = 0;
		for(String key : integrations.keySet()) {
			
			
			IntegrationConfiguration integration = (IntegrationConfiguration)integrations.get(key);
			
			String json = integration.toJSONString();
			
			configs.append(json);
			
			if(count < integrations.size()-1) {
				configs.append(",");
			}
			
			count ++;
		}
		
		configs.append("]");
		
		return configs.toString();
		
	}
	
	


}
