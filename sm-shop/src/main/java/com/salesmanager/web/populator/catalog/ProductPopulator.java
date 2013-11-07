package com.salesmanager.web.populator.catalog;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.populator.AbstractDataPopulator;

@Service(value="productPopulator")
public class ProductPopulator extends AbstractDataPopulator<Product, com.salesmanager.web.entity.catalog.Product> {

	@Override
	public com.salesmanager.web.entity.catalog.Product populate(Product source,
			com.salesmanager.web.entity.catalog.Product target) {
		// TODO Auto-generated method stub
		return null;
	}


}
