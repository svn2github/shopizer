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
    public ShoppingCart getShoppingCart( ShoppingCartData shoppingCartData) throws Exception
    {
        ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
        shoppingCartDataPopulator.setOrderService(orderService);
        shoppingCartDataPopulator.setPricingService(pricingService);
        
        return null;
        //return this.shoppingCartModelPopulator.populateToEntity( shoppingCartData );
    }

    @Override
    public ShoppingCartData addItemsToShoppingCart( ShoppingCartData shoppingCartData, ShoppingCartItem item,
                                                    MerchantStore store ) throws Exception
    {
        
        Customer customer = (Customer)getKeyValue(Constants.CUSTOMER);
        Language language = (Language)getKeyValue(Constants.LANGUAGE);
        com.salesmanager.core.business.shoppingcart.model.ShoppingCart cartModel =null;
        if(!StringUtils.isBlank(item.getCode()) && !(item.getCode().equalsIgnoreCase( "undefined" )))  {
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

       com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem item = new com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem( cartModel, product );
       item.setQuantity( shoppingCartItem.getQuantity() );
       
       List<ProductAttribute> productAttributes = new ArrayList<ProductAttribute>();
       
           productAttributes.addAll( product.getAttributes() );
           final FinalPrice finalPrice = productPriceUtils.getFinalProductPrice( product, productAttributes );
          
         
      
     
       
       item.setItemPrice( finalPrice.getFinalPrice());
       item.setShoppingCart( cartModel );
      
       // attributes
       List<ShoppingCartAttribute> cartAttributes = shoppingCartItem.getShoppingCartAttributes();
       if ( !CollectionUtils.isEmpty( cartAttributes ) )
       {
          for ( ShoppingCartAttribute attribute : cartAttributes )
           {
               ProductAttribute productAttribute = productAttributeService.getById( attribute.getAttributeId() );
               if ( productAttribute != null
                   && productAttribute.getProduct().getId().longValue() == product.getId().longValue() )
               {
                   com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem attributeItem =
                       new com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem( item,
                                                                                                        productAttribute );
                   if ( attribute.getAttributeId() > 0 )
                   {
                       attributeItem.setId( attribute.getId() );
                   }
                   item.addAttributes( attributeItem );
             }
  }
       }

       return item;

   }
   
   
   @Override
   public void removeCartItem(final Long itemID, final String cartId) throws Exception {
   	if(StringUtils.isNotBlank(cartId)){
   		MerchantStore store = (MerchantStore)getKeyValue(Constants.MERCHANT_STORE);
   		ShoppingCart cartModel = shoppingCartService.getByCode(cartId, store);
   		if(CollectionUtils.isNotEmpty(cartModel.getLineItems())){
   			Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> shoppingCartItemSet=new HashSet<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem>();
   			for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem shoppingCartItem :cartModel.getLineItems()){
   				if(shoppingCartItem.getId().longValue() != itemID.longValue()){
   					shoppingCartItemSet.add(shoppingCartItem);
   				}
   			}
   			cartModel.setLineItems(shoppingCartItemSet);
   			shoppingCartService.saveOrUpdate(cartModel);
   	}
   	}
   
   }

   
   private Object getKeyValue( final String key )
   {
       ServletRequestAttributes reqAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
       return reqAttr.getRequest().getAttribute( key );
   }


}
