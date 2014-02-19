package com.salesmanager.web.shop.controller.order.facade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.catalog.product.service.file.DigitalProductService;
import com.salesmanager.core.business.common.model.Billing;
import com.salesmanager.core.business.common.model.Delivery;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionValueService;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.model.payment.CreditCard;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.payments.model.CreditCardPayment;
import com.salesmanager.core.business.payments.model.CreditCardType;
import com.salesmanager.core.business.payments.model.Payment;
import com.salesmanager.core.business.payments.model.PaymentType;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.shipping.model.ShippingProduct;
import com.salesmanager.core.business.shipping.model.ShippingQuote;
import com.salesmanager.core.business.shipping.model.ShippingSummary;
import com.salesmanager.core.business.shipping.service.ShippingService;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.utils.CreditCardUtils;
import com.salesmanager.web.entity.customer.PersistableCustomer;
import com.salesmanager.web.entity.order.OrderEntity;
import com.salesmanager.web.entity.order.OrderTotal;
import com.salesmanager.web.entity.order.PersistableOrder;
import com.salesmanager.web.entity.order.PersistableOrderProduct;
import com.salesmanager.web.entity.order.ShopOrder;
import com.salesmanager.web.populator.customer.CustomerPopulator;
import com.salesmanager.web.populator.customer.PersistableCustomerPopulator;
import com.salesmanager.web.populator.order.OrderProductPopulator;
import com.salesmanager.web.populator.order.ShoppingCartItemPopulator;

@Service("orderFacade")
public class OrderFacadeImpl implements OrderFacade {


	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductAttributeService productAttributeService;
	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private DigitalProductService digitalProductService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private ZoneService zoneService;
	@Autowired
	private CustomerOptionService customerOptionService;
	@Autowired
	private CustomerOptionValueService customerOptionValueService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private ShippingService shippingService;


	@Override
	public ShopOrder initializeOrder(MerchantStore store, Customer customer,
			ShoppingCart shoppingCart, Language language) throws Exception {

		//assert not null shopping cart items
		
		ShopOrder order = new ShopOrder();
		
		OrderStatus orderStatus = OrderStatus.ORDERED;
		order.setOrderStatus(orderStatus);
		
		if(customer==null) {
				customer = this.initEmptyCustomer(store);
		}
		
		PersistableCustomer persistableCustomer = persistableCustomer(customer, store, language);
		order.setCustomer(persistableCustomer);

		//keep list of shopping cart items for core price calculation
		List<ShoppingCartItem> items = new ArrayList<ShoppingCartItem>(shoppingCart.getLineItems());
		order.setShoppingCartItems(items);
		
		return order;
	}
	


	@Override
	public OrderTotalSummary calculateOrderTotal(MerchantStore store,
			ShopOrder order, Language language) throws Exception {
		

		Customer customer = this.toCustomerModel(order.getCustomer(), store, language);
		OrderTotalSummary summary = this.calculateOrderTotal(store, customer, order, language);
		this.setOrderTotals(order, summary);
		return summary;
	}

	@Override
	public OrderTotalSummary calculateOrderTotal(MerchantStore store,
			PersistableOrder order, Language language) throws Exception {
	
		List<PersistableOrderProduct> orderProducts = order.getOrderProductItems();
		
		ShoppingCartItemPopulator populator = new ShoppingCartItemPopulator();
		populator.setProductAttributeService(productAttributeService);
		populator.setProductService(productService);
		populator.setShoppingCartService(shoppingCartService);
		
		List<ShoppingCartItem> items = new ArrayList<ShoppingCartItem>();
		for(PersistableOrderProduct orderProduct : orderProducts) {
			ShoppingCartItem item = populator.populate(orderProduct, new ShoppingCartItem(), store, language);
			items.add(item);
		}
		

		Customer customer = customer(order.getCustomer(), store, language);
		
		OrderTotalSummary summary = this.calculateOrderTotal(store, customer, order, language);

		return summary;
	}
	
	private OrderTotalSummary calculateOrderTotal(MerchantStore store, Customer customer, PersistableOrder order, Language language) throws Exception {
		
		OrderTotalSummary orderTotalSummary = null;
		
		OrderSummary summary = new OrderSummary();
		
		
		if(order instanceof ShopOrder) {
			ShopOrder o = (ShopOrder)order;
			summary.setProducts(o.getShoppingCartItems());
			
			if(o.getShippingSummary()!=null) {
				summary.setShippingSummary(o.getShippingSummary());
			}
			orderTotalSummary = orderService.caculateOrderTotal(summary, customer, store, language);
		} else {
			//need Set of ShoppingCartItem
			//PersistableOrder not implemented
			throw new Exception("calculateOrderTotal not yet implemented for PersistableOrder");
		}

		return orderTotalSummary;
		
	}
	
	
	private PersistableCustomer persistableCustomer(Customer customer, MerchantStore store, Language language) throws Exception {
		
		PersistableCustomerPopulator customerPopulator = new PersistableCustomerPopulator();
		PersistableCustomer persistableCustomer = customerPopulator.populate(customer, new PersistableCustomer(), store, language);
		return persistableCustomer;
		
	}
	
	private Customer customer(PersistableCustomer customer, MerchantStore store, Language language) throws Exception {
		CustomerPopulator customerPopulator = new CustomerPopulator();
		Customer cust = customerPopulator.populate(customer, new Customer(), store, language);
		return cust;
		
	}
	
	private void setOrderTotals(OrderEntity order, OrderTotalSummary summary) {
		
		List<OrderTotal> totals = new ArrayList<OrderTotal>();
		List<com.salesmanager.core.business.order.model.OrderTotal> orderTotals = summary.getTotals();
		for(com.salesmanager.core.business.order.model.OrderTotal t : orderTotals) {
			OrderTotal total = new OrderTotal();
			total.setCode(t.getOrderTotalCode());
			total.setTitle(t.getTitle());
			total.setValue(t.getValue());
			totals.add(total);
		}
		
		order.setTotals(totals);
		
	}


	/**
	 * Submitted object must be valided prior to the invocation of this method
	 */
	@Override
	public void processOrder(ShopOrder order, MerchantStore store,
			Language language) throws Exception {
				
		this.processOrderModel(order, null, store, language);

	}
	
	@Override
	public void processOrder(ShopOrder order, Transaction transaction, MerchantStore store,
			Language language) throws Exception {
				
		this.processOrderModel(order, transaction, store, language);

	}
	
	private Order processOrderModel(ShopOrder order, Transaction transaction, MerchantStore store,
			Language language) throws ServiceException {
		
		try {

			Customer customer = this.toCustomerModel(order.getCustomer(), store, language);
			
			//if(customer!=null && customer.getId()==null || customer.getId()==0) {
			//	customerService.saveOrUpdate(customer);
			//}
			
			Order modelOrder = new Order();
			modelOrder.setBilling(customer.getBilling());
			modelOrder.setDelivery(customer.getDelivery());
	
			List<ShoppingCartItem> shoppingCartItems = order.getShoppingCartItems();
			Set<OrderProduct> orderProducts = new HashSet<OrderProduct>();
			
			OrderProductPopulator orderProductPopulator = new OrderProductPopulator();
			orderProductPopulator.setDigitalProductService(digitalProductService);
			orderProductPopulator.setProductAttributeService(productAttributeService);
			orderProductPopulator.setProductService(productService);
			
			for(ShoppingCartItem item : shoppingCartItems) {
				OrderProduct orderProduct = new OrderProduct();
				orderProduct = orderProductPopulator.populate(item, orderProduct , store, language);
				orderProduct.setOrder(modelOrder);
				orderProducts.add(orderProduct);
			}
			
			modelOrder.setOrderProducts(orderProducts);
			
			OrderTotalSummary summary = order.getOrderTotalSummary();
			List<com.salesmanager.core.business.order.model.OrderTotal> totals = summary.getTotals();
			Set<com.salesmanager.core.business.order.model.OrderTotal> modelTotals = new HashSet<com.salesmanager.core.business.order.model.OrderTotal>();
			for(com.salesmanager.core.business.order.model.OrderTotal total : totals) {
				total.setOrder(modelOrder);
				modelTotals.add(total);
			}
			
			modelOrder.setOrderTotal(modelTotals);
			modelOrder.setTotal(order.getOrderTotalSummary().getTotal());
	
			//order misc objects
			modelOrder.setCurrency(store.getCurrency());
			modelOrder.setMerchant(store);
			OrderStatus status = OrderStatus.ORDERED;
			modelOrder.setStatus(status);
			//do not care about previous status
			
			
			//customer object
			orderCustomer(customer, modelOrder, language);
			
			//populate shipping information
			if(!StringUtils.isBlank(order.getShippingModule())) {
				modelOrder.setShippingModuleCode(order.getShippingModule());
			}
			
			String paymentType = order.getPaymentMethodType();
			Payment payment = null;
			if(PaymentType.CREDITCARD.name().equals(paymentType)) {
				payment = new CreditCardPayment();
				((CreditCardPayment)payment).setCardOwner(order.getPayment().get("creditcard_card_holder"));
				((CreditCardPayment)payment).setCredidCardValidationNumber(order.getPayment().get("creditcard_card_cvv"));
				((CreditCardPayment)payment).setCreditCardNumber(order.getPayment().get("creditcard_card_number"));
				((CreditCardPayment)payment).setExpirationMonth(order.getPayment().get("creditcard_card_expirationmonth"));
				((CreditCardPayment)payment).setExpirationYear(order.getPayment().get("creditcard_card_expirationyear"));
				
				CreditCardType creditCardType =null;
				String cardType = order.getPayment().get("creditcard_card_type");
				
				if(cardType.equalsIgnoreCase(CreditCardType.AMEX.name())) {
					creditCardType = CreditCardType.AMEX;
				} else if(cardType.equalsIgnoreCase(CreditCardType.VISA.name())) {
					creditCardType = CreditCardType.VISA;
				} else if(cardType.equalsIgnoreCase(CreditCardType.MASTERCARD.name())) {
					creditCardType = CreditCardType.MASTERCARD;
				} else if(cardType.equalsIgnoreCase(CreditCardType.DINERS.name())) {
					creditCardType = CreditCardType.DINERS;
				} else if(cardType.equalsIgnoreCase(CreditCardType.DISCOVERY.name())) {
					creditCardType = CreditCardType.DISCOVERY;
				}
				
				
				((CreditCardPayment)payment).setCreditCard(creditCardType);
			
				CreditCard cc = new CreditCard();
				cc.setCardType(creditCardType);
				cc.setCcCvv(((CreditCardPayment)payment).getCredidCardValidationNumber());
				cc.setCcOwner(((CreditCardPayment)payment).getCardOwner());
				cc.setCcExpires(((CreditCardPayment)payment).getExpirationMonth() + "-" + ((CreditCardPayment)payment).getExpirationYear());
			
				//hash credit card number
				String maskedNumber = CreditCardUtils.maskCardNumber(order.getPayment().get("creditcard_card_holder"));
				order.getCreditCard().setCcNumber(maskedNumber);
	
				modelOrder.setCreditCard(order.getCreditCard());
			}
			
			if(PaymentType.PAYPAL.name().equals(paymentType)) {
				
				//check for previous transaction
				if(transaction==null) {
					throw new ServiceException("payment.error");
				}
				
				payment = new com.salesmanager.core.business.payments.model.PaypalPayment();
				
				((com.salesmanager.core.business.payments.model.PaypalPayment)payment).setPayerId(transaction.getTransactionDetails().get("PAYERID"));
				((com.salesmanager.core.business.payments.model.PaypalPayment)payment).setPaymentToken(transaction.getTransactionDetails().get("TOKEN"));
				
				
			}
			
			if(!StringUtils.isBlank(order.getPaymentModule())) {
				modelOrder.setPaymentModuleCode(order.getPaymentModule());
			}
	
			
			orderService.processOrder(modelOrder, customer, order.getShoppingCartItems(), summary, payment, store);
			return modelOrder;
		
		} catch(ServiceException se) {
			throw se;
		} catch(Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	private void orderCustomer(Customer customer, Order order, Language language) throws Exception {
		
		Map<String,Country> countriesMap = countryService.getCountriesMap(language);
		Map<String,Zone> zonesMap = zoneService.getZones(language);
		//populate customer
		order.setBilling(customer.getBilling());
		order.setDelivery(customer.getDelivery());
		order.setCustomerEmailAddress(customer.getEmailAddress());
		order.setCustomerId(customer.getId());

		
		
	}



	@Override
	public Customer initEmptyCustomer(MerchantStore store) {
		
		Customer customer = new Customer();
		Billing billing = new Billing();
		billing.setCountry(store.getCountry());
		billing.setZone(store.getZone());
		billing.setState(store.getStorestateprovince());
		billing.setPostalCode(store.getStorepostalcode());
		customer.setBilling(billing);
		
		Delivery delivery = new Delivery();
		delivery.setCountry(store.getCountry());
		delivery.setZone(store.getZone());
		delivery.setState(store.getStorestateprovince());
		delivery.setPostalCode(store.getStorepostalcode());
		customer.setDelivery(delivery);
		
		return customer;
	}



	@Override
	public void refreshOrder(ShopOrder order, MerchantStore store,
			Customer customer, ShoppingCart shoppingCart, Language language)
			throws Exception {
		if(customer==null && order.getCustomer()!=null) {
			order.getCustomer().setId(0L);//reset customer id
		}
		
		if(customer!=null) {
			PersistableCustomer persistableCustomer = persistableCustomer(customer, store, language);
			order.setCustomer(persistableCustomer);
		}
		
		List<ShoppingCartItem> items = new ArrayList<ShoppingCartItem>(shoppingCart.getLineItems());
		order.setShoppingCartItems(items);
		
		return;
	}
	
	@Override
	public ShippingQuote getShippingQuote(ShoppingCart cart, ShopOrder order, MerchantStore store, Language language) throws Exception {
		

		//create shipping products
		List<ShippingProduct> shippingProducts = shoppingCartService.createShippingProduct(cart);

		if(CollectionUtils.isEmpty(shippingProducts)) {
			return null;//products are virtual
		}
				
		Customer customer = this.toCustomerModel(order.getCustomer(), store, language);
		
		Delivery delivery = new Delivery();
		
		//adjust shipping and billing
		if(order.isShipToBillingAdress()) {
			Billing billing = customer.getBilling();
			delivery.setAddress(billing.getAddress());
			delivery.setCompany(billing.getCompany());
			delivery.setPostalCode(billing.getPostalCode());
			delivery.setState(billing.getState());
			delivery.setCountry(billing.getCountry());
			delivery.setZone(billing.getZone());
		} else {
			delivery = customer.getDelivery();
		}
		
		
		
		ShippingQuote quote = shippingService.getShippingQuote(store, delivery, shippingProducts, language);

		return quote;

	}
	
	@Override
	public List<Country> getShipToCountry(MerchantStore store, Language language) throws Exception {
		
		List<Country> shippingCountriesList = shippingService.getShipToCountryList(store, language);
		return shippingCountriesList;
		
	}
	
	private Customer toCustomerModel(PersistableCustomer persistableCustomer, MerchantStore store, Language language) throws ConversionException {
		
		CustomerPopulator populator = new CustomerPopulator();
		Customer customer = new Customer();
		populator.setCountryService(countryService);
		populator.setCustomerOptionService(customerOptionService);
		populator.setCustomerOptionValueService(customerOptionValueService);
		populator.setLanguageService(languageService);
		populator.setZoneService(zoneService);
		return populator.populate(persistableCustomer, customer, store, language);
		
	}



	@Override
	public ShippingSummary getShippingSummary(ShippingQuote quote,
			MerchantStore store, Language language) {
		
		if(quote.getSelectedShippingOption()!=null) {
		
			ShippingSummary summary = new ShippingSummary();
			summary.setFreeShipping(quote.isFreeShipping());
			summary.setTaxOnShipping(quote.isApplyTaxOnShipping());
			summary.setHandling(quote.getHandlingFees());
			summary.setShipping(quote.getSelectedShippingOption().getOptionPrice());
			summary.setShippingOption(quote.getSelectedShippingOption().getOptionName());
			summary.setShippingModule(quote.getShippingModuleCode());
			
			return summary;
		
		} else {
			return null;
		}
	}

}
