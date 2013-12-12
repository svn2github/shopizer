package com.salesmanager.web.shop.controller.order.facade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
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


	@Override
	public ShopOrder initializeOrder(MerchantStore store, Customer customer,
			ShoppingCart shoppingCart, Language language) throws Exception {

		//assert not null shopping cart items
		
		ShopOrder order = new ShopOrder();
		
		OrderStatus orderStatus = OrderStatus.ORDERED;
		order.setOrderStatus(orderStatus);
		
		if(customer==null) {

				customer = new Customer();
				Billing billing = new Billing();
				billing.setCountry(store.getCountry());
				billing.setZone(store.getZone());
				billing.setState(store.getStorestateprovince());
				customer.setBilling(billing);
				
				Delivery delivery = new Delivery();
				delivery.setCountry(store.getCountry());
				delivery.setZone(store.getZone());
				delivery.setState(store.getStorestateprovince());
				customer.setDelivery(delivery);

		}
		
		PersistableCustomer persistableCustomer = persistableCustomer(customer, store, language);
		order.setCustomer(persistableCustomer);
		order.setShoppingCartCustomer(customer);
		
		List<ShoppingCartItem> items = new ArrayList<ShoppingCartItem>(shoppingCart.getLineItems());
		order.setShoppingCartItems(items);
		
		return order;
	}
	


	@Override
	public OrderTotalSummary calculateOrderTotal(MerchantStore store,
			ShopOrder order, Language language) throws Exception {

		OrderTotalSummary summary = this.calculateOrderTotal(store, order.getShoppingCartCustomer(), order.getShoppingCartItems(), language);
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
		
		OrderTotalSummary summary = this.calculateOrderTotal(store, customer, items, language);

		return summary;
	}
	
	private OrderTotalSummary calculateOrderTotal(MerchantStore store, Customer customer, List<ShoppingCartItem> items, Language language) throws Exception {
		
		
		ShoppingCart temporaryCart = new ShoppingCart();
		temporaryCart.setLineItems(new HashSet<ShoppingCartItem>(items));
		

		//order total
		OrderSummary summary = new OrderSummary();
		summary.setProducts(items);
		//no default shipping summary
		
		OrderTotalSummary orderTotalSummary = orderService.caculateOrderTotal(summary, customer, store, language);
		
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
		
		
		
		Customer customer = order.getShoppingCartCustomer();
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

}
