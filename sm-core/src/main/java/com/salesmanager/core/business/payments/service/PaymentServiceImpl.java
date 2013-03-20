package com.salesmanager.core.business.payments.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.payments.model.Payment;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.shipping.model.ShippingQuote;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.business.system.service.ModuleConfigurationService;
import com.salesmanager.core.modules.integration.payment.model.PaymentModule;
import com.salesmanager.core.modules.integration.shipping.model.ShippingQuoteModule;

@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {
	
	

	private final static String PAYMENT_MODULES = "PAYMENT";
	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;
	
	@Autowired
	private ModuleConfigurationService moduleConfigurationService;
	
	@Autowired
	@Resource(name="paymentModules")
	private Map<String,PaymentModule> paymentModules;
	
	@Override
	public List<IntegrationModule> getPaymentMethods(MerchantStore store) throws ServiceException {
		
		List<IntegrationModule> modules =  moduleConfigurationService.getIntegrationModules(PAYMENT_MODULES);
		List<IntegrationModule> returnModules = new ArrayList<IntegrationModule>();
		
		for(IntegrationModule module : modules) {
			if(module.getRegionsSet().contains(store.getCountry().getIsoCode())
					|| module.getRegionsSet().contains("*")) {
				
				returnModules.add(module);
			}
		}
		
		return returnModules;
	}
	
	@Override
	public IntegrationModule getPaymentMethod(MerchantStore store, String moduleName) throws ServiceException {
		List<IntegrationModule> modules =  getPaymentMethods(store);

		for(IntegrationModule module : modules) {
			if(module.getModule().equals(moduleName)) {
				
				return module;
			}
		}
		
		return null;
	}
	
	@Override
	public Map<String,IntegrationConfiguration> getPaymentModulesConfigured(MerchantStore store) throws ServiceException {
		
		Map<String,IntegrationConfiguration> modules = new HashMap<String,IntegrationConfiguration>();
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(PAYMENT_MODULES, store);
		if(configuration!=null) {
			if(!StringUtils.isBlank(configuration.getValue())) {
				
				
				
			}
		}
		return modules;
	}
	

	
	private Map<String,IntegrationConfiguration> parseConfiguration(String value) throws Exception {
		
		Map<String,IntegrationConfiguration> modules = new HashMap<String,IntegrationConfiguration>();
		
		
		return modules;
		
		
	}

	@Override
	public Transaction processPayment(Order order, Customer customer,
			MerchantStore store, Payment payment, BigDecimal amount)
			throws ServiceException {
		// TODO Auto-generated method stub
		
		
		Validate.notNull(order);
		Validate.notNull(customer);
		Validate.notNull(store);
		Validate.notNull(payment);
		Validate.notNull(amount);
		
		
		//must have a shipping module configured
		Map<String, IntegrationConfiguration> modules = this.getPaymentModulesConfigured(store);
		if(modules==null){
			throw new ServiceException("No payment module configured");
		}
		
		IntegrationConfiguration configuration = modules.get(payment.getModuleName());
		
		if(configuration==null) {
			throw new ServiceException("Payment module " + payment.getModuleName() + " is not configured");
		}
		
		if(!configuration.isActive()) {
			throw new ServiceException("Payment module " + payment.getModuleName() + " is not active");
		}
		
		
		PaymentModule module = this.paymentModules.get(payment.getModuleName());
		
		if(module==null) {
			throw new ServiceException("Payment module " + payment.getModuleName() + " does not exist");
		}
		
		IntegrationModule integrationModule = getPaymentMethod(store,payment.getModuleName());
		

		
		//check transaction type
		
		return null;
	}

	@Override
	public Transaction processRefund(Order order, Customer customer,
			MerchantStore store, Payment payment, BigDecimal amount)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	


}
