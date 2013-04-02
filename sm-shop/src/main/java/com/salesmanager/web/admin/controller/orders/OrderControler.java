package com.salesmanager.web.admin.controller.orders;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderTotal;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.LabelUtils;

/**
 * Manage order details
 * @author csamson
 *
 */
@Controller
public class OrderControler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderControler.class);
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	CountryService countryService;

	@Secured("ORDER")
	@RequestMapping(value="/admin/orders/editOrder.html", method=RequestMethod.GET)
	public String displayOrderEdit(@RequestParam("id") long orderId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return displayOrder(orderId,model,request,response);

	}

	@SuppressWarnings("unused")
	private String displayOrder(Long orderId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//display menu
		setMenu(model,request);
		   
		com.salesmanager.web.entity.order.Order order = new com.salesmanager.web.entity.order.Order();
		Language language = (Language)request.getAttribute("LANGUAGE");
		List<Country> countries = countryService.getCountries(language);
		if(orderId!=null && orderId!=0) {		//edit mode		
			
			Set<OrderProduct> orderProducts = null;
			Set<OrderTotal> orderTotal = null;
			Set<OrderStatusHistory> orderHistory = null;
		
			Order dbOrder = orderService.getById(orderId);
			order.setId( orderId );
		
			order.setDatePurchased(DateUtil.formatDate(dbOrder.getDatePurchased()));
			order.setOrder( dbOrder );
			order.setBilling( dbOrder.getBilling() );
			order.setDelivery(dbOrder.getDelivery() );

			orderProducts = dbOrder.getOrderProducts();
			orderTotal = dbOrder.getOrderTotal();
			orderHistory = dbOrder.getOrderHistory();
			
		}	
		
		model.addAttribute("countries", countries);
		model.addAttribute("order",order);
		return "admin-orders-edit";
	}
	

	@Secured("ORDER")
	@RequestMapping(value="/admin/orders/save.html", method=RequestMethod.POST)
	public String saveOrder(@Valid @ModelAttribute("order") com.salesmanager.web.entity.order.Order entityOrder, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		
		String email_regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		Pattern pattern = Pattern.compile(email_regEx);
		OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
		
		
		Set<OrderProduct> orderProducts = new HashSet<OrderProduct>();
		Set<OrderTotal> orderTotal = new HashSet<OrderTotal>();
		Set<OrderStatusHistory> orderHistory = new HashSet<OrderStatusHistory>();

		com.salesmanager.core.business.order.model.Order newOrder = orderService.getById(entityOrder.getOrder().getId() );
	
		Date date = new Date();
		if(!StringUtils.isBlank(entityOrder.getDatePurchased() ) ){
			try {
				date = DateUtil.getDate(entityOrder.getDatePurchased());
			} catch (Exception e) {
				ObjectError error = new ObjectError("datePurchased",messages.getMessage("message.invalid.date", locale));
				result.addError(error);
			}
			
		} else{
			date = null;
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getCustomerFirstName() ) ){
			 ObjectError error = new ObjectError("customerFirstName", messages.getMessage("NotEmpty.order.customerFirstName", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getCustomerLastName() ) ){
			 ObjectError error = new ObjectError("customerLastName", messages.getMessage("NotEmpty.order.customerLastName", locale));
			 result.addError(error);
		}
		
		if( StringUtils.isBlank(entityOrder.getOrder().getCustomerStreetAddress() ) ){
			 ObjectError error = new ObjectError("customerStreetAddress", messages.getMessage("NotEmpty.order.customerStreetAddress", locale));
			 result.addError(error);
		}
		
		if( StringUtils.isBlank(entityOrder.getOrder().getCustomerCity() ) ){
			 ObjectError error = new ObjectError("customerCity", messages.getMessage("NotEmpty.order.customerCity", locale));
			 result.addError(error);
		}
		
		if( StringUtils.isBlank(entityOrder.getOrder().getCustomerPostCode() ) ){
			 ObjectError error = new ObjectError("customerPostCode", messages.getMessage("NotEmpty.order.customerPostCode", locale));
			 result.addError(error);
		}
		
		if( StringUtils.isBlank(entityOrder.getOrder().getCustomerTelephone() ) ){
			 ObjectError error = new ObjectError("customerTelephone", messages.getMessage("NotEmpty.order.customerTelephone", locale));
			 result.addError(error);
		}
		
		if(!StringUtils.isBlank(entityOrder.getOrder().getCustomerEmailAddress() ) ){
			 java.util.regex.Matcher matcher = pattern.matcher(entityOrder.getOrder().getCustomerEmailAddress());
			 
			 if(!matcher.find()) {
				ObjectError error = new ObjectError("customerEmailAddress",messages.getMessage("Email.order.customerEmailAddress", locale));
				result.addError(error);
			 }
		}else{
			ObjectError error = new ObjectError("customerEmailAddress",messages.getMessage("NotEmpty.order.customerEmailAddress", locale));
			result.addError(error);
		}

		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getName() ) ){
			 ObjectError error = new ObjectError("billingName", messages.getMessage("NotEmpty.order.billingName", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getAddress() ) ){
			 ObjectError error = new ObjectError("billingAddress", messages.getMessage("NotEmpty.order.billingStreetAddress", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getCity() ) ){
			 ObjectError error = new ObjectError("billingCity",messages.getMessage("NotEmpty.order.billingCity", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getState() ) ){
			 ObjectError error = new ObjectError("billingState",messages.getMessage("NoNotEmpty.order.billingState", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getPostalCode() ) ){
			 ObjectError error = new ObjectError("billingPostalCode", messages.getMessage("NotEmpty.order.billingPostCode", locale));
			 result.addError(error);
		}
	
	
		if (result.hasErrors()) {
			//  somehow we lose data, so reset Order detail info.
			entityOrder.getOrder().setOrderProducts( orderProducts);
			entityOrder.getOrder().setOrderTotal(orderTotal);
			entityOrder.getOrder().setOrderHistory(orderHistory);
			
			return "admin-orders-edit";
		}
		
		newOrder.setCustomerFirstName(entityOrder.getOrder().getCustomerFirstName() );
		newOrder.setCustomerLastName(entityOrder.getOrder().getCustomerLastName() );
		newOrder.setCustomerStreetAddress(entityOrder.getOrder().getCustomerStreetAddress() );
		newOrder.setCustomerCity(entityOrder.getOrder().getCustomerCity() );
		newOrder.setCustomerState(entityOrder.getOrder().getCustomerState() ); 
		newOrder.setCustomerPostCode(entityOrder.getOrder().getCustomerPostCode() );
		newOrder.setCustomerTelephone(entityOrder.getOrder().getCustomerTelephone() );
		newOrder.setCustomerEmailAddress(entityOrder.getOrder().getCustomerEmailAddress() );
		newOrder.setShippingMethod(entityOrder.getOrder().getShippingMethod() );
		newOrder.setPaymentMethod(entityOrder.getOrder().getPaymentMethod() );
		newOrder.setStatus(entityOrder.getOrder().getStatus() );		
		
		newOrder.setDatePurchased(date);
		newOrder.setLastModified( new Date() );
		
		if(!StringUtils.isBlank(entityOrder.getOrderHistoryComment() ) ) {
			orderStatusHistory.setComments( entityOrder.getOrderHistoryComment() );
			orderStatusHistory.setCustomerNotified(1);
			orderStatusHistory.setStatus(entityOrder.getOrder().getStatus());
			orderStatusHistory.setDateAdded(new Date() );
			orderStatusHistory.setOrder(newOrder);
			newOrder.getOrderHistory().add( orderStatusHistory );
			entityOrder.setOrderHistoryComment( "" );
		}
		
		newOrder.getBilling().setName(entityOrder.getOrder().getBilling().getName() );
		newOrder.getBilling().setAddress(entityOrder.getOrder().getBilling().getAddress() );
		newOrder.getBilling().setCity(entityOrder.getOrder().getBilling().getCity() );
		newOrder.getBilling().setState(entityOrder.getOrder().getBilling().getState() ); 
		newOrder.getBilling().setPostalCode( entityOrder.getOrder().getBilling().getPostalCode() );
		
		newOrder.getDelivery().setName(entityOrder.getOrder().getDelivery().getName() );
		newOrder.getDelivery().setAddress(entityOrder.getOrder().getDelivery().getAddress() );
		newOrder.getDelivery().setCity(entityOrder.getOrder().getDelivery().getCity() );
		newOrder.getDelivery().setState(entityOrder.getOrder().getDelivery().getState() ); 
		newOrder.getDelivery().setPostalCode( entityOrder.getOrder().getDelivery().getPostalCode() );
		
		orderService.saveOrUpdate(newOrder);
		entityOrder.setOrder(newOrder);
		model.addAttribute("order", entityOrder);
		model.addAttribute("success","success");
	
		
		return "admin-orders-edit";
	}
	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
	
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("order", "order");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");

		model.addAttribute("activeMenus",activeMenus);
		
		Menu currentMenu = (Menu)menus.get("order");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
