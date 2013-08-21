package com.salesmanager.core.business.customer.dao.attribute;

import java.util.List;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.attribute.CustomerAttribute;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface CustomerAttributeDao extends SalesManagerEntityDao<Long, CustomerAttribute> {

	List<CustomerAttribute> getByCustomer(MerchantStore store,
			Customer customer, Language language);



}
