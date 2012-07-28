package com.salesmanager.core.business.catalog.category.dao;

import java.util.List;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface CategoryDao extends SalesManagerEntityDao<Long, Category> {

	List<Category> listBySeUrl(MerchantStore store, String seUrl);

	List<Category> listByStoreAndParent(MerchantStore store, Category category);

	List<Category> listByLineage(MerchantStore store, String lineage);

	Category getByName(MerchantStore store, String name);

	Category getByCode(MerchantStore store, String code);
	
	List<Category> listByStore(MerchantStore store);

	List<Category> listByStore(MerchantStore store, Language language);

}
