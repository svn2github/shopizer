package com.salesmanager.web.entity.order;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.web.entity.customer.ReadableCustomer;

public class ReadableOrder extends OrderEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ReadableCustomer customer;
	private List<OrderProduct> items;
	public void setItems(List<OrderProduct> items) {
		this.items = items;
	}
	public List<OrderProduct> getItems() {
		return items;
	}
	public void setCustomer(ReadableCustomer customer) {
		this.customer = customer;
	}
	public ReadableCustomer getCustomer() {
		return customer;
	}

}
