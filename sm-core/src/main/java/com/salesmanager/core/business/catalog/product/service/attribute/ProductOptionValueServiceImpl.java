package com.salesmanager.core.business.catalog.product.service.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.attribute.ProductOptionValueDao;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("productOptionValueService")
public class ProductOptionValueServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductOptionValue> implements
		ProductOptionValueService {

	
	
	private ProductOptionValueDao productOptionValueDao;
	
	@Autowired
	public ProductOptionValueServiceImpl(
			ProductOptionValueDao productOptionValueDao) {
			super(productOptionValueDao);
			this.productOptionValueDao = productOptionValueDao;
	}



}
