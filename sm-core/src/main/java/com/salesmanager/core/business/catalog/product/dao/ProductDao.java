package com.salesmanager.core.business.catalog.product.dao;

import java.util.Locale;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.reference.language.model.Language;

public interface ProductDao extends SalesManagerEntityDao<Long, Product> {
	
	Product getProduct(long productId, Language language, Locale locale);
	
}
