package com.salesmanager.core.business.catalog.product.service;

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
		
		
		if(product.getDescriptions()==null) {
			product.setDescriptions(new HashSet<ProductDescription>());
		}
		
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
		List<Category> categories = categoryService.listByLineage(category.getMerchantStore(),lineage.toString());
		Set<Long> categoryIds = new HashSet<Long>();
		for(Category c : categories) {
			
			categoryIds.add(c.getId());
			
		}
		
		categoryIds.add(category.getId());
		
		//Get products
		List<Product> products = productDao.getProductsForLocale(category.getMerchantStore(), categoryIds, language, locale);
		
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
		List<Category> categories = categoryService.listByLineage(category.getMerchantStore(),lineage.toString());
		Set<Long> categoryIds = new HashSet<Long>();
		for(Category c : categories) {
			
			categoryIds.add(c.getId());
			
		}
		
		categoryIds.add(category.getId());
		
		//Get products
		ProductList productList = productDao.getProductsForLocale(category.getMerchantStore(), categoryIds, language, locale, startIndex, maxCount);
		
		return productList;
		
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
		
		//related - featured
		List<ProductRelationship> relationships = productRelationshipService.listByProduct(product);
		for(ProductRelationship relationship : relationships) {
			productRelationshipService.delete(relationship);
		}
		
		this.delete(product);
		
		
	}
	

	
	@Override	
	public void saveOrUpdate(Product product) throws ServiceException {
		

		
		LOGGER.debug("Creating description");
		

		
		if(product.getId()!=null && product.getId()>0) {
			super.update(product);
		} else {
			
			Set<ProductDescription> productDescriptions = product.getDescriptions();
			product.setDescriptions(null);
			
			super.create(product);
			
			for(ProductDescription productDescription : productDescriptions) {
				addProductDescription(product,productDescription);
			}
		}
		
		

		
		LOGGER.debug("Creating availabilities");
		
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
						price.setProductAvailability(availability);
						productPriceService.saveOrUpdate(price);
					}
				}	
			}
		}
		
		
		LOGGER.debug("Creating attributes");
		
		if(product.getAttributes()!=null && product.getAttributes().size()>0) {
			Set<ProductAttribute> attributes = product.getAttributes();
			for(ProductAttribute attribute : attributes) {
				attribute.setProduct(product);
				productAttributeService.saveOrUpdate(attribute);
			}
		}
		
		
		LOGGER.debug("Creating relationships");
		
		if(product.getRelationships()!=null && product.getRelationships().size()>0) {
			Set<ProductRelationship> relationships = product.getRelationships();
			for(ProductRelationship relationship : relationships) {
				relationship.setProduct(product);
				productRelationshipService.saveOrUpdate(relationship);
			}
		}
		
		
		LOGGER.debug("Creating images");

		//get images
		Set<ProductImage> images = product.getImages();
		if(images!=null && images.size()>0) {
			for(ProductImage image : images) {
				if(image.getImage()!=null && (image.getId()==null || image.getId()==0L)) {
					image.setProduct(product);
					productImageService.addProductImage(product, image);
				} else {
					productImageService.update(image);
				}
			}
		}
		
		
		
	}
	
	


	
}
