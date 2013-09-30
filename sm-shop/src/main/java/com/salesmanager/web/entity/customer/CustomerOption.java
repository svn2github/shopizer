package com.salesmanager.web.entity.customer;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.web.entity.ShopEntity;

public class CustomerOption extends ShopEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private String name;
	
	private List<Long> values;
	private String value;
	private Long idValue;



	private CustomerOptionValue defaultValue;

	
	private List<CustomerOptionValue> availableValues;


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CustomerOptionValue getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(CustomerOptionValue defaultValue) {
		this.defaultValue = defaultValue;
	}

	public List<CustomerOptionValue> getAvailableValues() {
		return availableValues;
	}

	public void setAvailableValues(List<CustomerOptionValue> availableValues) {
		this.availableValues = availableValues;
	}



	public void setIdValue(Long idValue) {
		this.idValue = idValue;
	}

	public Long getIdValue() {
		return idValue;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValues(List<Long> values) {
		this.values = values;
	}

	public List<Long> getValues() {
		return values;
	}





}
