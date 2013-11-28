package com.salesmanager.core.business.order.model.orderstatus;

public enum OrderStatus {
	
	ORDERED("ordered"),
	PAID("paid"),
	SHIPPED("shipped"),
	PREPARED("prepared"),
	REFUNDED("refunded"),
	DELIVERED("delivered")
	;
	
	private String value;
	
	private OrderStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
