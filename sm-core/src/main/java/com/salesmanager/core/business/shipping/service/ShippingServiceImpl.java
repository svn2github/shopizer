package com.salesmanager.core.business.shipping.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.shipping.model.ShippingConfiguration;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.business.system.service.ModuleConfigurationService;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.shipping.model.ShippingQuoteModule;
import com.salesmanager.core.utils.ModuleUtils;
import com.salesmanager.core.utils.reference.ConfigurationModulesLoader;

@Service("shippingService")
public class ShippingServiceImpl implements ShippingService {
	
	
	private final static String SUPPORTED_COUNTRIES = "SUPPORTED_CNTR";
	private final static String SHIPPING_MODULES = "SHIPPING";
	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;
	
	@Autowired
	private ModuleConfigurationService moduleConfigurationService;
	
	@Override
	public ShippingConfiguration getShippingConfiguration(MerchantStore store) throws ServiceException {
		
		
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(ShippingConstants.SHIPPING_CONFIGURATION, store);
		
		ShippingConfiguration shippingConfiguration = null;
		
		if(configuration!=null) {
			String value = configuration.getValue();
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				shippingConfiguration = mapper.readValue(value, ShippingConfiguration.class);
			} catch(Exception e) {
				throw new ServiceException("Cannot parse json string " + value);
			}
		}
		return shippingConfiguration;
		
	}
	
	@Override
	public void saveShippingConfiguration(ShippingConfiguration shippingConfiguration, MerchantStore store) throws ServiceException {
		
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(ShippingConstants.SHIPPING_CONFIGURATION, store);

		if(configuration==null) {
			configuration = new MerchantConfiguration();
			configuration.setMerchantStore(store);
			configuration.setKey(ShippingConstants.SHIPPING_CONFIGURATION);
		}
		
		String value = shippingConfiguration.toJSONString();
		configuration.setValue(value);
		merchantConfigurationService.saveOrUpdate(configuration);
		
	}
	

	@Override
	public List<IntegrationModule> getShippingMethods(MerchantStore store) throws ServiceException {
		
		List<IntegrationModule> modules =  moduleConfigurationService.getIntegrationModules(SHIPPING_MODULES);
		List<IntegrationModule> returnModules = new ArrayList<IntegrationModule>();
		
		for(IntegrationModule module : modules) {
			if(module.getRegionsSet().contains(store.getCountry().getIsoCode())
					|| module.getRegionsSet().contains("*")) {
				
				returnModules.add(module);
			}
		}
		
		return returnModules;
	}
	
	@Override
	public void saveShippingQuoteModuleConfiguration(IntegrationConfiguration configuration, MerchantStore store) throws ServiceException {
		
		
			
			//validate entries
			try {
				
				String moduleCode = configuration.getModuleCode();
				ShippingQuoteModule quoteModule = (ShippingQuoteModule)ModuleUtils.getModule(moduleCode);
				
				if(quoteModule==null) {
					throw new ServiceException("Shipping quote module " + moduleCode + " does not exist");
				}
				
				quoteModule.validateModuleConfiguration(configuration, store);
				
			} catch (IntegrationException ie) {
				throw ie;
			}
			
			try {
				Map<String,IntegrationConfiguration> modules = new HashMap<String,IntegrationConfiguration>();
				MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(SHIPPING_MODULES, store);
				if(merchantConfiguration!=null) {
					if(!StringUtils.isBlank(merchantConfiguration.getValue())) {
						modules = ConfigurationModulesLoader.loadIntegrationConfigurations(merchantConfiguration.getValue());
					}
				} else {
					merchantConfiguration = new MerchantConfiguration();
					merchantConfiguration.setMerchantStore(store);
					merchantConfiguration.setKey(SHIPPING_MODULES);
				}
				modules.put(configuration.getModuleCode(), configuration);
				
				String configs =  ConfigurationModulesLoader.toJSONString(modules);
				merchantConfiguration.setValue(configs);
				merchantConfigurationService.saveOrUpdate(merchantConfiguration);
				
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		
	}
	
	@Override
	public Map<String,IntegrationConfiguration> getShippingModulesConfigured(MerchantStore store) throws ServiceException {
		try {
			

			Map<String,IntegrationConfiguration> modules = new HashMap<String,IntegrationConfiguration>();
			MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(SHIPPING_MODULES, store);
			if(merchantConfiguration!=null) {
				if(!StringUtils.isBlank(merchantConfiguration.getValue())) {
					
					modules = ConfigurationModulesLoader.loadIntegrationConfigurations(merchantConfiguration.getValue());
					
				}
			}
			return modules;
		
		
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	

	@Override
	public List<String> getSupportedCountries(MerchantStore store) throws ServiceException {
		
		List<String> supportedCountries = new ArrayList<String>();
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(SUPPORTED_COUNTRIES, store);
		
		if(configuration!=null) {
			
			String countries = configuration.getValue();
			if(!StringUtils.isBlank(countries)) {

				Object objRegions=JSONValue.parse(countries); 
				JSONArray arrayRegions=(JSONArray)objRegions;
				@SuppressWarnings("rawtypes")
				Iterator i = arrayRegions.iterator();
				while(i.hasNext()) {
					supportedCountries.add((String)i.next());
				}
			}
			
		}
		
		return supportedCountries;
	}
	

	@Override
	public void setSupportedCountries(MerchantStore store, List<String> countryCodes) throws ServiceException {
		
		
		//transform a list of string to json entry
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String value  = mapper.writeValueAsString(countryCodes);
			
			MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(SUPPORTED_COUNTRIES, store);
			
			if(configuration==null) {
				configuration = new MerchantConfiguration();
				configuration.
				setKey(SUPPORTED_COUNTRIES);
				configuration.setMerchantStore(store);
			} 
			
			configuration.setValue(value);

			merchantConfigurationService.saveOrUpdate(configuration);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}
	


}
