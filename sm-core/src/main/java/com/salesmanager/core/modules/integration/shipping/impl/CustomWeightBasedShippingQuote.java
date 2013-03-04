package com.salesmanager.core.modules.integration.shipping.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.customer.model.Delivery;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.shipping.model.PackageDetails;
import com.salesmanager.core.business.shipping.model.ShippingConfiguration;
import com.salesmanager.core.business.shipping.model.ShippingOption;
import com.salesmanager.core.business.system.model.CustomIntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.shipping.model.CustomShippingQuotesConfiguration;
import com.salesmanager.core.modules.integration.shipping.model.ShippingQuoteModule;

public class CustomWeightBasedShippingQuote implements ShippingQuoteModule {
	
	public final static String MODULE_CODE = "weightBased";
	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;

	@Override
	public void validateModuleConfiguration(
			IntegrationConfiguration integrationConfiguration,
			MerchantStore store) throws IntegrationException {
		
		
		//not used, it has its own controller with complex validators

	}
	

	@Override
	public CustomIntegrationConfiguration getCustomModuleConfiguration(
			MerchantStore store) throws IntegrationException {

		try {

			MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(MODULE_CODE, store);
	
			if(configuration!=null) {
				String value = configuration.getValue();
				ObjectMapper mapper = new ObjectMapper();
				try {
					CustomShippingQuotesConfiguration config = mapper.readValue(value, CustomShippingQuotesConfiguration.class);
					return config;
				} catch(Exception e) {
					throw new ServiceException("Cannot parse json string " + value);
				}
	
			} else {
				CustomShippingQuotesConfiguration custom = new CustomShippingQuotesConfiguration();
				custom.setModuleCode(MODULE_CODE);
				return custom;
			}
		
		} catch (Exception e) {
			throw new IntegrationException(e);
		}
		
		
	}

	@Override
	public List<ShippingOption> getShippingQuotes(
			List<PackageDetails> packages, BigDecimal orderTotal,
			Delivery delivery, MerchantStore store,
			IntegrationConfiguration configuration, IntegrationModule module,
			ShippingConfiguration shippingConfiguration, Locale locale)
			throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}



}
