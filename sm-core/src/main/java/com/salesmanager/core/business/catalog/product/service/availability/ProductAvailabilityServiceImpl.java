package com.salesmanager.core.business.catalog.product.service.availability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.availability.ProductAvailabilityDao;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("productAvailabilityService")
public class ProductAvailabilityServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductAvailability> implements
		ProductAvailabilityService {

	
	private ProductAvailabilityDao productAvailabilityDao;
	
	@Autowired
	public ProductAvailabilityServiceImpl(
			ProductAvailabilityDao productAvailabilityDao) {
			super(productAvailabilityDao);
			this.productAvailabilityDao = productAvailabilityDao;
	}



}
