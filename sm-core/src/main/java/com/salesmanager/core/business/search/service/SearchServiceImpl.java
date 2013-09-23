package com.salesmanager.core.business.search.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.search.model.IndexProduct;


@Service("productSearchService")
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private com.shopizer.search.services.SearchService searchService;
	
	@Autowired
	private PricingService pricingService;

	@Override
	public void index(MerchantStore store, Product product, Language language)
			throws ServiceException {
		
		/**
		 * When a product is saved or updated the indexing will occur
		 * 
		 * A product entity will have to be transformed to a bean ProductIndex
		 * which contains the indices as described in product.json
		 * 
		 * {"product": {
						"properties" :  {
							"name" : {"type":"string","index":"analyzed"},
							"price" : {"type":"string","index":"not_analyzed"},
							"price" : {"type":"string","index":"not_analyzed"},
							"category" : {"type":"string","index":"not_analyzed"},
							"lang" : {"type":"string","index":"not_analyzed"},
							"availability" : {"type":"string","index":"not_analyzed"},
							"available" : {"type":"string","index":"not_analyzed"},
							"description" : {"type":"string","index":"analyzed","index_analyzer":"english"}, 
							"tags" : {"type":"string","index":"not_analyzed"} 
						 } 
			            }
			}
		 *
		 * productService saveOrUpdate as well as create and update will invoke
		 * productSearchService.index	
		 * 
		 * A copy of properies between Product to IndexProduct
		 * Then IndexProduct will be transfomed to a json representation by the invocation
		 * of .toJSONString on IndexProduct
		 * 
		 * The next step will be to delegate to com.shopizer.search.SearchService.index
		 * searchService.index(json, "product_<LANGUAGE_CODE>_<MERCHANT_CODE>", "product");
		 * 
		 * example ...index(json,"product_en_1",product)
		 * 
		 * Will need to make sure that 
		 * 
		 * -product.json is in src/main/resources/reference
		 * -shopizer-search.xml is in src/main/resources/spring
		 * 
		 * Test the indexing process by creating a unit test
		 * 
		 * 
		 */
		
		FinalPrice price = pricingService.calculateProductPrice(product);

		
		Set<ProductDescription> descriptions = product.getDescriptions();
		for(ProductDescription description : descriptions) {
			
			IndexProduct index = new IndexProduct();

			index.setAvailable(product.isAvailable());
			index.setDescription(description.getDescription());
			index.setName(description.getName());
			index.setPrice(price.getFinalPrice().doubleValue());
			index.setHighlight(description.getProductHighlight());
			if(!StringUtils.isBlank(description.getMetatagKeywords())){
				String[] tags = description.getMetatagKeywords().split(",");
				List<String> tagsList = Arrays.asList(tags);
				index.setTags(tagsList);
			}
			
			Set<Category> categories = product.getCategories();
			if(!CollectionUtils.isEmpty(categories)) {
				List<String> categoryList = new ArrayList<String>();
				for(Category category : categories) {
					categoryList.add(category.getCode());
				}
				index.setCategories(categoryList);
			}
			
			
			
		}
		
		//searchService.index(json, collection, object);

	}
	
	@Transactional
	public void deleteIndex(MerchantStore store, Product product, Language language) throws ServiceException {
		
		/**
		 * When a product is deleted the associated indexes are deleted
		 * 
		 */
		
		//searchService.deleteObject(collection, object, id);
		
	}
	
	//public SearchResponse searchForKeywords(MerchantStore store, String jsonString, int entriesCount) throws ServiceException {
		
	//}
	
}

