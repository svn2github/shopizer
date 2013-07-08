package com.salesmanager.web.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.content.model.content.Content;
import com.salesmanager.core.business.content.model.content.ContentDescription;
import com.salesmanager.core.business.content.model.content.ContentType;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.init.service.InitializationDatabase;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.system.service.SystemConfigurationService;
import com.salesmanager.core.utils.CacheUtils;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.web.admin.security.UserServicesImpl;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.AppConfiguration;






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
	private InitializationDatabase initializationDatabase;
	
	@Autowired
	private MerchantStoreService merchantService;
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private com.salesmanager.web.init.data.InitStoreData initStoreData;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
	@Autowired
	private UserServicesImpl userDetailsService;
	


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
			Language language = (Language) request.getSession().getAttribute("LANGUAGE");
			
			if(language==null) {
				
				//TODO get the Locale from Spring API, is it simply request.getLocale() ???
				//if so then based on the Locale language locale.getLanguage() get the appropriate Language
				//object as represented below
				
				language = languageService.getByCode("en");
				request.getSession().setAttribute("LANGUAGE", language);
				
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
				
				/******* Top Categories ********/
				this.getTopCategories(store, language, request);
				
				/******* Configuration objects *******/

				
				
				

				
				
				//Get page titles
				
				//TODO and by language
/*				Content content = contentService.getByCode("LANDING_PAGE", store);
				StoreLanding landing = new StoreLanding();
				
				List<StoreLandingDescription> descriptions = new ArrayList<StoreLandingDescription>();
				
				
				for(Language l : store.getLanguages()) {
					
					StoreLandingDescription landingDescription = null;
					if(content!=null) {
						for(ContentDescription desc : content.getDescriptions()) {
							if(desc.getLanguage().getCode().equals(l.getCode())) {
								landingDescription = new StoreLandingDescription();
								landingDescription.setDescription(desc.getMetatagDescription());
								landingDescription.setHomePageContent(desc.getDescription());
								landingDescription.setKeywords(desc.getMetatagKeywords());
								landingDescription.setTitle(desc.getName());//name is a not empty
								landingDescription.setLanguage(desc.getLanguage());
							}
						}
					}
					
					if(landingDescription==null) {
						landingDescription = new StoreLandingDescription();
						landingDescription.setLanguage(l);
					}
					

					
					descriptions.add(landingDescription);
				}
				
				landing.setDescriptions(descriptions);*/
				
				
				
				
				//store meta information
				
				//store root categories
				
				//shopping cart
			
			} catch (Exception e) {
				LOGGER.error("Error in StoreFilter",e);
			}

			return true;
		   
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
				 * The key is CONTENT_PAGE_<MERCHANT_ID>_<LOCALE>
				 * The value is a List of Content object
				 */
				
				StringBuilder contentKey = new StringBuilder();
				contentKey
				.append(Constants.CONTENT_PAGE_CACHE_KEY)
				.append(store.getId())
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
					request.setAttribute(Constants.REQUEST_CONTENT_PAGE_OBJECTS, contents);
				}
			   
	    }
	   
	@SuppressWarnings("unchecked")
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
			.append(Constants.CONTENT_CACHE_KEY)
			.append(store.getId())
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
				request.setAttribute(Constants.REQUEST_CONTENT_OBJECTS, contents);
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
			 * The key is CATEGORY_<MERCHANT_ID>_<LOCALE>
			 * The value is a List of Category object
			 */
			
			StringBuilder contentKey = new StringBuilder();
			contentKey
			.append(Constants.CATEGORIES_CACHE_KEY)
			.append(store.getId())
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
				.append(Constants.CATEGORIES_CACHE_KEY)
				.append(store.getId())
				.append(language.getCode()).toString();
				
				List<Category> categories = objects.get(key.toString());
				
				if(categories!=null) {
					request.setAttribute(Constants.REQUEST_TOP_CATEGORIES, objects.get(key.toString()));
				}
				
				
			}
		   
	   }
	
	
	   private Map<String, List<ContentDescription>> getContentPagesNames(MerchantStore store, Language language) throws Exception {
		   
		   
		   Map<String, List<ContentDescription>> contents = new HashMap<String, List<ContentDescription>>();
		   
			//Get boxes and sections from the database
			List<ContentType> contentTypes = new ArrayList<ContentType>();
			contentTypes.add(ContentType.PAGE);

			
			List<ContentDescription> contentPages = contentService.listNameByType(contentTypes, store, language);
			
			if(contentPages!=null && contentPages.size()>0) {
				
				//create a Map<String,List<Content>
				for(ContentDescription content : contentPages) {


						Language lang = language;
						String key = new StringBuilder()
						.append(Constants.CONTENT_PAGE_CACHE_KEY)
						.append(store.getId())
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
		   
		   
		   Map<String, List<Content>> contents = new HashMap<String, List<Content>>();
		   
			//Get boxes and sections from the database
			List<ContentType> contentTypes = new ArrayList<ContentType>();
			contentTypes.add(ContentType.BOX);
			contentTypes.add(ContentType.SECTION);
			
			List<Content> contentPages = contentService.listByType(contentTypes, store, language);
			
			if(contentPages!=null && contentPages.size()>0) {
				
				//create a Map<String,List<Content>
				for(Content content : contentPages) {

					List<ContentDescription> descriptions = content.getDescriptions();
					for(ContentDescription contentDescription : descriptions) {
						Language lang = contentDescription.getLanguage();
						String key = new StringBuilder()
						.append(Constants.CONTENT_CACHE_KEY)
						.append(store.getId())
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
			return contents;
	   }
	   
	   private Map<String, List<Category>> getCategories(MerchantStore store, Language language) throws Exception {
		   
		   Map<String, List<Category>> objects = new HashMap<String, List<Category>>();
		   

			List<Category> categories = categoryService.listByDepth(store, 0, language);
			
			if(categories!=null && categories.size()>0) {
				
				//create a Map<String,List<Content>
				for(Category category : categories) {

					List<CategoryDescription> descriptions = category.getDescriptions();
					for(CategoryDescription description : descriptions) {
						Language lang = description.getLanguage();
						String key = new StringBuilder()
						.append(Constants.CATEGORIES_CACHE_KEY)
						.append(store.getId())
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
			return objects;
	   }

	

}
