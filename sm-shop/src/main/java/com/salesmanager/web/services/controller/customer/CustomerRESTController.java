package com.salesmanager.web.services.controller.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionValueService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.GroupType;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.modules.email.Email;
import com.salesmanager.web.admin.entity.userpassword.UserReset;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.constants.EmailConstants;
import com.salesmanager.web.entity.customer.PersistableCustomer;
import com.salesmanager.web.entity.customer.ReadableCustomer;
import com.salesmanager.web.entity.customer.attribute.PersistableCustomerOption;
import com.salesmanager.web.entity.customer.attribute.PersistableCustomerOptionValue;
import com.salesmanager.web.populator.customer.CustomerPopulator;
import com.salesmanager.web.populator.customer.PersistableCustomerOptionPopulator;
import com.salesmanager.web.populator.customer.PersistableCustomerOptionValuePopulator;
import com.salesmanager.web.populator.customer.ReadableCustomerPopulator;
import com.salesmanager.web.services.controller.category.ShoppingCategoryRESTController;
import com.salesmanager.web.utils.EmailUtils;
import com.salesmanager.web.utils.FilePathUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;

@Controller
@RequestMapping("/shop/services")
public class CustomerRESTController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerOptionValueService customerOptionValueService;
	
	@Autowired
	private CustomerOptionService customerOptionService;
	
	
	@Autowired
	private MerchantStoreService merchantStoreService;
	
	@Autowired
	private LanguageService languageService;
	

	@Autowired
	private CountryService countryService;
	
	@Autowired
	private GroupService   groupService;
	
	@Autowired
	private ZoneService zoneService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	private LabelUtils messages;

	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCategoryRESTController.class);
	
	
	/**
	 * Returns a single customer for a given MerchantStore
	 */
	@RequestMapping( value="/private/customer/{store}/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ReadableCustomer getCustomer(@PathVariable final String store, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		
		Customer customer = customerService.getById(id);
		com.salesmanager.web.entity.customer.Customer customerProxy;
		if(customer == null){
			response.sendError(404, "No Customer found with id : " + id);
			return null;
		}
		
		ReadableCustomerPopulator populator = new ReadableCustomerPopulator();
		ReadableCustomer readableCustomer = new ReadableCustomer();
		populator.populate(customer, readableCustomer, merchantStore, merchantStore.getDefaultLanguage());
		
		return readableCustomer;
	}
	
	
	@RequestMapping( value="/shop/services/private/customer/optionValue/{store}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PersistableCustomerOptionValue createCustomerOptionValue(@PathVariable final String store, @Valid @RequestBody PersistableCustomerOptionValue optionValue, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {
			
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

			PersistableCustomerOptionValuePopulator populator = new PersistableCustomerOptionValuePopulator();
			populator.setLanguageService(languageService);
			
			com.salesmanager.core.business.customer.model.attribute.CustomerOptionValue optValue = new com.salesmanager.core.business.customer.model.attribute.CustomerOptionValue();
			populator.populate(optionValue, optValue, merchantStore, merchantStore.getDefaultLanguage());
		
			customerOptionValueService.save(optValue);
			
			optionValue.setId(optValue.getId());
			
			return optionValue;
			
		} catch (Exception e) {
			LOGGER.error("Error while saving customer option value",e);
			try {
				response.sendError(503, "Error while saving product option value" + e.getMessage());
			} catch (Exception ignore) {
			}	
			return null;
		}
	}
	
	
	@RequestMapping( value="/shop/services/private/customer/option/{store}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PersistableCustomerOption createCustomerOption(@PathVariable final String store, @Valid @RequestBody PersistableCustomerOption option, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {
			
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

			PersistableCustomerOptionPopulator populator = new PersistableCustomerOptionPopulator();
			populator.setLanguageService(languageService);
			
			com.salesmanager.core.business.customer.model.attribute.CustomerOption opt = new com.salesmanager.core.business.customer.model.attribute.CustomerOption();
			populator.populate(option, opt, merchantStore, merchantStore.getDefaultLanguage());
		
			customerOptionService.save(opt);
			
			option.setId(opt.getId());
			
			return option;
			
		} catch (Exception e) {
			LOGGER.error("Error while saving customer option",e);
			try {
				response.sendError(503, "Error while saving product option value" + e.getMessage());
			} catch (Exception ignore) {
			}	
			return null;
		}
	}
	
	
	/**
	 * Returns all customers for a given MerchantStore
	 */
	@RequestMapping( value="/private/customer/{store}", method=RequestMethod.GET)
	@ResponseBody
	public List<ReadableCustomer> getCustomers(@PathVariable final String store, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		
		List<Customer> customers = customerService.listByStore(merchantStore);
		List<ReadableCustomer> returnCustomers = new ArrayList<ReadableCustomer>();
		for(Customer customer : customers) {

			ReadableCustomerPopulator populator = new ReadableCustomerPopulator();
			ReadableCustomer readableCustomer = new ReadableCustomer();
			populator.populate(customer, readableCustomer, merchantStore, merchantStore.getDefaultLanguage());
			returnCustomers.add(readableCustomer);
			
		}
		
		return returnCustomers;
	}
	
	
/*	*//**
	 * Updates a customer for a given MerchantStore
	 *//*
	@RequestMapping( value="/private/customer/{store}/{id}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateCustomer(@PathVariable final String store, @PathVariable Long id, @Valid @RequestBody Customer customer, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		}
		
		Customer oldCustomer = customerService.getById(id);
		if(oldCustomer != null){
			customer.setId(id);
			Country country = (customer.getCountry() != null)?countryService.getByCode(customer.getCountry().getIsoCode().toUpperCase()):null;
			if(country != null){
				customer.setCountry(country);
			}
			
			Country billCountry = (customer.getBilling() != null)?countryService.getByCode(customer.getBilling().getCountry().getIsoCode().toUpperCase()):null;
			if(billCountry != null){
				customer.getBilling().setCountry(billCountry);
			}
			
			Country delCountry = (customer.getDelivery() != null)?countryService.getByCode(customer.getDelivery().getCountry().getIsoCode().toUpperCase()):null;
			if(delCountry != null){
				customer.getDelivery().setCountry(delCountry);
			}
			
			if(customer.getZone() != null){
				customer.setZone(zoneService.getByCode(customer.getZone().getCountry().getIsoCode().toUpperCase()));
				Country zoneCountry = countryService.getByCode(customer.getZone().getCountry().getIsoCode().toUpperCase());
				if(zoneCountry != null){
					customer.getZone().setCountry(zoneCountry);
				}
			}
			
			customer.setMerchantStore(merchantStore);
			customer.setDefaultLanguage(languageService.getByCode(Constants.DEFAULT_LANGUAGE));
			
			customerService.saveOrUpdate(customer);
		}else{
			response.sendError(404, "No Customer found for ID : " + id);
		}
	}*/
	
	
	/**
	 * Deletes a customer for a given MerchantStore
	 */
	@RequestMapping( value="/private/customer/{store}/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCustomer(@PathVariable final String store, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		try {
			
			Customer customer = customerService.getById(id);
			
			if(customer==null) {
				response.sendError(404, "No Customer found for ID : " + id);
				return;
			} 
				
				MerchantStore merchantStore = merchantStoreService.getByCode(store);
				if(merchantStore == null) {
					response.sendError(404, "Invalid merchant store : " + store);
					return;
				}
				
				if(merchantStore.getId().intValue()!= customer.getMerchantStore().getId().intValue()){
					response.sendError(404, "Customer id: " + id + " is not part of store " + store);
					return;
				}			
				
				customerService.delete(customer);
			
			
		} catch (ServiceException se) {
			LOGGER.error("Cannot delete customer",se);
			response.sendError(404, "An exception occured while removing the customer");
			return;
		}

	}
	
	
	/**
	 * Create new customer for a given MerchantStore
	 */
	@RequestMapping( value="/private/customer/{store}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PersistableCustomer createCustomer(@PathVariable final String store, @Valid @RequestBody PersistableCustomer customer, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		
		Customer cust = new Customer();
		
		CustomerPopulator populator = new CustomerPopulator();
		populator.setCountryService(countryService);
		populator.setCustomerOptionService(customerOptionService);
		populator.setCustomerOptionValueService(customerOptionValueService);
		populator.setLanguageService(languageService);
		populator.setLanguageService(languageService);
		populator.setZoneService(zoneService);
		populator.populate(customer, cust, merchantStore, merchantStore.getDefaultLanguage());
		
		List<Group> groups = groupService.listGroup(GroupType.ADMIN);
		cust.setGroups(groups);

		Locale customerLocale = LocaleUtils.getLocale(cust.getDefaultLanguage());
		
		String password = UserReset.generateRandomString();
		@SuppressWarnings("deprecation")
		String encodedPassword = passwordEncoder.encodePassword(password, null);
		
		customer.setPassword(encodedPassword);
		customerService.save(cust);
		customer.setId(cust.getId());

		
		try {


			
			Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(request, merchantStore, messages, customerLocale);
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getFirstName());
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getLastName());
			
			String[] greetingMessage = {merchantStore.getStorename(),FilePathUtils.buildCustomerUri(merchantStore, request),merchantStore.getStoreEmailAddress()};
			
			
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_GREETING, messages.getMessage("email.customer.greeting", greetingMessage, customerLocale));
			templateTokens.put(EmailConstants.EMAIL_USERNAME_LABEL, messages.getMessage("label.generic.username",customerLocale));
			templateTokens.put(EmailConstants.EMAIL_PASSWORD_LABEL, messages.getMessage("label.generic.password",customerLocale));
			templateTokens.put(EmailConstants.EMAIL_USER_NAME, customer.getUserName());
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_PASSWORD, password);


			Email email = new Email();
			email.setFrom(merchantStore.getStorename());
			email.setFromEmail(merchantStore.getStoreEmailAddress());
			email.setSubject(messages.getMessage("email.newuser.title",customerLocale));
			email.setTo(customer.getEmailAddress());
			email.setTemplateName(EmailConstants.EMAIL_CUSTOMER_TPL);
			email.setTemplateTokens(templateTokens);

			//send email
			emailService.sendHtmlEmail(merchantStore, email);
		
		} catch (Exception e) {
			LOGGER.error("Cannot send email to user",e);
		}

		return customer;
	}
	
}
