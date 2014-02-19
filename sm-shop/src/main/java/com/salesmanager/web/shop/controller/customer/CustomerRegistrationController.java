package com.salesmanager.web.shop.controller.customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.customer.CustomerRegistrationException;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.modules.email.Email;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.constants.EmailConstants;
import com.salesmanager.web.entity.customer.AnonymousCustomer;
import com.salesmanager.web.entity.customer.CustomerEntity;
import com.salesmanager.web.entity.customer.PersistableCustomer;
import com.salesmanager.web.entity.customer.ShopPersistableCustomer;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.utils.EmailUtils;
import com.salesmanager.web.utils.FilePathUtils;
import com.salesmanager.web.utils.LabelUtils;

/**
 * Registration of a new customer
 * @author Carl Samson
 *
 */

@SuppressWarnings( "deprecation" )
// http://stackoverflow.com/questions/17444258/how-to-use-new-passwordencoder-from-spring-security
@Controller
@RequestMapping("/shop/customer")
public class CustomerRegistrationController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRegistrationController.class);
    public static final String RECAPATCHA_PUBLIC_KEY="shopizer.recapatcha_public_key";
    public static final String RECAPATCHA_PRIVATE_KEY="shopizer.recapatcha_private_key";
    
	@Autowired
	private CoreConfiguration coreConfiguration;

	@Autowired
	private LanguageService languageService;


	@Autowired
	private CountryService countryService;

	
	@Autowired
	private ZoneService zoneService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	EmailService emailService;

	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private CustomerFacade customerFacade;
	
	@Autowired
    private AuthenticationManager customerAuthenticationManager;



	@RequestMapping(value="/registration.html", method=RequestMethod.GET)
	public String displayRegistration(final Model model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		model.addAttribute( "recapatcha_public_key", coreConfiguration.getProperty( RECAPATCHA_PUBLIC_KEY ) );
		
		ShopPersistableCustomer customer = new ShopPersistableCustomer();
		AnonymousCustomer anonymousCustomer = (AnonymousCustomer)request.getAttribute(Constants.ANONYMOUS_CUSTOMER);
		if(anonymousCustomer!=null) {
			customer.setBilling(anonymousCustomer.getBilling());
		}
		
		model.addAttribute("customer", customer);

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.register).append(".").append(store.getStoreTemplate());

		return template.toString();


	}

    @RequestMapping( value = "/register.html", method = RequestMethod.POST )
    public String registerCustomer( @Valid
    @ModelAttribute("customer") ShopPersistableCustomer customer, BindingResult bindingResult, Model model,
                                    HttpServletRequest request, final Locale locale )
        throws Exception
    {
        MerchantStore merchantStore = (MerchantStore) request.getAttribute( Constants.MERCHANT_STORE );
        Language language = (Language)request.getAttribute(Constants.LANGUAGE);
        
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPublicKey( coreConfiguration.getProperty( RECAPATCHA_PUBLIC_KEY ) );
        reCaptcha.setPrivateKey( coreConfiguration.getProperty( RECAPATCHA_PRIVATE_KEY ) );
        

        model.addAttribute( "recapatcha_public_key", coreConfiguration.getProperty( RECAPATCHA_PUBLIC_KEY ) );
        
        if ( StringUtils.isNotBlank( customer.getRecaptcha_challenge_field() )
            && StringUtils.isNotBlank( customer.getRecaptcha_response_field() ) )
        {
            ReCaptchaResponse reCaptchaResponse =
                reCaptcha.checkAnswer( request.getRemoteAddr(), customer.getRecaptcha_challenge_field(),
                                       customer.getRecaptcha_response_field() );
            if ( !reCaptchaResponse.isValid() )
            {
                LOGGER.info( "Captcha response does not matched" );
    			FieldError error = new FieldError("recaptcha_challenge_field","recaptcha_challenge_field",messages.getMessage("validaion.recaptcha.not.matched", locale));
    			bindingResult.addError(error);
            }

        }
        
        if ( StringUtils.isNotBlank( customer.getUserName() ) )
        {
            if ( customerFacade.checkIfUserExists( customer.getUserName(), merchantStore ) )
            {
                LOGGER.info( "Customer with username {} already exists for this store ", customer.getUserName() );
            	FieldError error = new FieldError("userName","userName",messages.getMessage("registration.username.already.exists", locale));
            	bindingResult.addError(error);
            }
        }
        
        
        if ( StringUtils.isNotBlank( customer.getPassword() ) &&  StringUtils.isNotBlank( customer.getCheckPassword() ))
        {
            if (! customer.getPassword().equals(customer.getCheckPassword()) )
            {
            	FieldError error = new FieldError("password","password",messages.getMessage("message.password.checkpassword.identical", locale));
            	bindingResult.addError(error);

            }
        }

        if ( bindingResult.hasErrors() )
        {
            LOGGER.info( "found {} validation error while validating in customer registration ",
                         bindingResult.getErrorCount() );
            StringBuilder template =
                new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
            return template.toString();

        }

        @SuppressWarnings( "unused" )
        CustomerEntity customerData = null;
        try
        {
            customerData = customerFacade.registerCustomer( customer, merchantStore );
        }
        catch ( CustomerRegistrationException cre )
        {
            LOGGER.error( "Error while registering customer.. ", cre);
        	ObjectError error = new ObjectError("registration",messages.getMessage("registration.failed", locale));
        	bindingResult.addError(error);
            StringBuilder template =
                            new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
             return template.toString();
        }
        catch ( Exception e )
        {
            LOGGER.error( "Error while registering customer.. ", e);
        	ObjectError error = new ObjectError("registration",messages.getMessage("registration.failed", locale));
        	bindingResult.addError(error);
            StringBuilder template =
                            new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
            return template.toString();
        }
              
        /**
         * Send registration email
         */
        sendRegistrationEmail( request, customer, merchantStore, locale );

        /**
         * Login user
         */
        
        try {
        	

	        
	    	Authentication authenticationToken =
	                new UsernamePasswordAuthenticationToken(customer.getUserName(), customer.getPassword());
	    	
	        Authentication authentication = customerAuthenticationManager.authenticate(authenticationToken);
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        
	        //refresh customer
	        Customer c = customerFacade.getCustomerByUserName(customer.getUserName(), merchantStore);
	        
	        super.setSessionAttribute(Constants.CUSTOMER, c, request);
	        
	        return "redirect:/shop/customer/dashboard.html";
        
        
        } catch(Exception e) {
        	LOGGER.error("Cannot authenticate user ",e);
        	ObjectError error = new ObjectError("registration",messages.getMessage("registration.failed", locale));
        	bindingResult.addError(error);
        }
        
        
        StringBuilder template =
                new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
        return template.toString();

    }
	
	
	@ModelAttribute("countryList")
	public List<Country> getCountries(final HttpServletRequest request){
	    
        Language language = (Language) request.getAttribute( "LANGUAGE" );
        try
        {
            if ( language == null )
            {
                language = (Language) request.getAttribute( "LANGUAGE" );
            }

            if ( language == null )
            {
                language = languageService.getByCode( Constants.DEFAULT_LANGUAGE );
            }
            
            List<Country> countryList=countryService.getCountries( language );
            return countryList;
        }
        catch ( ServiceException e )
        {
            LOGGER.error( "Error while fetching country list ", e );

        }
        return Collections.emptyList();
    }
	
	@ModelAttribute("zoneList")
    public List<Zone> getZones(final HttpServletRequest request){
	    return zoneService.list();
	}
	
	

	

    private void sendRegistrationEmail(final HttpServletRequest request,final PersistableCustomer customer,final MerchantStore merchantStore, final Locale customerLocale){
       LOGGER.info( "Sending welcome email to customer" );
       try {

           Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(request, merchantStore, messages, customerLocale);
           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());
           String[] greetingMessage = {merchantStore.getStorename(),FilePathUtils.buildCustomerUri(merchantStore, request),merchantStore.getStoreEmailAddress()};
           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_GREETING, messages.getMessage("email.customer.greeting", greetingMessage, customerLocale));
           templateTokens.put(EmailConstants.EMAIL_USERNAME_LABEL, messages.getMessage("label.generic.username",customerLocale));
           templateTokens.put(EmailConstants.EMAIL_PASSWORD_LABEL, messages.getMessage("label.generic.password",customerLocale));
           templateTokens.put(EmailConstants.EMAIL_USER_NAME, customer.getUserName());
           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_PASSWORD, customer.getPassword());


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
}
