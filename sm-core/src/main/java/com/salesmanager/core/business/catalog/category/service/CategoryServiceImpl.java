package com.salesmanager.core.business.catalog.category.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.category.dao.CategoryDao;
import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;

@Service("categoryService")
public class CategoryServiceImpl extends SalesManagerEntityServiceImpl<Long, Category> implements CategoryService {
	
	private CategoryDao categoryDao;
	
	  @Autowired
	  private LanguageService            languageService;
	  

	  @Autowired
	  private MerchantStoreService       merchantService;
	  
	  @Autowired
	  private ProductService productService;
	  
	  @Autowired
	  private ProductImageService productImageService;
	
	@Autowired
	public CategoryServiceImpl(CategoryDao categoryDao) {
		super(categoryDao);
		
		this.categoryDao = categoryDao;
	}
	
	public void create(Category category) throws ServiceException {
		
		super.create(category);
		
		Long id = category.getId();
		StringBuilder parenLineage = new StringBuilder();
		Category parent = category.getParent();
		if(parent!=null && parent.getId()!=null && parent.getId()!=0) {
			parenLineage.append(parent.getLineage());
			category.setDepth(parent.getDepth()+1);
		} else {
			parenLineage.append("/");
			category.setDepth(0);
		}
		category.setLineage(parenLineage.append(id).append("/").toString());
		super.update(category);
		
		
	}
	
	@Override
	public void saveOrUpdate(Category category) throws ServiceException {
		
		
		//save or update (persist and attach entities
		if(category.getId()!=null && category.getId()>0) {

			super.update(category);
			
		} else {
			
			super.save(category);
			
		}
		
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
	@Override
	public Category getByCode(MerchantStore store, String code) throws ServiceException {
		
		try {
			return categoryDao.getByCode(store, code);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	
	@Override
	public Category getById(Long id) {
		

			return categoryDao.getById(id);

		
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
	public void delete(Category category) throws ServiceException {
		
		//get category with lineage
		List<Category> categories = this.listByLineage(category.getMerchantStore(), category.getLineage());
		
		if(categories.size()==0) {
			categories.add(category);
		}
		
		Collections.reverse(categories);
		
		List<Long> categoryIds = new ArrayList<Long>();

			
		for(Category c : categories) {
				
				
				categoryIds.add(c.getId());
		}
			
			
		
		
		List<Product> products = productService.getProducts(categoryIds);
		
		for(Product product : products) {
			
			
			productService.delete(product);

		}
		
		for(Category c : categories) {
			
			
			categoryDao.delete(c);
		}

	}


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
		
		
		if(child==null || child.getMerchantStore()==null) {
			throw new ServiceException("Child category and merchant store should not be null");
		}
		
		try {
			
			if(parent==null) {
				
				//assign to root
				child.setParent(null);
				child.setDepth(0);
				child.setLineage(new StringBuilder().append("/").append(child.getId()).append("/").toString());
				
			} else {
				
				Category p = this.getById(parent.getId());
				
				

				
				String lineage = p.getLineage();
				int depth = p.getDepth();//TODO sometimes null
				
				child.setParent(p);
				child.setDepth(depth+1);
				child.setLineage(new StringBuilder().append(lineage).append(child.getId()).append("/").toString());
				
				
			}
			

			update(child);
			
			List<Category> subCategories = listByLineage(child.getMerchantStore(), child.getLineage());
			
			
			//ajust all sub categories lineages
			if(subCategories!=null && subCategories.size()>0) {
				for(Category subCategory : subCategories) {
					if(child.getId()!=subCategory.getId()) {
						addChild(child, subCategory);
					}
				}
				
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		

	}

	@Override
	public List<Category> getByName(MerchantStore store, String name, Language language) throws ServiceException {
		
		try {
			return categoryDao.getByName(store, name, language);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		
	}
	
	

	@Override
	public List<Category> listByStore(MerchantStore store)
			throws ServiceException {

		try {
			return categoryDao.listByStore(store);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<Category> listByStore(MerchantStore store, Language language)
			throws ServiceException {

		try {
			return categoryDao.listByStore(store, language);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	


}
