package com.salesmanager.web.populator.catalog;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;


public class ProductPopulator extends AbstractDataPopulator<Product, com.salesmanager.web.entity.catalog.Product> {

	@Override
	public com.salesmanager.web.entity.catalog.Product populateFromEntity(
			Product source, com.salesmanager.web.entity.catalog.Product target,
			MerchantStore store, Language language) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product populateToEntity(Product source,
			com.salesmanager.web.entity.catalog.Product target,
			MerchantStore store) {
		// TODO Auto-generated method stub
		return null;
	}


}
