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
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.service.OrderService;
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



	@Override
	public void saveOrder(ShopOrder order, MerchantStore store,
			Language language) throws Exception {
		
		
		
		Customer customer = this.toCustomerModel(order.getCustomer(), store, language);
		if(customer.getId()==null || customer.getId()==0) {
			customerService.saveOrUpdate(customer);
		}
		
		Order modelOrder = new Order(customer);

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
		
		//populate payment information
		if(order.getCreditCard()!=null) {
			//hash credit card number
			String maskedNumber = CreditCardUtils.maskCardNumber(order.getCreditCard().getCcNumber());
			order.getCreditCard().setCcNumber(maskedNumber);
			modelOrder.setCreditCard(order.getCreditCard());
		}
		
		if(!StringUtils.isBlank(order.getPaymentModule())) {
			modelOrder.setPaymentModuleCode(order.getPaymentModule());
		}

		
		orderService.create(modelOrder);
		
		

	}
	
	private void orderCustomer(Customer customer, Order order, Language language) throws Exception {
		
		Map<String,Country> countriesMap = countryService.getCountriesMap(language);
		Map<String,Zone> zonesMap = zoneService.getZones(language);
		//populate customer
		order.setBilling(customer.getBilling());
		order.setDelivery(customer.getDelivery());
		order.setCustomerCity(customer.getCity());
		order.setCustomerCountry(countriesMap.get(customer.getCountry().getIsoCode()).getName());
		order.setCustomerEmailAddress(customer.getEmailAddress());
		order.setCustomerFirstName(customer.getFirstname());
		order.setCustomerLastName(customer.getLastname());
		order.setCustomerId(customer.getId());
		order.setCustomerPostCode(customer.getPostalCode());
		if(customer.getZone()!=null) {
			Zone z = zonesMap.get(customer.getZone().getCode());
			order.setCustomerState(z.getName());
		} else {
			order.setCustomerState(customer.getState());
		}
		order.setCustomerStreetAddress(customer.getStreetAddress());
		order.setCustomerTelephone(customer.getTelephone());
			
		
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
		ShippingQuote quote = shippingService.getShippingQuote(store, customer, shippingProducts, language);

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
