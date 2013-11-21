/**
 * 
 */
package com.salesmanager.web.shop.controller.shoppingCart.facade;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartItem;

/**
 * </p>Shopping cart Facade which provide abstraction layer between 
 * SM core module and Controller.
 * Only Data Object will be exposed to controller by hiding model
 * object from view.</p>
 * @author Umesh Awasthi
 * @version 1.2
 * @since1.2
 *
 */


public interface ShoppingCartFacade {
	
	/**
	 * Method responsible for getting shopping cart from
	 * either session or from underlying DB.
	 */
    public ShoppingCartData getShoppingCartData(final Customer customer,final  MerchantStore store,final String shoppingCartId) throws Exception;
    public ShoppingCartData getShoppingCartData(final ShoppingCart shoppingCart) throws Exception;
    public ShoppingCartData recalculateCart( ShoppingCartData shoppingCart) throws Exception ;
    public ShoppingCart getShoppingCart(final ShoppingCartData shoppingCart) throws Exception;
    public ShoppingCartData addItemsToShoppingCart(ShoppingCartData shoppingCart,final ShoppingCartItem item, final MerchantStore store) throws Exception;
    public void removeCartItem(final Long itemID, final String cartId) throws Exception;

}
