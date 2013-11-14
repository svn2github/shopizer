package com.salesmanager.web.populator.catalog.rest;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.catalog.rest.product.ProductEntity;

public class ProductPopulator extends AbstractDataPopulator<Product, ProductEntity> {

	@Override
	public ProductEntity populateFromEntity(Product source,
			ProductEntity target, MerchantStore store, Language language)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product populateToEntity(Product target, ProductEntity source,
			MerchantStore store) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}


}
