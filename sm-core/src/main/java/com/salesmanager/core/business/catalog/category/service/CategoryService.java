package com.salesmanager.core.business.catalog.category.service;

import java.util.List;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface CategoryService extends SalesManagerEntityService<Long, Category> {

	List<Category> listByLineage(MerchantStore store, String lineage) throws ServiceException;
	
	List<Category> listBySeUrl(MerchantStore store, String seUrl) throws ServiceException;
	
	CategoryDescription getDescription(Category category, Language language) throws ServiceException;
	
	//void addProduct(Category category, Product product) throws ServiceException;
	
	//void removeProduct(Category category, Product product) throws ServiceException;
	
	//void addProducts(Category category, List<Product> products) throws ServiceException;
	
	//void removeProducts(Category category, List<Product> products) throws ServiceException;

	void addCategoryDescription(Category category, CategoryDescription description) throws ServiceException;

	void addChild(Category parent, Category child) throws ServiceException;

	List<Category> listByParent(Category category) throws ServiceException;
	
	List<Category> listByStoreAndParent(MerchantStore store, Category category) throws ServiceException;
	
	
	List<Category> getByName(MerchantStore store, String name, Language language) throws ServiceException;
	
	List<Category> listByStore(MerchantStore store) throws ServiceException;

	Category getByCode(MerchantStore store, String code)
			throws ServiceException;

	List<Category> listByStore(MerchantStore store, Language language)
			throws ServiceException;

	//Category getById(Long id);

	void saveOrUpdate(Category category) throws ServiceException;

	List<Category> listByDepth(MerchantStore store, int depth);

	/**
	 * Get root categories by store for a given language
	 * @param store
	 * @param depth
	 * @param language
	 * @return
	 */
	List<Category> listByDepth(MerchantStore store, int depth, Language language);

	List<Category> listByLineage(String storeCode, String lineage)
			throws ServiceException;

	Category getByCode(String storeCode, String code) throws ServiceException;

	Category getBySeUrl(MerchantStore store, String seUrl);

	List<Category> listByParent(Category category, Language language);

	Category getByLanguage(long categoryId, Language language);


	
	

}
