package com.salesmanager.core.business.order.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.dao.OrderDao;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.OrderList;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.order.model.OrderTotal;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.model.OrderTotalType;
import com.salesmanager.core.business.order.model.OrderValueType;
import com.salesmanager.core.business.order.model.Order_;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shipping.model.ShippingConfiguration;
import com.salesmanager.core.business.shipping.service.ShippingService;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.tax.model.TaxItem;
import com.salesmanager.core.business.tax.service.TaxService;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.modules.order.InvoiceModule;


@Service("orderService")
public class OrderServiceImpl  extends SalesManagerEntityServiceImpl<Long, Order> implements OrderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Autowired
	private InvoiceModule invoiceModule;
	
	@Autowired
	private ShippingService shippingService;
	
	@Autowired
	private TaxService taxService;
	
	private OrderDao orderDao;
	
	@Autowired
	public OrderServiceImpl(OrderDao orderDao) {
		super(orderDao);
		this.orderDao = orderDao;
	}

	@Override
	public Order getOrder(Long orderId ) {
		return getById(orderId);
	}
	
	@Override
	public List<Order> listByStore(MerchantStore merchantStore) {
		return listByField(Order_.merchant, merchantStore);
	}
	

	@Override
	public void addOrderStatusHistory(Order order, OrderStatusHistory history) throws ServiceException {
		order.getOrderHistory().add(history);
		history.setOrder(order);
		update(order);
	}
	

	
	@Override
	public void delete(Order order) throws ServiceException {
		
		//TODO delete sub objects
		super.delete(order);
	}
	
	@Override	
	public void saveOrUpdate(Order order) throws ServiceException {
				
		if(order.getId()!=null && order.getId()>0) {
			LOGGER.debug("Updating Order");	
			super.update(order);
			
		} else {
			LOGGER.debug("Creating Order");	
			super.create(order);

		}
	}

	@Override
	public OrderList listByStore(MerchantStore store, OrderCriteria criteria) {

		return orderDao.listByStore(store, criteria);
	}
	
	@Override
	public OrderTotalSummary caculateOrderTotal(OrderSummary orderSummary, MerchantStore store, Language language) throws ServiceException {
		Validate.notNull(orderSummary,"Order summary cannot be null");
		Validate.notNull(orderSummary.getProducts(),"Order summary.products cannot be null");
		Validate.notNull(store,"MerchantStore cannot be null");
		
		try {
			return caculateOrder(orderSummary, null, store, language);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	@Override
	public OrderTotalSummary caculateOrderTotal(OrderSummary orderSummary, Customer customer, MerchantStore store, Language language) throws ServiceException {
		Validate.notNull(orderSummary,"Order summary cannot be null");
		Validate.notNull(orderSummary.getProducts(),"Order summary.products cannot be null");
		Validate.notNull(store,"MerchantStore cannot be null");
		Validate.notNull(customer,"Customer cannot be null");
		
		try {
			return caculateOrder(orderSummary, customer, store, language);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	private OrderTotalSummary caculateOrder(OrderSummary summary, Customer customer, MerchantStore store, Language language) throws Exception {
		
		OrderTotalSummary totalSummary = new OrderTotalSummary();
		List<OrderTotal> orderTotals = new ArrayList<OrderTotal>();
		Map<String,OrderTotal> otherPricesTotals = new HashMap<String,OrderTotal>();
		
		ShippingConfiguration shippingConfiguration = null;
		
		BigDecimal grandTotal = new BigDecimal(0);
		
		//price by item
		/**
		 * qty * price
		 * subtotal
		 */
		BigDecimal subTotal = new BigDecimal(0);
		for(ShoppingCartItem item : summary.getProducts()) {
			
			BigDecimal st = item.getItemPrice().multiply(new BigDecimal(item.getQuantity()));
			item.setSubTotal(st);
			subTotal = subTotal.add(st);
			//Other prices
			FinalPrice finalPrice = item.getFinalPrice();
			if(finalPrice!=null) {
				List<FinalPrice> otherPrices = finalPrice.getAdditionalPrices();
				if(otherPrices!=null) {
					for(FinalPrice price : otherPrices) {
						if(!price.isDefaultPrice()) {
							OrderTotal itemSubTotal = otherPricesTotals.get(price.getProductPrice().getCode());
							
							if(itemSubTotal==null) {
								itemSubTotal = new OrderTotal();
								itemSubTotal.setModule(Constants.OT_ITEM_PRICE_MODULE_CODE);
								itemSubTotal.setOrderTotalCode(price.getProductPrice().getCode());
								itemSubTotal.setOrderTotalType(OrderTotalType.PRODUCT);
								itemSubTotal.setSortOrder(0);
								otherPricesTotals.put(price.getProductPrice().getCode(), itemSubTotal);
							}
							
							BigDecimal orderTotalValue = itemSubTotal.getValue();
							if(orderTotalValue==null) {
								orderTotalValue = new BigDecimal(0);
							}
							
							orderTotalValue = orderTotalValue.add(price.getFinalPrice());
							itemSubTotal.setValue(orderTotalValue);
							if(price.getProductPrice().getProductPriceType().name().equals(OrderValueType.ONE_TIME)) {
								subTotal = subTotal.add(price.getFinalPrice());
							}
						}
					}
				}
			}
			
		}

		
		totalSummary.setSubTotal(subTotal);
		grandTotal=grandTotal.add(subTotal);
		
		OrderTotal orderTotalSubTotal = new OrderTotal();
		orderTotalSubTotal.setModule(Constants.OT_SUBTOTAL_MODULE_CODE);
		orderTotalSubTotal.setOrderTotalType(OrderTotalType.SUBTOTAL);
		orderTotalSubTotal.setOrderTotalCode("order.total.subtotal");
		orderTotalSubTotal.setSortOrder(5);
		orderTotalSubTotal.setValue(subTotal);
		
		//TODO autowire a list of post processing modules for price calculation - drools, custom modules
		//may affect the sub total
		
		orderTotals.add(orderTotalSubTotal);
		
		
		//shipping
		if(summary.getShippingSummary()!=null) {

			OrderTotal shippingSubTotal = new OrderTotal();
			shippingSubTotal.setModule(Constants.OT_SHIPPING_MODULE_CODE);
			shippingSubTotal.setOrderTotalType(OrderTotalType.SHIPPING);
			shippingSubTotal.setOrderTotalCode("order.total.shipping");
			shippingSubTotal.setSortOrder(10);
			
			orderTotals.add(shippingSubTotal);
			
			
			if(!summary.getShippingSummary().isFreeShipping()) {
				shippingSubTotal.setValue(summary.getShippingSummary().getShipping());
				grandTotal=grandTotal.add(summary.getShippingSummary().getShipping());
			} else {
				shippingSubTotal.setValue(new BigDecimal(0));
				grandTotal=grandTotal.add(new BigDecimal(0));
			}
			
			//check handling fees
			shippingConfiguration = shippingService.getShippingConfiguration(store);
			if(summary.getShippingSummary().getHandling()!=null && summary.getShippingSummary().getHandling().doubleValue()>0) {
				if(shippingConfiguration.getHandlingFees()!=null && shippingConfiguration.getHandlingFees().doubleValue()>0) {
					OrderTotal handlingubTotal = new OrderTotal();
					handlingubTotal.setModule(Constants.OT_HANDLING_MODULE_CODE);
					handlingubTotal.setOrderTotalType(OrderTotalType.HANDLING);
					handlingubTotal.setOrderTotalCode("order.total.handling");
					handlingubTotal.setSortOrder(12);
					handlingubTotal.setValue(summary.getShippingSummary().getHandling());
					orderTotals.add(handlingubTotal);
					grandTotal=grandTotal.add(summary.getShippingSummary().getHandling());
				}
			}
		}
		
		//tax
		List<TaxItem> taxes = taxService.calculateTax(summary, customer, store, language);
		if(taxes!=null && taxes.size()>0) {
			
			int taxCount = 20;
			for(TaxItem tax : taxes) {
				
				OrderTotal taxLine = new OrderTotal();
				taxLine.setModule(Constants.OT_TAX_MODULE_CODE);
				taxLine.setOrderTotalType(OrderTotalType.TAX);
				taxLine.setOrderTotalCode(tax.getItemCode());
				taxLine.setSortOrder(taxCount);
				taxLine.setText(tax.getLabel());
				taxLine.setValue(tax.getItemPrice());
				
				orderTotals.add(taxLine);
				grandTotal=grandTotal.add(tax.getItemPrice());
				
				taxCount ++;
				
			}
		}
		
		// grand total
		OrderTotal orderTotal = new OrderTotal();
		orderTotal.setModule(Constants.OT_TOTAL_MODULE_CODE);
		orderTotal.setOrderTotalType(OrderTotalType.TOTAL);
		orderTotal.setOrderTotalCode("order.total.total");
		orderTotal.setSortOrder(30);
		orderTotal.setValue(grandTotal);
		
		totalSummary.setTotal(grandTotal);
		

		return totalSummary;
		
	}
	
	@Override
	public ByteArrayOutputStream generateInvoice(MerchantStore store, Order order, Language language) throws ServiceException {
		
		Validate.notNull(order.getOrderProducts(),"Order products cannot be null");
		Validate.notNull(order.getOrderTotal(),"Order totals cannot be null");
		
		try {
			ByteArrayOutputStream stream = invoiceModule.createInvoice(store, order, language);
			return stream;
		} catch(Exception e) {
			throw new ServiceException(e);
		}
		
		
		
	}
}
