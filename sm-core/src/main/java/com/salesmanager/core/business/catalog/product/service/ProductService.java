package com.salesmanager.core.business.catalog.product.service;

import java.util.List;
import java.util.Locale;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.reference.language.model.Language;

public interface ProductService extends SalesManagerEntityService<Long, Product> {

	void addProductDescription(Product product, ProductDescription description) throws ServiceException;
	
	ProductDescription getProductDescription(Product product, Language language);
	
	Product getProductForLocale(long productId, Language language, Locale locale) throws ServiceException;
	
	List<Product> getProductsForLocale(Category category, Language language, Locale locale) throws ServiceException;
	
	/**
	 * Returns a List o product for pagination
	 * @param category
	 * @param language
	 * @param locale
	 * @param startIndex
	 * @param maxCount
	 * @return
	 * @throws ServiceException
	 */
	ProductList getProductsForLocale(Category category, Language language, Locale locale, int startIndex, int maxCount) throws ServiceException;
}
