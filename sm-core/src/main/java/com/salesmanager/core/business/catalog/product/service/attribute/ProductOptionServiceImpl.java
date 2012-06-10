package com.salesmanager.core.business.catalog.product.service.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.attribute.ProductOptionDao;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("productOptionService")
public class ProductOptionServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductOption> implements ProductOptionService {

	
	private ProductOptionDao productOptionDao;
	
	@Autowired
	public ProductOptionServiceImpl(
			ProductOptionDao productOptionDao) {
			super(productOptionDao);
			this.productOptionDao = productOptionDao;
	}



}
