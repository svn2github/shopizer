package com.salesmanager.portlet.filter;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.ResourceFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;

public class GlobalFilter implements RenderFilter, ActionFilter, ResourceFilter {


	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalFilter.class);
	
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig arg0) throws PortletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(RenderRequest renderRequest, RenderResponse renderResponse,
			FilterChain filterChain) throws IOException, PortletException {
		
		try {
			setGlobalObjects(renderRequest,renderResponse);
		} catch (Exception e) {
			LOGGER.error("Exception in GlobalFilter", e);
		}
		
		
		filterChain.doFilter(renderRequest, renderResponse);

	}

	@Override
	public void doFilter(ActionRequest request, ActionResponse response,
			
			FilterChain filterChain) throws IOException, PortletException {
			
			try {
				
				setGlobalObjects(request,response);
				
			} catch (Exception e) {
				LOGGER.error("Exception in GlobalFilter", e);
			}
		
		
			
			filterChain.doFilter(request, response);
		
	}
	
	private void setGlobalObjects(PortletRequest request, PortletResponse response) throws Exception {
		//Get merchantstore
		MerchantStore store = (MerchantStore)request.getPortletSession().getAttribute("MERCHANT_STORE",PortletSession.APPLICATION_SCOPE);
		if(store==null) {
			MerchantStoreService merchantService = (MerchantStoreService) ContextLoader.getCurrentWebApplicationContext().getBean(
					"merchantService");
			
			if(merchantService==null) {
				System.out.println("*** MerchantService is null ***");
			} else {
				store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
				request.getPortletSession().setAttribute("MERCHANT_STORE", store, PortletSession.APPLICATION_SCOPE);
			}
		}
		
		//Locale
		
		//User to Customer
	}

	@Override
	public void doFilter(ResourceRequest request, ResourceResponse response,
			FilterChain filterChain) throws IOException, PortletException {
		try {
			setGlobalObjects(request,response);
		} catch (Exception e) {
			LOGGER.error("Exception in GlobalFilter", e);
		}
		filterChain.doFilter(request, response);
		
	}

}
