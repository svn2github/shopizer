package com.salesmanager.web.shop.controller.category;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.utils.CacheUtils;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shop.PageInformation;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.utils.CatalogUtils;
import com.salesmanager.web.utils.LocaleUtils;


@Controller
public class ShoppingCategoryController {
	
	@Autowired
	LanguageService languageService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CatalogUtils catalogUtils;
	
	
	/**
	 * Category page entry point
	 * @param friendlyUrl
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/shop/category/{friendlyUrl}.html")
	public String displayCategory(@PathVariable final String friendlyUrl, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		
		
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		//get category
		Category category = categoryService.getBySeUrl(store, friendlyUrl);
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		if(category==null) {
			//TODO not found object
		}
		
		com.salesmanager.web.entity.catalog.Category categoryProxy = catalogUtils.buildProxyCategory(category, store, locale);
		

		//meta information
		PageInformation pageInformation = new PageInformation();
		pageInformation.setPageDescription(categoryProxy.getMetaDescription());
		pageInformation.setPageKeywords(categoryProxy.getKeyWords());
		pageInformation.setPageTitle(categoryProxy.getTitle());
		pageInformation.setPageUrl(categoryProxy.getFriendlyUrl());
		
		request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);
		
		//TODO Cache
		StringBuilder subCategoriesCacheKey = new StringBuilder();
		subCategoriesCacheKey
		.append(store.getId())
		.append("_")
		.append(Constants.SUBCATEGORIES_CACHE_KEY)
		.append("-")
		.append(language.getCode());
		
		StringBuilder subCategoriesMissed = new StringBuilder();
		subCategoriesMissed
		.append(subCategoriesCacheKey.toString())
		.append(Constants.MISSED_CACHE_KEY);
		
		List<com.salesmanager.web.entity.catalog.Category> subCategories = null;
		
		if(store.isUseCache()) {
			
		    CacheUtils cache = CacheUtils.getInstance();
			
			//get from the cache
			subCategories = (List<com.salesmanager.web.entity.catalog.Category>) cache.getFromCache(subCategoriesCacheKey.toString());
			
			Boolean missedContent = null;
			if(subCategories==null) {
				//get from missed cache
				missedContent = (Boolean)cache.getFromCache(subCategoriesMissed.toString());
			}
			
			if(subCategories==null && missedContent==null) {
				subCategories = getSubCategories(store,category,language,locale);
			}
		
		} else {
			subCategories = getSubCategories(store,category,language,locale);
		}

		
		//TODO get products by category
		//TODO number of items by category
		
		model.addAttribute("category", categoryProxy);
		model.addAttribute("subCategories", subCategories);
		
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Category.category).append(".").append(store.getStoreTemplate());

		return template.toString();
	}

	/**
	 * Returns all categories for a given MerchantStore
	 */
	@RequestMapping("/shop/services/category/{store}/{language}")
	@ResponseBody
	public com.salesmanager.web.entity.catalog.Category[] displayCategory(@PathVariable final String language, @PathVariable final String store, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Language> langs = languageService.getLanguagesMap();
		
		return null;
	}

	/**
	 * Returns an array of products belonging to a given category
	 * in a given language for a given store
	 * url example :  http://<host>/sm-shop/shop/services/products/DEFAULT/BOOKS
	 * @param store
	 * @param language
	 * @param category
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shop/services/products/{store}/{language}/{category}.html")
	@ResponseBody
	public com.salesmanager.web.entity.catalog.Product[] getProducts(@PathVariable final String store, @PathVariable final String language, @PathVariable final String category, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//http://localhost:8080/sm-shop/shop/services/products/DEFAULT/en/book.html
		
		
	
		try {

		
			/**
			 * How to Spring MVC Rest web service - ajax / jquery
			 * http://codetutr.com/2013/04/09/spring-mvc-easy-rest-based-json-services-with-responsebody/
			 */
			
			MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
			Map<String,Language> langs = languageService.getLanguagesMap();
			
			if(merchantStore!= null) {//TODO
				//store = merchantStore.getCode();
			}
			
			//get the category by code
			Category cat = categoryService.getByCode(store, category);
			
			//TODO
			if(cat==null) {
				//log & return null
				response.sendError(503, "Category is null");//TODO localized message
			}
			
			String lineage = new StringBuilder().append(cat.getLineage()).append(cat.getId()).append("/").toString();
			
			List<Category> categories = categoryService.listByLineage(store, lineage);
			
			//TODO
			if(categories==null || categories.size()==0) {
				
			}
			
			List<Long> ids = new ArrayList<Long>();
			for(Category c : categories) {
				ids.add(c.getId());
			}
			ids.add(cat.getId());
			Language lang = langs.get(language);
			if(lang==null) {
				lang = langs.get(Constants.DEFAULT_LANGUAGE);
			}
			
			List<com.salesmanager.core.business.catalog.product.model.Product> products = productService.getProducts(ids, lang);
			
			com.salesmanager.web.entity.catalog.Product[] returnedProducts = new com.salesmanager.web.entity.catalog.Product[products.size()];
			
			int i = 0;
			for(Product product : products) {
				
				
				//create new proxy product
				com.salesmanager.web.entity.catalog.Product p = catalogUtils.buildProxyProduct(product,merchantStore,LocaleUtils.getLocale(lang));
				returnedProducts[i] = p;
				i++;
				
			}
			
			
			return returnedProducts;
			
		
		} catch (Exception e) {
			//TODO log
			response.sendError(503, "TODO error");
		}
		
		return null;
	}
	
	
	/**
	 * Will page products of a given category
	 * @param store
	 * @param language
	 * @param category
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shop/services/products/page/{start}/{max}/{store}/{language}/{category}.html")
	@ResponseBody
	public com.salesmanager.web.entity.catalog.Product[] pageProducts(@PathVariable int start, @PathVariable int max, @PathVariable String store, @PathVariable final String language, @PathVariable final String category, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		try {

			
			/**
			 * How to Spring MVC Rest web service - ajax / jquery
			 * http://codetutr.com/2013/04/09/spring-mvc-easy-rest-based-json-services-with-responsebody/
			 */
			
			MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
			Map<String,Language> langs = languageService.getLanguagesMap();
			
			if(merchantStore!= null) {//TODO
				//store = merchantStore.getCode();
			}
			
			//get the category by code
			Category cat = categoryService.getByCode(store, category);
			
			//TODO
			if(cat==null) {
				//log & return null
				response.sendError(503, "Category is null");//TODO localized message
			}
			
			String lineage = new StringBuilder().append(cat.getLineage()).append(cat.getId()).append("/").toString();
			
			List<Category> categories = categoryService.listByLineage(store, lineage);
			
			//TODO
			if(categories==null || categories.size()==0) {
				
			}
			
			List<Long> ids = new ArrayList<Long>();
			for(Category c : categories) {
				ids.add(c.getId());
			}
			ids.add(cat.getId());
			Language lang = langs.get(language);
			if(lang==null) {
				lang = langs.get(Constants.DEFAULT_LANGUAGE);
			}
			
			
			//TODO page products
			List<com.salesmanager.core.business.catalog.product.model.Product> products = productService.getProducts(ids, lang);
			
			com.salesmanager.web.entity.catalog.Product[] returnedProducts = new com.salesmanager.web.entity.catalog.Product[products.size()];
			
			int i = 0;
			for(Product product : products) {
				
				
				//create new proxy product
				com.salesmanager.web.entity.catalog.Product p = catalogUtils.buildProxyProduct(product,merchantStore,LocaleUtils.getLocale(lang));
				returnedProducts[i] = p;
				i++;
				
			}
			
			
			return returnedProducts;
			
		
		} catch (Exception e) {
			//TODO log
			response.sendError(503, "TODO error");
		}
		
		return null;
	}
	
	private List<com.salesmanager.web.entity.catalog.Category> getSubCategories(MerchantStore store, Category category, Language language, Locale locale) {
		
		
		//sub categories
		List<Category> subCategories = categoryService.listByParent(category, language);
		List<com.salesmanager.web.entity.catalog.Category> subCategoryProxies = new ArrayList<com.salesmanager.web.entity.catalog.Category>();
		for(Category sub : subCategories) {
			
			com.salesmanager.web.entity.catalog.Category cProxy =  catalogUtils.buildProxyCategory(sub, store, locale);
			subCategoryProxies.add(cProxy);
		}
		
		return subCategoryProxies;
		
	}
	
	
}
