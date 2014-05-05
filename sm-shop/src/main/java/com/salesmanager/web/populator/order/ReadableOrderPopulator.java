package com.salesmanager.web.populator.order;

import java.math.BigDecimal;

import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderTotal;
import com.salesmanager.core.business.order.model.OrderTotalType;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.order.ReadableOrder;

public class ReadableOrderPopulator extends
		AbstractDataPopulator<Order, ReadableOrder> {

	@Override
	public ReadableOrder populate(Order source, ReadableOrder target,
			MerchantStore store, Language language) throws ConversionException {
		
		
		
		target.setId(source.getId());
		target.setDatePurchased(source.getDatePurchased());
		target.setOrderStatus(source.getStatus());
		
		com.salesmanager.web.entity.order.OrderTotal taxTotal = null;
		com.salesmanager.web.entity.order.OrderTotal shippingTotal = null;
		
		for(OrderTotal t : source.getOrderTotal()) {
			if(t.getOrderTotalType().name().equals(OrderTotalType.TOTAL.name())) {
				com.salesmanager.web.entity.order.OrderTotal totalTotal = createTotal(t);
				target.setTotal(totalTotal);
			}
			if(t.getOrderTotalType().name().equals(OrderTotalType.TAX.name())) {
				com.salesmanager.web.entity.order.OrderTotal totalTotal = createTotal(t);
				if(taxTotal==null) {
					taxTotal = totalTotal;
				} else {
					BigDecimal v = taxTotal.getValue();
					v = v.add(totalTotal.getValue());
					taxTotal.setValue(v);
				}
				target.setTax(totalTotal);
			}
			if(t.getOrderTotalType().name().equals(OrderTotalType.SHIPPING.name())) {
				com.salesmanager.web.entity.order.OrderTotal totalTotal = createTotal(t);
				if(shippingTotal==null) {
					shippingTotal = totalTotal;
				} else {
					BigDecimal v = shippingTotal.getValue();
					v = v.add(totalTotal.getValue());
					shippingTotal.setValue(v);
				}
				target.setShipping(totalTotal);
			}
			if(t.getOrderTotalType().name().equals(OrderTotalType.HANDLING.name())) {
				com.salesmanager.web.entity.order.OrderTotal totalTotal = createTotal(t);
				if(shippingTotal==null) {
					shippingTotal = totalTotal;
				} else {
					BigDecimal v = shippingTotal.getValue();
					v = v.add(totalTotal.getValue());
					shippingTotal.setValue(v);
				}
				target.setShipping(totalTotal);
			}
		}
		
		
		
		return target;
	}
	
	private com.salesmanager.web.entity.order.OrderTotal createTotal(OrderTotal t) {
		com.salesmanager.web.entity.order.OrderTotal totalTotal = new com.salesmanager.web.entity.order.OrderTotal();
		totalTotal.setCode(t.getOrderTotalCode());
		totalTotal.setId(t.getId());
		totalTotal.setModule(t.getModule());
		totalTotal.setOrder(t.getSortOrder());
		totalTotal.setValue(t.getValue());
		return totalTotal;
	}

	@Override
	protected ReadableOrder createTarget() {

		return null;
	}

}
