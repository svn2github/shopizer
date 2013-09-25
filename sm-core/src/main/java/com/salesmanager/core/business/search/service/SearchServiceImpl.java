package com.salesmanager.core.business.search.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.search.model.IndexProduct;
import com.shopizer.search.services.SearchResponse;


@Service("productSearchService")
public class SearchServiceImpl implements SearchService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);
	
	
	private final static String PRODUCT_INDEX_NAME = "product";
	private final static String UNDERSCORE = "_";
	
	@Autowired
	private com.shopizer.search.services.SearchService searchService;
	
	@Autowired
	private PricingService pricingService;

	@Override
	public void index(MerchantStore store, Product product)
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
		 * example ...index(json,"product_en_default",product)
		 * 
		 */
		
		FinalPrice price = pricingService.calculateProductPrice(product);

		
		Set<ProductDescription> descriptions = product.getDescriptions();
		for(ProductDescription description : descriptions) {
			
			StringBuilder collectionName = new StringBuilder();
			collectionName.append(PRODUCT_INDEX_NAME).append(UNDERSCORE).append(description.getLanguage().getCode()).append(UNDERSCORE).append(store.getCode().toLowerCase());
			
			IndexProduct index = new IndexProduct();

			index.setId(String.valueOf(product.getId()));
			index.setStore(store.getCode().toLowerCase());
			index.setLang(description.getLanguage().getCode());
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
			
			String jsonString = index.toJSONString();
			try {
				searchService.index(jsonString, collectionName.toString(), new StringBuilder().append(PRODUCT_INDEX_NAME).append(UNDERSCORE).append(description.getLanguage().getCode()).toString());
			} catch (Exception e) {
				throw new ServiceException("Cannot index product id [" + product.getId() + "], " + e.getMessage() ,e);
			}
		}
	}

	@Override
	public void deleteIndex(MerchantStore store, Product product) throws ServiceException {
		
		Set<ProductDescription> descriptions = product.getDescriptions();
		for(ProductDescription description : descriptions) {
			
			StringBuilder collectionName = new StringBuilder();
			collectionName.append(PRODUCT_INDEX_NAME).append(UNDERSCORE).append(description.getLanguage().getCode()).append(UNDERSCORE).append(store.getCode().toLowerCase());

			try {
				searchService.deleteObject(collectionName.toString(), new StringBuilder().append(PRODUCT_INDEX_NAME).append(UNDERSCORE).append(description.getLanguage().getCode()).toString(), String.valueOf(product.getId()));
			} catch (Exception e) {
				LOGGER.error("Cannot delete index for product id [" + product.getId() + "], ",e);
				//do not throw exception ??
				//throw new ServiceException("Cannot delete index for product id [" + product.getId() + "], " + e.getMessage() ,e);
			}
		}
	
	}
	
	public void searchForKeywords(MerchantStore store, String jsonString, int entriesCount) throws ServiceException {
		
		/**
		 * 	$('#search').searchAutocomplete({
			url: '<%=request.getContextPath()%>/search/autocomplete/keyword_en'
		  		//filter: function() { 
				//return '\"filter\" : {\"numeric_range\" : {\"price\" : {\"from\" : \"22\",\"to\" : \"45\",\"include_lower\" : true,\"include_upper\" : true}}}';
		  		//}
     		});
     		
     		
     		$('#searchForm').submit(function() {

			$('#profiles').html('');
			$('#facets').html('');

			$.search.searchTerm({
				  	field: $('#search'),
				  	url: '<%=request.getContextPath()%>/search/product_en',
				  	highlights: '\"highlight\":{\"fields\":{\"description\":{\"pre_tags\" : [\"<strong>\"], \"post_tags\" : [\"</strong>\"]},\"name\":{\"pre_tags\" : [\"<strong>\"], \"post_tags\" : [\"</strong>\"]}}}',
				  	facets: function() { 
						return '\"facets\" : { \"category\" : { \"terms\" : {\"field\" : \"category\"}}}';
				  	}
				  },
				  function(suggestions) {//handle responses
					  var i =1;
					  searchresults = '';
					  $.each(suggestions, function() {
							var s = this.source;
							var h = this.highlightFields;
							var description = s.description;
							var name = s.name;
							
							if(h != null) {
								if(h.description!=null) {
									//alert(h.description.fragments[0]);
									description = h.description.fragments[0];
								}
								if(h.name!=null) {
									name = h.name.fragments[0];
								}
							} 
							var c = 'span-5';
							if(i%4==0) {
								c = c + ' last';
							}
							searchresults = searchresults + '<div class="' + c + '" style="height:250px;"><div>' + s.id + '<br/>' + name + '<br/>' + s.price + '<br/>' + description + '</div></div>';
							i++;
				  	  });
					  $('#profiles').html(searchresults);
				  },
				  function(facets) {//handle facets
					  
					  var facet = '';
					  
					  $.each(facets, function() {
						  
						  var name = this.name;
						  facet = facet + '<ul><strong>' + name + '</strong>';
						  var entries = this.entries;
						  $.each(entries, function() {
							  
							  facet = facet + '<li>' + this.name + ' (' + this.count + ')</li>';
							  
						  });
						  
						  facet = facet + '<ul><br/><br/>';
						  
						  
					  });
					  
					  $('#facets').html(facet);
					  
				  }
			);


			return false; 
	});
		 */
		try {
			SearchResponse response = searchService.searchAutoComplete("", "", 25);
			
			//transform t SearchResponse specific to sm-core
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

