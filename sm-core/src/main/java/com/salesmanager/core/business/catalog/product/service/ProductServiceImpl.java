package com.salesmanager.core.business.catalog.product.service;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
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
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.modules.cms.InputContentImage;
import com.salesmanager.core.modules.cms.OutputContentImage;
import com.salesmanager.core.modules.cms.ProductFileManager;

@Service("productService")
public class ProductServiceImpl extends SalesManagerEntityServiceImpl<Long, Product> implements ProductService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	ProductDao productDao;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductFileManager productFileManager;
	
	@Autowired
	ProductImageService productImageService;
	
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
	public void addProductImage(Product product, ProductImage productImage, File file) throws ServiceException {
		
		productImage.setProduct(product);
		
		//upload the image in the CMS
		InputContentImage contentImage = new InputContentImage();
		contentImage.setFile(file);
		contentImage.setDefaultImage(productImage.isDefaultImage());
		contentImage.setImageName(productImage.getProductImage());
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentType = fileNameMap.getContentTypeFor(file.getName());
		contentImage.setImageContentType(contentType);
		productFileManager.uploadProductImage(productImage, contentImage);
		
		//insert ProductImage
		productImageService.create(productImage);
		
	}
	
	@Override
	public OutputContentImage getProductImage(ProductImage productImage) throws ServiceException {
		
		OutputContentImage outputImage = productFileManager.getProductImage(productImage);
		
		return outputImage;
		
	}
	
	@Override
	public List<OutputContentImage> getProductImages(Product product) throws ServiceException {
		return productFileManager.getImages(product);
	}
	
}
