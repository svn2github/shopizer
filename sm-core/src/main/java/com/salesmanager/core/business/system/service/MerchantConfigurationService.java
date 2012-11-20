package com.salesmanager.core.business.system.service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.system.model.MerchantConfiguration;

public interface MerchantConfigurationService extends
		SalesManagerEntityService<Long, MerchantConfiguration> {
	
	MerchantConfiguration getMerchantConfiguration(String key, MerchantStore store) throws ServiceException;

	public void saveOrUpdate(MerchantConfiguration entity) throws ServiceException;

}
