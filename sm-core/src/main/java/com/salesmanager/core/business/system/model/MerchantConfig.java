package com.salesmanager.core.business.system.model;

import java.io.Serializable;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class MerchantConfig implements Serializable, JSONAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean displayCustomerSection;

	@SuppressWarnings("unchecked")
	@Override
	public String toJSONString() {
		JSONObject data = new JSONObject();
		data.put("displayCustomerSection", this.isDisplayCustomerSection());
		return data.toJSONString();
	}

	public void setDisplayCustomerSection(boolean displayCustomerSection) {
		this.displayCustomerSection = displayCustomerSection;
	}

	public boolean isDisplayCustomerSection() {
		return displayCustomerSection;
	}

}
