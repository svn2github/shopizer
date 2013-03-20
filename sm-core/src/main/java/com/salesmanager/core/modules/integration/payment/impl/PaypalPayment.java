package com.salesmanager.core.modules.integration.payment.impl;

import java.math.BigDecimal;
import java.util.Map;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.payments.model.Payment;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.payment.model.PaymentModule;

public class PaypalPayment implements PaymentModule {

	@Override
	public Map<String, String> initTransaction(Customer customer, Order order,
			BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction authorize(Customer customer, Order order,
			BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction capture(Customer customer, Order order,
			BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction authorizeAndCapture(Customer customer, Order order,
			BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction refund(Transaction transaction, Order order,
			BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}



}
