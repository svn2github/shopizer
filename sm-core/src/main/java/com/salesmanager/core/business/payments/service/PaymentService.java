package com.salesmanager.core.business.payments.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.payments.model.Payment;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;

public interface PaymentService {



	public List<IntegrationModule> getPaymentMethods(MerchantStore store)
			throws ServiceException;

	Map<String, IntegrationConfiguration> getPaymentModulesConfigured(
			MerchantStore store) throws ServiceException;
	
	Transaction processPayment(Order order, Customer customer, MerchantStore store, Payment payment, BigDecimal amount) throws ServiceException;
	Transaction processRefund(Order order, Customer customer, MerchantStore store, Payment payment, BigDecimal amount) throws ServiceException;

	/**
	 * Get a specific Payment module
	 * @param store
	 * @param moduleName
	 * @return
	 * @throws ServiceException
	 */
	IntegrationModule getPaymentMethod(MerchantStore store, String moduleName)
			throws ServiceException;

	/**
	 * Saves a payment module configuration
	 * @param configuration
	 * @param store
	 * @throws ServiceException
	 */
	void savePaymentModuleConfiguration(IntegrationConfiguration configuration,
			MerchantStore store) throws ServiceException;

}