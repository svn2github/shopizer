package com.salesmanager.web.admin.controller.orders;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
	

	

	
	
	@Secured("ORDER")
	@RequestMapping(value="/admin/orders/editOrder.html", method=RequestMethod.GET)
	public String displayOrderEdit(@RequestParam("id") long orderId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return displayOrder(orderId,model,request,response);

	}

	@SuppressWarnings("unused")
	private String displayOrder(Long orderId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//	System.out.println( "total sort order, title, value = " + ototal.getSortOrder() + " , " + ototal.getTitle() + " , " + ototal.getValue() );

		
		


		//display menu
		setMenu(model,request);
		   
		com.salesmanager.web.entity.order.Order order = new com.salesmanager.web.entity.order.Order();

		
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
		
//		System.out.println( "saveOrder..  newOrder.getId() =  "  + newOrder.getId() );
	
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
		 
		 
		if(!StringUtils.isBlank(entityOrder.getOrder().getCustomer_Email_Address() ) ){
			 java.util.regex.Matcher matcher = pattern.matcher(entityOrder.getOrder().getCustomer_Email_Address());
			 
			 if(!matcher.find()) {
				ObjectError error = new ObjectError("customer_Email_Address",messages.getMessage("Email.customerEmailAddress", locale));
				result.addError(error);
			 }
		}

		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getName() ) ){
			 ObjectError error = new ObjectError("billing_name","Billing Name: " + messages.getMessage("NotEmpty", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getAddress() ) ){
			 ObjectError error = new ObjectError("billing_address","Billing Address: " + messages.getMessage("NotEmpty", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getCity() ) ){
			 ObjectError error = new ObjectError("billing_city","Billing City: " + messages.getMessage("NotEmpty", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getState() ) ){
			 ObjectError error = new ObjectError("billing_state","Billing State: " + messages.getMessage("NotEmpty", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getPostalCode() ) ){
			 ObjectError error = new ObjectError("billing_postalCode","Billing Postal Code: " + messages.getMessage("NotEmpty", locale));
			 result.addError(error);
		}
	
	
		if (result.hasErrors()) {
			//  somehow we lose data, so reset Order detail info.
			entityOrder.getOrder().setOrderProducts( orderProducts);
			entityOrder.getOrder().setOrderTotal(orderTotal);
			entityOrder.getOrder().setOrderHistory(orderHistory);
			
			return "admin-orders-edit";
		}
		
		newOrder.setCustomer_Firstname(entityOrder.getOrder().getCustomer_Firstname() );
		newOrder.setCustomer_Lastname(entityOrder.getOrder().getCustomer_Lastname() );
		newOrder.setCustomer_Street_Address(entityOrder.getOrder().getCustomer_Street_Address() );
		newOrder.setCustomer_City(entityOrder.getOrder().getCustomer_City() );
		newOrder.setCustomer_State(entityOrder.getOrder().getCustomer_State() ); 
		newOrder.setCustomer_PostCode(entityOrder.getOrder().getCustomer_PostCode() );
		newOrder.setCustomer_Telephone(entityOrder.getOrder().getCustomer_Telephone() );
		newOrder.setCustomer_Email_Address(entityOrder.getOrder().getCustomer_Email_Address() );
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
