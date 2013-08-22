package com.salesmanager.core.business.customer.service.attribute;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.dao.attribute.CustomerAttributeDao;
import com.salesmanager.core.business.customer.model.attribute.CustomerAttribute;
import com.salesmanager.core.business.customer.model.attribute.CustomerOption;
import com.salesmanager.core.business.customer.model.attribute.CustomerOptionValue;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;

@Service("customerAttributeService")
public class CustomerAttributeServiceImpl extends
		SalesManagerEntityServiceImpl<Long, CustomerAttribute> implements CustomerAttributeService {
	
	private CustomerAttributeDao customerAttributeDao;

	@Autowired
	public CustomerAttributeServiceImpl(CustomerAttributeDao customerAttributeDao) {
		super(customerAttributeDao);
		this.customerAttributeDao = customerAttributeDao;
	}
	




	@Override
	public void saveOrUpdate(CustomerAttribute customerAttribute)
			throws ServiceException {
		if(customerAttribute.getId()!=null && customerAttribute.getId()>0) {
			customerAttributeDao.update(customerAttribute);
		} else {
			customerAttributeDao.save(customerAttribute);
		}
		
	}
	
	@Override
	public void delete(CustomerAttribute attribute) throws ServiceException {
		
		//override method, this allows the error that we try to remove a detached instance
		attribute = this.getById(attribute.getId());
		super.delete(attribute);
		
	}
	
	@Override
	public List<CustomerAttribute> getByCustomerOption(MerchantStore store, CustomerOption customerOption) {
		return customerAttributeDao.getByOptionId(store, customerOption.getId());
	}
	
	@Override
	public List<CustomerAttribute> getByCustomerOptionValue(MerchantStore store, CustomerOptionValue customerOptionValue) {
		return customerAttributeDao.getByOptionValueId(store, customerOptionValue.getId());
	}

}
