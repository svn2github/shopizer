package com.salesmanager.core.business.customer.model.attribute;

import javax.persistence.AssociationOverrides;
import javax.persistence.AssociationOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.TableGenerator;
import javax.validation.Valid;

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

	@Valid
	private CustomerOptionSetId pk = new CustomerOptionSetId();
	
	@Column(name = "CUSTOMER_OPTIONSET_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUST_OPTSET_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	


	private int sortOrder;
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
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
