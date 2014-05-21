/**
 *
 */
package com.salesmanager.web.shop.controller.customer.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.customer.CustomerRegistrationException;
import com.salesmanager.core.business.customer.exception.CustomerNotFoundException;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionValueService;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartCalculationService;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.GroupType;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.PermissionService;
import com.salesmanager.web.admin.entity.userpassword.UserReset;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.customer.Address;
import com.salesmanager.web.entity.customer.CustomerEntity;
import com.salesmanager.web.entity.customer.PersistableCustomer;
import com.salesmanager.web.entity.customer.ReadableCustomer;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.populator.customer.CustomerBillingAddressPopulator;
import com.salesmanager.web.populator.customer.CustomerDeliveryAddressPopulator;
import com.salesmanager.web.populator.customer.CustomerEntityPopulator;
import com.salesmanager.web.populator.customer.CustomerPopulator;
import com.salesmanager.web.populator.customer.PersistableCustomerBillingAddressPopulator;
import com.salesmanager.web.populator.customer.PersistableCustomerShippingAddressPopulator;
import com.salesmanager.web.populator.customer.ReadableCustomerPopulator;
import com.salesmanager.web.populator.shoppingCart.ShoppingCartDataPopulator;


/**
 * Customer Facade work as an abstraction layer between Controller and Service layer.
 * It work as an entry point to service layer.
 * @author Umesh Awasthi
 *
 */

@Service("customerFacade")
//// http://stackoverflow.com/questions/17444258/how-to-use-new-passwordencoder-from-spring-security
public class CustomerFacadeImpl implements CustomerFacade
{

	private static final Logger LOG = LoggerFactory.getLogger(CustomerFacadeImpl.class);
	private final static int USERNAME_LENGTH=6;

	 @Autowired
     private CustomerService customerService;

     @Autowired
     private ShoppingCartService shoppingCartService;

     @Autowired
     private ShoppingCartCalculationService shoppingCartCalculationService;

     @Autowired
     private PricingService pricingService;

     @Autowired
     private ProductService productService;

     @Autowired
     private ProductAttributeService productAttributeService;
     
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
     private PermissionService   permissionService;

     @Autowired
     private ZoneService zoneService;
     
     @SuppressWarnings( "deprecation" )
     @Autowired
     private PasswordEncoder passwordEncoder;
     
 	 @Autowired
 	 private EmailService emailService;
 	 
 	 @Autowired
     private AuthenticationManager customerAuthenticationManager;




    /**
     * Method used to fetch customer based on the username and storecode.
     * Customer username is unique to each store.
     *
     * @param userName
     * @param storeCode
     * @throws ConversionException
     */
    @Override
    public CustomerEntity getCustomerDataByUserName( final String userName, final MerchantStore store, final Language language ) throws Exception
    {
        LOG.info( "Fetching customer with userName" +userName);
        Customer customer=customerService.getByNick( userName );

        if(customer !=null){
            LOG.info( "Found customer, converting to CustomerEntity");
            try{
            CustomerEntityPopulator customerEntityPopulator=new CustomerEntityPopulator();
            return customerEntityPopulator.populate( customer, store, language ); //store, language

            }
            catch(ConversionException ex){
                LOG.error( "Error while converting Customer to CustomerEntity", ex );
                throw new Exception(ex);
            }
        }

        return null;

    }


    /* (non-Javadoc)
    *  @see com.salesmanager.web.shop.controller.customer.facade#mergeCart(final Customer customerModel, final String sessionShoppingCartId ,final MerchantStore store,final Language language)
    */
    @Override
    public ShoppingCartData mergeCart( final Customer customerModel, final String sessionShoppingCartId ,final MerchantStore store,final Language language)
        throws Exception
    {

        LOG.debug( "Starting merge cart process" );
        if(customerModel != null){
            ShoppingCart customerCart = shoppingCartService.getByCustomer( customerModel );
            if(StringUtils.isNotBlank( sessionShoppingCartId )){
	            ShoppingCart sessionShoppingCart = shoppingCartService.getByCode( sessionShoppingCartId, store );
	            if(sessionShoppingCart != null){
	               if(customerCart == null){
	            	   if(sessionShoppingCart.getCustomerId()==null) {//saved shopping cart does not belong to a customer
		                   LOG.debug( "Not able to find any shoppingCart with current customer" );
		                   //give it to the customer
		                   sessionShoppingCart.setCustomerId( customerModel.getId() );
		                   shoppingCartService.saveOrUpdate( sessionShoppingCart );
		                   customerCart =shoppingCartService.getById( sessionShoppingCart.getId(), store );
		                   return populateShoppingCartData(customerCart,store,language);
	            	   } else {
	            		   return null;
	            	   }
	               }
	               else{
	                    if(sessionShoppingCart.getCustomerId()==null) {//saved shopping cart does not belong to a customer
	                    	//assign it to logged in user
	                    	LOG.debug( "Customer shopping cart as well session cart is available, merging carts" );
	                    	customerCart=shoppingCartService.mergeShoppingCarts( customerCart, sessionShoppingCart, store );
	                    	customerCart =shoppingCartService.getById( customerCart.getId(), store );
		                    return populateShoppingCartData(customerCart,store,language);
	                    } else {
	                    	if(sessionShoppingCart.getCustomerId().longValue()==customerModel.getId().longValue()) {
	                    		if(!customerCart.getShoppingCartCode().equals(sessionShoppingCart.getShoppingCartCode())) {
		                    		//merge carts
		                    		LOG.info( "Customer shopping cart as well session cart is available" );
		                    		customerCart=shoppingCartService.mergeShoppingCarts( customerCart, sessionShoppingCart, store );
		                    		customerCart =shoppingCartService.getById( customerCart.getId(), store );
		    	                    return populateShoppingCartData(customerCart,store,language);
	                    		} else {
	                    			return populateShoppingCartData(sessionShoppingCart,store,language);
	                    		}
	                    	} else {
	                    		//the saved cart belongs to another user
	                    		return null;
	                    	}
	                    }
	            	    
	                    
	              }
	            }
            }
            else{
                 if(customerCart !=null){
                     return populateShoppingCartData(customerCart,store,language);
                 }
                 return null;

            }
        }
        LOG.info( "Seems some issue with system, unable to find any customer after successful authentication" );
        return null;

    }


    private ShoppingCartData populateShoppingCartData(final ShoppingCart cartModel , final MerchantStore store, final Language language){

        ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
        shoppingCartDataPopulator.setShoppingCartCalculationService( shoppingCartCalculationService );
        shoppingCartDataPopulator.setPricingService( pricingService );
        try
        {
            return shoppingCartDataPopulator.populate(  cartModel ,  store,  language);
        }
        catch ( ConversionException ce )
        {
           LOG.error( "Error in converting shopping cart to shopping cart data", ce );

        }
        return null;
    }


 	@Override
 	public Customer getCustomerByUserName(String userName, MerchantStore store)
		throws Exception {
 		return customerService.getByNick( userName, store.getId() );
 	}


   /**
    * <p>
    * Method to check if given user exists for given username under given store.
    * System treat username as unique for a given store, 
    * customer is not allowed
    * to use same username twice for a given store, however it can be used for 
    * different stores.</p>
    * 
    * @param userName Customer slected userName
    * @param store store for which customer want to register
    * @return boolean flag indicating if user exists for given store or not
    * @throws Exception 
    * 
    */
   @Override
    public boolean checkIfUserExists( final String userName, final MerchantStore store )
        throws Exception
    {
        if ( StringUtils.isNotBlank( userName ) && store != null )
        {
            Customer customer = customerService.getByNick( userName, store.getId() );
            if ( customer != null )
            {
                LOG.info( "Customer with userName {} already exists for store {} ", userName, store.getStorename() );
                return true;
            }
            
            LOG.info( "No customer found with userName {} for store {} ", userName, store.getStorename());
            return false;

        }
        LOG.info( "Either userName is empty or we have not found any value for store" );
        return false;
    }


    @Override
    public CustomerEntity registerCustomer( final PersistableCustomer customer,final MerchantStore merchantStore, Language language )
        throws Exception
    {
       LOG.info( "Starting customer registration process.." );
        Customer customerModel= getCustomerModel(customer,merchantStore,language);
        if(customerModel == null){
            LOG.equals( "Unable to create customer in system" );
            throw new CustomerRegistrationException( "Unable to register customer" );
        }
        
        LOG.info( "About to persist customer to database." );
        customerService.saveOrUpdate( customerModel );
        
       LOG.info( "Returning customer data to controller.." );
       return customerEntityPoulator(customerModel,merchantStore);
     }
    
    @Override
    public Customer getCustomerModel(final PersistableCustomer customer,final MerchantStore merchantStore, Language language) throws Exception {
        
        LOG.info( "Starting to populate customer model from customer data" );
        Customer customerModel=null;
        CustomerPopulator populator = new CustomerPopulator();
        populator.setCountryService(countryService);
        populator.setCustomerOptionService(customerOptionService);
        populator.setCustomerOptionValueService(customerOptionValueService);
        populator.setLanguageService(languageService);
        populator.setLanguageService(languageService);
        populator.setZoneService(zoneService);


            customerModel= populator.populate( customer, merchantStore, language );
			//set groups
            if(!StringUtils.isBlank(customerModel.getPassword()) && !StringUtils.isBlank(customerModel.getNick())) {
            	customerModel.setPassword(passwordEncoder.encodePassword(customer.getPassword(), null));
            	setCustomerModelDefaultProperties(customerModel, merchantStore);
            }

          return customerModel;

    }
    

    
    
    private CustomerEntity customerEntityPoulator(final Customer customerModel,final MerchantStore merchantStore){
        CustomerEntityPopulator customerPopulator=new CustomerEntityPopulator();
        try
        {
            CustomerEntity customerEntity= customerPopulator.populate( customerModel, merchantStore, merchantStore.getDefaultLanguage() );
            if(customerEntity !=null){
                customerEntity.setId( customerModel.getId() );
                LOG.info( "Retunring populated instance of customer entity" );
                return customerEntity;
            }
            LOG.warn( "Seems some issue with customerEntity populator..retunring null instance of customerEntity " );
            return null;
              
        }
        catch ( ConversionException e )
        {
           LOG.error( "Error while converting customer model to customer entity ",e );
          
        }
        return null;
    }


	@Override
	public void setCustomerModelDefaultProperties(Customer customer,
			MerchantStore store) throws Exception {
		Validate.notNull(customer, "Customer object cannot be null");
		if(customer.getId()==null || customer.getId()==0) {
			if(StringUtils.isBlank(customer.getNick())) {
				String userName = UserReset.generateRandomString(USERNAME_LENGTH);
				customer.setNick(userName);
			}
			if(StringUtils.isBlank(customer.getPassword())) {
	        	String password = UserReset.generateRandomString();
	        	String encodedPassword = passwordEncoder.encodePassword(password, null);
	        	customer.setPassword(encodedPassword);
			}
		}
		
		if(CollectionUtils.isEmpty(customer.getGroups())) {
			List<Group> groups = groupService.listGroup(GroupType.CUSTOMER);
			for(Group group : groups) {
				  if(group.getGroupName().equals(Constants.GROUP_CUSTOMER)) {
					  customer.getGroups().add(group);
				  }
			}
			
		}
		
	}



	
	@SuppressWarnings("deprecation")
	public void authenticate(Customer customer, String userName, String password) throws Exception {
		
			Validate.notNull(customer, "Customer cannot be null");

        	Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			GrantedAuthority role = new GrantedAuthorityImpl(Constants.PERMISSION_CUSTOMER_AUTHENTICATED);//required to login
			authorities.add(role); 
			List<Integer> groupsId = new ArrayList<Integer>();
			List<Group> groups = customer.getGroups();
			if(groups!=null) {
				for(Group group : groups) {
					groupsId.add(group.getId());
					
				}

		    	List<Permission> permissions = permissionService.getPermissions(groupsId);
		    	for(Permission permission : permissions) {
		    		GrantedAuthority auth = new GrantedAuthorityImpl(permission.getPermissionName());
		    		authorities.add(auth);
		    	}
			}

			Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(userName, password, authorities);
    	
			Authentication authentication = customerAuthenticationManager.authenticate(authenticationToken);
    
			SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}


	@Override
    public Address getAddress( Long userId, final MerchantStore merchantStore,boolean isBillingAddress)
        throws Exception
    {
	     LOG.info( "Fetching customer for id {} ", userId);
        Address address=null;
        final Customer customerModel=customerService.getById( userId );
        
        if(customerModel == null){
            LOG.error( "Customer with ID {} does not exists..", userId);
            throw new CustomerNotFoundException( "customer with given id does not exists" ); 
        }
        
       if(isBillingAddress){
            LOG.info( "getting billing address.." );
            CustomerBillingAddressPopulator billingAddressPopulator=new CustomerBillingAddressPopulator();
            address =billingAddressPopulator.populate( customerModel, merchantStore, merchantStore.getDefaultLanguage() );
            address.setBillingAddress( true );
            return address;
        }
        
        LOG.info( "getting Delivery address.." );
        CustomerDeliveryAddressPopulator deliveryAddressPopulator=new CustomerDeliveryAddressPopulator();
        return deliveryAddressPopulator.populate( customerModel, merchantStore, merchantStore.getDefaultLanguage() );
    
    }


    @Override
    public void updateAddress( Long userId, MerchantStore merchantStore, Address address, final Language language )
        throws Exception
    {
       
     Customer customerModel=customerService.getById( userId );
       Map<String, Country> countriesMap = countryService.getCountriesMap( language );
       Country country = countriesMap.get( address.getCountry() );
      
      if(customerModel ==null){
           LOG.error( "Customer with ID {} does not exists..", userId);
           throw new CustomerNotFoundException( "customer with given id does not exists" );
           
       }
       if(address.isBillingAddress()){
           LOG.info( "updating customer billing address..");
           PersistableCustomerBillingAddressPopulator billingAddressPopulator=new PersistableCustomerBillingAddressPopulator();
           customerModel= billingAddressPopulator.populate( address, customerModel, merchantStore, merchantStore.getDefaultLanguage() );
           customerModel.getBilling().setCountry( country );
           if(StringUtils.isNotBlank( address.getZone() )){
               Zone zone = zoneService.getByCode(address.getZone());
               if(zone==null) {
                  throw new ConversionException("Unsuported zone code " + address.getZone());
               }
               else{
                   customerModel.getBilling().setZone( zone );
               }
               
           }
          
       }
       else{
           LOG.info( "updating customer shipping address..");
           PersistableCustomerShippingAddressPopulator shippingAddressPopulator=new PersistableCustomerShippingAddressPopulator();
           customerModel= shippingAddressPopulator.populate( address, customerModel, merchantStore, merchantStore.getDefaultLanguage() );
           customerModel.getDelivery().setCountry( country );
           if(StringUtils.isNotBlank( address.getZone() )){
               Zone zone = zoneService.getByCode(address.getZone());
               if(zone==null) {
                   throw new ConversionException("Unsuported zone code " + address.getZone());
               }
               else{
                   customerModel.getDelivery().setZone( zone );
               }
              
           }
           
       }
  
     
      // same update address with customer model
       this.customerService.saveOrUpdate( customerModel );
       
    }
    
	@Override
	public ReadableCustomer getCustomerById(final Long id, final MerchantStore merchantStore, final Language language) throws Exception {
		Customer customerModel = customerService.getById(id);
		if(customerModel==null) {
			return null;
		}
		
		ReadableCustomer readableCustomer = new ReadableCustomer();
		
		ReadableCustomerPopulator customerPopulator = new ReadableCustomerPopulator();
		customerPopulator.populate(customerModel,readableCustomer, merchantStore, language);
		
		return readableCustomer;
	}


	@Override
	public Customer populateCustomerModel(Customer customerModel,
			PersistableCustomer customer, MerchantStore merchantStore,
			Language language) throws Exception {
        LOG.info( "Starting to populate customer model from customer data" );
        CustomerPopulator populator = new CustomerPopulator();
        populator.setCountryService(countryService);
        populator.setCustomerOptionService(customerOptionService);
        populator.setCustomerOptionValueService(customerOptionValueService);
        populator.setLanguageService(languageService);
        populator.setLanguageService(languageService);
        populator.setZoneService(zoneService);


            customerModel= populator.populate( customer, customerModel, merchantStore, language );
			//set groups
            //if(!StringUtils.isBlank(customerModel.getPassword()) && !StringUtils.isBlank(customerModel.getNick())) {
            //	customerModel.setPassword(passwordEncoder.encodePassword(customer.getPassword(), null));
            //	setCustomerModelDefaultProperties(customerModel, merchantStore);
            //}

            LOG.info( "About to persist customer to database." );
            customerService.saveOrUpdate( customerModel );
            return customerModel;
	}

}
