package com.salesmanager.core.business.search.service;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface SearchService {
	
	void index(MerchantStore store, Product product, Language language) throws ServiceException;

}
