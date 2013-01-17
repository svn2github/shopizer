package com.salesmanager.web.admin.controller.orders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.LabelUtils;


/**
 * Manage order list
 * Manage search order
 * @author csamson 
 *
 */
@Controller
public class OrdersController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	LabelUtils messages;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderControler.class);
	
	
	
	
	@RequestMapping(value="/admin/orders/orders.html", method=RequestMethod.GET)
	public String displayOrders(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		//the list of orders is from page method
		
		return "admin-orders";
		
		
	}
	
	
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(value="/admin/orders/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageOrders(HttpServletRequest request, HttpServletResponse response) {

		//TODO 
		//filter customer name
		//filter date
		
		AjaxPageableResponse resp = new AjaxPageableResponse();
		
		

		
		try {
			
			int startRow = Integer.parseInt(request.getParameter("_startRow"));
			int endRow = Integer.parseInt(request.getParameter("_endRow"));
			
			Language language = (Language)request.getAttribute("LANGUAGE");
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			

			
			//TODO add filters as criteria
			List<Order> orders = orderService.getMerchantOrders(store);
					
					
			
			for(Order order : orders) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("orderId", order.getId());

				entry.put("customer", order.getBilling().getName());
				//entry.put("amount", order.getTotal());//todo format total
				//entry.put("date", order.getDatePurchased());//todo format date
				entry.put("status", order.getStatus().name());
				entry.put("paymentMethod", order.getPaymentMethod());//todo get the name from modules bundle
				resp.addDataEntry(entry);
				
				
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			

		
		} catch (Exception e) {
			LOGGER.error("Error while paging orders", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("order", "order");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("order");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
