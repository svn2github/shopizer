package com.salesmanager.core.business.catalog.product.dao;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface ProductDao extends SalesManagerEntityDao<Long, Product> {
	
	Product getProductForLocale(long productId, Language language, Locale locale);

	@SuppressWarnings("rawtypes")
	List<Product> getProductsForLocale(MerchantStore store, Set categoryIds, Language language,
			Locale locale);
	
	@SuppressWarnings("rawtypes")
	ProductList getProductsForLocale(MerchantStore store, Set categoryIds, Language language,
			Locale locale, int startIndex, int maxCount);

	@SuppressWarnings("rawtypes")
	List<Product> getProductsListByCategories(Set categoryIds);

	Product getById(long productId);

	ProductList listByStore(MerchantStore store, Language language, ProductCriteria criteria);
	
}
