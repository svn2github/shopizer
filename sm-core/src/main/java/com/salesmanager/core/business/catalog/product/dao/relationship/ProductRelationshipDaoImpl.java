package com.salesmanager.core.business.catalog.product.dao.relationship;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("productRelationshipDao")
public class ProductRelationshipDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductRelationship>
		implements ProductRelationshipDao {



}
