package com.salesmanager.core.business.order.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.orderaccount.OrderAccount;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.utils.CloneUtils;

@Entity
@Table (name="ORDERS", schema = SchemaConstant.SALESMANAGER_SCHEMA)
public class Order extends SalesManagerEntity<Long, Order> {
	private static final long serialVersionUID = -4932198678436149138L;
	
	@Id
	@Column (name ="ORDER_ID" , unique=true , nullable=false )
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "ORDER_ID_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column (name ="ORDER_STATUS")
	@Enumerated(value = EnumType.STRING)
	private OrderStatus status;
	
	@Column (name ="COUPON_CODE")
	private String couponCode;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name ="LAST_MODIFIED")
	private Date lastModified;
	
	@Temporal(TemporalType.DATE)
	@Column (name ="DATE_PURCHASED")
	private Date datePurchased;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name ="ORDER_DATE_FINISHED")
	private Date orderDateFinished;
	
	@Column (name ="CURRENCY_VALUE")
	private BigDecimal currencyValue;
	
	@Column (name ="ORDER_TOTAL")
	private BigDecimal total;
	
	@Column (name ="ORDER_TAX")
	private BigDecimal orderTax;
	
	@Column (name ="IP_ADDRESS")
	private String ipAddress;
	
	@Column (name ="CHANNEL")
	private int channel;
	
	// TODO : Order : replace by a credit card object
	@Column (name ="CARD_TYPE")
	private String cardType;
	
	@Column (name ="CC_OWNER")
	private String ccOwner;
	
	@Column (name ="CC_NUMBER")
	private String ccNumber;
	
	@Column (name ="CC_EXPIRES")
	private String ccExpires;
	
	@Column (name ="CC_CVV")
	private String ccCvv;
	
	@Column (name ="DISPLAY_INVOICE_PAYMENTS")
	private boolean displayInvoicePayments;	
	
	// TODO : Order : Replace by a payment object
	@Column (name ="PAYMENT_METHOD")
	private String paymentMethod;
	
	@Column (name ="PAYMENT_MODULE_CODE")
	private String paymentModuleCode;
	
	// TODO : Order : replace by a shiping object
	@Column (name ="SHIPPING_METHOD")
	private String shippingMethod;
	
	@Column (name ="SHIPPING_MODULE_CODE")
	private String shippingModuleCode;

	// TODO : Order : replace by a delivery object
	@Column (name ="DELIVERY_NAME")
	private String deliveryName;
	
	@Column (name ="DELIVERY_COMPANY")
	private String deliveryCompany;
	
	@Column (name ="DELIVERY_STREET_ADDRESS")
	private String deliveryStreetAddress;
	
	@Column (name ="DELIVERY_SUBURB")
	private String deliverySuburb;
	
	@Column (name ="DELIVERY_CITY")
	private String deliveryCity;
	
	@Column (name ="DELIVERY_POSTCODE")
	private String deliveryPostcode;
	
	@Column (name ="DELIVERY_STATE")
	private String deliveryState;
	
	@Column (name ="DELIVERY_COUNTRY")
	private String deliveryCountry;
	
	@Column (name ="DELIVERY_ADDRESS_FORMAT_ID")
	private int deliveryAddressFormatId;
	
	// TODO : Order : replace by a billing object
	@Column (name ="BILLING_NAME")
	private String billingName;
	
	@Column (name ="BILLING_COMPANY")
	private String billingCompany;
	
	@Column (name ="BILLING_STREET_ADDRESS")
	private String billingStreetAddress;
	
	@Column (name ="BILLING_SUBURB")
	private String billingSuburb;
	
	@Column (name ="BILLING_CITY")
	private String billingCity;
	
	@Column (name ="BILLING_POSTCODE")
	private String billingPostcode;
	
	@Column (name ="BILLING_STATE")
	private String billingState;
	
	@Column (name ="BILLING_COUNTRY")
	private String billingCountry;
	
	@Column (name ="BILLING_ADDRESS_FORMAT_ID")
	private int billingAddressFormatId;
	
	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(name="CUSTOMER_ID" )
	private Customer customer;
	
	@ManyToOne(targetEntity = Currency.class)
	@JoinColumn(name = "CURRENCY_ID")
	private Currency currency;
	
	@ManyToOne(targetEntity = MerchantStore.class)
	@JoinColumn(name="MERCHANTID")
	private MerchantStore merchant;
	
	@OneToMany(mappedBy = "order")
	private Set<OrderAccount> orderAccounts = new HashSet<OrderAccount>();
	
	@OneToMany(mappedBy = "order")
	private Set<OrderProduct> orderProducts = new HashSet<OrderProduct>();
	
	@OneToMany(mappedBy = "order")
	private Set<OrderTotal> orderTotal = new HashSet<OrderTotal>();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderStatusHistory> orderHistory = new HashSet<OrderStatusHistory>();
	
	public Order() {
	}

	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Date getLastModified() {
		return CloneUtils.clone(lastModified);
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = CloneUtils.clone(lastModified);
	}

	public Date getDatePurchased() {
		return CloneUtils.clone(datePurchased);
	}

	public void setDatePurchased(Date datePurchased) {
		this.datePurchased = CloneUtils.clone(datePurchased);
	}

	public Date getOrderDateFinished() {
		return CloneUtils.clone(orderDateFinished);
	}

	public void setOrderDateFinished(Date orderDateFinished) {
		this.orderDateFinished = CloneUtils.clone(orderDateFinished);
	}

	public BigDecimal getCurrencyValue() {
		return currencyValue;
	}

	public void setCurrencyValue(BigDecimal currencyValue) {
		this.currencyValue = currencyValue;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getOrderTax() {
		return orderTax;
	}

	public void setOrderTax(BigDecimal orderTax) {
		this.orderTax = orderTax;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCcOwner() {
		return ccOwner;
	}

	public void setCcOwner(String ccOwner) {
		this.ccOwner = ccOwner;
	}

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	public String getCcExpires() {
		return ccExpires;
	}

	public void setCcExpires(String ccExpires) {
		this.ccExpires = ccExpires;
	}

	public String getCcCvv() {
		return ccCvv;
	}

	public void setCcCvv(String ccCvv) {
		this.ccCvv = ccCvv;
	}

	public boolean isDisplayInvoicePayments() {
		return displayInvoicePayments;
	}

	public void setDisplayInvoicePayments(boolean displayInvoicePayments) {
		this.displayInvoicePayments = displayInvoicePayments;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentModuleCode() {
		return paymentModuleCode;
	}

	public void setPaymentModuleCode(String paymentModuleCode) {
		this.paymentModuleCode = paymentModuleCode;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getShippingModuleCode() {
		return shippingModuleCode;
	}

	public void setShippingModuleCode(String shippingModuleCode) {
		this.shippingModuleCode = shippingModuleCode;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	public String getDeliveryCompany() {
		return deliveryCompany;
	}

	public void setDeliveryCompany(String deliveryCompany) {
		this.deliveryCompany = deliveryCompany;
	}

	public String getDeliveryStreetAddress() {
		return deliveryStreetAddress;
	}

	public void setDeliveryStreetAddress(String deliveryStreetAddress) {
		this.deliveryStreetAddress = deliveryStreetAddress;
	}

	public String getDeliverySuburb() {
		return deliverySuburb;
	}

	public void setDeliverySuburb(String deliverySuburb) {
		this.deliverySuburb = deliverySuburb;
	}

	public String getDeliveryCity() {
		return deliveryCity;
	}

	public void setDeliveryCity(String deliveryCity) {
		this.deliveryCity = deliveryCity;
	}

	public String getDeliveryPostcode() {
		return deliveryPostcode;
	}

	public void setDeliveryPostcode(String deliveryPostcode) {
		this.deliveryPostcode = deliveryPostcode;
	}

	public String getDeliveryState() {
		return deliveryState;
	}

	public void setDeliveryState(String deliveryState) {
		this.deliveryState = deliveryState;
	}

	public String getDeliveryCountry() {
		return deliveryCountry;
	}

	public void setDeliveryCountry(String deliveryCountry) {
		this.deliveryCountry = deliveryCountry;
	}

	public int getDeliveryAddressFormatId() {
		return deliveryAddressFormatId;
	}

	public void setDeliveryAddressFormatId(int deliveryAddressFormatId) {
		this.deliveryAddressFormatId = deliveryAddressFormatId;
	}

	public String getBillingName() {
		return billingName;
	}

	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}

	public String getBillingCompany() {
		return billingCompany;
	}

	public void setBillingCompany(String billingCompany) {
		this.billingCompany = billingCompany;
	}

	public String getBillingStreetAddress() {
		return billingStreetAddress;
	}

	public void setBillingStreetAddress(String billingStreetAddress) {
		this.billingStreetAddress = billingStreetAddress;
	}

	public String getBillingSuburb() {
		return billingSuburb;
	}

	public void setBillingSuburb(String billingSuburb) {
		this.billingSuburb = billingSuburb;
	}

	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	public String getBillingPostcode() {
		return billingPostcode;
	}

	public void setBillingPostcode(String billingPostcode) {
		this.billingPostcode = billingPostcode;
	}

	public String getBillingState() {
		return billingState;
	}

	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}

	public String getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}

	public int getBillingAddressFormatId() {
		return billingAddressFormatId;
	}

	public void setBillingAddressFormatId(int billingAddressFormatId) {
		this.billingAddressFormatId = billingAddressFormatId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public MerchantStore getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantStore merchant) {
		this.merchant = merchant;
	}

	public Set<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(Set<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public Set<OrderTotal> getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Set<OrderTotal> orderTotal) {
		this.orderTotal = orderTotal;
	}

	public Set<OrderStatusHistory> getOrderHistory() {
		return orderHistory;
	}

	public void setOrderHistory(Set<OrderStatusHistory> orderHistory) {
		this.orderHistory = orderHistory;
	}

	public Set<OrderAccount> getOrderAccounts() {
		return orderAccounts;
	}

	public void setOrderAccounts(Set<OrderAccount> orderAccounts) {
		this.orderAccounts = orderAccounts;
	}
}