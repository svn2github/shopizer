package com.salesmanager.web.populator.customer;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.customer.ReadableCustomer;

public class ReadableCustomerPopulator extends
		AbstractDataPopulator<Customer, ReadableCustomer> {

	@Override
	public ReadableCustomer populate(Customer source, ReadableCustomer target,
			MerchantStore store, Language language) throws ConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ReadableCustomer createTarget() {
		return null;
	}

}
