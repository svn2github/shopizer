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

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.tax.model.taxclass.TaxClass;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.utils.CloneUtils;

@Entity
@Table (name="ORDER_PRODUCTS" , schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class OrderProduct extends SalesManagerEntity<Long, OrderProduct> {
	private static final long serialVersionUID = 176131742783954627L;
	
	@Id
	@Column (name="ORDER_PRODUCTS_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ORDER_PRODUCT_ID_NEXT_VALUE")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column (name="PRODUCTS_MODEL" , length=32)
	private String productModel;

	@Column (name="PRODUCTS_NAME" , length=64 , nullable=false)
	private String productName;

	@Column (name="PRODUCTS_QUANTITY")
	private int productQuantity;

	@Column (name="ONETIME_CHARGES" , precision=15, scale=4, nullable=false )
	private BigDecimal onetimeCharge;//

	// TODO : Order : Replace by a special price object
	@Column (name="PRODUCT_SPECIAL_NEW_PRICE" , precision=15, scale=4 )
	private BigDecimal productSpecialNewPrice;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name="PRODUCT_SPECIAL_DATE_AVAILABLE" , length=0)
	private Date productSpecialDateAvailable;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name="PRODUCT_SPECIAL_DATE_EXPIRE" , length=0 )
	private Date productSpecialDateExpire;
	
	@Column (name="SOLD_PRICE" , precision=15, scale=4, nullable=false )
	private BigDecimal soldPrice;
	
	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_ID", nullable = false)
	private Order order;
	
	@ManyToOne(targetEntity = Product.class)
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	private Product product;
	
	@ManyToOne(targetEntity = TaxClass.class)
	@JoinColumn(name = "TAX_ID", nullable = false)
	private TaxClass tax;
		
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

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
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

	public BigDecimal getProductSpecialNewPrice() {
		return productSpecialNewPrice;
	}

	public void setProductSpecialNewPrice(BigDecimal productSpecialNewPrice) {
		this.productSpecialNewPrice = productSpecialNewPrice;
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

	public BigDecimal getSoldPrice() {
		return soldPrice;
	}

	public void setSoldPrice(BigDecimal soldPrice) {
		this.soldPrice = soldPrice;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public TaxClass getTax() {
		return tax;
	}

	public void setTax(TaxClass tax) {
		this.tax = tax;
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
	
}
