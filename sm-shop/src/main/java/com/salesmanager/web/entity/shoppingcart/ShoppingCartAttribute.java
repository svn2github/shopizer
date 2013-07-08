package com.salesmanager.web.entity.shoppingcart;

import java.io.Serializable;

public class ShoppingCartAttribute implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long optionId;
	private long optionValueId;
	private String optionName;
	private String optionValue;
	public long getOptionId() {
		return optionId;
	}
	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}
	public long getOptionValueId() {
		return optionValueId;
	}
	public void setOptionValueId(long optionValueId) {
		this.optionValueId = optionValueId;
	}
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
}
