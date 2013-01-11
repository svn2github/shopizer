package com.salesmanager.core.business.catalog.product.model.attribute;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table(name="PRODUCT_ATTRIBUTE", schema=SchemaConstant.SALESMANAGER_SCHEMA,
	uniqueConstraints={
		@UniqueConstraint(columnNames={
				"OPTION_ID",
				"OPTION_VALUE_ID",
				"PRODUCT_ID"
			})
	}
)
public class ProductAttribute extends SalesManagerEntity<Long, ProductAttribute> {
	private static final long serialVersionUID = -6537491946539803265L;
	
	@Id
	@Column(name = "PRODUCT_ATTRIBUTE_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_ATTR_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column(name="PRODUCT_ATRIBUTE_PRICE")
	private BigDecimal optionValuePrice;

	@Column(name="PRODUCT_ATTRIBUTE_SORT_ORD")
	private Integer productOptionSortOrder;
	
	@Column(name="PRODUCT_ATTRIBUTE_FREE")
	private Boolean productAttributeIsFree;
	
	@Column(name="PRODUCT_ATTRIBUTE_WEIGHT")
	private BigDecimal productAttributeWeight;
	
	@Column(name="PRODUCT_ATTRIBUTE_DEFAULT")
	private Boolean attributeDefault=false;
	
	@Column(name="PRODUCT_ATTRIBUTE_REQUIRED")
	private Boolean attributeRequired=false;
	
	@Column(name="PRODUCT_ATTRIBUTE_FOR_DISP")
	private Boolean attributeDisplayOnly=false;
	
	@Column(name="PRODUCT_ATTRIBUTE_DISCOUNTED")
	private Boolean attributeDiscounted=false;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="OPTION_ID", nullable=false)
	private ProductOption productOption;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name="OPTION_VALUE_ID", nullable=false)
	private ProductOptionValue productOptionValue;
	
	@Transient
	private String attributePrice;
	
	public String getAttributePrice() {
		return attributePrice;
	}

	public void setAttributePrice(String attributePrice) {
		this.attributePrice = attributePrice;
	}

	@ManyToOne(targetEntity = Product.class)
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	private Product product;
	
	public ProductAttribute() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getOptionValuePrice() {
		return optionValuePrice;
	}

	public void setOptionValuePrice(BigDecimal optionValuePrice) {
		this.optionValuePrice = optionValuePrice;
	}

	public Integer getProductOptionSortOrder() {
		return productOptionSortOrder;
	}

	public void setProductOptionSortOrder(Integer productOptionSortOrder) {
		this.productOptionSortOrder = productOptionSortOrder;
	}

	public Boolean getProductAttributeIsFree() {
		return productAttributeIsFree;
	}

	public void setProductAttributeIsFree(Boolean productAttributeIsFree) {
		this.productAttributeIsFree = productAttributeIsFree;
	}

	public BigDecimal getProductAttributeWeight() {
		return productAttributeWeight;
	}

	public void setProductAttributeWeight(BigDecimal productAttributeWeight) {
		this.productAttributeWeight = productAttributeWeight;
	}

	public Boolean getAttributeDefault() {
		return attributeDefault;
	}

	public void setAttributeDefault(Boolean attributeDefault) {
		this.attributeDefault = attributeDefault;
	}

	public Boolean getAttributeRequired() {
		return attributeRequired;
	}

	public void setAttributeRequired(Boolean attributeRequired) {
		this.attributeRequired = attributeRequired;
	}

	public Boolean getAttributeDisplayOnly() {
		return attributeDisplayOnly;
	}

	public void setAttributeDisplayOnly(Boolean attributeDisplayOnly) {
		this.attributeDisplayOnly = attributeDisplayOnly;
	}

	public Boolean getAttributeDiscounted() {
		return attributeDiscounted;
	}

	public void setAttributeDiscounted(Boolean attributeDiscounted) {
		this.attributeDiscounted = attributeDiscounted;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}


}
