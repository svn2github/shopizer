package com.salesmanager.core.business.catalog.category.dao;

import java.util.List;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface CategoryDao extends SalesManagerEntityDao<Long, Category> {

	List<Category> listBySeUrl(String seUrl);
	
	//void delete(Category category);

	List<Category> listByByParent(Category category);

}
