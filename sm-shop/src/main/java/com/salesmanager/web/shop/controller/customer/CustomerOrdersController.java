package com.salesmanager.web.shop.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.order.ReadableOrderList;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;

@Controller
@RequestMapping(Constants.SHOP_URI + "/customer")
public class CustomerOrdersController extends AbstractController {
	
    @Autowired
	private MerchantStoreService merchantStoreService;
    
    @Autowired
    private LanguageService languageService;
    
    @Autowired
    private OrderFacade orderFacade;
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOrdersController.class);
	
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/orders.html", method=RequestMethod.GET)
	public String listOrders(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerOrders).append(".").append(store.getStoreTemplate());

		return template.toString();
	}
	
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping( value="/{store}/orders/", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public ReadableOrderList listOrders(@PathVariable final String store, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
		
		//get additional request parameters for orders
		String lang = request.getParameter(Constants.LANG);		
		String start = request.getParameter(Constants.START);
		String max = request.getParameter(Constants.MAX);
		
		int startCount = 0;
		int maxCount = 0;
		
		if(StringUtils.isBlank(lang)) {
			lang = merchantStore.getDefaultLanguage().getCode();
		}
		
		
		Language language = languageService.getByCode(lang);
		
		if(language==null) {
			LOGGER.error("Language is null for code " + lang);
			response.sendError(503, "Language is null for code " + lang);
			return null;
		}
		
		try {
			startCount = Integer.parseInt(start);
		} catch (Exception e) {
			LOGGER.info("Invalid value for start " + start);
		}
		
		try {
			maxCount = Integer.parseInt(max);
		} catch (Exception e) {
			LOGGER.info("Invalid value for max " + max);
		}
		
		ReadableOrderList returnList = orderFacade.getReadableOrderList(merchantStore, startCount, maxCount, language);

		return returnList;
		
		
		
	}
}
