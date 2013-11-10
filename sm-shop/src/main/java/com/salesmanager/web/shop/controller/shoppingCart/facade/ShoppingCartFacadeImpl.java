/**
 * 
 */
package com.salesmanager.web.shop.controller.shoppingCart.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartItem;
import com.salesmanager.web.populator.shoppingCart.ShoppingCartDataPopulator;

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
    private ProductPriceUtils productPriceUtils;

    @Autowired
    private ProductService productService;

    @Override
    public ShoppingCartData getShoppingCartData( final Customer customer, final MerchantStore store,
                                                 final Language language, final String shoppingCartId )
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
        return new ShoppingCartDataPopulator().populateFromEntity(cart, new ShoppingCartData(), store, language);

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

}
