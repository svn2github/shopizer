package com.salesmanager.core.business.tax.model;

import com.salesmanager.core.business.common.model.OrderTotalItem;

public class TaxItem extends OrderTotalItem {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String label;

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}


}
