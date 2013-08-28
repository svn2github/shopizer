package com.salesmanager.core.business.customer.service.attribute;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.dao.attribute.CustomerOptionDao;
import com.salesmanager.core.business.customer.dao.attribute.CustomerOptionSetDao;
import com.salesmanager.core.business.customer.model.attribute.CustomerAttribute;
import com.salesmanager.core.business.customer.model.attribute.CustomerOption;
import com.salesmanager.core.business.customer.model.attribute.CustomerOptionSet;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Service("customerOptionService")
public class CustomerOptionServiceImpl extends
		SalesManagerEntityServiceImpl<Long, CustomerOption> implements CustomerOptionService {

	
	private CustomerOptionDao customerOptionDao;
	
	@Autowired
	private CustomerAttributeService customerAttributeService;
	
	@Autowired
	private CustomerOptionSetDao customerOptionSetDao;
	

	@Autowired
	public CustomerOptionServiceImpl(
			CustomerOptionDao customerOptionDao) {
			super(customerOptionDao);
			this.customerOptionDao = customerOptionDao;
	}
	
	@Override
	public List<CustomerOption> listByStore(MerchantStore store, Language language) throws ServiceException {

		return customerOptionDao.listByStore(store, language);

	}
	

	@Override
	public void saveOrUpdate(CustomerOption entity) throws ServiceException {
		
		
		//save or update (persist and attach entities
		if(entity.getId()!=null && entity.getId()>0) {
			super.update(entity);
		} else {
			super.save(entity);
		}
		
	}
	
	@Override
	public void addCustomerOptionSet(CustomerOptionSet optionSet, CustomerOption option) throws ServiceException {
		
		Validate.notNull(optionSet,"optionSet cannot be null");
		Validate.notNull(option,"option cannot be null");
		
		option.getCustomerOptions().add(optionSet);
		this.saveOrUpdate(option);
	}
	
	@Override
	public List<CustomerOptionSet> listByOption(CustomerOption option, MerchantStore store) throws ServiceException {
		Validate.notNull(store,"merchant store cannot be null");
		Validate.notNull(option,"option cannot be null");
		
		return customerOptionSetDao.getByOptionId(store, option.getId());
	}
	
	@Override
	public List<CustomerOptionSet> listCustomerOptionSetByStore(MerchantStore store, Language language) throws ServiceException {
		Validate.notNull(store,"merchant store cannot be null");

		
		return customerOptionSetDao.listByStore(store,language);
	}
	
	@Override
	public CustomerOptionSet getCustomerOptionSetById(Long id) throws ServiceException {
		return customerOptionSetDao.getById(id);
	}
	
	@Override
	public void removeCustomerOptionSet(CustomerOptionSet customerOptionSet) throws ServiceException {
		Validate.notNull(customerOptionSet,"customerOptionSet cannot be null");
		customerOptionSetDao.delete(customerOptionSet);
	}
	
	@Override
	public void updateCustomerOptionSet(CustomerOptionSet customerOptionSet) throws ServiceException {
		Validate.notNull(customerOptionSet,"customerOptionSet cannot be null");
		customerOptionSetDao.update(customerOptionSet);
	}
	
	@Override
	public void delete(CustomerOption customerOption) throws ServiceException {
		
		//remove all attributes having this option
		List<CustomerAttribute> attributes = customerAttributeService.getByCustomerOption(customerOption.getMerchantStore(), customerOption);
		
		for(CustomerAttribute attribute : attributes) {
			customerAttributeService.delete(attribute);
		}
		
		CustomerOption option = this.getById(customerOption.getId());
		
		//remove option
		super.delete(option);
		
	}
	

	




}
