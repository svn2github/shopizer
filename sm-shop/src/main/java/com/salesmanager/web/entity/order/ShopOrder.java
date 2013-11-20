package com.salesmanager.web.entity.order;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;

/**
 * Orders persisted on the website
 * @author Carl Samson
 *
 */
public class ShopOrder extends PersistableOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ShoppingCartItem> shoppingCartItems;//overrides parent API list of shoppingcartitem
	private Customer shoppingCartCustomer;//overrides parent API PersistableCustomer
	
	
	public void setShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
		this.shoppingCartItems = shoppingCartItems;
	}
	public List<ShoppingCartItem> getShoppingCartItems() {
		return shoppingCartItems;
	}
	public void setShoppingCartCustomer(Customer shoppingCartCustomer) {
		this.shoppingCartCustomer = shoppingCartCustomer;
	}
	public Customer getShoppingCartCustomer() {
		return shoppingCartCustomer;
	}

}
