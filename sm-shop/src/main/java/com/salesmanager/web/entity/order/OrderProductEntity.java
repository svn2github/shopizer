package com.salesmanager.web.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderProductEntity extends OrderProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int orderedQuantity;
	private BigDecimal price;
	
	public void setOrderedQuantity(int orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}
	public int getOrderedQuantity() {
		return orderedQuantity;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
