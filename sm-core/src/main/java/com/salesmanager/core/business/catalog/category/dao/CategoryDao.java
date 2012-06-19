package com.salesmanager.core.business.catalog.category.dao;

import java.util.List;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public interface CategoryDao extends SalesManagerEntityDao<Long, Category> {

	List<Category> listBySeUrl(String seUrl);

	List<Category> listByStoreAndParent(MerchantStore store, Category category);

}
