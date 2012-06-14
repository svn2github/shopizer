package com.salesmanager.core.business.catalog.category.service;

import java.util.List;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.reference.language.model.Language;

public interface CategoryService extends SalesManagerEntityService<Long, Category> {

	List<Category> listByLineage(String lineage);
	
	List<Category> listBySeUrl(String seUrl);
	
	CategoryDescription getDescription(Category category, Language language);
	
	void addProduct(Category category, Product product) throws ServiceException;
	
	void removeProduct(Category category, Product product) throws ServiceException;
	
	void addProducts(Category category, List<Product> products) throws ServiceException;
	
	void removeProducts(Category category, List<Product> products) throws ServiceException;

	void addCategoryDescription(Category category, CategoryDescription description) throws ServiceException;

	void addChild(Category parent, Category child) throws ServiceException;

	List<Category> listByParent(Category category);
}
