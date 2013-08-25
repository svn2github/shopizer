package com.salesmanager.web.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.content.model.Content;
import com.salesmanager.core.business.content.model.ContentDescription;
import com.salesmanager.core.business.content.model.ContentType;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.init.service.InitializationDatabase;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.model.MerchantConfigurationType;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.business.system.service.SystemConfigurationService;
import com.salesmanager.core.utils.CacheUtils;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.web.admin.security.UserServicesImpl;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shop.Breadcrumb;
import com.salesmanager.web.entity.shop.BreadcrumbItem;
import com.salesmanager.web.entity.shop.BreadcrumbItemType;
import com.salesmanager.web.entity.shop.PageInformation;
import com.salesmanager.web.entity.shoppingcart.ShoppingCart;
import com.salesmanager.web.utils.AppConfiguration;
import com.salesmanager.web.utils.LabelUtils;






/**
 * Servlet Filter implementation class StoreFilter
 */

public class StoreFilter extends HandlerInterceptorAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StoreFilter.class);
	

	
	@Autowired
	private AppConfiguration appConfiguration;
	
	@Autowired
	private CoreConfiguration configuration;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private InitializationDatabase initializationDatabase;
	
	@Autowired
	private MerchantStoreService merchantService;
	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private com.salesmanager.web.init.data.InitStoreData initStoreData;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
	@Autowired
	private UserServicesImpl userDetailsService;
	
	@Autowired
	private LabelUtils messages;
	


    /**
     * Default constructor. 
     */
    public StoreFilter() {
        // TODO Auto-generated constructor stub
    }
    
	   public boolean preHandle(
	            HttpServletRequest request,
	            HttpServletResponse response,
	            Object handler) throws Exception {

			request.setCharacterEncoding("UTF-8");
			
			//Language
			//TODO Locale to language
			Language language = (Language) request.getSession().getAttribute(Constants.LANGUAGE);
			
			if(language==null) {
				
				//TODO get the Locale from Spring API, is it simply request.getLocale() ???
				//if so then based on the Locale language locale.getLanguage() get the appropriate Language
				//object as represented below
				
				language = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
				request.getSession().setAttribute(Constants.LANGUAGE, language);
				
				//TODO store default language
			}
			
			//Locale locale = (Locale) request.getSession().getAttribute("LOCALE");
			
			//if(language==null) {
			//	language = languageService.getByCode("en");
			//	request.getSession().setAttribute("LANGUAGE", language);
			//}
			
			
			
			request.setAttribute(Constants.LANGUAGE, language);
			Locale locale = request.getLocale();
			

			
			try {
				
				
				//TODO global object
/*				if (initializationDatabase.isEmpty()) {
					LOGGER.info(String.format("%s : Shopizer database is empty, populate it....", "sm-shop"));
			
					initializationDatabase.populate("sm-shop");
					userDetailsService.createDefaultAdmin();
					loadData();
	
				}*/
	
				
				
				MerchantStore store = (MerchantStore)request.getSession().getAttribute(Constants.MERCHANT_STORE);
				if(store==null) {
						
						//TODO get the merchant store code
						store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
						request.getSession().setAttribute(Constants.MERCHANT_STORE, store);

				}
				
				request.setAttribute(Constants.MERCHANT_STORE, store);
				
				/** Breadcrumbs **/
				setBreadcrumb(request,locale);

				
				/**
				 * Get global objects
				 * Themes are built on a similar way displaying
				 * Header, Body and Footer
				 * Header and Footer are displayed on each page
				 * Some themes also contain side bars which may include
				 * similar emements
				 * 
				 * Elements from Header :
				 * - CMS links
				 * - Customer
				 * - Mini shopping cart
				 * - Store name / logo
				 * - Top categories
				 * - Search
				 * 
				 * Elements from Footer :
				 * - CMS links
				 * - Store address
				 * - Global payment information
				 * - Global shipping information
				 */
				

				//get from the cache first
				/**
				 * The cache for each object contains 2 objects, a Cache and a Missed-Cache
				 * Get objects from the cache
				 * If not null use those objects
				 * If null, get entry from missed-cache
				 * If missed-cache not null then nothing exist
				 * If missed-cache null, add missed-cache entry and load from the database
				 * If objects from database not null store in cache
				 */
				
				/******* CMS Objects ********/
				this.getContentObjects(store, language, request);
				
				/******* CMS Page names **********/
				this.getContentPageNames(store, language, request);
				
				/******* Top Categories ********/
				this.getTopCategories(store, language, request);
				
				/******* Default metatags *******/
				
				/**
				 * Title
				 * Description
				 * Keywords
				 */
				
				PageInformation pageInformation = new PageInformation();
				pageInformation.setPageTitle(store.getStorename());
				pageInformation.setPageDescription(store.getStorename());
				pageInformation.setPageKeywords(store.getStorename());
				
				
				@SuppressWarnings("unchecked")
				List<Content> contents = (List<Content>)request.getAttribute(Constants.REQUEST_CONTENT_OBJECTS);
				
				if(contents!=null) {
					for(Content content : contents) {
						if(content.getCode().equals(Constants.CONTENT_LANDING_PAGE)) {
							pageInformation.setPageTitle(content.getDescriptions().get(0).getMetatagTitle());
							pageInformation.setPageDescription(content.getDescriptions().get(0).getMetatagDescription());
							pageInformation.setPageKeywords(content.getDescriptions().get(0).getMetatagKeywords());
							break;
						}
					}
				}
				
				request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);
				
				
				/******* Configuration objects  *******/
				
				/**
				 * SHOP configuration type
				 * Should contain
				 * - Different configuration flags
				 * - Google analytics
				 * - Facebook page
				 * - Twitter handle
				 */
				
				this.getMerchantConfigurations(store,request);
				
				/******* Shopping Cart *********/
				
				ShoppingCart shoppingCart = (ShoppingCart)request.getSession().getAttribute(Constants.SESSION_SHOPPING_CART);
				if(shoppingCart!=null) {
					request.setAttribute(Constants.REQUEST_SHOPPING_CART, shoppingCart);
				}
				

			
			} catch (Exception e) {
				LOGGER.error("Error in StoreFilter",e);
			}

			return true;
		   
	   }
	   
	   private void getMerchantConfigurations(MerchantStore store, HttpServletRequest request) throws Exception {
		   
		   CacheUtils cache = CacheUtils.getInstance();

			
			StringBuilder configKey = new StringBuilder();
			configKey
			.append(store.getId())
			.append("_")
			.append(Constants.CONFIG_CACHE_KEY);
			
			
			StringBuilder configKeyMissed = new StringBuilder();
			configKeyMissed
			.append(configKey.toString());
			
			Map<String, String> configs = null;
			
			if(store.isUseCache()) {
			
				//get from the cache
				configs = (Map<String, String>) cache.getFromCache(configKey.toString());
				
				Boolean missedContent = null;
				if(configs==null) {
					//get from missed cache
					missedContent = (Boolean)cache.getFromCache(configKeyMissed.toString());
				}
				
				   if(configs==null && missedContent==null) {
					
					   configs = this.getConfigurations(store);

						
						//put in cache
						cache.putInCache(configs, configKey.toString());
						
					} else {
						
						//put in missed cache
						cache.putInCache(new Boolean(true), configKeyMissed.toString());
						
					}
					
				
	
			} else {
				

				 configs = this.getConfigurations(store);


			}
			
			
			if(configs!=null && configs.size()>0) {

				request.setAttribute(Constants.REQUEST_CONFIGS, configs);

			}
		   
		   
	   }
	   
	   
		@SuppressWarnings("unchecked")
		private void getContentPageNames(MerchantStore store, Language language, HttpServletRequest request) throws Exception {
			   
			   CacheUtils cache = CacheUtils.getInstance();
				/**
				 * CMS links
				 * Those links are implemented as pages (Content)
				 * ContentDescription will provide attributes name for the
				 * label to be displayed and seUrl for the friendly url page
				 */
				
				//build the key
				/**
				 * The cache is kept as a Map<String,Object>
				 * The key is <MERCHANT_ID>_CONTENTPAGELOCALE
				 * The value is a List of Content object
				 */
				
				StringBuilder contentKey = new StringBuilder();
				contentKey
				.append(store.getId())
				.append("_")
				.append(Constants.CONTENT_PAGE_CACHE_KEY)
				.append("-")
				.append(language.getCode());
				
				StringBuilder contentKeyMissed = new StringBuilder();
				contentKeyMissed
				.append(contentKey.toString())
				.append(Constants.MISSED_CACHE_KEY);
				
				Map<String, List<ContentDescription>> contents = null;
				
				if(store.isUseCache()) {
				
					//get from the cache
					contents = (Map<String, List<ContentDescription>>) cache.getFromCache(contentKey.toString());
					
					Boolean missedContent = null;
					if(contents==null) {
						//get from missed cache
						missedContent = (Boolean)cache.getFromCache(contentKeyMissed.toString());
					}
					
					   if(contents==null && missedContent==null) {
						
							contents = this.getContentPagesNames(store, language);

							
							//put in cache
							cache.putInCache(contents, contentKey.toString());
							
						} else {
							
							//put in missed cache
							cache.putInCache(new Boolean(true), contentKeyMissed.toString());
							
						}
						
					
		
				} else {
					

					contents = this.getContentPagesNames(store, language);	

				}
				
				
				if(contents!=null && contents.size()>0) {
					//only store objects in request
					String key = new StringBuilder()
					.append(store.getId())
					.append("_")
					.append(Constants.CONTENT_PAGE_CACHE_KEY)
					.append("-")
					.append(language.getCode()).toString();
					
					List<ContentDescription> descriptions = contents.get(key.toString());
					
					if(descriptions!=null) {
						request.setAttribute(Constants.REQUEST_CONTENT_PAGE_OBJECTS, descriptions);
					}
					
					
				}
				   
	    }
	   
	@SuppressWarnings({ "unchecked"})
	private void getContentObjects(MerchantStore store, Language language, HttpServletRequest request) throws Exception {
		   
		   CacheUtils cache = CacheUtils.getInstance();
			/**
			 * CMS links
			 * Those links are implemented as pages (Content)
			 * ContentDescription will provide attributes name for the
			 * label to be displayed and seUrl for the friendly url page
			 */
			
			//build the key
			/**
			 * The cache is kept as a Map<String,Object>
			 * The key is CONTENT_<MERCHANT_ID>_<LOCALE>
			 * The value is a List of Content object
			 */
			
			StringBuilder contentKey = new StringBuilder();
			contentKey
			.append(store.getId())
			.append("_")
			.append(Constants.CONTENT_CACHE_KEY)
			.append("-")
			.append(language.getCode());
			
			StringBuilder contentKeyMissed = new StringBuilder();
			contentKeyMissed
			.append(contentKey.toString())
			.append(Constants.MISSED_CACHE_KEY);
			
			Map<String, List<Content>> contents = null;
			
			if(store.isUseCache()) {
			
				//get from the cache
				contents = (Map<String, List<Content>>) cache.getFromCache(contentKey.toString());
				Boolean missedContent = null;
				if(contents==null) {
					//get from missed cache
					missedContent = (Boolean)cache.getFromCache(contentKeyMissed.toString());
				}
				
				if(contents==null && missedContent==null) {
					
					contents = this.getContent(store, language);

					//put in cache
					cache.putInCache(contents, contentKey.toString());
						
					} else {
						//put in missed cache
						cache.putInCache(new Boolean(true), contentKeyMissed.toString());
				}
					
				
	
			} else {

				contents = this.getContent(store, language);	

			}
			
			
			
			if(contents!=null && contents.size()>0) {
				//only store objects in request
				String key = new StringBuilder()
				.append(store.getId())
				.append("_")
				.append(Constants.CONTENT_CACHE_KEY)
				.append("-")
				.append(language.getCode()).toString();
				
				List<Content> c = contents.get(key.toString());
				
				if(c!=null) {
					request.setAttribute(Constants.REQUEST_CONTENT_OBJECTS, c);
				}
				
				
			}

		   
    }

	@SuppressWarnings("unchecked")
	private void getTopCategories(MerchantStore store, Language language, HttpServletRequest request) throws Exception {
		   
		   CacheUtils cache = CacheUtils.getInstance();
			/**
			 * Top categories
			 * Top categories are implemented as Category entity
			 * CategoryDescription will provide attributes name for the
			 * label to be displayed and seUrl for the friendly url page
			 */
			
			//build the key
			/**
			 * The categories is kept as a Map<String,Object>
			 * The key is <MERCHANT_ID>_CATEGORYLOCALE
			 * The value is a List of Category object
			 */
			
			StringBuilder contentKey = new StringBuilder();
			contentKey
			.append(store.getId())
			.append("_")
			.append(Constants.CATEGORIES_CACHE_KEY)
			.append("-")
			.append(language.getCode());
			
			StringBuilder contentKeyMissed = new StringBuilder();
			contentKeyMissed
			.append(contentKey.toString())
			.append(Constants.MISSED_CACHE_KEY);
			
			Map<String, List<Category>> objects = null;
			
			if(store.isUseCache()) {
			
				//get from the cache
				objects = (Map<String, List<Category>>) cache.getFromCache(contentKey.toString());
			
				Boolean missedContent = null;
				if(objects==null) {
					missedContent = (Boolean)cache.getFromCache(contentKeyMissed.toString());
				}
				
				if(objects==null && missedContent==null) {
	
					//Get top categories from the database
					
					objects = this.getCategories(store, language);
	
						
						//put in cache
						cache.putInCache(objects, contentKey.toString());
						
				} else {
						
						//put in missed cache
						cache.putInCache(new Boolean(true), contentKeyMissed.toString());
						
				}
				
			} else {
				
				
				objects = this.getCategories(store, language);
				
			}
			
			if(objects!=null && objects.size()>0) {
				//only store objects in request
				String key = new StringBuilder()
				.append(store.getId())
				.append("_")
				.append(Constants.CATEGORIES_CACHE_KEY)
				.append("-")
				.append(language.getCode()).toString();
				
				List<Category> categories = objects.get(key.toString());
				
				if(categories!=null) {
					request.setAttribute(Constants.REQUEST_TOP_CATEGORIES, objects.get(key.toString()));
				}
				
				
			}
		   
	   }
	
	
	   private Map<String, List<ContentDescription>> getContentPagesNames(MerchantStore store, Language language) throws Exception {
		   
		   
		   Map<String, List<ContentDescription>> contents = new ConcurrentHashMap<String, List<ContentDescription>>();
		   
			//Get boxes and sections from the database
			List<ContentType> contentTypes = new ArrayList<ContentType>();
			contentTypes.add(ContentType.PAGE);

			
			List<ContentDescription> contentPages = contentService.listNameByType(contentTypes, store, language);
			
			if(contentPages!=null && contentPages.size()>0) {
				
				//create a Map<String,List<Content>
				for(ContentDescription content : contentPages) {


						Language lang = language;
						String key = new StringBuilder()
						.append(store.getId())
						.append("_")
						.append(Constants.CONTENT_PAGE_CACHE_KEY)
						.append("-")
						.append(lang.getCode()).toString();
						List<ContentDescription> contentList = null;
						if(contents==null) {
							contents = new HashMap<String, List<ContentDescription>>();
						}
						if(!contents.containsKey(key)) {
							contentList = new ArrayList<ContentDescription>();

							contents.put(key, contentList);
						}
						contentList.add(content);
				}
			}
			return contents;
	   }
	   
	   private Map<String, List<Content>> getContent(MerchantStore store, Language language) throws Exception {
		   
		   
		   Map<String, List<Content>> contents = new ConcurrentHashMap<String, List<Content>>();
		   
			//Get boxes and sections from the database
			List<ContentType> contentTypes = new ArrayList<ContentType>();
			contentTypes.add(ContentType.BOX);
			contentTypes.add(ContentType.SECTION);
			
			List<Content> contentPages = contentService.listByType(contentTypes, store, language);
			
			if(contentPages!=null && contentPages.size()>0) {
				
				//create a Map<String,List<Content>
				for(Content content : contentPages) {
					if(content.isVisible()) {
						List<ContentDescription> descriptions = content.getDescriptions();
						for(ContentDescription contentDescription : descriptions) {
							Language lang = contentDescription.getLanguage();
							String key = new StringBuilder()
							.append(store.getId())
							.append("_")
							.append(Constants.CONTENT_CACHE_KEY)
							.append("-")
							.append(lang.getCode()).toString();
							List<Content> contentList = null;
							if(contents==null) {
								contents = new HashMap<String, List<Content>>();
							}
							if(!contents.containsKey(key)) {
								contentList = new ArrayList<Content>();
	
								contents.put(key, contentList);
							}
							contentList.add(content);
						}
					}
					
				}
				
				
		   
			}
			return contents;
	   }
	   
	   private Map<String, List<Category>> getCategories(MerchantStore store, Language language) throws Exception {
		   
		   Map<String, List<Category>> objects = new ConcurrentHashMap<String, List<Category>>();
		   

			List<Category> categories = categoryService.listByDepth(store, 0, language);
			
			if(categories!=null && categories.size()>0) {
				
				//create a Map<String,List<Content>
				for(Category category : categories) {
					if(category.isVisible()) {
						List<CategoryDescription> descriptions = category.getDescriptions();
						for(CategoryDescription description : descriptions) {
							Language lang = description.getLanguage();
							String key = new StringBuilder()
							.append(store.getId())
							.append("_")
							.append(Constants.CATEGORIES_CACHE_KEY)
							.append("-")
							.append(lang.getCode()).toString();
							
							List<Category> cacheCategories = null;
							if(objects==null) {
								objects = new HashMap<String, List<Category>>();
							}
							if(!objects.containsKey(key)) {
								cacheCategories = new ArrayList<Category>();
	
								objects.put(key, cacheCategories);
							} else {
								cacheCategories = objects.get(key.toString());
							}
							cacheCategories.add(category);
						}
					}
				}
				
				
	      }
			return objects;
	   }
	   
	   private Map<String,String> getConfigurations(MerchantStore store) {
		   
		   try {
			   
			   List<MerchantConfiguration> configurations = merchantConfigurationService.listByType(MerchantConfigurationType.CONFIG, store);
			   Map<String,String> configs = null;
			   for(MerchantConfiguration configuration : configurations) {
				   
				   if(configs==null) {
					   configs = new HashMap<String,String>();
				   }
				   configs.put(configuration.getKey(), configuration.getValue());
 
			   }
			   
			   return configs;
			
		   } catch (Exception e) {
			   LOGGER.error("Exception while getting configurations",e);
		   }
		   
		   return null;
		   
	   }
	   
	   private void setBreadcrumb(HttpServletRequest request, Locale locale) {
		   
		   
		   
		   try {
			
				//breadcrumb
				Breadcrumb breadCrumb = (Breadcrumb) request.getSession().getAttribute(Constants.BREADCRUMB);
				Language language = (Language)request.getAttribute(Constants.LANGUAGE);
				if(breadCrumb==null) {
					breadCrumb = new Breadcrumb();
					breadCrumb.setLanguage(language);
					BreadcrumbItem item = this.getDefaultBreadcrumbItem(language, locale);
					breadCrumb.getBreadCrumbs().add(item);
				} else {
					
					//check language
					if(language.getCode().equals(breadCrumb.getLanguage().getCode())) {
						
						//rebuild using the appropriate language
						//breadCrumb = getDefaultBreadcrumb(language,locale);
						//breadCrumb = new Breadcrumb();
						List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();
						for(BreadcrumbItem item : breadCrumb.getBreadCrumbs()) {
							
							if(item.getItemType().name().equals(BreadcrumbItemType.HOME)) {
								BreadcrumbItem homeItem = this.getDefaultBreadcrumbItem(language, locale);
							}else if(item.getItemType().name().equals(BreadcrumbItemType.PRODUCT)) {
								Product product = productService.getProductForLocale(item.getId(), language, locale);
								if(product!=null) {
									BreadcrumbItem productItem = new  BreadcrumbItem();
									productItem.setId(product.getId());
									productItem.setItemType(BreadcrumbItemType.PRODUCT);
									productItem.setLabel(product.getProductDescription().getName());
									productItem.setUrl(product.getProductDescription().getSeUrl());
									items.add(productItem);
								}
							}else if(item.getItemType().name().equals(BreadcrumbItemType.CATEGORY)) {
								Category category = categoryService.getByLanguage(item.getId(), language);
								if(category!=null) {
									BreadcrumbItem categoryItem = new  BreadcrumbItem();
									categoryItem.setId(category.getId());
									categoryItem.setItemType(BreadcrumbItemType.CATEGORY);
									categoryItem.setLabel(category.getDescription().getName());
									categoryItem.setUrl(category.getDescription().getSeUrl());
									items.add(categoryItem);
								}
							}else if(item.getItemType().name().equals(BreadcrumbItemType.PAGE)) {
								Content content = contentService.getByLanguage(item.getId(), language);
								if(content!=null) {
									BreadcrumbItem contentItem = new  BreadcrumbItem();
									contentItem.setId(content.getId());
									contentItem.setItemType(BreadcrumbItemType.PAGE);
									contentItem.setLabel(content.getDescription().getName());
									contentItem.setUrl(content.getDescription().getSeUrl());
									items.add(contentItem);
								}
							}
							
						}
						
						breadCrumb = new Breadcrumb();
						breadCrumb.setLanguage(language);
						breadCrumb.setBreadCrumbs(items);
						
					}
					
				}
			
			request.getSession().setAttribute(Constants.BREADCRUMB, breadCrumb);
			request.setAttribute(Constants.BREADCRUMB, breadCrumb);
			
			} catch (Exception e) {
				LOGGER.error("Error while building breadcrumbs",e);
			}
		   
	   }
	   
	   private BreadcrumbItem getDefaultBreadcrumbItem(Language language, Locale locale) {

			//set home page item
			BreadcrumbItem item = new BreadcrumbItem();
			item.setItemType(BreadcrumbItemType.HOME);
			item.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, locale));
			item.setUrl(Constants.HOME_URL);
			return item;
		   
	   }

	

}
