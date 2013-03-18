package com.salesmanager.core.business.payments.model;

public abstract class Payment {
	
	private PaymentType paymentType;
	private TransactionType transactionType;

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

}
