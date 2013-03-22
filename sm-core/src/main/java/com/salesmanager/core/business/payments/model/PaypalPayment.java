package com.salesmanager.core.business.payments.model;

public class PaypalPayment extends Payment {
	
	//express checkout
	private String payerId;
	private String paymentToken;
	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}
	public String getPayerId() {
		return payerId;
	}
	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}
	public String getPaymentToken() {
		return paymentToken;
	}

}
