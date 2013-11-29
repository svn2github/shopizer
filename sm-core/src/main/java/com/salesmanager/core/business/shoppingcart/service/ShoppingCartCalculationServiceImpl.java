/**
 * 
 */
package com.salesmanager.core.business.shoppingcart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.service.OrderServiceImpl;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
/**
 * <p>Implementation class responsible for calculating state of shopping cart.
 * This class will take care of calculating price of each line items of shopping cart
 * as well any discount including sub-total and total amount.
 * </p>
 * 
 * @author Umesh Awasthi
 * @version 1.2
 */
@Service("shoppingCartCalculationService")
public class ShoppingCartCalculationServiceImpl implements ShoppingCartCalculationService
{


    protected  final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private PricingService pricingService;

    @Autowired
    private ShoppingCartService shoppingCartService;



    /**
     * <p>Method used to recalculate state of shopping cart every time any change has been made
     * to underlying {@link ShoppingCart} object in DB.</p>
     * Following operations will be performed by this method.
     * 
     * <li>Calculate price for each {@link ShoppingCartItem} and update it. </li>
     * <p>
     * This method is backbone method for all price calculation related to shopping cart.</p>
     * 
     * @see OrderServiceImpl
     * 
     * @param cartModel
     * @param customer
     * @param store
     * @param language
     * @throws ServiceException
     */
    @Override
    public void calculate( final ShoppingCart cartModel ,final Customer customer, final MerchantStore store, final Language language ) throws ServiceException
    {

        Validate.notNull(cartModel,"cart cannot be null");
        Validate.notNull(cartModel.getLineItems(),"Cart should have line items.");
        Validate.notNull(store,"MerchantStore cannot be null");
        Validate.notNull(customer,"Customer cannot be null");
        calculatePrice(cartModel,customer);


    }


    /**
     * <p>Method used to recalculate state of shopping cart every time any change has been made
     * to underlying {@link ShoppingCart} object in DB.</p>
     * Following operations will be performed by this method.
     * 
     * <li>Calculate price for each {@link ShoppingCartItem} and update it. </li>
     * <p>
     * This method is backbone method for all price calculation related to shopping cart.</p>
     * 
     * @see OrderServiceImpl
     * 
     * @param cartModel
     * @param store
     * @param language
     * @throws ServiceException
     */
    @Override
    public void calculate( final ShoppingCart cartModel , final MerchantStore store, final Language language ) throws ServiceException
    {

        Validate.notNull(cartModel,"cart cannot be null");
        Validate.notNull(cartModel.getLineItems(),"Cart should have line items.");
        Validate.notNull(store,"MerchantStore cannot be null");
        calculatePrice(cartModel,null);


    }


    /**
     * <p>Method used to recalculate state of shopping cart every time any change has been made
     * to underlying {@link ShoppingCart} object in DB.</p>
     * Following operations will be performed by this method.
     * 
     * <li>Calculate price for each {@link ShoppingCartItem} and update it. </li>
     * <p>
     * This method is backbone method for all price calculation related to shopping cart.
     * 
     * </p>
     * 
     * @see OrderServiceImpl
     * 
     * @param cartModel
     * @param store
     * @param language
     * @throws ServiceException
     */

    @Override
    public OrderTotalSummary calculateCartTotal( final ShoppingCart cartModel, final MerchantStore store, final Language language )
                    throws ServiceException
                    {
        Validate.notNull(cartModel,"cart cannot be null");
        Validate.notNull(cartModel.getLineItems(),"Cart should have line items.");
        Validate.notNull(store,"MerchantStore cannot be null");
        return calculateCartTotal(cartModel,null,store,language);


                    }



    /**
     * <p>Method used to calculate total as well sub-total for {@link ShoppingCart}.
     * It will iterate through each line item in ShoppingCart and will
     * calculate total and sub-total.
     * </p>
     * <p>
     * Actual total which includes Shipping and Taxes will not be a part of this service and will
     * be calculated at time of checkout.
     * </p>
     * 
     * @param cartModel {@link ShoppingCart}
     * @param customer {@link Customer}
     * @param store {@link MerchantStore}
     * @param language {@link Language}
     * @throws ServiceException
     */
    @Override
    public OrderTotalSummary calculateCartTotal(final ShoppingCart cartModel ,final Customer customer, final MerchantStore store, final Language language) throws ServiceException{

        LOG.info( "Calculating Cart total amount " );
        OrderTotalSummary totalSummary = new OrderTotalSummary();
        BigDecimal subTotal = new BigDecimal(0);
        for(ShoppingCartItem item : cartModel.getLineItems()) {

            BigDecimal itemSubTotal = item.getItemPrice().multiply(new BigDecimal(item.getQuantity()));
            subTotal = subTotal.add(itemSubTotal);
        }
        totalSummary.setSubTotal(subTotal);
        // since Shopping cart do not have any shipping and tax calculations,total and subtotal will be same
        totalSummary.setTotal( subTotal );
        return totalSummary;

    }




    private void calculatePrice(final ShoppingCart cartModel,final Customer customer) throws ServiceException{
        BigDecimal cartSubTotal = new BigDecimal(0);
        if(CollectionUtils.isNotEmpty( cartModel.getLineItems() )){
            List<ProductAttribute> productAttributeList=new ArrayList<ProductAttribute>();
            LOG.info( "Found items in cart. starting to calculate final price" );
            for(ShoppingCartItem shoppingCartItem : cartModel.getLineItems()){
                productAttributeList.addAll( shoppingCartItem.getProduct().getAttributes() );
                FinalPrice finalPrice=pricingService.calculateProductPrice( shoppingCartItem.getProduct(), productAttributeList, customer );
                BigDecimal itemSubTotal = finalPrice.getFinalPrice().multiply(new BigDecimal(shoppingCartItem.getQuantity()));
                shoppingCartItem.setItemPrice( finalPrice.getFinalPrice() );
                shoppingCartItem.setSubTotal( itemSubTotal );
                cartSubTotal = cartSubTotal.add(itemSubTotal);
            }
        }

        shoppingCartService.saveOrUpdate( cartModel );
    }




    public PricingService getPricingService()
    {
        return pricingService;
    }


    public void setPricingService( final PricingService pricingService )
    {
        this.pricingService = pricingService;
    }


    public ShoppingCartService getShoppingCartService()
    {
        return shoppingCartService;
    }




}
