package com.salesmanager.core.modules.integration.payment.model;

import java.math.BigDecimal;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.payments.model.Payment;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.modules.integration.IntegrationException;

public interface PaymentModule {
	
	/**
	 * Returns token-value related to the initialization of the transaction This
	 * method is invoked for paypal express checkout
	 * @param customer
	 * @param order
	 * @return
	 * @throws IntegrationException
	 */
	public Transaction initTransaction(
			Customer customer, Order order, BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException;
	
	public Transaction authorize(
			Customer customer, Order order, BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException;
	
	public Transaction capture(
			Customer customer, Order order, BigDecimal amount, Payment payment, Transaction transaction, IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException;
	
	public Transaction authorizeAndCapture(
			Customer customer, Order order, BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException;
	
	public Transaction refund(
			Transaction transaction, Order order, BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException;

}
