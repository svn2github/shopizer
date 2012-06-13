package com.salesmanager.core.business.merchant.service;

import java.util.Collection;
import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public interface MerchantStoreService extends SalesManagerEntityService<Integer, MerchantStore>{
	
	void addProduct(MerchantStore merchantStore, Product product) throws ServiceException;
	
	void addProducts(MerchantStore merchantStore, List<Product> products) throws ServiceException;
	
	void removeProduct(MerchantStore merchantStore, Product product) throws ServiceException;
	
	void removeProducts(MerchantStore merchantStore, List<Product> products) throws ServiceException;
	
	Collection<Product> getProducts(MerchantStore merchantStore) throws ServiceException;
	
	MerchantStore getMerchantStore(Integer merchantStoreId) throws ServiceException;
	
}
