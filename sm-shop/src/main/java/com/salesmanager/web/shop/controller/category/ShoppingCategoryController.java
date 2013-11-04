package com.salesmanager.web.shop.controller.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.CacheUtils;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shop.PageInformation;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.utils.CatalogUtils;
import com.salesmanager.web.utils.PageBuilderUtils;


/**
 * Renders a given category page based on friendly url
 * Can also filter by facets such as manufacturer
 * @author Carl Samson
 *
 */
@Controller
public class ShoppingCategoryController {
	

	
	@Autowired
	private CategoryService categoryService;
	

	
	@Autowired
	private CatalogUtils catalogUtils;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCategoryController.class);
	
	
	/**
	 * 
	 * @param friendlyUrl
	 * @param ref
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shop/category/{friendlyUrl}.html/ref={ref}")
	public String displayCategoryWithReference(@PathVariable final String friendlyUrl, @PathVariable final String ref, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		
		
		return this.displayCategory(friendlyUrl,ref,model,request,response,locale);
	}
	
	@RequestMapping("/shop/category/filter/{friendlyUrl}.html/ref={ref}/filter={filterType}/fillter-value={filterValue}")
	public String displayCategoryWithReference(@PathVariable final String friendlyUrl, @PathVariable final String ref, @PathVariable final String filterType, @PathVariable final String filterValue, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		
		
		return this.displayCategory(friendlyUrl,ref,model,request,response,locale);
	}
	
	
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
	public String displayCategoryNoReference(@PathVariable final String friendlyUrl, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		
		
		return this.displayCategory(friendlyUrl,null,model,request,response,locale);
	}
	
	//TODO handle filters
	@SuppressWarnings("unchecked")
	private String displayCategory(final String friendlyUrl, final String ref, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		//get category
		Category category = categoryService.getBySeUrl(store, friendlyUrl);
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		if(category==null) {
			LOGGER.error("No category found for friendlyUrl " + friendlyUrl);
			//redirect on page not found
			return PageBuilderUtils.build404(store);
			
		}
		
		com.salesmanager.web.entity.catalog.Category categoryProxy = catalogUtils.buildProxyCategory(category, store, locale);
		

		//meta information
		PageInformation pageInformation = new PageInformation();
		pageInformation.setPageDescription(categoryProxy.getMetaDescription());
		pageInformation.setPageKeywords(categoryProxy.getKeyWords());
		pageInformation.setPageTitle(categoryProxy.getTitle());
		pageInformation.setPageUrl(categoryProxy.getFriendlyUrl());
		
		request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);

		String lineage = new StringBuilder().append(category.getLineage()).append(category.getId()).append("/").toString();

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
		Map<String,Long> countProductsByCategories = null;
		
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
				countProductsByCategories = getProductsByCategory(store, lineage);
				subCategories = getSubCategories(store,category,countProductsByCategories,language,locale);
			}
			

			
		} else {
			countProductsByCategories = getProductsByCategory(store, lineage);
			subCategories = getSubCategories(store,category,countProductsByCategories,language,locale);
			
		}

		//Parent category
		com.salesmanager.web.entity.catalog.Category parentProxy = null;
		if(!StringUtils.isBlank(ref)) {
			try {
				Long parentId = Long.parseLong(ref);
				Category parent = categoryService.getById(parentId);
				parentProxy = catalogUtils.buildProxyCategory(parent, store, locale);
				
			} catch(Exception e) {
				LOGGER.error("Cannot parse category id to Long ",ref );
			}
		}

		model.addAttribute("parent", parentProxy);
		model.addAttribute("category", categoryProxy);
		model.addAttribute("subCategories", subCategories);
		
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Category.category).append(".").append(store.getStoreTemplate());

		return template.toString();
	}
	
	private Map<String,Long> getProductsByCategory(MerchantStore store, String lineage) throws Exception {
		
		
		List<Category> categories = categoryService.listByLineage(store, lineage);
		
		if(CollectionUtils.isEmpty(categories)) {
			return null;
		}
		List<Long> ids = new ArrayList<Long>();
		if(categories!=null && categories.size()>0) {
			for(Category c : categories) {
				ids.add(c.getId());
			}
		} 

		List<Object[]> countProductsByCategories = categoryService.countProductsByCategories(store, ids);
		Map<String, Long> countByCategories = new HashMap<String,Long>();
		for(Object[] counts : countProductsByCategories) {
			countByCategories.put((String)counts[0], (Long)counts[1]);
		}
		
		return countByCategories;
		
	}
	
	private List<com.salesmanager.web.entity.catalog.Category> getSubCategories(MerchantStore store, Category category, Map<String,Long> productCount, Language language, Locale locale) {
		
		
		//sub categories
		List<Category> subCategories = categoryService.listByParent(category, language);
		List<com.salesmanager.web.entity.catalog.Category> subCategoryProxies = new ArrayList<com.salesmanager.web.entity.catalog.Category>();
		for(Category sub : subCategories) {
			
			com.salesmanager.web.entity.catalog.Category cProxy =  catalogUtils.buildProxyCategory(sub, store, locale);
			if(productCount!=null) {
				Long total = productCount.get(cProxy.getCode());
				if(total!=null) {
					cProxy.setTotalCount(total);
				}
			}
			subCategoryProxies.add(cProxy);
		}
		
		return subCategoryProxies;
		
	}

}
