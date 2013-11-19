package com.salesmanager.web.shop.controller.order.facade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.web.entity.customer.PersistableCustomer;
import com.salesmanager.web.entity.order.PersistableOrder;
import com.salesmanager.web.entity.order.PersistableOrderProduct;
import com.salesmanager.web.entity.order.ShopOrder;
import com.salesmanager.web.populator.customer.PersistableCustomerPopulator;

@Service("orderFacade")
public class OrderFacadeImpl implements OrderFacade {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductAttributeService productAttributeService;
	

	@Autowired
	private OrderService orderService;

	@Override
	public ShopOrder initializeOrder(MerchantStore store, Customer customer,
			ShoppingCart shoppingCart, Language language) throws Exception {

		ShopOrder order = new ShopOrder();
		if(customer!=null) {
			PersistableCustomer persistableCustomer = persistableCustomer(customer, store, language);
			order.setCustomer(persistableCustomer);
		}
		
		List<ShoppingCartItem> items = new ArrayList<ShoppingCartItem>(shoppingCart.getLineItems());
		order.setShoppingCartItems(items);
		
		return order;
	}
	


	@Override
	public OrderTotalSummary calculateOrderTotal(MerchantStore store,
			ShopOrder order, Language language) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderTotalSummary calculateOrderTotal(MerchantStore store,
			PersistableOrder order, Language language) throws Exception {
		// TODO Auto-generated method stub
		
		List<PersistableOrderProduct> orderProducts = order.getOrderProductItems();
		
		for(PersistableOrderProduct orderProduct : orderProducts) {
			
			Product product = productService.getById(orderProduct.getProduct().getId());
			if(orderProduct.getAttributes()!=null) {

				for(com.salesmanager.web.entity.catalog.rest.product.attribute.ProductAttribute attr : orderProduct.getAttributes()) {
					ProductAttribute attribute = productAttributeService.getById(attr.getId());
					if(attribute==null) {
						throw new Exception("ProductAttribute with id " + attr.getId() + " is null");
					}
					if(attribute.getProduct().getId().longValue()!=orderProduct.getProduct().getId().longValue()) {
						throw new Exception("ProductAttribute with id " + attr.getId() + " is not assigned to Product id " + orderProduct.getProduct().getId());
					}
					product.getAttributes().add(attribute);
				}
				
			}
			
		}
		
			//for each Product get the Product and get ProductAttribute
		
			//Create a ShoppingCartItem from shoppingCartService
		
		return null;
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

}
