package com.salesmanager.core.business.tax.service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.tax.model.TaxConfiguration;


public interface TaxService {

	/**
	 * Retrieves tax configurations (TaxConfiguration) for a given MerchantStore
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	TaxConfiguration getTaxConfiguration(MerchantStore store)
			throws ServiceException;

	/**
	 * Saves ShippingConfiguration to MerchantConfiguration table
	 * @param shippingConfiguration
	 * @param store
	 * @throws ServiceException
	 */
	void saveTaxConfiguration(TaxConfiguration shippingConfiguration,
			MerchantStore store) throws ServiceException;


}
