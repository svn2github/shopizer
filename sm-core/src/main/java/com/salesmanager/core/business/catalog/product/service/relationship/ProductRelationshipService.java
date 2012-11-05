package com.salesmanager.core.business.catalog.product.service.relationship;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationshipType;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface ProductRelationshipService extends
		SalesManagerEntityService<Long, ProductRelationship> {

	void saveOrUpdate(ProductRelationship relationship) throws ServiceException;

	List<ProductRelationship> getByType(MerchantStore store, Product product,
			ProductRelationshipType type, Language language) throws ServiceException;

}
