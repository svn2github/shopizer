package com.salesmanager.core.business.order.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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

import org.hibernate.annotations.OrderBy;

import com.salesmanager.core.business.common.model.Billing;
import com.salesmanager.core.business.common.model.Delivery;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.order.model.payment.CreditCard;
import com.salesmanager.core.business.payments.model.PaymentType;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.utils.CloneUtils;

@Entity
@Table (name="ORDERS", schema = SchemaConstant.SALESMANAGER_SCHEMA)
public class Order extends SalesManagerEntity<Long, Order> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column (name ="ORDER_ID" , unique=true , nullable=false )
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "ORDER_ID_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column (name ="ORDER_STATUS")
	@Enumerated(value = EnumType.STRING)
	private OrderStatus status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name ="LAST_MODIFIED")
	private Date lastModified;
	
	//the customer object can be detached. An order can exist and the customer deleted
	@Column (name ="CUSTOMER_ID")
	private Long customerId;
	
	@Temporal(TemporalType.DATE)
	@Column (name ="DATE_PURCHASED")
	private Date datePurchased;
	
	//used for an order payable on multiple installment
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name ="ORDER_DATE_FINISHED")
	private Date orderDateFinished;
	
	//What was the exchange rate
	@Column (name ="CURRENCY_VALUE")
	private BigDecimal currencyValue = new BigDecimal(1);//default 1-1
	
	@Column (name ="ORDER_TOTAL")
	private BigDecimal total;

	@Column (name ="IP_ADDRESS")
	private String ipAddress;

	@Column (name ="CHANNEL")
	@Enumerated(value = EnumType.STRING)
	private OrderChannel channel;


	
	@Column (name ="PAYMENT_TYPE")
	@Enumerated(value = EnumType.STRING)
	private PaymentType paymentType;
	
	@Column (name ="PAYMENT_MODULE_CODE")
	private String paymentModuleCode;
	
	
	@Column (name ="SHIPPING_MODULE_CODE")
	private String shippingModuleCode;

	@Embedded
	private Delivery delivery = null;
	
	@Embedded
	private Billing billing = null;
	
	@Embedded
	private CreditCard creditCard = null;

	
	@ManyToOne(targetEntity = Currency.class)
	@JoinColumn(name = "CURRENCY_ID")
	private Currency currency;
	
	@ManyToOne(targetEntity = MerchantStore.class)
	@JoinColumn(name="MERCHANTID")
	private MerchantStore merchant;
	
	//@OneToMany(mappedBy = "order")
	//private Set<OrderAccount> orderAccounts = new HashSet<OrderAccount>();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderProduct> orderProducts = new HashSet<OrderProduct>();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	@OrderBy(clause = "sort_order asc")
	private Set<OrderTotal> orderTotal = new HashSet<OrderTotal>();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	@OrderBy(clause = "ORDER_STATUS_HISTORY_ID asc")
	private Set<OrderStatusHistory> orderHistory = new HashSet<OrderStatusHistory>();
	
	public Order() {
	}
	
	
	public Order(Customer customer) {
		this.setBilling(customer.getBilling());
		this.setDelivery(customer.getDelivery());
		
		this.setCustomerCity(customer.getCity());
		this.setCustomerCountry(customer.getCountry().getName());
		if(customer.getZone()!=null) {
			this.setCustomerState(customer.getZone().getName());
		} else {
			this.setCustomerState(customer.getState());
		}
		this.setCustomerEmailAddress(customer.getEmailAddress());
		this.setCustomerStreetAddress(customer.getStreetAddress());
		this.setCustomerFirstName(customer.getFirstname());
		this.setCustomerLastName(customer.getLastname());
		this.setCustomerPostCode(customer.getPostalCode());
		this.setCustomerId(customer.getId());
	}
	
	@Column (name ="CUSTOMER_FIRSTNAME", length=64 , nullable=false)
	private String customerFirstName;
	
	@Column (name ="CUSTOMER_LASTNAME", length=64 , nullable=false)
	private String customerLastName;
	
	@Column (name ="CUSTOMER_STREET_ADDRESS",length=256)
	private String customerStreetAddress;
	
	@Column (name ="CUSTOMER_CITY", length=100)
	private String customerCity;
	
	@Column (name ="CUSTOMER_STATE", length=100)
	private String customerState;
	
	@Column (name ="CUSTOMER_COUNTRY", length=100)
	private String customerCountry;
	
	@Column (name ="CUSTOMER_POSTCODE", length=20)
	private String customerPostCode;
	
	@Column (name ="CUSTOMER_TELEPHONE", length=20)
	private String customerTelephone;
	
	//@Email
	@Column (name ="CUSTOMER_EMAIL_ADDRESS", length=50, nullable=false)
	private String customerEmailAddress;


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


	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}


	public String getPaymentModuleCode() {
		return paymentModuleCode;
	}

	public void setPaymentModuleCode(String paymentModuleCode) {
		this.paymentModuleCode = paymentModuleCode;
	}



	public String getShippingModuleCode() {
		return shippingModuleCode;
	}

	public void setShippingModuleCode(String shippingModuleCode) {
		this.shippingModuleCode = shippingModuleCode;
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


	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setBilling(Billing billing) {
		this.billing = billing;
	}

	public Billing getBilling() {
		return billing;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	

	

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getCustomerStreetAddress() {
		return customerStreetAddress;
	}

	public void setCustomerStreetAddress(String customerStreetAddress) {
		this.customerStreetAddress = customerStreetAddress;
	}

	public String getCustomerCity() {
		return customerCity;
	}

	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}

	public String getCustomerState() {
		return customerState;
	}

	public void setCustomerState(String customerState) {
		this.customerState = customerState;
	}

	public String getCustomerPostCode() {
		return customerPostCode;
	}

	public void setCustomerPostCode(String customerPostCode) {
		this.customerPostCode = customerPostCode;
	}

	public String getCustomerTelephone() {
		return customerTelephone;
	}

	public void setCustomerTelephone(String customerTelephone) {
		this.customerTelephone = customerTelephone;
	}

	public String getCustomerEmailAddress() {
		return customerEmailAddress;
	}

	public void setCustomerEmailAddress(String customerEmailAddress) {
		this.customerEmailAddress = customerEmailAddress;
	}

	public void setCustomerCountry(String customerCountry) {
		this.customerCountry = customerCountry;
	}

	public String getCustomerCountry() {
		return customerCountry;
	}


	public void setChannel(OrderChannel channel) {
		this.channel = channel;
	}


	public OrderChannel getChannel() {
		return channel;
	}


	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}


	public CreditCard getCreditCard() {
		return creditCard;
	}


	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}


	public PaymentType getPaymentType() {
		return paymentType;
	}

}