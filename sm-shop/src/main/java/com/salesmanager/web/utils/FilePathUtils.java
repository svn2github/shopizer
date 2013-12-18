package com.salesmanager.web.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.business.catalog.product.model.file.DigitalProduct;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.web.constants.Constants;

public class FilePathUtils {
	
	
	private final static String CUSTOMER_ACCESS_LINK = "/store//customer/dashboard.html";
	
	
	/**
	 * Builds a static content content file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param imageName
	 * @return
	 */
	public static String buildStaticFilePath(MerchantStore store, String imageName) {
		return new StringBuilder().append(Constants.FILES_URI).append("/").append(store.getCode()).append("/").append(imageName).toString();
	}
	
	public static String buildAdminDownloadProductFilePath(MerchantStore store, DigitalProduct digitalProduct) {
		return new StringBuilder().append(Constants.FILES_URI).append("/").append(store.getCode()).append("/").append(digitalProduct.getProductFileName()).toString();
	}
	
	/**
	 * Builds http://<domain name>/<context path>
	 * @param store
	 * @param request
	 * @return
	 */
	public static String buildStoreUri(MerchantStore store, HttpServletRequest request) {
		StringBuilder resourcePath = new StringBuilder();
		HttpSession session= request.getSession();
		@SuppressWarnings("unchecked")
		Map<String,String> configurations = (Map<String, String>)session.getAttribute(Constants.STORE_CONFIGURATION);
		String scheme = Constants.HTTP_SCHEME;
		if(configurations!=null) {
			scheme = (String)configurations.get("scheme");
		}
		
		String domainName = store.getDomainName();
		if(StringUtils.isBlank(domainName)) {
			domainName = Constants.DEFAULT_DOMAIN_NAME;
		}
		
		resourcePath.append(scheme).append("://")
		.append(domainName)
		.append(request.getContextPath());
		
		return resourcePath.toString();
		
	}
	
	
	/**
	 * Access to the customer section
	 * @param store
	 * @param request
	 * @return
	 */
	public static String buildCustomerUri(MerchantStore store, HttpServletRequest request) {
		StringBuilder resourcePath = new StringBuilder();
		HttpSession session= request.getSession();
		@SuppressWarnings("unchecked")
		Map<String,String> configurations = (Map<String, String>)session.getAttribute(Constants.STORE_CONFIGURATION);
		String scheme = Constants.HTTP_SCHEME;
		if(configurations!=null) {
			scheme = (String)configurations.get("scheme");
		}
		
		String domainName = store.getDomainName();
		if(StringUtils.isBlank(domainName)) {
			domainName = Constants.DEFAULT_DOMAIN_NAME;
		}
		
		resourcePath.append(scheme).append("://")
		.append(domainName)
		.append(request.getContextPath())
		.append(CUSTOMER_ACCESS_LINK);
		
		return resourcePath.toString();
		
	}
	
	public static String buildAdminUri(MerchantStore store, HttpServletRequest request) {
		StringBuilder resourcePath = new StringBuilder();
		HttpSession session= request.getSession();
		@SuppressWarnings("unchecked")
		Map<String,String> configurations = (Map<String, String>)session.getAttribute(Constants.STORE_CONFIGURATION);
		String scheme = Constants.HTTP_SCHEME;
		if(configurations!=null) {
			scheme = (String)configurations.get("scheme");
		}
		
		String domainName = store.getDomainName();
		if(StringUtils.isBlank(domainName)) {
			domainName = Constants.DEFAULT_DOMAIN_NAME;
		}
		
		resourcePath.append(scheme).append("://")
		.append(domainName)
		.append(request.getContextPath())
		.append(Constants.ADMIN_URL);
		
		return resourcePath.toString();
		
	}
	

}
