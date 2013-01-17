package com.salesmanager.core.business.order.model.orderproduct;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.utils.CloneUtils;

@Entity
@Table (name="ORDER_PRODUCTS" , schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class OrderProduct extends SalesManagerEntity<Long, OrderProduct> {
	private static final long serialVersionUID = 176131742783954627L;
	
	@Id
	@Column (name="ORDER_PRODUCT_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ORDER_PRODUCT_ID_NEXT_VALUE")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column (name="PRODUCT_SKU")
	private String sku;

	@Column (name="PRODUCT_NAME" , length=64 , nullable=false)
	private String productName;

	@Column (name="PRODUCT_QUANTITY")
	private int productQuantity;

	@Column (name="ONETIME_CHARGE" , precision=15, scale=4, nullable=false )
	private BigDecimal onetimeCharge;//

	@Column (name="PRODUCT_SPECIAL_PRICE" , precision=15, scale=4 )
	private BigDecimal productSpecialPrice;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name="PRODUCT_SPECIAL_DATE_AVAILABLE" , length=0)
	private Date productSpecialDateAvailable;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name="PRODUCT_SPECIAL_DATE_EXPIRE" , length=0 )
	private Date productSpecialDateExpire;
	
	@Column (name="FINAL_PRICE" , precision=15, scale=4, nullable=false )
	private BigDecimal finalPrice;
	
	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_ID", nullable = false)
	private Order order;

	@OneToMany(mappedBy = "orderProduct", cascade = CascadeType.ALL)
	private Set<OrderProductAttribute> orderAttributes = new HashSet<OrderProductAttribute>();

	@OneToMany(mappedBy = "orderProduct", cascade = CascadeType.ALL)
	private Set<OrderProductPrice> prices = new HashSet<OrderProductPrice>();

	@OneToMany(mappedBy = "orderProduct", cascade = CascadeType.ALL)
	private Set<OrderProductDownload> downloads = new HashSet<OrderProductDownload>();	// all product prices
	
	public OrderProduct() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	public BigDecimal getOnetimeCharge() {
		return onetimeCharge;
	}

	public void setOnetimeCharge(BigDecimal onetimeCharge) {
		this.onetimeCharge = onetimeCharge;
	}



	public Date getProductSpecialDateAvailable() {
		return CloneUtils.clone(productSpecialDateAvailable);
	}

	public void setProductSpecialDateAvailable(Date productSpecialDateAvailable) {
		this.productSpecialDateAvailable = CloneUtils.clone(productSpecialDateAvailable);
	}

	public Date getProductSpecialDateExpire() {
		return CloneUtils.clone(productSpecialDateExpire);
	}

	public void setProductSpecialDateExpire(Date productSpecialDateExpire) {
		this.productSpecialDateExpire = CloneUtils.clone(productSpecialDateExpire);
	}



	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}


	public Set<OrderProductAttribute> getOrderAttributes() {
		return orderAttributes;
	}

	public void setOrderAttributes(Set<OrderProductAttribute> orderAttributes) {
		this.orderAttributes = orderAttributes;
	}

	public Set<OrderProductPrice> getPrices() {
		return prices;
	}

	public void setPrices(Set<OrderProductPrice> prices) {
		this.prices = prices;
	}

	public Set<OrderProductDownload> getDownloads() {
		return downloads;
	}

	public void setDownloads(Set<OrderProductDownload> downloads) {
		this.downloads = downloads;
	}

	public void setProductSpecialPrice(BigDecimal productSpecialPrice) {
		this.productSpecialPrice = productSpecialPrice;
	}

	public BigDecimal getProductSpecialPrice() {
		return productSpecialPrice;
	}

	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}

	public BigDecimal getFinalPrice() {
		return finalPrice;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getSku() {
		return sku;
	}
	
}
