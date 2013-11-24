/**
 * 
 */
package com.salesmanager.web.shop.controller.shoppingCart.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.order.CartModificationException;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartAttribute;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartItem;
import com.salesmanager.web.populator.shoppingCart.ShoppingCartDataPopulator;
import com.salesmanager.web.populator.shoppingCart.ShoppingCartModelPopulator;

/**
 * @author Umesh Awasthi
 * @version 1.2
 * @since 1.2
 */
@Service( value = "shoppingCartFacade" )
public class ShoppingCartFacadeImpl
    implements ShoppingCartFacade
{

    protected final Logger LOG = Logger.getLogger( getClass() );

    @Autowired
    private ShoppingCartService shoppingCartService;

    
    @Autowired
    private ShoppingCartModelPopulator shoppingCartModelPopulator;
    
   

    @Autowired
    private ProductPriceUtils productPriceUtils;

    @Autowired
    private ProductService productService;

    
    @Autowired
    private PricingService pricingService;

    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ProductAttributeService productAttributeService;
    
   
    



    @Override
    public ShoppingCartData getShoppingCartData( final Customer customer, final MerchantStore store,
                                                final String shoppingCartId )
        throws Exception
    {

        ShoppingCart cart = null;
        try
        {
            if ( customer != null )
            {
                LOG.info( "Reteriving customer shopping cart..." );

                cart = shoppingCartService.getShoppingCart( customer );

            }

            else
            {
                if ( StringUtils.isNotBlank( shoppingCartId ) && cart == null )
                {
                    cart = shoppingCartService.getByCode( shoppingCartId, store );
                }

            }
        }
        catch ( ServiceException ex )
        {
            LOG.error( "Error while retriving cart from customer", ex );
        }

        if ( cart == null )
        {
            return new ShoppingCartData();
        }

        LOG.info( "Cart model found." );
        
        ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
        shoppingCartDataPopulator.setOrderService(orderService);
        shoppingCartDataPopulator.setPricingService(pricingService);

        Language language = (Language)getKeyValue(Constants.LANGUAGE);
        MerchantStore merchantStore = (MerchantStore)getKeyValue(Constants.MERCHANT_STORE);
        return shoppingCartDataPopulator.populate( cart,merchantStore,language);
        
       


    }

    @Override
    public ShoppingCartData recalculateCart( ShoppingCartData shoppingCart )
    {
        List<ShoppingCartItem> shoppingCartItems = Collections.emptyList();
        if ( CollectionUtils.isNotEmpty( shoppingCart.getShoppingCartItems() ) )
        {
            shoppingCartItems = new ArrayList<ShoppingCartItem>();
            List<ProductAttribute> productAttributes = new ArrayList<ProductAttribute>();
            LOG.info( "Calculating final price for cart items." );
            for ( ShoppingCartItem shoppingCartItem : shoppingCart.getShoppingCartItems() )
            {
                Product product = productService.getById( shoppingCartItem.getProductId() );
                productAttributes.addAll( product.getAttributes() );
                final FinalPrice finalPrice = productPriceUtils.getFinalProductPrice( product, productAttributes );
               
                shoppingCartItem.setProductPrice( finalPrice.getFinalPrice() );
                shoppingCartItems.add( shoppingCartItem );
            }
        }

        if ( CollectionUtils.isNotEmpty( shoppingCartItems ) )
        {
            shoppingCart.setShoppingCartItems( shoppingCartItems );
        }

        return shoppingCart;

    }

    @Override
    public ShoppingCartData getShoppingCartData( ShoppingCart shoppingCartModel)
        throws Exception
    {
       
        ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
        shoppingCartDataPopulator.setOrderService(orderService);
        shoppingCartDataPopulator.setPricingService(pricingService);
        Language language = (Language)getKeyValue(Constants.LANGUAGE);
        MerchantStore merchantStore = (MerchantStore)getKeyValue(Constants.MERCHANT_STORE);
        return shoppingCartDataPopulator.populate( shoppingCartModel,merchantStore,language);
    }

    
    @Override
    public ShoppingCartData addItemsToShoppingCart( ShoppingCartData shoppingCartData, ShoppingCartItem item,
                                                    MerchantStore store ) throws Exception
    {
        
        Customer customer = (Customer)getKeyValue(Constants.CUSTOMER);
        Language language = (Language)getKeyValue(Constants.LANGUAGE);
        com.salesmanager.core.business.shoppingcart.model.ShoppingCart cartModel =null;
        if(!StringUtils.isBlank(item.getCode()))  {
        //get it from the db
        cartModel = shoppingCartService.getByCode(item.getCode(), store);
        if(cartModel==null){
            cartModel=new ShoppingCart();
            cartModel.setShoppingCartCode( shoppingCartData.getCode() );
            cartModel.setMerchantStore( store );
            if ( customer != null )
            {
                cartModel.setCustomerId( customer.getId() );
            }
            shoppingCartService.create( cartModel );
            
        }
        
       
    }
        
       
        com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem shoppingCartItem= createCartItem(cartModel,item,store);
        cartModel.getLineItems().add( shoppingCartItem );
        shoppingCartService.saveOrUpdate(cartModel);
       
        ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
        shoppingCartDataPopulator.setOrderService(orderService);
        shoppingCartDataPopulator.setPricingService(pricingService);
       
        return shoppingCartDataPopulator.populate( cartModel, store, language );
    }
    
    
    
    
    
   
   
   
   private com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem createCartItem( ShoppingCart cartModel,
                                                                                              ShoppingCartItem shoppingCartItem,
                                                                                              MerchantStore store ) throws Exception
   {

       Product product = productService.getById( shoppingCartItem.getProductId() );
     

       if ( product == null )
       {
           throw new Exception( "Item with id " + shoppingCartItem.getProductId() + " does not exist" );
       }

       if ( product.getMerchantStore().getId().intValue() != store.getId().intValue() )
       {
           throw new Exception( "Item with id " + shoppingCartItem.getProductId() + " does not belong to merchant "
               + store.getId() );
       }
       
       com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem item = shoppingCartService.populateShoppingCartItem(product);

       item.setQuantity( shoppingCartItem.getQuantity() );
       item.setShoppingCart(cartModel);

       // attributes
       List<ShoppingCartAttribute> cartAttributes = shoppingCartItem.getShoppingCartAttributes();
       if ( !CollectionUtils.isEmpty( cartAttributes ) )
       {
          for ( ShoppingCartAttribute attribute : cartAttributes )
           {
               ProductAttribute productAttribute = productAttributeService.getById( attribute.getAttributeId() );
               if ( productAttribute != null
                   && productAttribute.getProduct().getId().longValue() == product.getId().longValue() ) {
                   com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem attributeItem =
                       new com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem( item,
                                                                                                        productAttribute );
                   if ( attribute.getAttributeId() > 0 ) {
                       attributeItem.setId( attribute.getId() );
                   }
                   item.addAttributes( attributeItem );
              }
           }
       }

       return item;

   }
   
   
   @Override
   public ShoppingCartData removeCartItem(final Long itemID, final String cartId) throws Exception {
   	if(StringUtils.isNotBlank(cartId)){
   	
   		ShoppingCart cartModel = getCartModel(cartId);
   		if(cartModel != null){
   		if(CollectionUtils.isNotEmpty(cartModel.getLineItems())){
   			Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> shoppingCartItemSet=new HashSet<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem>();
   			for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem shoppingCartItem :cartModel.getLineItems()){
   				if(shoppingCartItem.getId().longValue() != itemID.longValue()){
   					shoppingCartItemSet.add(shoppingCartItem);
   				}
   			}
   			cartModel.setLineItems(shoppingCartItemSet);
   			shoppingCartService.saveOrUpdate(cartModel);
   			
   			ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
   	        shoppingCartDataPopulator.setOrderService(orderService);
   	        shoppingCartDataPopulator.setPricingService(pricingService);
   	        
   	        MerchantStore store = (MerchantStore)getKeyValue(Constants.MERCHANT_STORE);
	        Language language = (Language)getKeyValue(Constants.LANGUAGE);
   	        return shoppingCartDataPopulator.populate( cartModel, store, language );
   	}
   		}
   	}
    return null;
   }

   
   @Override
   public ShoppingCartData updateCartItem(final Long itemID, final String cartId, final long newQuantity)
   		throws Exception {
	   if(newQuantity < 1){
		  throw new CartModificationException("Quantity must not be less than one");
	   }
		if(StringUtils.isNotBlank(cartId)){
			ShoppingCart cartModel = getCartModel(cartId);
			if(cartModel !=null){
				com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem entryToUpdate=getEntryToUpdate(itemID.longValue(),cartModel);
				
				if (entryToUpdate == null)
				{
					throw new CartModificationException("Unknown entry number.");
				}
			    
				LOG.info("Updating cart entry quantity to" +newQuantity);
				entryToUpdate.setQuantity((int)newQuantity);
				List<ProductAttribute> productAttributes = new ArrayList<ProductAttribute>();
			    productAttributes.addAll(entryToUpdate.getProduct().getAttributes() );
		        final FinalPrice finalPrice = productPriceUtils.getFinalProductPrice( entryToUpdate.getProduct(), productAttributes );
		        entryToUpdate.setItemPrice(finalPrice.getFinalPrice());
		        shoppingCartService.saveOrUpdate(cartModel);
		       
		        LOG.info("Cart entry updated with desired quantity");
		        ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
		        shoppingCartDataPopulator.setOrderService(orderService);
		        shoppingCartDataPopulator.setPricingService(pricingService);
		        MerchantStore store = (MerchantStore)getKeyValue(Constants.MERCHANT_STORE);
		        Language language = (Language)getKeyValue(Constants.LANGUAGE);
		        return shoppingCartDataPopulator.populate( cartModel, store, language );
		        
			}
		}
   	   return null;
   }
   
   
 private ShoppingCart getCartModel(final String cartId){
		if(StringUtils.isNotBlank(cartId)){
			MerchantStore store = (MerchantStore)getKeyValue(Constants.MERCHANT_STORE);
			try {
				return shoppingCartService.getByCode(cartId, store);
			} catch (ServiceException e) {
				LOG.error("unable to find any cart asscoiated with this Id: "+cartId);
				LOG.error("error while fetching cart model..." ,e);
				return null;
			}
		}
		return null;
   }

   
   private Object getKeyValue( final String key )
   {
       ServletRequestAttributes reqAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
       return reqAttr.getRequest().getAttribute( key );
   }


  private com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem getEntryToUpdate(final long entryId, final ShoppingCart cartModel){
	  if(CollectionUtils.isNotEmpty(cartModel.getLineItems())){
		  for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem shoppingCartItem :cartModel.getLineItems()){
			  if(shoppingCartItem.getId().longValue() == entryId){
				  LOG.info("Found line item  for given entry id: "+entryId);
 					return shoppingCartItem;
 					
 				} 
		  }
	  }
	  LOG.info("Unable to find any entry for given Id: "+entryId);
	  return null;
  }

}
