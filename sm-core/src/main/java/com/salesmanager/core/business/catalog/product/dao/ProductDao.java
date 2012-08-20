package com.salesmanager.core.business.catalog.product.dao;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.reference.language.model.Language;

public interface ProductDao extends SalesManagerEntityDao<Long, Product> {
	
	Product getProductForLocale(long productId, Language language, Locale locale);

	@SuppressWarnings("rawtypes")
	List<Product> getProductsForLocale(Set categoryIds, Language language,
			Locale locale);
	
	@SuppressWarnings("rawtypes")
	ProductList getProductsForLocale(Set categoryIds, Language language,
			Locale locale, int startIndex, int maxCount);

	@SuppressWarnings("rawtypes")
	List<Product> getProductsListByCategories(Set categoryIds);
	
}
