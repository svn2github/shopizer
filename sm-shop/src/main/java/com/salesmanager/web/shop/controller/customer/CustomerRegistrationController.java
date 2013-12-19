package com.salesmanager.web.shop.controller.customer;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionValueService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
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
import com.salesmanager.web.entity.customer.Customer;
import com.salesmanager.web.entity.customer.PersistableCustomer;
import com.salesmanager.web.populator.customer.CustomerPopulator;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.utils.EmailUtils;
import com.salesmanager.web.utils.FilePathUtils;
import com.salesmanager.web.utils.LabelUtils;

/**
 * Registration of a new customer
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping("/shop/customer")
public class CustomerRegistrationController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRegistrationController.class);
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private CustomerOptionValueService customerOptionValueService;
	
	@Autowired
	private CustomerOptionService customerOptionService;
	

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
	

	
	@RequestMapping(value="/registration.html", method=RequestMethod.GET)
	public String displayRegistration(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		
		//TODO object will change
		Customer customer = new Customer();
		
		model.addAttribute("customer", customer);
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.register).append(".").append(store.getStoreTemplate());

		return template.toString();
		
		
	}
	
	@RequestMapping( value="/register.html", method=RequestMethod.POST)
	public String registerCustomer(Model model, @Valid @ModelAttribute PersistableCustomer customer, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		
		//manual validations
		/** check if the customer exists **/
		
		com.salesmanager.core.business.customer.model.Customer cust = new com.salesmanager.core.business.customer.model.Customer();

		customerService.save(cust);
		customer.setId(cust.getId());

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

		Locale customerLocale = locale;
		
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
		
		String[] greetingMessage = {customer.getEmailAddress(),FilePathUtils.buildCustomerUri(merchantStore, request),merchantStore.getStoreEmailAddress()};

		
		//prepare response
		String registrationConfirmation = messages.getMessage("label.register.confirmation", greetingMessage, customerLocale);
		model.addAttribute("",registrationConfirmation);
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.registerConfirmation).append(".").append(merchantStore.getStoreTemplate());

		return template.toString();

	}

}
