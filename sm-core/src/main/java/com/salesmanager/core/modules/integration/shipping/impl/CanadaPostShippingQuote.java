package com.salesmanager.core.modules.integration.shipping.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.shipping.model.ShippingQuoteModule;

public class CanadaPostShippingQuote implements ShippingQuoteModule {

	@Override
	public void validateModuleConfiguration(
			IntegrationConfiguration integrationConfiguration,
			MerchantStore store) throws IntegrationException {
		
		
		List<String> errorFields = null;
		
		//validate integrationKeys['account']
		Map<String,String> keys = integrationConfiguration.getIntegrationKeys();
		if(keys==null || StringUtils.isBlank(keys.get("account"))) {
			errorFields = new ArrayList<String>();
			errorFields.add("integrationKeys['account']");
		}

		//validate at least one integrationOptions['packages']
		Map<String,String[]> options = integrationConfiguration.getIntegrationOptions();
		if(options==null) {
			errorFields = new ArrayList<String>();
			errorFields.add("integrationKeys['account']");
		}
		
		String[] packages = options.get("packages");
		if(packages==null || packages.length==0) {
			if(errorFields==null) {
				errorFields = new ArrayList<String>();
			}
			errorFields.add("integrationOptions['packages']");
		}
		
		if(errorFields!=null) {
			IntegrationException ex = new IntegrationException(IntegrationException.ERROR_VALIDATION_SAVE);
			ex.setErrorFields(errorFields);
			throw ex;
			
		}

	}

}
