package com.salesmanager.web.entity.shoppingcart;

import java.io.Serializable;

public class ShoppingCartItem implements Serializable {
	
	private String name;
	private String price;
	private int quantity;
	private long id;
	
	private ShoppingCartAttribute[] shoppingCartAttributes;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setShoppingCartAttributes(ShoppingCartAttribute[] shoppingCartAttributes) {
		this.shoppingCartAttributes = shoppingCartAttributes;
	}
	public ShoppingCartAttribute[] getShoppingCartAttributes() {
		return shoppingCartAttributes;
	}


}
