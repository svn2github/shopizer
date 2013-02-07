package com.salesmanager.core.business.shipping.service;

import java.util.List;
import java.util.Map;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.shipping.model.ShippingConfiguration;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;

public interface ShippingService {

	/**
	 * Returns a list of supported countries configured by merchant
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	public  List<String> getSupportedCountries(MerchantStore store)
			throws ServiceException;

	public  void setSupportedCountries(MerchantStore store,
			List<String> countryCodes) throws ServiceException;

	/**
	 * Returns a list of available shipping modules
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	public List<IntegrationModule> getShippingMethods(MerchantStore store)
			throws ServiceException;

	
	/**
	 * Returns a list of configured shipping modules for a given merchant
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	Map<String, IntegrationConfiguration> getShippingModulesConfigured(
			MerchantStore store) throws ServiceException;

	/**
	 * Adds a Shipping configuration
	 * @param configuration
	 * @param store
	 * @throws ServiceException
	 */
	void saveShippingQuoteModuleConfiguration(IntegrationConfiguration configuration,
			MerchantStore store) throws ServiceException;

	/**
	 * ShippingType (NATIONAL, INTERNATIONSL)
	 * ShippingBasisType (SHIPPING, BILLING)
	 * ShippingPriceOptionType (ALL, LEAST, HIGHEST)
	 * Packages
	 * Handling
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	ShippingConfiguration getShippingConfiguration(MerchantStore store)
			throws ServiceException;

	/**
	 * Saves ShippingConfiguration for a given MerchantStore
	 * @param shippingConfiguration
	 * @param store
	 * @throws ServiceException
	 */
	void saveShippingConfiguration(ShippingConfiguration shippingConfiguration,
			MerchantStore store) throws ServiceException;

}