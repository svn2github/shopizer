package com.salesmanager.web.populator.order;

import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.order.ReadableOrderProduct;

public class ReadableOrderProductPopulator extends
		AbstractDataPopulator<OrderProduct, ReadableOrderProduct> {

	@Override
	public ReadableOrderProduct populate(OrderProduct source,
			ReadableOrderProduct target, MerchantStore store, Language language)
			throws ConversionException {
		target.setId(source.getId());
		target.setOrderedQuantity(source.getProductQuantity());
		target.setPrice(source.getOneTimeCharge());
		return target;
	}

	@Override
	protected ReadableOrderProduct createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
