/**
 * 
 */
package com.salesmanager.core.business.shoppingcart.model;

import java.util.Set;


import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

/**
 * <p>Shopping cart is responsible for storing and carrying
 * shopping cart information.Shopping Cart consists of {@link ShoppingCartItem}
 * which represents individual lines items associated with the shopping cart</p> 
 * @author Umesh Awasthi
 * version 2.0
 *
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "SHOPPING_CART", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class ShoppingCart extends SalesManagerEntity<Long, ShoppingCart> implements Auditable{

	
	private static final long serialVersionUID = 1L;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	@Id
	@Column(name = "SHP_CART_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "SHP_CRT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	/**
	 * Will be used to fetch shopping cart model from the controller
	 * this code will also be stored in the ShoppingCartData which will be
	 * used in the UI.
	 * 
	 */
	@Column(name = "SHP_CART_CODE", unique=true, nullable=false)
	private String shoppingCartCode;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "shoppingCart")
	private Set<ShoppingCartItem> lineItems;
	
	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(name = "CUSTOMER_ID", nullable = false)
	private Customer customer;
    
	@Override
	public AuditSection getAuditSection() {
		return auditSection;
	}

	@Override
	public void setAuditSection(AuditSection audit) {
		this.auditSection = audit;
		
	}
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
	}

	public Set<ShoppingCartItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(Set<ShoppingCartItem> lineItems) {
		this.lineItems = lineItems;
	}

    public String getShoppingCartCode()
    {
        return shoppingCartCode;
    }

    public void setShoppingCartCode( String shoppingCartCode )
    {
        this.shoppingCartCode = shoppingCartCode;
    }
	
	

}
