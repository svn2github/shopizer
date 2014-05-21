package com.salesmanager.web.entity.order;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.web.entity.customer.PersistableCustomer;

public class PersistableOrder extends OrderEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PersistableCustomer customer;//might already exist if id > 0, otherwise persist
	private List<PersistableOrderProduct> orderProductItems;
	private boolean shipToBillingAdress = true;
	
	
	public void setOrderProductItems(List<PersistableOrderProduct> orderProductItems) {
		this.orderProductItems = orderProductItems;
	}
	public List<PersistableOrderProduct> getOrderProductItems() {
		return orderProductItems;
	}
	public void setCustomer(PersistableCustomer customer) {
		this.customer = customer;
	}
	public PersistableCustomer getCustomer() {
		return customer;
	}
	public boolean isShipToBillingAdress() {
		return shipToBillingAdress;
	}
	public void setShipToBillingAdress(boolean shipToBillingAdress) {
		this.shipToBillingAdress = shipToBillingAdress;
	}



}
