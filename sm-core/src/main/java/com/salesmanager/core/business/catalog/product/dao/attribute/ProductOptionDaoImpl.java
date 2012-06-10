package com.salesmanager.core.business.catalog.product.dao.attribute;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("productOptionDao")
public class ProductOptionDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductOption>
		implements ProductOptionDao {



}
