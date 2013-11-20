package com.salesmanager.web.services.controller.product;

import java.util.ArrayList;
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
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionValueService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.tax.service.TaxClassService;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.ProductList;
import com.salesmanager.web.entity.catalog.product.PersistableProduct;
import com.salesmanager.web.entity.catalog.product.ReadableProduct;
import com.salesmanager.web.entity.catalog.product.ReadableProductList;
import com.salesmanager.web.populator.catalog.PersistableProductPopulator;
import com.salesmanager.web.populator.catalog.ReadableProductPopulator;
import com.salesmanager.web.shop.model.filter.QueryFilter;
import com.salesmanager.web.shop.model.filter.QueryFilterType;
import com.salesmanager.web.utils.LocaleUtils;

/**
 * API for create, read and delete Product
 * @author Carl Samson
 *
 */
@Controller
public class ShopProductRESTController {
	
	@Autowired
	private MerchantStoreService merchantStoreService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private PricingService pricingService;

	@Autowired
	private ProductOptionService productOptionService;
	
	@Autowired
	private ProductOptionValueService productOptionValueService;
	
	@Autowired
	private TaxClassService taxClassService;
	
	@Autowired
	private ManufacturerService manufacturerService;
	
	@Autowired
	private LanguageService languageService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShopProductRESTController.class);
	
	
	/**
	 * Create new product for a given MerchantStore
	 */
	@RequestMapping( value="/shop/services/private/products/{store}/create", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PersistableProduct createProduct(@PathVariable final String store, @Valid @RequestBody PersistableProduct product, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
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

			PersistableProductPopulator populator = new PersistableProductPopulator();
			populator.setCategoryService(categoryService);
			populator.setProductService(productService);
			populator.setProductOptionService(productOptionService);
			populator.setProductOptionValueService(productOptionValueService);
			populator.setManufacturerService(manufacturerService);
			populator.setTaxClassService(taxClassService);
			populator.setLanguageService(languageService);
			
			populator.populate(product, new Product(), merchantStore, merchantStore.getDefaultLanguage());
			
			return product;
			
		} catch (Exception e) {
			LOGGER.error("Error while saving product",e);
			try {
				response.sendError(503, "Error while saving product " + e.getMessage());
			} catch (Exception ignore) {
			}
			
			return null;
		}
		
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
	@RequestMapping("/shop/services/public/products/page/{start}/{max}/{store}/{language}/{category}.html")
	@ResponseBody
	public ReadableProductList getProducts(@PathVariable int start, @PathVariable int max, @PathVariable String store, @PathVariable final String language, @PathVariable final String category, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		return this.getProducts(start, max, store, language, category, null, model, request, response);
	}
	
	
	/**
	 * An entry point for filtering by another entity such as Manufacturer
	 * filter=BRAND&filter-value=123
	 * @param start
	 * @param max
	 * @param store
	 * @param language
	 * @param category
	 * @param filterType
	 * @param filterValue
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shop/services/products/public/page/{start}/{max}/{store}/{language}/{category}.html/filter={filterType}/filter-value={filterValue}")
	@ResponseBody
	public ReadableProductList getProductsFilteredByType(@PathVariable int start, @PathVariable int max, @PathVariable String store, @PathVariable final String language, @PathVariable final String category, @PathVariable final String filterType, @PathVariable final String filterValue, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List<QueryFilter> queryFilters = null;
		try {
			if(filterType.equals(QueryFilterType.BRAND.name())) {//the only one implemented so far
				QueryFilter filter = new QueryFilter();
				filter.setFilterType(QueryFilterType.BRAND);
				filter.setFilterId(Long.parseLong(filterValue));
				if(queryFilters==null) {
					queryFilters = new ArrayList<QueryFilter>();
				}
				queryFilters.add(filter);
			}
		} catch(Exception e) {
			LOGGER.error("Invalid filter or filter-value " + filterType + " - " + filterValue,e);
		}
		
		return this.getProducts(start, max, store, language, category, queryFilters, model, request, response);
	}
	
	
	private ReadableProductList getProducts(final int start, final int max, final String store, final String language, final String category, final List<QueryFilter> filters, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
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
			
			if(filters!=null) {
				for(QueryFilter filter : filters) {
					if(filter.getFilterType().name().equals(QueryFilterType.BRAND.name())) {//the only filter implemented
						productCriteria.setManufacturerId(filter.getFilterId());
					}
				}
			}

			com.salesmanager.core.business.catalog.product.model.ProductList products = productService.listByStore(merchantStore, lang, productCriteria);

			
			ReadableProductPopulator populator = new ReadableProductPopulator();
			populator.setPricingService(pricingService);
			
			
			ReadableProductList productList = new ReadableProductList();
			for(Product product : products.getProducts()) {

				//create new proxy product
				ReadableProduct readProduct = populator.populate(product, new ReadableProduct(), merchantStore, lang);
				
				//com.salesmanager.web.entity.catalog.Product p = catalogUtils.buildProxyProduct(product,merchantStore,LocaleUtils.getLocale(lang));
				productList.getProducts().add(readProduct);
				
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