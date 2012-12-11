package com.salesmanager.web.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.init.service.InitializationDatabase;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.system.model.SystemConfiguration;
import com.salesmanager.core.business.system.service.SystemConfigurationService;
import com.salesmanager.core.constants.SystemConstants;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.web.admin.security.UserServicesImpl;
import com.salesmanager.web.constants.ApplicationConstants;
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
	CoreConfiguration configuration;
	
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
			}
			
			//Locale locale = (Locale) request.getSession().getAttribute("LOCALE");
			
			//if(language==null) {
			//	language = languageService.getByCode("en");
			//	request.getSession().setAttribute("LANGUAGE", language);
			//}
			
			
			
			request.setAttribute("LANGUAGE", language);
			
			try {
				
				

				if (initializationDatabase.isEmpty()) {
					LOGGER.info(String.format("%s : Shopizer database is empty, populate it....", "sm-shop"));
			
					initializationDatabase.populate("sm-shop");
					userDetailsService.createDefaultAdmin();
					loadData();
	
				}
	
				
				
				MerchantStore store = (MerchantStore)request.getSession().getAttribute(Constants.MERCHANT_STORE);
				if(store==null) {
					//MerchantStoreService merchantService = (MerchantStoreService) ContextLoader.getCurrentWebApplicationContext().getBean(
					//		"merchantService");
					
					if(merchantService==null) {
						System.out.println("*** MerchantService is null ***");
					} else {
						store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
						request.getSession().setAttribute(Constants.MERCHANT_STORE, store);
						
					}
				}
				
				request.setAttribute(Constants.MERCHANT_STORE, store);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		   
	   }
	   
		private void loadData() throws ServiceException {
	
			List<SystemConfiguration> configurations = systemConfigurationService.list();
			
			
			String loadTestData = appConfiguration.getProperty(ApplicationConstants.POPULATE_TEST_DATA);
			boolean loadData =  !StringUtils.isBlank(loadTestData) && loadTestData.equals(SystemConstants.CONFIG_VALUE_TRUE);
			
			
			if(loadData) {
				
				SystemConfiguration configuration = systemConfigurationService.getByKey(ApplicationConstants.TEST_DATA_LOADED);
			
				if(configuration!=null) {
						if(configuration.getKey().equals(ApplicationConstants.TEST_DATA_LOADED)) {
							if(configuration.getValue().equals(SystemConstants.CONFIG_VALUE_TRUE)) {
								return;		
							}
						}		
				}
				
				initStoreData.initInitialData();
				
				configuration = new SystemConfiguration();
				configuration.getAuditSection().setModifiedBy(SystemConstants.SYSTEM_USER);
				configuration.setKey(ApplicationConstants.TEST_DATA_LOADED);
				configuration.setValue(SystemConstants.CONFIG_VALUE_TRUE);
				systemConfigurationService.create(configuration);
				
				
			}
		}


	
	

}
