package com.salesmanager.core.business.order.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;


/**
 * This object is used as input object for many services
 * such as order total calculation and tax calculation
 * @author casams1
 *
 */
public class OrderSummary implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal shipping;
	private List<ShoppingCartItem> products;
	public void setShipping(BigDecimal shipping) {
		this.shipping = shipping;
	}
	public BigDecimal getShipping() {
		return shipping;
	}
	public void setProducts(List<ShoppingCartItem> products) {
		this.products = products;
	}
	public List<ShoppingCartItem> getProducts() {
		return products;
	}

}
