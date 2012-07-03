package com.salesmanager.core.business.catalog.category.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.business.catalog.category.dao.CategoryDao;
import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;

@Service("categoryService")
public class CategoryServiceImpl extends SalesManagerEntityServiceImpl<Long, Category> implements CategoryService {
	
	private CategoryDao categoryDao;
	
	  @Autowired
	  protected LanguageService            languageService;
	  

	  @Autowired
	  protected MerchantStoreService       merchantService;
	
	@Autowired
	public CategoryServiceImpl(CategoryDao categoryDao) {
		super(categoryDao);
		
		this.categoryDao = categoryDao;
	}

	@Override
	public List<Category> listByLineage(MerchantStore store, String lineage) throws ServiceException {
		try {
			return categoryDao.listByLineage(store, lineage);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		
	}
	

	@Override
	public List<Category> listBySeUrl(MerchantStore store, String seUrl) throws ServiceException{
		
		try {
			return categoryDao.listBySeUrl(store, seUrl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	public List<Category> listByCode(MerchantStore store, String code) throws ServiceException {
		
		try {
			return categoryDao.listByCode(store, code);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	@Override
	public List<Category> listByParent(Category category) throws ServiceException {
		
		try {
			return categoryDao.listByStoreAndParent(null, category);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	@Override
	public List<Category> listByStoreAndParent(MerchantStore store, Category category) throws ServiceException {
		
		try {
			return categoryDao.listByStoreAndParent(store, category);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	@Override
	public void addCategoryDescription(Category category, CategoryDescription description)
			throws ServiceException {
		
		
		
		try {
			category.getDescriptions().add(description);
			description.setCategory(category);
			update(category);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	
	//@Override
	//public void delete(Category category) throws ServiceException {
		//reject if category has products attached
		//categoryDao.delete(category);
	//}


	//@Override
	//public void addProduct(Category category, Product product) throws ServiceException {
		//if (!category.getProducts().contains(product)) {
		//	category.getProducts().add(product);
		//	update(category);
		//}
	//}

	//@Override
	//public void removeProduct(Category category, Product product)
			//throws ServiceException {
		//if (category.getProducts().contains(product)) {
		//	category.getProducts().remove(product);
		//	update(category);
		//}
	//}

	//@Override
	//public void addProducts(Category category, List<Product> products)
	//		throws ServiceException {
		//if (!category.getProducts().contains(products)) {
		//	category.getProducts().addAll(products);
		//	update(category);
		//}
	//}

	//@Override
	//public void removeProducts(Category category, List<Product> products)
		//	throws ServiceException {
		//if (category.getProducts().contains(products)) {
		//	category.getProducts().removeAll(products);
		//	update(category);
		//}
	//}

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
		
		
		try {
			parent.getCategories().add(child);
			child.setParent(parent);
			update(child);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		

	}

	@Override
	public Category getByName(MerchantStore store, String name) throws ServiceException {
		
		try {
			return categoryDao.getByName(store, name);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		
	}
	
	


}
