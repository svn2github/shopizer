package com.salesmanager.web.populator.catalog.rest;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.catalog.rest.product.ProductEntity;

public class ProductPopulator extends AbstractDataPopulator<ProductEntity, Product> {

	@Override
	public Product populateFromEntity(ProductEntity source, Product target,
			MerchantStore store, Language language) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductEntity populateToEntity(ProductEntity source, Product target,
			MerchantStore store) {
		// TODO Auto-generated method stub
		return null;
	}

}
