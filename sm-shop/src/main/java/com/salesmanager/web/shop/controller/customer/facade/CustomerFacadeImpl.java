/**
 *
 */
package com.salesmanager.web.shop.controller.customer.facade;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;

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
import com.salesmanager.core.business.generic.exception.ServiceException;
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
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.GroupType;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.web.entity.customer.Address;
import com.salesmanager.web.entity.customer.CustomerEntity;
import com.salesmanager.web.entity.customer.PersistableCustomer;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.populator.customer.CustomerBillingAddressPopulator;
import com.salesmanager.web.populator.customer.CustomerDeliveryAddressPopulator;
import com.salesmanager.web.populator.customer.CustomerEntityPopulator;
import com.salesmanager.web.populator.customer.CustomerPopulator;
import com.salesmanager.web.populator.customer.PersistableCustomerBillingAddressPopulator;
import com.salesmanager.web.populator.customer.PersistableCustomerShippingAddressPopulator;
import com.salesmanager.web.populator.shoppingCart.ShoppingCartDataPopulator;


/**
 * Customer Facade work as an abstraction layer between Controller and Service layer.
 * It work as an entry point to service layer.
 * @author Umesh Awasthi
 *
 */

//@Service("customerFacade")
//// http://stackoverflow.com/questions/17444258/how-to-use-new-passwordencoder-from-spring-security
public class CustomerFacadeImpl implements CustomerFacade
{

	private static final Logger LOG = LoggerFactory.getLogger(CustomerFacadeImpl.class);

     @Autowired
     CustomerService customerService;

     @Autowired
     private ShoppingCartService shoppingCartService;

     @Autowired
     ShoppingCartCalculationService shoppingCartCalculationService;

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
     private ZoneService zoneService;
     
     @SuppressWarnings( "deprecation" )
     @Autowired
     private PasswordEncoder passwordEncoder;


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


    /**
     * <p>Method responsible for performing customer auto-login, Method will perform following operations
     * <li> Fetch customer based on userName and Store.</li>
     * <li> Merge Customer Shopping Cart with Session Cart if any.</li>
     * <li> Convert Customer to {@link CustomerEntity} </li>
     * </p>
     * <p>There is a possibility that customer might have added few items in his/ her shoppingCart
     * and for this case, we will use sessionShoppingCartId to fetch shoppingCart from database and
     * will merge sessionShoppingCart with ShoppingCart associated with the Customer.</p>
     *
     * @param userName username of Customer
     * @param storeCode storeCode to which user is associated/
     * @param sessionShoppingCartId session shopping cart, if user already have few items in Cart.
     * @throws Exception
     */
    @Override
    public ShoppingCartData customerAutoLogin( final String userName, final String sessionShoppingCartId ,final MerchantStore store,final Language language)
        throws Exception
    {

        LOG.info( "Starting customer autologin process" );
        Customer customerModel=getCustomerByUserName(userName, store);
        if(customerModel != null){
            ShoppingCart cartModel =shoppingCartService.getByCustomer( customerModel );
            if(StringUtils.isNotBlank( sessionShoppingCartId )){
            ShoppingCart sessionShoppingCart=shoppingCartService.getByCode( sessionShoppingCartId, store );
            if(sessionShoppingCart !=null){
               if(cartModel == null){
                   LOG.debug( "Not able to find any shoppingCart with current customer" );
                   sessionShoppingCart.setCustomerId( customerModel.getId() );
                   shoppingCartService.saveOrUpdate( sessionShoppingCart );
                   cartModel =shoppingCartService.getById( sessionShoppingCart.getId(), store );
                   return populateShoppingCartData(cartModel,store,language);
               }
               else{
                   LOG.info( "Customer shopping cart as well session cart is available" );
                    cartModel=shoppingCartService.mergeShoppingCarts( cartModel, sessionShoppingCart, store );
                    cartModel =shoppingCartService.getById( cartModel.getId(), store );
                    return populateShoppingCartData(cartModel,store,language);
              }
            }
            }
            else{
                 if(cartModel !=null){
                     return populateShoppingCartData(cartModel,store,language);
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
    public CustomerEntity registerCustomer( final PersistableCustomer customer,final MerchantStore merchantStore )
        throws Exception
    {
       LOG.info( "Starting customer registration process.." );
        Customer customerModel=populateCustomerModel(customer,merchantStore);
        if(customerModel == null){
            LOG.equals( "Unable to create customer in system" );
            throw new CustomerRegistrationException( "Unable to register customer" );
        }
        
       LOG.info( "Returning customer data to controller.." );
       return customerEntityPoulator(customerModel,merchantStore);
     }
    
    @SuppressWarnings( "deprecation" )
    private Customer populateCustomerModel(final PersistableCustomer customer,final MerchantStore merchantStore){
        
        LOG.info( "Starting to populate customer model from customer data" );
        Customer customerModel=null;
        CustomerPopulator populator = new CustomerPopulator();
        populator.setCountryService(countryService);
        populator.setCustomerOptionService(customerOptionService);
        populator.setCustomerOptionValueService(customerOptionValueService);
        populator.setLanguageService(languageService);
        populator.setLanguageService(languageService);
        populator.setZoneService(zoneService);
        try
        {
            //populator.populate(customer, customerModel, merchantStore, merchantStore.getDefaultLanguage());
            customerModel= populator.populate( customer, merchantStore, merchantStore.getDefaultLanguage() );
            List<Group> groups = groupService.listGroup(GroupType.CUSTOMER);
            customerModel.setGroups( groups );
            customerModel.setPassword(passwordEncoder.encodePassword(customer.getPassword(), null));
            LOG.info( "About to persist customer to database." );
            customerService.saveOrUpdate( customerModel );
       }
        catch ( ConversionException e )
        {
           LOG.error( "Exception while converting customer data to customer model ",e );
           return null;
        }
        catch ( ServiceException e )
        {
            LOG.error( "Unable to find any group ",e );
            return null;
        }
       
        if(customerModel.getId() !=null){
            LOG.info( "Returning update instance of customer" );
            return customerService.getById( customerModel.getId() ); 
        }
       
        Log.info( "Seems some issue while persisting customer  to database..returning null" );
        return null;

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
                   //throw new ConversionException("Unsuported zone code " + address.getZone());
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
                   //throw new ConversionException("Unsuported zone code " + address.getZone());
               }
               else{
                   customerModel.getDelivery().setZone( zone );
               }
              
           }
           
       }
  
      // same update address with customer model
       this.customerService.saveOrUpdate( customerModel );
       
    }
  

}
