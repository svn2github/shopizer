package com.salesmanager.web.shop.controller.category;


import java.util.ArrayList;
import java.util.List;
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
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.web.constants.Constants;


@Controller
public class ShoppingCategoryController {
	
	@Autowired
	LanguageService languageService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	
	/**
	 * Category page entry point
	 * @param friendlyUrl
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shop/category/{friendlyUrl}.html")
	public String displayCategory(@PathVariable final String friendlyUrl, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		
		return "";
	}

	/**
	 * Returns all categories for a given MerchantStore
	 */
	@RequestMapping("/shop/services/category/{language}/{store}")
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
	public com.salesmanager.web.entity.catalog.Product[] getProducts(@PathVariable String store, @PathVariable final String language, @PathVariable final String category, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		Map<String,Language> langs = languageService.getLanguagesMap();
		
		if(merchantStore!= null) {
			store = merchantStore.getCode();
		}
		
		//get the category by code
		Category cat = categoryService.getByCode(store, category);
		
		//TODO
		if(cat==null) {
			
		}
		
		List<Category> categories = categoryService.listByLineage(store, cat.getLineage());
		
		//TODO
		if(categories==null || categories.size()==0) {
			
		}
		
		List<Long> ids = new ArrayList<Long>();
		for(Category c : categories) {
			ids.add(c.getId());
		}
		
		Language lang = langs.get(language);
		if(lang==null) {
			lang = langs.get(Constants.DEFAULT_LANGUAGE);
		}
		
		List<com.salesmanager.core.business.catalog.product.model.Product> products = productService.getProducts(ids, lang);
		
		return null;
	}
	
}
