package com.salesmanager.core.business.search.service;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public interface SearchService {
	
	/**
	 * The indexing service for products. The index service must be invoked when a product is
	 * created or updated
	 * @param store
	 * @param product
	 * @throws ServiceException
	 */
	void index(MerchantStore store, Product product) throws ServiceException;

	/**
	 * Deletes an index in the appropriate language. Must be invoked when a product is deleted
	 * @param store
	 * @param product
	 * @throws ServiceException
	 */
	void deleteIndex(MerchantStore store, Product product)
			throws ServiceException;

}
