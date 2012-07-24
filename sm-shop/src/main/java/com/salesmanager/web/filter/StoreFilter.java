package com.salesmanager.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.web.init.service.InitializationDatabase;





/**
 * Servlet Filter implementation class StoreFilter
 */

public class StoreFilter extends HandlerInterceptorAdapter {
	
	@Autowired
	private InitializationDatabase initializationDatabase;
	
	@Autowired
	private MerchantStoreService merchantService;
	
	@Autowired
	private LanguageService languageService;

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
		   
		   
		   System.out.println(" ***** In the Interceptor *****");
			


			request.setCharacterEncoding("UTF-8");
			
			//Language
			//TODO Locale to language
			Language language = (Language) request.getSession().getAttribute("LANGUAGE");
			
			if(language==null) {
				language = languageService.getByCode("en");
				request.getSession().setAttribute("LANGUAGE", language);
			}
			
			request.setAttribute("LANGUAGE", language);
			
			try {
			
			//InitializationDatabase database = (InitializationDatabase) ContextLoader.getCurrentWebApplicationContext().getBean(
			//		"initializationDatabase");
			
			
			if (initializationDatabase.isEmpty()) {
				//LOGGER.info(String.format("%s : Shopizer database is empty, populate it....", name));
		
				initializationDatabase.populate("sm-shop");

			}

			
			
			MerchantStore store = (MerchantStore)request.getSession().getAttribute("MERCHANT_STORE");
			if(store==null) {
				//MerchantStoreService merchantService = (MerchantStoreService) ContextLoader.getCurrentWebApplicationContext().getBean(
				//		"merchantService");
				
				if(merchantService==null) {
					System.out.println("*** MerchantService is null ***");
				} else {
					store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
					request.getSession().setAttribute("MERCHANT_STORE", store);
				}
			}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		   
	   }


	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
    /**
     * Set attributes
     * STORE
     * templateId
     * principal
     */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		System.out.println(" ***** In the filter *****");
		
		try {

/*		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		request.setCharacterEncoding("UTF-8");
		
		
		
		InitializationDatabase database = (InitializationDatabase) ContextLoader.getCurrentWebApplicationContext().getBean(
				"initializationDatabase");
		
		
		if (database.isEmpty()) {
			//LOGGER.info(String.format("%s : Shopizer database is empty, populate it....", name));
	
				database.populate("sm-shop");

		}

		
		
		MerchantStore store = (MerchantStore)request.getSession().getAttribute("MERCHANT_STORE");
		if(store==null) {
			MerchantStoreService merchantService = (MerchantStoreService) ContextLoader.getCurrentWebApplicationContext().getBean(
					"merchantService");
			
			if(merchantService==null) {
				System.out.println("*** MerchantService is null ***");
			} else {
				store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
				request.getSession().setAttribute("MERCHANT_STORE", store);
			}
		}*/
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		


		// pass the request along the filter chain
		chain.doFilter(req, resp);
	}

	
	

}
