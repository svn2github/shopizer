package com.salesmanager.web.shop.controller.customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.customer.CustomerRegistrationException;
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
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.constants.EmailConstants;
import com.salesmanager.web.entity.customer.AnonymousCustomer;
import com.salesmanager.web.entity.customer.CustomerEntity;
import com.salesmanager.web.entity.customer.PersistableCustomer;
import com.salesmanager.web.entity.customer.ShopPersistableCustomer;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.shop.controller.data.CountryData;
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
public class CustomerRegistrationController{

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
    @ModelAttribute
    final ShopPersistableCustomer customer, final BindingResult bindingResult, final Model model,
                                    final HttpServletRequest request, final Locale locale )
        throws Exception
    {
        final MerchantStore merchantStore = (MerchantStore) request.getAttribute( Constants.MERCHANT_STORE );
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPublicKey( coreConfiguration.getProperty( RECAPATCHA_PUBLIC_KEY ) );
        reCaptcha.setPrivateKey( coreConfiguration.getProperty( RECAPATCHA_PRIVATE_KEY ) );
        model.addAttribute( "recapatcha_public_key", coreConfiguration.getProperty( RECAPATCHA_PUBLIC_KEY ) );
        model.addAttribute("customer", customer);
        
        if ( StringUtils.isNotBlank( customer.getRecaptcha_challenge_field() )
            && StringUtils.isNotBlank( customer.getRecaptcha_response_field() ) )
        {
            ReCaptchaResponse reCaptchaResponse =
                reCaptcha.checkAnswer( request.getRemoteAddr(), customer.getRecaptcha_challenge_field(),
                                       customer.getRecaptcha_response_field() );
            if ( !reCaptchaResponse.isValid() )
            {
                LOGGER.warn( "Captcha response does not matched" );
                bindingResult.rejectValue( "recaptcha_challenge_field", "validaion.recaptcha.not.matched",
                                           new Object[] {}, "validaion.recaptcha.not.matched" );
            }

        }
        
        if ( StringUtils.isNotBlank( customer.getUserName() ) )
        {
            if ( customerFacade.checkIfUserExists( customer.getUserName(), merchantStore ) )
            {
                LOGGER.warn( "Customer with username {} already exists for this store ", customer.getUserName() );
                bindingResult.rejectValue( "userName", "registration.username.already.exists", new Object[] {},
                                           "registration.username.already.exists" );
            }
        }

        if ( bindingResult.hasErrors() )
        {
            LOGGER.warn( "found {} validation error while validating in customer registration ",
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
            bindingResult.reject( "registration.failed" );
            StringBuilder template =
                            new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
             return template.toString();
        }
        catch ( Exception e )
        {
            LOGGER.error( "Error while registering customer.. {} ", e);
            bindingResult.reject( "registration.failed" );
            StringBuilder template =
                            new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
            return template.toString();
        }
                     
        sendRegistrationEmail( request, customer, merchantStore, locale );

        String[] greetingMessage =
            { customer.getEmailAddress(), FilePathUtils.buildCustomerUri( merchantStore, request ),
                merchantStore.getStoreEmailAddress() };

        String registrationConfirmation = messages.getMessage( "label.register.confirmation", greetingMessage, locale );
        model.addAttribute( "registrationConfirmation", registrationConfirmation );

        /** template **/
        StringBuilder template =
            new StringBuilder().append( ControllerConstants.Tiles.Customer.registerConfirmation ).append( "." ).append( merchantStore.getStoreTemplate() );
        LOGGER.info( "Sending customer to {} page ", template.toString() );
        return template.toString();

    }
	
	
	@ModelAttribute("countryList")
	public List<CountryData> getCountries(final HttpServletRequest request){
	    
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
            if(CollectionUtils.isNotEmpty( countryList )){
                List<CountryData> countryDataList=new ArrayList<CountryData>();
                LOGGER.info( "Creating country list data " );
                for(Country country:countryList){
                    CountryData countryData=new CountryData();
                    countryData.setName( country.getName() );
                    countryData.setIsoCode( country.getIsoCode() );
                    countryData.setId( country.getId() );
                    countryDataList.add( countryData );
                }
                return countryDataList;
            }
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
	
	
	/**
	 * <p>Method responsible for fetching list of zones for a given country.
	 * It will be used for registration page where customer will be able to 
	 * choose zone based on his country of choice. </p>
	 * @param request
	 * @param country
	 * @return list of zones for a given country
	 */
    @RequestMapping( value = "/getZonesByCountry.html", method = RequestMethod.GET ,produces="application/json")
    public @ResponseBody
    String getZones( final HttpServletRequest request, @RequestParam
    final String countryCode )
    {
        AjaxResponse resp = new AjaxResponse();
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

            Map<String, Country> countriesMap = countryService.getCountriesMap( language );
            Country country = countriesMap.get( countryCode );
            List<Zone> zones = zoneService.getZones( country, language );
            if(zones!=null && zones.size()>0) {
                for(Zone zone : zones) {
                    Map <String, String> entry = new HashMap<String, String>();
                    entry.put("name", zone.getName());
                    entry.put("code", zone.getCode());
                    //entry.put("id", zone.getId());
                    resp.addDataEntry(entry);
                }
                LOGGER.info( "Setting response to success" );
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            }
            else{
                resp.setStatus( AjaxResponse.RESPONSE_STATUS_FAIURE );
            }
           
        }
        catch ( ServiceException e )
        {
            LOGGER.info( "Error while fetching zones for county {} ", countryCode );
            LOGGER.error( "Error while fetching zones for county ", e );
            resp.setStatus( AjaxResponse.RESPONSE_STATUS_FAIURE );

        }
        String responseData = resp.toJSONString();
        return responseData;
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
