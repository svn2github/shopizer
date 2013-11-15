/**
 * 
 */
package com.salesmanager.web.shop.controller.shoppingCart.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartAttribute;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartItem;
import com.salesmanager.web.populator.shoppingCart.ShoppingCartDataPopulator;
import com.salesmanager.web.populator.shoppingCart.ShoppingCartModelPopulator;
import com.salesmanager.web.utils.ImageFilePathUtils;

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
    private ShoppingCartDataPopulator shoppingCartDataPopulator;

    @Autowired
    private ProductPriceUtils productPriceUtils;

    @Autowired
    private ProductService productService;

    
    @Autowired
    private PricingService pricingService;

    
    @Autowired
    private OrderService orderService;
    
   
    



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

        return this.shoppingCartDataPopulator.populateFromEntity( cart);

       /* ShoppingCartDataPopulator populator = new ShoppingCartDataPopulator();
        populator.setOrderService(this.orderService);
        populator.setPricingService(this.pricingService);
        return populator.populateFromEntity(cart, new ShoppingCartData(), store, language);*/


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
    public ShoppingCartData getShoppingCartData( ShoppingCart shoppingCart)
        throws Exception
    {
       
        return this.shoppingCartDataPopulator.populateFromEntity( shoppingCart );
    }

    @Override
    public ShoppingCart getShoppingCart( ShoppingCartData shoppingCart) throws Exception
    {
      return this.shoppingCartModelPopulator.populateToEntity( shoppingCart );
    }

    @Override
    public ShoppingCartData addItemsToShoppingCart( ShoppingCartData shoppingCart, ShoppingCartItem item,
                                                    MerchantStore store ) throws Exception
    {
        List<ShoppingCartItem> items = shoppingCart.getShoppingCartItems();
        boolean itemFound = false;
        if(items!=null) {
            for(ShoppingCartItem shoppingCartItem : items) {
                if(shoppingCartItem.getId()==item.getId()) {
                    itemFound = true;
                    if(CollectionUtils.isEmpty(shoppingCartItem.getShoppingCartAttributes()) 
                            && CollectionUtils.isEmpty(item.getShoppingCartAttributes())) {
                        int qty = shoppingCartItem.getQuantity();
                        qty = qty + item.getQuantity();
                        shoppingCartItem.setQuantity(qty);
                        break;
                    } else {
                        List<ShoppingCartAttribute> itemAttributes = item.getShoppingCartAttributes();
                        List<ShoppingCartAttribute> originalitemAttributes = shoppingCartItem.getShoppingCartAttributes();
                        if(!CollectionUtils.isEmpty(itemAttributes) && !CollectionUtils.isEmpty(originalitemAttributes)) {  
                                if(itemAttributes.size()==originalitemAttributes.size()) {
                                    boolean attributesMatch = true;
                                    for(ShoppingCartAttribute itemAttribute : itemAttributes) {
                                        boolean singleAttributeMatch = false;
                                        for(ShoppingCartAttribute originalAttribute : originalitemAttributes) {
                                            if(originalAttribute.getOptionId()==itemAttribute.getOptionId()) {
                                                singleAttributeMatch = true;
                                            }
                                        }
                                        if(!singleAttributeMatch) {
                                            //Attributes are not identical
                                            createItemInShoppingCart(shoppingCart,item,store);
                                            attributesMatch = false;
                                            break;
                                        }
                                    }
                                    
                                    if(attributesMatch) {
                                        int qty = shoppingCartItem.getQuantity();
                                        qty = qty + item.getQuantity();
                                        shoppingCartItem.setQuantity(qty);
                                        break;
                                    }
                                    
                                } else {
                                    createItemInShoppingCart(shoppingCart,item,store);
                                }
                        }//end if
                    }//end else
                    
                }
            }//end for
        }//end if
        if(!itemFound) {
            createItemInShoppingCart(shoppingCart,item,store);
        }
        
        
        BigDecimal subTotal = item.getProductPrice().multiply(new BigDecimal(item.getQuantity()));
        item.setSubTotal(pricingService.getDisplayAmount(subTotal, store));
        
        ShoppingCart entity=getShoppingCart( shoppingCart );
        shoppingCartService.saveOrUpdate(entity);
        return this.shoppingCartDataPopulator.populateFromEntity( entity );
    }
    
   private void createItemInShoppingCart(ShoppingCartData cart, ShoppingCartItem item, MerchantStore store) throws Exception {
        
        Product product = productService.getById(item.getProductId());
        
        if(product==null) {
            throw new Exception("Item with id " + item.getProductId() + " does not exist");
        }
        
        if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
            throw new Exception("Item with id " + item.getProductId() + " does not belong to merchant " + store.getId());
        }
        
        item.setCode(cart.getCode());
        item.setId(product.getId());
        item.setName(product.getProductDescription().getName());
        
        
        List<ShoppingCartAttribute> cartAttributes = item.getShoppingCartAttributes();
        Set<ProductAttribute> attributes = product.getAttributes();
        List<ProductAttribute> pricingAttributes = null;
        if(!CollectionUtils.isEmpty(cartAttributes) && !CollectionUtils.isEmpty(attributes)) {
            List<ShoppingCartAttribute> savedAttributes = new ArrayList<ShoppingCartAttribute>();
            for(ShoppingCartAttribute cartAttribute : cartAttributes) {
                boolean attributeFound = false;
                for(ProductAttribute productAttribute : attributes) {
                    if(productAttribute.getId().longValue()==cartAttribute.getAttributeId()){
                        attributeFound = true;
                        if(pricingAttributes==null) {
                            pricingAttributes = new ArrayList<ProductAttribute>();
                        }
                        pricingAttributes.add(productAttribute);
                        cartAttribute.setOptionName(productAttribute.getProductOption().getDescriptionsList().get(0).getName());
                        cartAttribute.setOptionValue(productAttribute.getProductOptionValue().getDescriptionsSettoList().get(0).getName());
                        cartAttribute.setOptionId(productAttribute.getProductOption().getId());
                        cartAttribute.setOptionValueId(productAttribute.getProductOptionValue().getId());
                    }
                }
                if(attributeFound) {
                    savedAttributes.add(cartAttribute);
                }
            }
            
            item.setShoppingCartAttributes(savedAttributes);
            
            
        } else {//check if product has default attributes
            item.setShoppingCartAttributes(null);
            if(!CollectionUtils.isEmpty(attributes)) {
                
                for(ProductAttribute productAttribute : attributes) {
                    
                        if(productAttribute.getAttributeDefault()) {
                    
                            if(pricingAttributes==null) {
                                pricingAttributes = new ArrayList<ProductAttribute>();
                            }
                            pricingAttributes.add(productAttribute);
                            ShoppingCartAttribute cartAttribute = new ShoppingCartAttribute();
                            cartAttribute.setOptionName(productAttribute.getProductOption().getDescriptionsList().get(0).getName());
                            cartAttribute.setOptionValue(productAttribute.getProductOptionValue().getDescriptionsSettoList().get(0).getName());
                            cartAttribute.setOptionId(productAttribute.getProductOption().getId());
                            cartAttribute.setOptionValueId(productAttribute.getProductOptionValue().getId());
                            
                        }
                }
                
            }
        }
        
        ProductImage image = product.getProductImage();
        if(image!=null) {
            String imagePath = ImageFilePathUtils.buildProductImageFilePath(store, product.getSku(), image.getProductImage());
            item.setImage(imagePath);
        }
        FinalPrice finalPrice = pricingService.calculateProductPrice(product, pricingAttributes);
        item.setPrice(pricingService.getDisplayAmount(finalPrice.getFinalPrice(), store));
        item.setProductPrice(finalPrice.getFinalPrice());
        
        List<ShoppingCartItem> items = cart.getShoppingCartItems();
        if(items == null) {
            items = new ArrayList<ShoppingCartItem>();
            cart.setShoppingCartItems(items);
        }

        items.add(item);
    
        
    }

}
