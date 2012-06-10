package com.salesmanager.core.business.order.model.orderproduct;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table (name="ORDER_PRODUCT_ATTRIBUTE" , schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class OrderProductAttribute implements Serializable {
	private static final long serialVersionUID = 6037571119918073015L;

	@Id
	@Column (name="ORDER_PRODUCT_ATTRIBUTE_ID", nullable=false , unique=true )
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ORDER_PRODUCT_ATTR_ID_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column ( name= "OPTION_VALUES_PRICE" , nullable=false , precision=15 , scale=4 )
	private BigDecimal optionValuePrice;

	@Column ( name= "PRODUCT_ATTRIBUTE_IS_FREE" , nullable=false )
	private boolean productAttributeIsFree;

	@Column ( name= "PRODUCT_ATTRIBUTE_WEIGHT" , precision=15 , scale=4 )
	private java.math.BigDecimal productAttributeWeight;
	
	@ManyToOne
	@JoinColumn(name = "ORDER_PRODUCT_ID", nullable = false)
	private OrderProduct orderProduct;
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT_OPTION_ID", nullable = false)
	private ProductOption productOption;

	@ManyToOne
	@JoinColumn(name = "PRODUCT_OPTIONS_VALUE_ID", nullable = false)
	private ProductOptionValue productOptionValue;

	public OrderProductAttribute() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getOptionValuePrice() {
		return optionValuePrice;
	}

	public void setOptionValuePrice(BigDecimal optionValuePrice) {
		this.optionValuePrice = optionValuePrice;
	}

	public boolean isProductAttributeIsFree() {
		return productAttributeIsFree;
	}

	public void setProductAttributeIsFree(boolean productAttributeIsFree) {
		this.productAttributeIsFree = productAttributeIsFree;
	}

	public java.math.BigDecimal getProductAttributeWeight() {
		return productAttributeWeight;
	}

	public void setProductAttributeWeight(
			java.math.BigDecimal productAttributeWeight) {
		this.productAttributeWeight = productAttributeWeight;
	}

	public OrderProduct getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(OrderProduct orderProduct) {
		this.orderProduct = orderProduct;
	}

	public ProductOption getProductOption() {
		return productOption;
	}

	public void setProductOption(ProductOption productOption) {
		this.productOption = productOption;
	}

	public ProductOptionValue getProductOptionValue() {
		return productOptionValue;
	}

	public void setProductOptionValue(ProductOptionValue productOptionValue) {
		this.productOptionValue = productOptionValue;
	}
}
