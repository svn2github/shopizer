package com.salesmanager.core.business.customer.model.attribute;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table(name="CUSTOMER_OPTION_SET", schema=SchemaConstant.SALESMANAGER_SCHEMA)
@IdClass(CustomerOptionSetId.class)
//@AssociationOverrides({
//        @AssociationOverride(name = "pk.customerOption", 
//            joinColumns = @JoinColumn(name = "CUSTOMER_OPTION_ID")),
//        @AssociationOverride(name = "pk.customerOptionValue", 
//            joinColumns = @JoinColumn(name = "CUSTOMER_OPTION_VALUE_ID")) })

public class CustomerOptionSet extends SalesManagerEntity<Long, CustomerOptionSet> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//@Valid
	//private CustomerOptionSetId pk = new CustomerOptionSetId();
	
	//@Column(name = "CUSTOMER_OPTIONSET_ID", unique=true, nullable=false)
	//@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUST_OPTSET_SEQ_NEXT_VAL")
	//@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Id
	private CustomerOption customerOption = null;

	@Id
	private CustomerOptionValue customerOptionValue = null;
	


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

	
	//@EmbeddedId
	//public CustomerOptionSetId getPk() {
	//	return pk;
	//}

	//public void setPk(CustomerOptionSetId pk) {
	//	this.pk = pk;
	//}

	public void setCustomerOptionValue(CustomerOptionValue customerOptionValue) {
		this.customerOptionValue = customerOptionValue;
	}

	public CustomerOptionValue getCustomerOptionValue() {
		return customerOptionValue;
	}

	public void setCustomerOption(CustomerOption customerOption) {
		this.customerOption = customerOption;
	}

	public CustomerOption getCustomerOption() {
		return customerOption;
	}


}
