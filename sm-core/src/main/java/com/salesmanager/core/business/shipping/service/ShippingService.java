package com.salesmanager.core.business.shipping.service;

import java.util.List;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.system.model.IntegrationModule;

public interface ShippingService {

	public  List<String> getSupportedCountries(MerchantStore store)
			throws ServiceException;

	public  void setSupportedCountries(MerchantStore store,
			List<String> countryCodes) throws ServiceException;

	public List<IntegrationModule> getShippingMethods(MerchantStore store)
			throws ServiceException;

}