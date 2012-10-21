package com.salesmanager.core.business.catalog.product.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.common.CatalogServiceHelper;
import com.salesmanager.core.business.catalog.product.dao.ProductDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.catalog.product.service.availability.ProductAvailabilityService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.catalog.product.service.price.ProductPriceService;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.CoreConfiguration;

@Service("productService")
public class ProductServiceImpl extends SalesManagerEntityServiceImpl<Long, Product> implements ProductService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	ProductDao productDao;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductAvailabilityService productAvailabilityService;
	
	@Autowired
	ProductPriceService productPriceService;
	
	@Autowired
	ProductAttributeService productAttributeService;
	
	@Autowired
	ProductRelationshipService productRelationshipService;
	

	
	@Autowired
	ProductImageService productImageService;
	
	@Autowired
	CoreConfiguration configuration;
	
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
	public List<Product> getProducts(List<Long> categoryIds) throws ServiceException {
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Set ids = new HashSet(categoryIds);
		return productDao.getProductsListByCategories(ids);
		
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
		
		if(category==null) {
			throw new ServiceException("The category is null");
		}
		
		//Get the category list
		StringBuilder lineage = new StringBuilder().append(category.getLineage()).append(category.getId()).append("/");
		List<Category> categories = categoryService.listByLineage(category.getMerchantSore(),lineage.toString());
		Set categoryIds = new HashSet();
		for(Category c : categories) {
			
			categoryIds.add(c.getId());
			
		}
		
		categoryIds.add(category.getId());
		
		//Get products
		List<Product> products = productDao.getProductsForLocale(category.getMerchantSore(), categoryIds, language, locale);
		
		//Filter availability
		
		return products;
	}
	
	@Override
	public ProductList listByStore(MerchantStore store,
			Language language, ProductCriteria criteria) {
		
		return productDao.listByStore(store, language, criteria);
	}

	@Override
	public ProductList getProductsForLocale(Category category,
			Language language, Locale locale, int startIndex, int maxCount)
			throws ServiceException {

		if(category==null) {
			throw new ServiceException("The category is null");
		}
		
		//Get the category list
		StringBuilder lineage = new StringBuilder().append(category.getLineage()).append(category.getId()).append("/");
		List<Category> categories = categoryService.listByLineage(category.getMerchantSore(),lineage.toString());
		Set categoryIds = new HashSet();
		for(Category c : categories) {
			
			categoryIds.add(c.getId());
			
		}
		
		categoryIds.add(category.getId());
		
		//Get products
		ProductList productList = productDao.getProductsForLocale(category.getMerchantSore(), categoryIds, language, locale, startIndex, maxCount);
		
		return productList;
		
	}
	
	@Override
	public Product getById(Long id) {
		
		
		return productDao.getProductById(id);
	}
	
	
	@Override
	public void removeProduct(Product product) throws ServiceException {
		
		
		product = this.getById(product.getId());//Prevents detached entity error
		product.setCategories(null);
		
		Set<ProductImage> images = product.getImages();
		
		for(ProductImage image : images) {
			
			productImageService.removeProductImage(image);
			
		}
		
		product.setImages(null);
		
		this.delete(product);
		
		
	}
	
	@Override
	public void saveOrUpdate(Product product, File productImage) throws ServiceException {
		this.saveOrUpdateProduct(product,productImage);
	}
	
	@Override
	public void saveOrUpdate(Product product) throws ServiceException {
		this.saveOrUpdateProduct(product,null);
	}
	
	
	private void saveOrUpdateProduct(Product product, File file) throws ServiceException {
		
		

		
		if(product.getId()!=null && product.getId()>0) {
			super.update(product);
		} else {
			super.create(product);
		}
		

		
		//get availabilities
		Set<ProductAvailability> availabilities = product.getAvailabilities();
		
		if(availabilities!=null && availabilities.size()>0) {
			for(ProductAvailability availability : availabilities) {
				availability.setProduct(product);
				productAvailabilityService.saveOrUpdate(availability);
				//check prices
				Set<ProductPrice> prices = availability.getPrices();
				if(prices!=null && prices.size()>0) {

					for(ProductPrice price : prices) {
						price.setProductPriceAvailability(availability);
						productPriceService.saveOrUpdate(price);
					}
				}	
			}
		}
		

		if(file!=null) {
			//get images
			Set<ProductImage> images = product.getImages();
			if(images!=null && images.size()>0) {
				for(ProductImage image : images) {
					productImageService.addProductImage(product, image, file);
				}
			}
		}
		
		if(product.getAttributes()!=null && product.getAttributes().size()>0) {
			Set<ProductAttribute> attributes = product.getAttributes();
			for(ProductAttribute attribute : attributes) {
				
				productAttributeService.saveOrUpdate(attribute);
				
				
			}
		}
		
		
		if(product.getRelationships()!=null && product.getRelationships().size()>0) {
			Set<ProductRelationship> relationships = product.getRelationships();
			for(ProductRelationship relationship : relationships) {
				
				productRelationshipService.saveOrUpdate(relationship);
				
				
			}
		}
		
		
	}
	
	


	
}
