package com.salesmanager.web.populator.order;

import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.order.ReadableOrder;

public class ReadableOrderPopulator extends
		AbstractDataPopulator<Order, ReadableOrder> {

	@Override
	public ReadableOrder populate(Order source, ReadableOrder target,
			MerchantStore store, Language language) throws ConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ReadableOrder createTarget() {

		return null;
	}

}
