package com.salesmanager.core.business.customer.service.attribute;

import java.util.List;

import com.salesmanager.core.business.customer.model.attribute.CustomerAttribute;
import com.salesmanager.core.business.customer.model.attribute.CustomerOption;
import com.salesmanager.core.business.customer.model.attribute.CustomerOptionValue;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public interface CustomerAttributeService extends
		SalesManagerEntityService<Long, CustomerAttribute> {

	void saveOrUpdate(CustomerAttribute customerAttribute)
			throws ServiceException;

	List<CustomerAttribute> getByCustomerOption(MerchantStore store,
			CustomerOption customerOption);

	List<CustomerAttribute> getByCustomerOptionValue(MerchantStore store,
			CustomerOptionValue customerOptionValue);
	

}
