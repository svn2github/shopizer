package com.salesmanager.core.business.catalog.product.service.attribute;

import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface ProductAttributeService extends
		SalesManagerEntityService<Long, ProductAttribute> {

	void saveOrUpdate(ProductAttribute productAttribute)
			throws ServiceException;
}
