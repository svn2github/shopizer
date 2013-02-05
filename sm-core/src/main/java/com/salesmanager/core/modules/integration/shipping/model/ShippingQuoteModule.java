package com.salesmanager.core.modules.integration.shipping.model;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.modules.integration.IntegrationException;

public interface ShippingQuoteModule {
	
	public void validateModuleConfiguration(IntegrationConfiguration integrationConfiguration, MerchantStore store) throws IntegrationException;

}
