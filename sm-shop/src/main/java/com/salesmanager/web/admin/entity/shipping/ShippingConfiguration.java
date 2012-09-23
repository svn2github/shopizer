package com.salesmanager.web.admin.entity.shipping;

import java.io.Serializable;

import com.salesmanager.core.constants.ShippingConstants;

public class ShippingConfiguration implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2083704239395795541L;
	
	
	private String mode = ShippingConstants.SHIPPING_NATIONAL;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}



}
