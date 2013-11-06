package com.salesmanager.web.shop.controller.category.rest;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.CatalogUtils;
import com.salesmanager.web.utils.LocaleUtils;

/**
 * Rest services for category management
 * @author Carl Samson
 *
 */
@Controller
public class ShoppingCategoryRESTController {
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private MerchantStoreService merchantStoreService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CatalogUtils catalogUtils;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCategoryRESTController.class);
	



	
	
	/**
	 * Updates a category for a given MerchantStore
	 */
	@RequestMapping( value="/shop/services/rest/category/{store}/{language}/{id}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateCategory(@PathVariable final String store, @PathVariable final String language, @PathVariable Long id, @Valid @RequestBody Category category, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Language> langs = languageService.getLanguagesMap();
		Language lang = langs.get(language);
		if(lang==null) {
			lang = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
		}
		
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		if(merchantStore!=null) {
			if(!merchantStore.getCode().equals(store)) {
				merchantStore = null;
			}
		}
		
		if(merchantStore== null) {
			merchantStore = merchantStoreService.getByCode(store);
		}
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);
		}
		
		Category oldCategory = categoryService.getById(id);
		if(oldCategory != null){
			category.setId(oldCategory.getId());
			category.setMerchantStore(merchantStore);
			
			List<CategoryDescription> descriptions = category.getDescriptions();
			if(descriptions != null) {
				for(CategoryDescription description : descriptions) {
					description.setLanguage(lang);
					description.setCategory(category);
				}
			}
			
			categoryService.saveOrUpdate(category);
		}else{
			response.sendError(404, "No Category found for ID : " + id);
		}
	}
	
	
	/**
	 * Deletes a category for a given MerchantStore
	 */
	@RequestMapping( value="/shop/services/rest/category/{store}/{language}/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable final String store, @PathVariable final String language, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Category category = categoryService.getById(id);
		if(category != null && category.getMerchantStore().getCode().equalsIgnoreCase(store)){
			categoryService.delete(category);
		}else{
			response.sendError(404, "No Category found for ID : " + id);
		}
	}
	
	
	/**
	 * Create new category for a given MerchantStore
	 */
	@RequestMapping( value="/shop/services/rest/category/{store}/{language}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public com.salesmanager.web.entity.catalog.Category createCategory(@PathVariable final String store, @PathVariable final String language, @Valid @RequestBody Category category, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Language> langs = languageService.getLanguagesMap();
		Language lang = langs.get(language);
		if(lang==null) {
			lang = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
		}
		
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		if(merchantStore!=null) {
			if(!merchantStore.getCode().equals(store)) {
				merchantStore = null;
			}
		}
		
		if(merchantStore== null) {
			merchantStore = merchantStoreService.getByCode(store);
		}
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);
			return null;
		}
		
		List<CategoryDescription> descriptions = category.getDescriptions();
		if(descriptions != null) {
			for(CategoryDescription description : descriptions) {
				description.setLanguage(lang);
				description.setCategory(category);
			}
		}
		
		//check parent
		if(category.getParent() != null) {
			if(category.getParent().getId()==-1) {//this is a root category
				category.setParent(null);
				category.setLineage("/");
				category.setDepth(0);
			}
		}
		
		category.setMerchantStore(merchantStore);
		
		categoryService.saveOrUpdate(category);
		
		if(category.getParent()!=null && category.getParent().getId()!=-1) { 
			Category parent = new Category();
			parent.setId(categoryService.getByCode(store, category.getParent().getCode()).getId());
			parent.setMerchantStore(merchantStore);
			categoryService.addChild(parent, category);
		}
		
		com.salesmanager.web.entity.catalog.Category categoryProxy = catalogUtils.buildProxyCategory(category, merchantStore, LocaleUtils.getLocale(lang));
		return categoryProxy;
	}
	
	/**
	 * Updates a product for a given MerchantStore
	 */
	@RequestMapping( value="/shop/services/rest/products/{store}/{language}/{category}/{id}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateProduct(@PathVariable final String store, @PathVariable final String language, @PathVariable final String category, @PathVariable Long id, @Valid @RequestBody Product product, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Language> langs = languageService.getLanguagesMap();
		Language lang = langs.get(language);
		if(lang==null) {
			lang = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
		}
		
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		if(merchantStore!=null) {
			if(!merchantStore.getCode().equals(store)) {
				merchantStore = null;
			}
		}
		
		if(merchantStore== null) {
			merchantStore = merchantStoreService.getByCode(store);
		}
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);
		}
		
		Product oldProduct = productService.getById(id);
		if(oldProduct != null){
			product.setId(oldProduct.getId());
			product.setMerchantStore(merchantStore);
			
			//TODO: Implementation goes here
			
			productService.saveOrUpdate(product);
		}else{
			response.sendError(404, "No Product found for ID : " + id);
		}
	}
	
	
	/**
	 * Deletes a product for a given MerchantStore
	 */
	@RequestMapping( value="/shop/services/rest/products/{store}/{language}/{category}/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable final String store, @PathVariable final String language, @PathVariable final String category, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Product product = productService.getById(id);
		if(product != null && product.getMerchantStore().getCode().equalsIgnoreCase(store)){
			productService.delete(product);
		}else{
			response.sendError(404, "No Product found for ID : " + id);
		}
	}
	
	
	/**
	 * Create new product for a given MerchantStore
	 */
	@RequestMapping( value="/shop/services/rest/products/{store}/{language}/{category}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public com.salesmanager.web.entity.catalog.Product createProduct(@PathVariable final String store, @PathVariable final String language, @PathVariable final String category, @Valid @RequestBody Product product, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Language> langs = languageService.getLanguagesMap();
		Language lang = langs.get(language);
		if(lang==null) {
			lang = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
		}
		
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		if(merchantStore!=null) {
			if(!merchantStore.getCode().equals(store)) {
				merchantStore = null;
			}
		}
		
		if(merchantStore== null) {
			merchantStore = merchantStoreService.getByCode(store);
		}
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);
			return null;
		}
		
		//TODO: Implementation goes here
		
		
		
		product.setMerchantStore(merchantStore);
		productService.saveOrUpdate(product);
		
		com.salesmanager.web.entity.catalog.Product productProxy = catalogUtils.buildProxyProduct(product, merchantStore, LocaleUtils.getLocale(lang));
		return productProxy;
	}
	
}
