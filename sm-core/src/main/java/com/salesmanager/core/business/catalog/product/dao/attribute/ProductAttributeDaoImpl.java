package com.salesmanager.core.business.catalog.product.dao.attribute;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("productAttributeDao")
public class ProductAttributeDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductAttribute> 
	implements ProductAttributeDao {

}
