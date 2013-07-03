package com.salesmanager.web.entity.shoppingcart;

import java.io.Serializable;

public class ShoppingCart implements Serializable {
	
	private String message;
	private String code;
	private int quantity;
	private String total;
	
	private ShoppingCartItem[] shoppingCartItems;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public void setShoppingCartItems(ShoppingCartItem[] shoppingCartItems) {
		this.shoppingCartItems = shoppingCartItems;
	}
	public ShoppingCartItem[] getShoppingCartItems() {
		return shoppingCartItems;
	}

}
