package com.salesmanager.web.entity.catalog.product.attribute;

import java.io.Serializable;
import java.util.List;

public class PersistableProductOptionValue extends ProductOptionValueEntity
		implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ProductOptionValueDescription> description;

	public void setDescription(List<ProductOptionValueDescription> description) {
		this.description = description;
	}

	public List<ProductOptionValueDescription> getDescription() {
		return description;
	}

}
