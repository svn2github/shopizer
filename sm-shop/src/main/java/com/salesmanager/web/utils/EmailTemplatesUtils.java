package com.salesmanager.web.utils;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.cookie.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderTotal;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.modules.email.Email;
import com.salesmanager.web.constants.ApplicationConstants;
import com.salesmanager.web.constants.EmailConstants;
import com.salesmanager.web.entity.customer.PersistableCustomer;


@Component
public class EmailTemplatesUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailTemplatesUtils.class);
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ZoneService zoneService;
	
	@Autowired
	private PricingService pricingService;
	
	private final static String LINE_BREAK = "<br/>";
	private final static String TABLE = "<table width=\"100%\">";
	private final static String CLOSING_TABLE = "</table>";
	private final static String TR = "<tr>";
	private final static String TR_BORDER = "<tr class=\"border\">";
	private final static String CLOSING_TR = "</tr>";
	private final static String TD = "<td valign=\"top\">";
	private final static String CLOSING_TD = "</td>";
	

	/**
	 * Sends an email to the customer afer a completed order
	 * @param customer
	 * @param order
	 * @param customerLocale
	 * @param language
	 * @param merchantStore
	 * @param contextPath
	 */
	@Async
	public void sendOrderEmail(Customer customer, Order order, Locale customerLocale, Language language, MerchantStore merchantStore, String contextPath) {
			   /** issue with putting that elsewhere **/ 
		       LOGGER.info( "Sending welcome email to customer" );
		       try {
		    	   
		    	   Map<String,Country> countries = countryService.getCountriesMap(language);
		    	   
		    	   //format Billing address
		    	   StringBuilder billing = new StringBuilder();
		    	   if(StringUtils.isBlank(order.getBilling().getCompany())) {
		    		   billing.append(order.getBilling().getFirstName()).append(" ")
		    		   .append(order.getBilling().getLastName()).append(LINE_BREAK);
		    	   } else {
		    		   billing.append(order.getBilling().getCompany()).append(LINE_BREAK);
		    	   }
		    	   billing.append(order.getBilling().getAddress()).append(LINE_BREAK);
		    	   billing.append(order.getBilling().getCity()).append(", ");
		    	   
		    	   if(order.getBilling().getZone()!=null) {
		    		   Zone zone = zoneService.getByCode(order.getBilling().getZone().getCode());
		    		   billing.append(zone.getName()).append(LINE_BREAK);
		    	   } else if(!StringUtils.isBlank(order.getBilling().getState())) {
		    		   billing.append(order.getBilling().getState()).append(LINE_BREAK); 
		    	   }
		    	   Country country = countries.get(order.getBilling().getCountry().getIsoCode());
		    	   if(country!=null) {
		    		   billing.append(country.getName()).append(" ");
		    	   }
		    	   billing.append(order.getBilling().getPostalCode());
		    	   
		    	   
		    	   //format shipping address
		    	   StringBuilder shipping = null;
		    	   if(order.getDelivery()!=null && !StringUtils.isBlank(order.getDelivery().getFirstName())) {
		    		   shipping = new StringBuilder();
			    	   if(StringUtils.isBlank(order.getDelivery().getCompany())) {
			    		   shipping.append(order.getDelivery().getFirstName()).append(" ")
			    		   .append(order.getDelivery().getLastName()).append(LINE_BREAK);
			    	   } else {
			    		   shipping.append(order.getDelivery().getCompany()).append(LINE_BREAK);
			    	   }
			    	   shipping.append(order.getDelivery().getAddress()).append(LINE_BREAK);
			    	   shipping.append(order.getDelivery().getCity()).append(", ");
			    	   
			    	   if(order.getDelivery().getZone()!=null) {
			    		   Zone zone = zoneService.getByCode(order.getDelivery().getZone().getCode());
			    		   shipping.append(zone.getName()).append(LINE_BREAK);
			    	   } else if(!StringUtils.isBlank(order.getDelivery().getState())) {
			    		   shipping.append(order.getDelivery().getState()).append(LINE_BREAK); 
			    	   }
			    	   Country deliveryCountry = countries.get(order.getDelivery().getCountry().getIsoCode());
			    	   if(country!=null) {
			    		   shipping.append(deliveryCountry.getName()).append(" ");
			    	   }
			    	   shipping.append(order.getDelivery().getPostalCode());
		    	   }
		    	   
		    	   if(shipping==null && StringUtils.isNotBlank(order.getShippingModuleCode())) {
		    		   //TODO IF HAS NO SHIPPING
		    		   shipping = billing;
		    	   }
		    	   
		    	   //format order
		    	   //String storeUri = FilePathUtils.buildStoreUri(merchantStore, contextPath);
		    	   StringBuilder orderTable = new StringBuilder();
		    	   orderTable.append(TABLE);
		    	   for(OrderProduct product : order.getOrderProducts()) {
		    		   //Product productModel = productService.getByCode(product.getSku(), language);
		    		   orderTable.append(TR);
		    		   	   //images are ugly
/*		    		       orderTable.append(TD);
			    		   if(productModel!=null && productModel.getProductImage()!=null) {
			    			   String productImage = new StringBuilder().append(storeUri).append(ImageFilePathUtils.buildProductImageFilePath(merchantStore, productModel, productModel.getProductImage().getProductImage())).toString();
			    			   
			    			   String imgSrc = new StringBuilder().append("<img src=\"").append(productImage).append("\" width=\"40\">").toString();
			    			   orderTable.append(imgSrc);
			    		   } else {
			    			   orderTable.append("&nbsp;");
			    		   }
			    		   orderTable.append(CLOSING_TD);*/
			    		   orderTable.append(TD).append(product.getProductName()).append(CLOSING_TD);
		    		   	   orderTable.append(TD).append(messages.getMessage("label.quantity", customerLocale)).append(": ").append(product.getProductQuantity()).append(CLOSING_TD);
	    		   		   orderTable.append(TD).append(pricingService.getDisplayAmount(product.getOneTimeCharge(), merchantStore)).append(CLOSING_TD);
    		   		   orderTable.append(CLOSING_TR);
		    	   }

		    	   //order totals
		    	   for(OrderTotal total : order.getOrderTotal()) {
		    		   orderTable.append(TR_BORDER);
		    		   		//orderTable.append(TD);
		    		   		//orderTable.append(CLOSING_TD);
		    		   		orderTable.append(TD);
		    		   		orderTable.append(CLOSING_TD);
		    		   		orderTable.append(TD);
		    		   		orderTable.append("<strong>");
		    		   			if(total.getModule().equals("tax")) {
		    		   				orderTable.append(total.getText()).append(": ");

		    		   			} else {
		    		   				//if(total.getModule().equals("total") || total.getModule().equals("subtotal")) {
		    		   				//}
		    		   				orderTable.append(messages.getMessage(total.getOrderTotalCode(), customerLocale)).append(": ");
		    		   				//if(total.getModule().equals("total") || total.getModule().equals("subtotal")) {
		    		   					
		    		   				//}
		    		   			}
		    		   		orderTable.append("</strong>");
		    		   		orderTable.append(CLOSING_TD);
		    		   		orderTable.append(TD);
		    		   			orderTable.append("<strong>");

		    		   			orderTable.append(pricingService.getDisplayAmount(total.getValue(), merchantStore));

	    		   				orderTable.append("</strong>");
		    		   		orderTable.append(CLOSING_TD);
		    		   orderTable.append(CLOSING_TR);
		    	   }
		    	   orderTable.append(CLOSING_TABLE);

		           Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
		           templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
		           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, order.getBilling().getFirstName());
		           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, order.getBilling().getLastName());
		           
		           String[] params = {String.valueOf(order.getId())};
		           String[] dt = {DateUtils.formatDate(order.getDatePurchased())};
		           templateTokens.put(EmailConstants.EMAIL_ORDER_NUMBER, messages.getMessage("email.order.confirmation", params, customerLocale));
		           templateTokens.put(EmailConstants.EMAIL_ORDER_DATE, messages.getMessage("email.order.ordered", dt, customerLocale));
		           templateTokens.put(EmailConstants.EMAIL_ORDER_THANKS, messages.getMessage("email.order.thanks",customerLocale));
		           templateTokens.put(EmailConstants.ADDRESS_BILLING, billing.toString());
		           
		           templateTokens.put(EmailConstants.ORDER_PRODUCTS_DETAILS, orderTable.toString());
		           templateTokens.put(EmailConstants.EMAIL_ORDER_DETAILS_TITLE, messages.getMessage("label.order.details",customerLocale));
		           templateTokens.put(EmailConstants.ADDRESS_BILLING_TITLE, messages.getMessage("label.customer.billinginformation",customerLocale));
		           templateTokens.put(EmailConstants.PAYMENT_METHOD_TITLE, messages.getMessage("label.order.paymentmode",customerLocale));
		           templateTokens.put(EmailConstants.PAYMENT_METHOD_DETAILS, messages.getMessage(new StringBuilder().append("payment.type.").append(order.getPaymentType().name()).toString(),customerLocale,order.getPaymentType().name()));
		           
		           if(StringUtils.isNotBlank(order.getShippingModuleCode())) {
		        	   templateTokens.put(EmailConstants.SHIPPING_METHOD_DETAILS, messages.getMessage(new StringBuilder().append("module.shipping.").append(order.getShippingModuleCode()).toString(),customerLocale,order.getShippingModuleCode()));
		        	   templateTokens.put(EmailConstants.ADDRESS_SHIPPING_TITLE, messages.getMessage("label.order.shippingmethod",customerLocale));
		        	   templateTokens.put(EmailConstants.ADDRESS_DELIVERY_TITLE, messages.getMessage("label.customer.shippinginformation",customerLocale));
		        	   templateTokens.put(EmailConstants.SHIPPING_METHOD_TITLE, messages.getMessage("label.customer.shippinginformation",customerLocale));
		        	   templateTokens.put(EmailConstants.ADDRESS_DELIVERY, shipping.toString());
		           } else {
		        	   templateTokens.put(EmailConstants.SHIPPING_METHOD_DETAILS, "");
		        	   templateTokens.put(EmailConstants.ADDRESS_SHIPPING_TITLE, "");
		        	   templateTokens.put(EmailConstants.ADDRESS_DELIVERY_TITLE, "");
		        	   templateTokens.put(EmailConstants.SHIPPING_METHOD_TITLE, "");
		        	   templateTokens.put(EmailConstants.ADDRESS_DELIVERY, "");
		           }
		           String[] status = {messages.getMessage(new StringBuilder().append("label.order.").append(order.getStatus().name()).toString(),customerLocale)};
		           templateTokens.put(EmailConstants.ORDER_STATUS, messages.getMessage("email.order.status", status, customerLocale));
		           

		           String[] title = {merchantStore.getStorename(), String.valueOf(order.getId())};
		           Email email = new Email();
		           email.setFrom(merchantStore.getStorename());
		           email.setFromEmail(merchantStore.getStoreEmailAddress());
		           email.setSubject(messages.getMessage("email.order.title", title, customerLocale));
		           email.setTo(customer.getEmailAddress());
		           email.setTemplateName(EmailConstants.EMAIL_ORDER_TPL);
		           email.setTemplateTokens(templateTokens);

		           LOGGER.info( "Sending email to {} for order id {} ",customer.getEmailAddress(), order.getId() );
		           emailService.sendHtmlEmail(merchantStore, email);

		       } catch (Exception e) {
		           LOGGER.error("Error occured while sending order confirmation email ",e);
		       }
			
		}
	
	/**
	 * Sends an email to the customer after registration
	 * @param request
	 * @param customer
	 * @param merchantStore
	 * @param customerLocale
	 */
	@Async
	public void sendRegistrationEmail(
		PersistableCustomer customer, MerchantStore merchantStore,
			Locale customerLocale, String contextPath) {
		   /** issue with putting that elsewhere **/ 
	       LOGGER.info( "Sending welcome email to customer" );
	       try {

	           Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
	           templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
	           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
	           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());
	           String[] greetingMessage = {merchantStore.getStorename(),FilePathUtils.buildCustomerUri(merchantStore,contextPath),merchantStore.getStoreEmailAddress()};
	           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_GREETING, messages.getMessage("email.customer.greeting", greetingMessage, customerLocale));
	           templateTokens.put(EmailConstants.EMAIL_USERNAME_LABEL, messages.getMessage("label.generic.username",customerLocale));
	           templateTokens.put(EmailConstants.EMAIL_PASSWORD_LABEL, messages.getMessage("label.generic.password",customerLocale));
	           templateTokens.put(EmailConstants.CUSTOMER_ACCESS_LABEL, messages.getMessage("label.customer.accessportal",customerLocale));
	           templateTokens.put(EmailConstants.ACCESS_NOW_LABEL, messages.getMessage("label.customer.accessnow",customerLocale));
	           templateTokens.put(EmailConstants.EMAIL_USER_NAME, customer.getUserName());
	           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_PASSWORD, customer.getPassword());

	           //shop url
	           String customerUrl = FilePathUtils.buildStoreUri(merchantStore, contextPath);
	           templateTokens.put(EmailConstants.CUSTOMER_ACCESS_URL, customerUrl);

	           Email email = new Email();
	           email.setFrom(merchantStore.getStorename());
	           email.setFromEmail(merchantStore.getStoreEmailAddress());
	           email.setSubject(messages.getMessage("email.newuser.title",customerLocale));
	           email.setTo(customer.getEmailAddress());
	           email.setTemplateName(EmailConstants.EMAIL_CUSTOMER_TPL);
	           email.setTemplateTokens(templateTokens);

	           LOGGER.info( "Sending email to {} on their  registered email id {} ",customer.getBilling().getFirstName(),customer.getEmailAddress() );
	           emailService.sendHtmlEmail(merchantStore, email);

	       } catch (Exception e) {
	           LOGGER.error("Error occured while sending welcome email ",e);
	       }
		
	}
	
	/**
	 * Send an email to the customer with download instructions
	 * @param request
	 * @param customer
	 * @param order
	 * @param merchantStore
	 * @param customerLocale
	 */
	@Async
	public void sendOrderDownloadEmail(
			Customer customer, Order order, MerchantStore merchantStore,
			Locale customerLocale, String contextPath) {
		   /** issue with putting that elsewhere **/ 
	       LOGGER.info( "Sending download email to customer" );
	       try {

	           Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
	           templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
	           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
	           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());
	           String[] downloadMessage = {String.valueOf(ApplicationConstants.MAX_DOWNLOAD_DAYS), String.valueOf(order.getId()), FilePathUtils.buildCustomerUri(merchantStore, contextPath), merchantStore.getStoreEmailAddress()};
	           templateTokens.put(EmailConstants.EMAIL_ORDER_DOWNLOAD, messages.getMessage("email.order.download.text", downloadMessage, customerLocale));
	           templateTokens.put(EmailConstants.CUSTOMER_ACCESS_LABEL, messages.getMessage("label.customer.accessportal",customerLocale));
	           templateTokens.put(EmailConstants.ACCESS_NOW_LABEL, messages.getMessage("label.customer.accessnow",customerLocale));

	           //shop url
	           String customerUrl = FilePathUtils.buildStoreUri(merchantStore, contextPath);
	           templateTokens.put(EmailConstants.CUSTOMER_ACCESS_URL, customerUrl);

	           String[] orderInfo = {String.valueOf(order.getId())};
	           
	           Email email = new Email();
	           email.setFrom(merchantStore.getStorename());
	           email.setFromEmail(merchantStore.getStoreEmailAddress());
	           email.setSubject(messages.getMessage("email.order.download.title", orderInfo, customerLocale));
	           email.setTo(customer.getEmailAddress());
	           email.setTemplateName(EmailConstants.EMAIL_ORDER_DOWNLOAD_TPL);
	           email.setTemplateTokens(templateTokens);

	           LOGGER.info( "Sending email to {} with download info",customer.getEmailAddress() );
	           emailService.sendHtmlEmail(merchantStore, email);

	       } catch (Exception e) {
	           LOGGER.error("Error occured while sending order download email ",e);
	       }
		
	}

}