package com.salesmanager.core.business.customer.model.attribute;

import javax.persistence.AssociationOverrides;
import javax.persistence.AssociationOverride;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table(name="CUSTOMER_OPTION_OPT_VALUE", schema=SchemaConstant.SALESMANAGER_SCHEMA)
@AssociationOverrides({
        @AssociationOverride(name = "pk.customerOption", 
            joinColumns = @JoinColumn(name = "CUSTOMER_OPTION_ID")),
        @AssociationOverride(name = "pk.customerOptionValue", 
            joinColumns = @JoinColumn(name = "CUSTOMER_OPTION_VALUE_ID")) })

public class CustomerOptionSet extends SalesManagerEntity<Long, CustomerOptionSet> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CustomerOptionSetId pk = new CustomerOptionSetId();
	


	private int sortOrder;
	
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@EmbeddedId
	public CustomerOptionSetId getPk() {
		return pk;
	}

	public void setPk(CustomerOptionSetId pk) {
		this.pk = pk;
	}


}
