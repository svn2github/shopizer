package com.salesmanager.web.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.utils.ProductPriceUtils;

@Component
public class CatalogUtils {
	
	@Autowired
	private ProductPriceUtils productPriceUtils;
	
	public com.salesmanager.web.entity.catalog.Product buildProxyProduct(Product product, Locale locale) throws Exception {
		
		
		ProductDescription description = product.getProductDescription();
		com.salesmanager.web.entity.catalog.Product proxyProduct = new com.salesmanager.web.entity.catalog.Product();
		
		if(description!=null) {
			proxyProduct.setFriendlyUrl(description.getSeUrl());
			proxyProduct.setName(description.getName());
			proxyProduct.setDescription(description.getDescription());
		}
		ProductImage image = product.getProductImage();
		if(image!=null) {
			proxyProduct.setImage(image.getProductImage());
		}
		

		FinalPrice price = productPriceUtils.getFinalPrice(product);
		proxyProduct.setProductPrice(productPriceUtils.getFormatedAmountWithCurrency(product.getMerchantStore(),price.getFinalPrice(),locale));

		return proxyProduct;
		
		
	}
	
	public com.salesmanager.web.entity.catalog.Category buildCategoryProxy(Category category, Locale locale) {
		
		com.salesmanager.web.entity.catalog.Category categoryProxy = new com.salesmanager.web.entity.catalog.Category();
		
		if(category.getDescriptions()!=null && category.getDescriptions().size()>0) {
			
			CategoryDescription description = category.getDescription();
			
			if(category.getDescriptions().size()>1) {
				for(CategoryDescription desc : category.getDescriptions()) {
					if(desc.getLanguage().getCode().equals(locale.getLanguage())) {
						description = desc;
						break;
					}
				}
			}
		
		
		
			if(description!=null) {
				categoryProxy.setFriendlyUrl(description.getSeUrl());
				categoryProxy.setName(description.getName());
				categoryProxy.setDescription(description.getName());
				categoryProxy.setKeyWords(description.getMetatagKeywords());
				categoryProxy.setHighlights(description.getCategoryHighlight());
				categoryProxy.setTitle(description.getMetatagTitle());
				categoryProxy.setMetaDescription(description.getMetatagDescription());
			}
		
		}
		
		categoryProxy.setCode(category.getCode());
		
		
		return null;
	}

}
