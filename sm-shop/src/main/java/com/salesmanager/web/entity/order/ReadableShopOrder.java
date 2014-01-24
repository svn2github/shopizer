package com.salesmanager.web.entity.order;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.core.business.shipping.model.ShippingOption;
import com.salesmanager.core.business.shipping.model.ShippingSummary;

public class ReadableShopOrder extends ReadableOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ShippingSummary shippingSummary;
	private ShippingOption selectedShippingOption = null;//Default selected option

	
	private String errorMessage = null;//global error message
	
	private List<ReadableOrderTotal> subTotals;//order calculation
	
	private String grandTotal;//grand total - order calculation
	
	
	public ShippingSummary getShippingSummary() {
		return shippingSummary;
	}
	public void setShippingSummary(ShippingSummary shippingSummary) {
		this.shippingSummary = shippingSummary;
	}
	public ShippingOption getSelectedShippingOption() {
		return selectedShippingOption;
	}
	public void setSelectedShippingOption(ShippingOption selectedShippingOption) {
		this.selectedShippingOption = selectedShippingOption;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(String grandTotal) {
		this.grandTotal = grandTotal;
	}
	public List<ReadableOrderTotal> getSubTotals() {
		return subTotals;
	}
	public void setSubTotals(List<ReadableOrderTotal> subTotals) {
		this.subTotals = subTotals;
	}

}
