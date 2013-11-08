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
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.utils.CacheUtils;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.Manufacturer;
import com.salesmanager.web.entity.catalog.ProductList;
import com.salesmanager.web.entity.shop.Breadcrumb;
import com.salesmanager.web.entity.shop.BreadcrumbItem;
import com.salesmanager.web.entity.shop.BreadcrumbItemType;
import com.salesmanager.web.entity.shop.PageInformation;
import com.salesmanager.web.populator.manufacturer.ManufacturerPopulator;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.utils.CatalogUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;
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
	private LanguageService languageService;
	
	@Autowired
	private MerchantStoreService merchantStoreService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ManufacturerService manufacturerService;
	
	@Autowired
	private LabelUtils messages;
	
    
    @Autowired
    private ManufacturerPopulator manufacturerPopulator;
	

	
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
	
	@RequestMapping("/shop/category/{friendlyUrl}.html/ref={ref}/filter={filterType}/filter-value={filterValue}")
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
		
		String lineage = new StringBuilder().append(category.getLineage()).append(category.getId()).append(Constants.CATEGORY_LINEAGE_DELIMITER).toString();

		/** retrieves category id drill down**/
		String[] categoryPath = lineage.split(Constants.CATEGORY_LINEAGE_DELIMITER);
		List<Long> ids = new ArrayList<Long>();
		for(int i=0 ; i<categoryPath.length; i++) {
			String sId = categoryPath[i];
			if(!StringUtils.isBlank(sId)) {
				ids.add(Long.parseLong(sId));
			}
		}
	
		List<Category> categories = categoryService.listByIds(store, ids, language);
		
		/** Rebuild breadcrumb **/
		BreadcrumbItem home = new BreadcrumbItem();
		home.setItemType(BreadcrumbItemType.HOME);
		home.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, locale));
		home.setUrl(Constants.HOME_URL);

		Breadcrumb breadCrumb = new Breadcrumb();
		breadCrumb.setLanguage(language);
		
		List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();
		items.add(home);
		
		//category path - use lineage
		for(Category c : categories) {
			BreadcrumbItem categoryBreadcrump = new BreadcrumbItem();
			home.setItemType(BreadcrumbItemType.CATEGORY);
			home.setLabel(c.getDescription().getName());
			home.setUrl(c.getDescription().getSeUrl());
			items.add(categoryBreadcrump);
		}

		breadCrumb.setBreadCrumbs(items);
		breadCrumb.setItemType(BreadcrumbItemType.CATEGORY);
		request.getSession().setAttribute(Constants.BREADCRUMB, breadCrumb);
		request.setAttribute(Constants.BREADCRUMB, breadCrumb);
		/** **/
		
		
		request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);


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
		
		/** List of manufacturers **/
		List<com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manufacturers = manufacturerService.listByProductsByCategoriesId(store, ids, language);
		List<Manufacturer> manufacturerList = new ArrayList<Manufacturer>();
		for(com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer manufacturer : manufacturers) {
			
			Manufacturer manuf = manufacturerPopulator.populate(manufacturer, new Manufacturer());
			manufacturerList.add(manuf);
			
		}
		
		
		model.addAttribute("manufacturers", manufacturerList);
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
	
	

	/**
	 * Returns all categories for a given MerchantStore
	 */
	@RequestMapping("/shop/services/category/{store}/{language}")
	@ResponseBody
	public List<com.salesmanager.web.entity.catalog.Category> getCategories(@PathVariable final String language, @PathVariable final String store, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Language> langs = languageService.getLanguagesMap();
		Language l = langs.get(language);
		if(l==null) {
			l = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
		}
		
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
		
		List<Category> categories = categoryService.listByStore(merchantStore, l);
		
		List<com.salesmanager.web.entity.catalog.Category> returnCategories = new ArrayList<com.salesmanager.web.entity.catalog.Category>();
		for(Category category : categories) {
			com.salesmanager.web.entity.catalog.Category categoryProxy = catalogUtils.buildProxyCategory(category, merchantStore, LocaleUtils.getLocale(l));
			returnCategories.add(categoryProxy);
		}
		
		return returnCategories;
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
	@RequestMapping("/shop/services/products/{store}/{language}/{category}")
	@ResponseBody
	public ProductList getProducts(@PathVariable final String store, @PathVariable final String language, @PathVariable final String category, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//http://localhost:8080/sm-shop/shop/services/products/DEFAULT/en/book.html

		try {

		
			/**
			 * How to Spring MVC Rest web service - ajax / jquery
			 * http://codetutr.com/2013/04/09/spring-mvc-easy-rest-based-json-services-with-responsebody/
			 */
			
			MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
			Map<String,Language> langs = languageService.getLanguagesMap();

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
			
			//get the category by code
			Category cat = categoryService.getBySeUrl(merchantStore, category);

			if(cat==null) {
				LOGGER.error("Category with friendly url " + category + " is null");
				response.sendError(503, "Category is null");//TODO localized message
			}
			
			String lineage = new StringBuilder().append(cat.getLineage()).append(cat.getId()).append("/").toString();
			
			List<Category> categories = categoryService.listByLineage(store, lineage);
			
			List<Long> ids = new ArrayList<Long>();
			if(categories!=null && categories.size()>0) {
				for(Category c : categories) {
					ids.add(c.getId());
				}
			} 
			ids.add(cat.getId());
			
			Language lang = langs.get(language);
			if(lang==null) {
				lang = langs.get(Constants.DEFAULT_LANGUAGE);
			}
			
			List<com.salesmanager.core.business.catalog.product.model.Product> products = productService.getProducts(ids, lang);
			
			ProductList productList = new ProductList();

			for(Product product : products) {
				//create new proxy product
				com.salesmanager.web.entity.catalog.Product p = catalogUtils.buildProxyProduct(product,merchantStore,LocaleUtils.getLocale(lang));
				productList.getProducts().add(p);
	
			}
			
			productList.setTotalCount(productList.getProducts().size());
			return productList;
			
		
		} catch (Exception e) {
			LOGGER.error("Error while getting category",e);
			response.sendError(503, "Error while getting category");
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
	public ProductList getProducts(@PathVariable int start, @PathVariable int max, @PathVariable String store, @PathVariable final String language, @PathVariable final String category, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		try {

			
			/**
			 * How to Spring MVC Rest web service - ajax / jquery
			 * http://codetutr.com/2013/04/09/spring-mvc-easy-rest-based-json-services-with-responsebody/
			 */
			
			MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
			
			
			Map<String,Language> langs = languageService.getLanguagesMap();
			
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
			
			//get the category by code
			Category cat = categoryService.getBySeUrl(merchantStore, category);
			
			if(cat==null) {
				LOGGER.error("Category " + category + " is null");
				response.sendError(503, "Category is null");//TODO localized message
				return null;
			}
			
			String lineage = new StringBuilder().append(cat.getLineage()).append(cat.getId()).append("/").toString();
			
			List<Category> categories = categoryService.listByLineage(store, lineage);
			
			List<Long> ids = new ArrayList<Long>();
			if(categories!=null && categories.size()>0) {
				for(Category c : categories) {
					ids.add(c.getId());
				}
			} 
			ids.add(cat.getId());
			

			Language lang = langs.get(language);
			if(lang==null) {
				lang = langs.get(Constants.DEFAULT_LANGUAGE);
			}
			
			ProductCriteria productCriteria = new ProductCriteria();
			productCriteria.setMaxCount(max);
			productCriteria.setStartIndex(start);
			productCriteria.setCategoryIds(ids);

			com.salesmanager.core.business.catalog.product.model.ProductList products = productService.listByStore(merchantStore, lang, productCriteria);

			ProductList productList = new ProductList();
			for(Product product : products.getProducts()) {

				//create new proxy product
				com.salesmanager.web.entity.catalog.Product p = catalogUtils.buildProxyProduct(product,merchantStore,LocaleUtils.getLocale(lang));
				productList.getProducts().add(p);
				
			}
			
			productList.setTotalCount(products.getTotalCount());
			
			
			return productList;
			
		
		} catch (Exception e) {
			LOGGER.error("Error while getting products",e);
			response.sendError(503, "An error occured while retrieving products " + e.getMessage());
		}
		
		return null;
	}
	

	

	

}
