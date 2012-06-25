package com.salesmanager.core.business.catalog.product.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.common.CatalogServiceHelper;
import com.salesmanager.core.business.catalog.product.dao.ProductDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.reference.language.model.Language;

@Service("productService")
public class ProductServiceImpl extends SalesManagerEntityServiceImpl<Long, Product> implements ProductService {
	
	ProductDao productDao;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	public ProductServiceImpl(ProductDao productDao) {
		super(productDao);
		this.productDao = productDao;
	}

	@Override
	public void addProductDescription(Product product, ProductDescription description)
			throws ServiceException {
		product.getDescriptions().add(description);
		description.setProduct(product);
		update(product);
	}

	@Override
	public ProductDescription getProductDescription(Product product, Language language) {
		for (ProductDescription description : product.getDescriptions()) {
			if (description.getLanguage().equals(language)) {
				return description;
			}
		}
		return null;
	}

	@Override
	public Product getProductForLocale(long productId, Language language, Locale locale)
			throws ServiceException {
		Product product =  productDao.getProductForLocale(productId, language, locale);
		
		//TODO do we still need this
		CatalogServiceHelper.setToAvailability(product, locale);
		CatalogServiceHelper.setToLanguage(product, language.getId());
		return product;
	}

	@Override
	public List<Product> getProductsForLocale(Category category,
			Language language, Locale locale) throws ServiceException {
		// TODO Auto-generated method stub
		
		if(category==null) {
			throw new ServiceException("The category is null");
		}
		
		//Get the category list
		String lineage = category.getLineage();
		List<Category> categories = categoryService.listByLineage(lineage);
		Set categoryIds = new HashSet();
		for(Category c : categories) {
			
			categoryIds.add(c.getId());
			
		}
		
		//Get products
		List<Product> products = productDao.getProductsForLocale(categoryIds, language, locale);
		
		//Filter availability
		
		return null;
	}
	
}
