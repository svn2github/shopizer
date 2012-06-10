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

import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table (name="ORDER_PRODUCT_PRICE" , schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class OrderProductPrice implements Serializable {
	private static final long serialVersionUID = 3734737890163564311L;

	@Id
	@Column (name="ORDER_PRODUCT_PRICE_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "ORDER_PRD_PRICE_ID_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ORDER_PRODUCT_ID", nullable = false)
	private OrderProduct orderProduct;

	@Column(name = "PRODUCT_PRICE_TYPE_ID", nullable = false)
	private int productPriceTypeId;

	@Column(name = "PRODUCT_PRICE_MODULE_NAME", nullable = false , length=64 )
	private String productPriceModuleName;

	@Column(name = "PRODUCT_PRICE_AMOUNT", nullable = false ,  precision=15 )
	private BigDecimal productPriceAmount;

	@Column(name = "DEFAULT_PRICE", nullable = false)
	private Boolean defaultPrice;

	@Column(name = "PRODUCT_HAS_TAX", nullable = false)
	private Boolean productHasTax;

	@Column(name = "PRODUCT_PRICE_NAME", nullable = false)
	private String productPriceName;
	
	public OrderProductPrice() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getProductPriceTypeId() {
		return productPriceTypeId;
	}

	public void setProductPriceTypeId(int productPriceTypeId) {
		this.productPriceTypeId = productPriceTypeId;
	}

	public String getProductPriceModuleName() {
		return productPriceModuleName;
	}

	public void setProductPriceModuleName(String productPriceModuleName) {
		this.productPriceModuleName = productPriceModuleName;
	}

	public BigDecimal getProductPriceAmount() {
		return productPriceAmount;
	}

	public void setProductPriceAmount(BigDecimal productPriceAmount) {
		this.productPriceAmount = productPriceAmount;
	}

	public Boolean getDefaultPrice() {
		return defaultPrice;
	}

	public void setDefaultPrice(Boolean defaultPrice) {
		this.defaultPrice = defaultPrice;
	}

	public Boolean getProductHasTax() {
		return productHasTax;
	}

	public void setProductHasTax(Boolean productHasTax) {
		this.productHasTax = productHasTax;
	}

	public String getProductPriceName() {
		return productPriceName;
	}

	public void setProductPriceName(String productPriceName) {
		this.productPriceName = productPriceName;
	}

	public OrderProduct getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(OrderProduct orderProduct) {
		this.orderProduct = orderProduct;
	}
}
