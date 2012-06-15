package com.salesmanager.core.business.catalog.category.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.category.dao.CategoryDao;
import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.model.Category_;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.reference.language.model.Language;

@Service("categoryService")
public class CategoryServiceImpl extends SalesManagerEntityServiceImpl<Long, Category> implements CategoryService {
	
	private CategoryDao categoryDao;
	
	@Autowired
	public CategoryServiceImpl(CategoryDao categoryDao) {
		super(categoryDao);
		
		this.categoryDao = categoryDao;
	}

	@Override
	public List<Category> listByLineage(String lineage) {
		return listByField(Category_.lineage, lineage);
	}
	

	@Override
	public List<Category> listBySeUrl(String seUrl) {
		return categoryDao.listBySeUrl(seUrl);
	}
	
	@Override
	public List<Category> listByParent(Category category) {
		return categoryDao.listByByParent(category);
	}
	
	@Override
	public void addCategoryDescription(Category category, CategoryDescription description)
			throws ServiceException {
		category.getDescriptions().add(description);
		description.setCategory(category);
		update(category);
	}
	
	//@Override
	//public void delete(Category category) throws ServiceException {
		//reject if category has products attached
		//categoryDao.delete(category);
	//}

	@Override
	public void addProduct(Category category, Product product) throws ServiceException {
		//if (!category.getProducts().contains(product)) {
		//	category.getProducts().add(product);
		//	update(category);
		//}
	}

	@Override
	public void removeProduct(Category category, Product product)
			throws ServiceException {
		//if (category.getProducts().contains(product)) {
		//	category.getProducts().remove(product);
		//	update(category);
		//}
	}

	@Override
	public void addProducts(Category category, List<Product> products)
			throws ServiceException {
		//if (!category.getProducts().contains(products)) {
		//	category.getProducts().addAll(products);
		//	update(category);
		//}
	}

	@Override
	public void removeProducts(Category category, List<Product> products)
			throws ServiceException {
		//if (category.getProducts().contains(products)) {
		//	category.getProducts().removeAll(products);
		//	update(category);
		//}
	}

	@Override
	public CategoryDescription getDescription(Category category, Language language) {
		for (CategoryDescription description : category.getDescriptions()) {
			if (description.getLanguage().equals(language)) {
				return description;
			}
		}
		return null;
	}
	
	@Override
	public void addChild(Category parent, Category child) throws ServiceException {
		parent.getCategories().add(child);
		child.setParent(parent);
		update(child);
	}
}
