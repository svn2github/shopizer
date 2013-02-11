package com.salesmanager.core.business.shipping.model;

import java.io.Serializable;
import java.util.List;

public class ShippingQuote implements Serializable {
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String NO_SHIPPING_TO_SELECTED_COUNTRY = "NO_SHIPPING_TO_SELECTED_COUNTRY";
	public final static String NO_SHIPPING_MODULE_CONFIGURED= "NO_SHIPPING_MODULE_CONFIGURED";

	private String shippingModuleCode;
	private List<ShippingOption> shippingOptions;
	private String shippingReturnCode = null;
	
	
	
	
	public void setShippingOptions(List<ShippingOption> shippingOptions) {
		this.shippingOptions = shippingOptions;
	}
	public List<ShippingOption> getShippingOptions() {
		return shippingOptions;
	}
	public void setShippingModuleCode(String shippingModuleCode) {
		this.shippingModuleCode = shippingModuleCode;
	}
	public String getShippingModuleCode() {
		return shippingModuleCode;
	}
	public void setShippingReturnCode(String shippingReturnCode) {
		this.shippingReturnCode = shippingReturnCode;
	}
	public String getShippingReturnCode() {
		return shippingReturnCode;
	}
	
	

}
