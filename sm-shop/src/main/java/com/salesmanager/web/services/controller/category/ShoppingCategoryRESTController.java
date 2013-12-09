package com.salesmanager.web.services.controller.category;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.category.PersistableCategory;
import com.salesmanager.web.entity.catalog.category.ReadableCategory;
import com.salesmanager.web.populator.catalog.PersistableCategoryPopulator;
import com.salesmanager.web.populator.catalog.ReadableCategoryPopulator;

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
	

	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCategoryRESTController.class);
	

	
	@RequestMapping( value="/shop/services/public/category/{store}/{language}/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ReadableCategory getCategory(@PathVariable final String store, @PathVariable final String language, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		
		
		try {
			
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
			
			Category dbCategory = categoryService.getByLanguage(id, lang);
			
			if(dbCategory==null) {
				response.sendError(503,  "Invalid category id");
				return null;
			}
			
			if(dbCategory.getMerchantStore().getId().intValue()!=merchantStore.getId().intValue()){
				response.sendError(503, "Invalid category id");
				return null;
			}
			

			ReadableCategoryPopulator populator = new ReadableCategoryPopulator();

			//TODO count products by category
			ReadableCategory category = populator.populate(dbCategory, new ReadableCategory(), merchantStore, merchantStore.getDefaultLanguage());

			return category;
		
		} catch (Exception e) {
			LOGGER.error("Error while saving category",e);
			try {
				response.sendError(503, "Error while saving category " + e.getMessage());
			} catch (Exception ignore) {
			}
			return null;
		}
	}
	
/*	*//**
	 * Updates a category for a given MerchantStore
	 *//*
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
	}*/
	
	
	/**
	 * Create new category for a given MerchantStore
	 */
	@RequestMapping( value="/shop/services/private/category/{store}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PersistableCategory createCategory(@PathVariable final String store, @Valid @RequestBody PersistableCategory category, HttpServletRequest request, HttpServletResponse response) {
		
		
		try {


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

			PersistableCategoryPopulator populator = new PersistableCategoryPopulator();
			populator.setCategoryService(categoryService);
			populator.setLanguageService(languageService);
			
			Category dbCategory = populator.populate(category, new Category(), merchantStore, merchantStore.getDefaultLanguage());

			dbCategory.setMerchantStore(merchantStore);
			
			categoryService.saveOrUpdate(dbCategory);
			category.setId(dbCategory.getId());

			return category;
		
		} catch (Exception e) {
			LOGGER.error("Error while saving category",e);
			try {
				response.sendError(503, "Error while saving category " + e.getMessage());
			} catch (Exception ignore) {
			}
			return null;
		}
	}
	
	
	
	/**
	 * Deletes a category for a given MerchantStore
	 */
	@RequestMapping( value="/shop/services/private/category/{store}/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable final String store, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Category category = categoryService.getById(id);
		if(category != null && category.getMerchantStore().getCode().equalsIgnoreCase(store)){
			categoryService.delete(category);
		}else{
			response.sendError(404, "No Category found for ID : " + id);
		}
	}
	

	
/*	*//**
	 * Updates a product for a given MerchantStore
	 *//*
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
	}*/
	
	
	/**
	 * Deletes a product for a given MerchantStore
	 */
	@RequestMapping( value="/shop/services/private/products/{store}/{language}/{category}/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable final String store, @PathVariable final String language, @PathVariable final String category, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Product product = productService.getById(id);
		if(product != null && product.getMerchantStore().getCode().equalsIgnoreCase(store)){
			productService.delete(product);
		}else{
			response.sendError(404, "No Product found for ID : " + id);
		}
	}
	
	

	
}
