package com.salesmanager.web.entity.order;

import java.io.Serializable;

import com.salesmanager.web.entity.customer.PersistableCustomer;

public class PersistableOrder extends OrderEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PersistableCustomer customer;

}
