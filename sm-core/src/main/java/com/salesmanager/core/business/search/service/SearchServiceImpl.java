package com.salesmanager.core.business.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Service("productSearchService")
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private com.shopizer.search.services.SearchService searchService;

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
		 * searchService.index(json, "product_<LANGUAGE_CODE>_<MERCHANT_ID>", "product");
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

