package com.salesmanager.core.business.catalog.product.service.relationship;

import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface ProductRelationshipService extends
		SalesManagerEntityService<Long, ProductRelationship> {

	void saveOrUpdate(ProductRelationship relationship) throws ServiceException;

}
