/**
 *
 */
package com.salesmanager.web.shop.controller.customer.facade;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartCalculationService;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.web.entity.customer.CustomerEntity;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.populator.customer.CustomerEntityPopulator;
import com.salesmanager.web.populator.shoppingCart.ShoppingCartDataPopulator;

/**
 * Customer Facade work as an abstraction layer between Controller and Service layer.
 * It work as an entry point to service layer.
 * @author Umesh Awasthi
 *
 */

//@Service("customerFacade")
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
}
