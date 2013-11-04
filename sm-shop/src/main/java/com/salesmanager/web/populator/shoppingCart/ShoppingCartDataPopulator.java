/**
 * 
 */
package com.salesmanager.web.populator.shoppingCart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.inject.Provider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.ConversionException;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.populator.AbstractDataPopulator;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartAttribute;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartItem;
import com.salesmanager.web.utils.ImageFilePathUtils;


/**
 * @author Umesh A
 *
 */

@Service(value="shoppingCartDataPopulator")
public class ShoppingCartDataPopulator extends AbstractDataPopulator<ShoppingCart,ShoppingCartData>
{

    @Autowired
    private Provider<ShoppingCartData> targetBean;
    
    @Autowired
    private PricingService pricingService;
    

    @Autowired
    private OrderService orderService;
    
    @Override
    public ShoppingCartData populate(final ShoppingCart shoppingCart,final ShoppingCartData cart) {
        MerchantStore store = (MerchantStore)getKeyValue(Constants.MERCHANT_STORE);
        Language language = (Language)getKeyValue("LANGUAGE");
        cart.setCode(shoppingCart.getShoppingCartCode());
        Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> items = shoppingCart.getLineItems();
        List<ShoppingCartItem> shoppingCartItemsList=Collections.emptyList();
        try{
        if(items!=null) {
            shoppingCartItemsList=new ArrayList<ShoppingCartItem>();
            for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem item : items) {
                
                ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                shoppingCartItem.setCode(cart.getCode());
                shoppingCartItem.setProductCode(item.getProduct().getSku());
                
                shoppingCartItem.setProductId(item.getProductId());
                shoppingCartItem.setId(item.getId());
                shoppingCartItem.setName(item.getProduct().getProductDescription().getName());
                shoppingCartItem.setPrice(pricingService.getDisplayAmount(item.getItemPrice(),store));
                shoppingCartItem.setQuantity(item.getQuantity());
                shoppingCartItem.setProductPrice(item.getItemPrice());
                shoppingCartItem.setSubTotal(pricingService.getDisplayAmount(item.getSubTotal(), store));
                ProductImage image = item.getProduct().getProductImage();
                if(image!=null) {
                    String imagePath = ImageFilePathUtils.buildProductImageFilePath(store, item.getProduct().getSku(), image.getProductImage());
                    shoppingCartItem.setImage(imagePath);
                }
                Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem> attributes = item.getAttributes();
                if(attributes!=null) {
                    List<ShoppingCartAttribute> cartAttributes = new ArrayList<ShoppingCartAttribute>();
                    for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem attribute : attributes) {
                        ShoppingCartAttribute cartAttribute = new ShoppingCartAttribute();
                        cartAttribute.setId(attribute.getId());
                        cartAttribute.setAttributeId(attribute.getProductAttributeId());
                        cartAttribute.setOptionId(attribute.getProductAttribute().getProductOption().getId());
                        cartAttribute.setOptionValueId(attribute.getProductAttribute().getProductOptionValue().getId());
                        cartAttribute.setOptionName(attribute.getProductAttribute().getProductOption().getDescriptionsList().get(0).getName());
                        cartAttribute.setOptionValue(attribute.getProductAttribute().getProductOptionValue().getDescriptionsList().get(0).getName());
                        cartAttributes.add(cartAttribute);
                    }
                    shoppingCartItem.setShoppingCartAttributes(cartAttributes);
                }
                shoppingCartItemsList.add(shoppingCartItem);
            }
        }
        if(CollectionUtils.isNotEmpty(shoppingCartItemsList)){
            cart.setShoppingCartItems(shoppingCartItemsList);
        }
        
        OrderSummary summary = new OrderSummary();
        List<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> productsList = new ArrayList<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem>();
        productsList.addAll(shoppingCart.getLineItems());
        summary.setProducts(productsList);
        OrderTotalSummary orderSummary = orderService.caculateOrderTotal(summary, store, language);
        cart.setSubTotal(pricingService.getDisplayAmount(orderSummary.getSubTotal(), store));
        cart.setTotal(pricingService.getDisplayAmount(orderSummary.getTotal(), store));
        cart.setQuantity(shoppingCart.getLineItems().size());
        }
        catch(ServiceException ex){
          Log.error( "Error while converting cart Model to cart Data.."+ex );
          throw new ConversionException( "Unable to create cart data", ex );
        }
        return cart;
        
    }

    @Override
    protected ShoppingCartData createTarget()
    {
      return this.targetBean.get();  
    }
    
    private Object getKeyValue(final String key){
       ServletRequestAttributes reqAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
       return reqAttr.getRequest().getAttribute( key );
    }
}
