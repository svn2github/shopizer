package com.salesmanager.web.shop.controller.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationshipType;
import com.salesmanager.core.business.content.model.Content;
import com.salesmanager.core.business.content.model.ContentDescription;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.search.model.SearchKeywords;
import com.salesmanager.core.business.search.model.SearchResponse;
import com.salesmanager.core.business.search.service.SearchService;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shop.Breadcrumb;
import com.salesmanager.web.entity.shop.BreadcrumbItem;
import com.salesmanager.web.entity.shop.BreadcrumbItemType;
import com.salesmanager.web.entity.shop.PageInformation;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.model.search.AutoCompleteRequest;
@Controller
public class SearchController {
	
	@Autowired
	private MerchantStoreService merchantStoreService;
	
	@Autowired
	private SearchService searchService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
	
	private final static int AUTOCOMPLETE_ENTRIES_COUNT = 15;
	
	
	/**
	 * Retrieves a list of keywords for a given series of character typed by the end user
	 * This is used for auto complete on search input field
	 * @param json
	 * @param store
	 * @param language
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shop/services/search/{store}/{language}/autocomplete.html")
	@ResponseBody
	public String autocomplete(@RequestParam("q") String query, @PathVariable String store, @PathVariable final String language, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		if(merchantStore!=null) {
			if(!merchantStore.getCode().equals(store)) {
				merchantStore = null; //reset for the current request
			}
		}
		
		if(merchantStore== null) {
			merchantStore = merchantStoreService.getByCode(store);
		}
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);//TODO localized message
			return null;
		}
		
		AutoCompleteRequest req = new AutoCompleteRequest(store,language);
		
		SearchKeywords keywords = searchService.searchForKeywords(req.getCollectionName(), req.toJSONString(query), AUTOCOMPLETE_ENTRIES_COUNT);
		return keywords.toJSONString();
		
	}

	
	/**
	 * Displays the search result page
	 * @param json
	 * @param store
	 * @param language
	 * @param start
	 * @param max
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shop/services/search/{store}/{language}/{start}/{max}/term.html")
	@ResponseBody
	public String search(@RequestBody String json, @PathVariable String store, @PathVariable final String language, @PathVariable int start, @PathVariable int max, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		if(merchantStore!=null) {
			if(!merchantStore.getCode().equals(store)) {
				merchantStore = null; //reset for the current request
			}
		}
		
		if(merchantStore== null) {
			merchantStore = merchantStoreService.getByCode(store);
		}
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);//TODO localized message
			return null;
		}
		
		SearchResponse resp = searchService.search(merchantStore, language, json, max, start);
		return null;
		
	}
	
	@RequestMapping(value={"/shop/search/search.html"}, method=RequestMethod.POST)
	public String displaySearch(@RequestParam("q") String query, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Search.search).append(".").append(store.getStoreTemplate());

		return template.toString();
	}
	
	
}
