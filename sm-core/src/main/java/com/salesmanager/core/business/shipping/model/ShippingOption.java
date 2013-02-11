package com.salesmanager.core.business.shipping.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ShippingOption implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal optionPrice;
	private String optionName;
	private String optionCode;
	private Date optionDeliveryDate;
	public void setOptionPrice(BigDecimal optionPrice) {
		this.optionPrice = optionPrice;
	}
	public BigDecimal getOptionPrice() {
		return optionPrice;
	}
	public void setOptionDeliveryDate(Date optionDeliveryDate) {
		this.optionDeliveryDate = optionDeliveryDate;
	}
	public Date getOptionDeliveryDate() {
		return optionDeliveryDate;
	}
	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}
	public String getOptionCode() {
		return optionCode;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public String getOptionName() {
		return optionName;
	}

}
