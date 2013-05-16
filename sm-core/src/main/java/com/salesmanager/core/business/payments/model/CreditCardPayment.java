package com.salesmanager.core.business.payments.model;

public class CreditCardPayment extends Payment {
	
	private String creditCardNumber;
	private String credidCardValidationNumber;
	private String expirationMonth;
	private String expirationYear;
	private String cardOwner;
	private CreditCard creditCard;
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public String getCredidCardValidationNumber() {
		return credidCardValidationNumber;
	}
	public void setCredidCardValidationNumber(String credidCardValidationNumber) {
		this.credidCardValidationNumber = credidCardValidationNumber;
	}
	public String getExpirationMonth() {
		return expirationMonth;
	}
	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}
	public String getExpirationYear() {
		return expirationYear;
	}
	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}
	public String getCardOwner() {
		return cardOwner;
	}
	public void setCardOwner(String cardOwner) {
		this.cardOwner = cardOwner;
	}
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

}
