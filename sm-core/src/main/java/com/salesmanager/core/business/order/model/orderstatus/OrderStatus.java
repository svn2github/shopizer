package com.salesmanager.core.business.order.model.orderstatus;

public enum OrderStatus {
	
	ORDERED("ordered"),
	SHIPPED("shipped"),
	PREPARED("prepared")
	;
	
	private String value;
	
	private OrderStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
