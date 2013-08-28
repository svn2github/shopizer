package com.salesmanager.core.business.customer.service.attribute;

import java.util.List;

import com.salesmanager.core.business.customer.model.attribute.CustomerOption;
import com.salesmanager.core.business.customer.model.attribute.CustomerOptionSet;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface CustomerOptionService extends SalesManagerEntityService<Long, CustomerOption> {

	List<CustomerOption> listByStore(MerchantStore store, Language language)
			throws ServiceException;



	void saveOrUpdate(CustomerOption entity) throws ServiceException;


	/**
	 * Adds a CustomerOptionSet to a customer option
	 * @param optionSet
	 * @param option
	 * @throws ServiceException
	 */
	void addCustomerOptionSet(CustomerOptionSet optionSet, CustomerOption option)
			throws ServiceException;



	/**
	 * Returns a list of CustomerOptionSet belonging to a given CustomerOption
	 * @param option
	 * @param store
	 * @return
	 * @throws Exception
	 */
	List<CustomerOptionSet> listByOption(CustomerOption option,
			MerchantStore store) throws Exception;


	/**
	 * Returns a CustomerOptionSet by the entity id
	 * @param option
	 * @return
	 * @throws ServiceException
	 */
	CustomerOptionSet getCustomerOptionSetById(Long id)
			throws ServiceException;


	/**
	 * Removes a CustomerOptionSet. Can also be removed from the parent entity CustomerOption
	 * by removing the CustomerOptionSet from the Set<CustomerOptionSet> and then invoke 
	 * customerOptionService.update(customerOption)
	 * @param customerOptionSet
	 * @throws ServiceException
	 */
	void removeCustomerOptionSet(CustomerOptionSet customerOptionSet)
			throws ServiceException;



	void updateCustomerOptionSet(CustomerOptionSet customerOptionSet)
			throws ServiceException;



	List<CustomerOptionSet> listCustomerOptionSetByStore(MerchantStore store,
			Language language) throws ServiceException;
	



}
