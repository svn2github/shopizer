/**
 * 
 */
package com.salesmanager.web.facade.shoppingCart;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;

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
	public ShoppingCartData getShoppingCartData(final Customer customer,final  MerchantStore store,final Language language,final String shoppingCartId);

}
