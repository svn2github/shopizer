package com.salesmanager.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.init.service.InitializationDatabase;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.system.service.SystemConfigurationService;
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
			
			
			
			request.setAttribute("LANGUAGE", language);
			
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
				
				//get global objects
				
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

	
	

}
