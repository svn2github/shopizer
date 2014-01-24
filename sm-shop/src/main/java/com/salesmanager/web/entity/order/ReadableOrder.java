package com.salesmanager.web.entity.order;

import java.io.Serializable;

import com.salesmanager.web.entity.customer.ReadableCustomer;

public class ReadableOrder extends OrderEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ReadableCustomer customer;
	public void setCustomer(ReadableCustomer customer) {
		this.customer = customer;
	}
	public ReadableCustomer getCustomer() {
		return customer;
	}
	public OrderTotal getTotal() {
		return total;
	}
	public void setTotal(OrderTotal total) {
		this.total = total;
	}
	private OrderTotal total;

}
