package com.salesmanager.core.modules.integration.payment.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentReq;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentRequestType;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentResponseType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsReq;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsRequestType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutReq;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutRequestType;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.DoExpressCheckoutPaymentRequestDetailsType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsItemType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsType;
import urn.ebay.apis.eBLBaseComponents.PaymentInfoType;
import urn.ebay.apis.eBLBaseComponents.SetExpressCheckoutRequestDetailsType;

import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.payments.model.Payment;
import com.salesmanager.core.business.payments.model.PaymentType;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.payments.model.TransactionType;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.payment.model.PaymentModule;
import com.salesmanager.core.utils.CoreConfiguration;

public class PayPalExpressCheckoutPayment implements PaymentModule {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PayPalExpressCheckoutPayment.class);
	
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private CoreConfiguration coreConfiguration;

	@Override
	public void validateModuleConfiguration(
			IntegrationConfiguration integrationConfiguration,
			MerchantStore store) throws IntegrationException {
		
		
		List<String> errorFields = null;
		
		//validate integrationKeys['account']
		Map<String,String> keys = integrationConfiguration.getIntegrationKeys();
		if(keys==null || StringUtils.isBlank(keys.get("api"))) {
			errorFields = new ArrayList<String>();
			errorFields.add("api");
		}
		
		if(keys==null || StringUtils.isBlank(keys.get("username"))) {
			if(errorFields==null) {
				errorFields = new ArrayList<String>();
			}
			errorFields.add("username");
		}
		
		if(keys==null || StringUtils.isBlank(keys.get("signature"))) {
			if(errorFields==null) {
				errorFields = new ArrayList<String>();
			}
			errorFields.add("signature");
		}
		

		if(errorFields!=null) {
			IntegrationException ex = new IntegrationException(IntegrationException.ERROR_VALIDATION_SAVE);
			ex.setErrorFields(errorFields);
			throw ex;
			
		}
		
		
	}

	@Override
	public Transaction initTransaction(MerchantStore store, Customer customer,
			BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		
			throw new IntegrationException("Not imlemented");
	}

	@Override
	public Transaction authorize(MerchantStore store, Customer customer,
			List<ShoppingCartItem> items, BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {

		
		com.salesmanager.core.business.payments.model.PaypalPayment paypalPayment = (com.salesmanager.core.business.payments.model.PaypalPayment)payment;
		Validate.notNull(paypalPayment.getPaymentToken(), "A paypal payment token is required to process this transaction");
		
		return processTransaction(store, customer, items, amount, paypalPayment, configuration, module);
		
		
	}

	@Override
	public Transaction capture(MerchantStore store, Customer customer,
			List<ShoppingCartItem> items, BigDecimal amount, Payment payment, Transaction transaction,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		
		com.salesmanager.core.business.payments.model.PaypalPayment paypalPayment = (com.salesmanager.core.business.payments.model.PaypalPayment)payment;
		Validate.notNull(paypalPayment.getPaymentToken(), "A paypal payment token is required to process this transaction");
		
		return processTransaction(store, customer, items, amount, paypalPayment, configuration, module);
		
	}
	
	public Transaction initPaypalTransaction(MerchantStore store,
			Customer customer, List<ShoppingCartItem> items, BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		
		

		try {
			
			
			PaymentDetailsType paymentDetails = new PaymentDetailsType();
			if(configuration.getIntegrationKeys().get("transaction").equalsIgnoreCase(TransactionType.AUTHORIZECAPTURE.name())) {
				paymentDetails.setPaymentAction(urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType.SALE);
			} else {
				paymentDetails.setPaymentAction(urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType.AUTHORIZATION);
			}
			

			List<PaymentDetailsItemType> lineItems = new ArrayList<PaymentDetailsItemType>();
			
			
			for(ShoppingCartItem cartItem : items) {
			
				PaymentDetailsItemType item = new PaymentDetailsItemType();
				BasicAmountType amt = new BasicAmountType();
				amt.setCurrencyID(urn.ebay.apis.eBLBaseComponents.CurrencyCodeType.fromValue(payment.getCurrency().getCode()));
				amt.setValue(pricingService.getStringAmount(amount, store));
				//amt.setValue(itemAmount);
				int itemQuantity = cartItem.getQuantity();
				item.setQuantity(itemQuantity);
				item.setName(cartItem.getProduct().getProductDescription().getName());
				item.setAmount(amt);
				
				lineItems.add(item);
			
			}
				

			paymentDetails.setPaymentDetailsItem(lineItems);
			BasicAmountType orderTotal = new BasicAmountType();
			orderTotal.setCurrencyID(urn.ebay.apis.eBLBaseComponents.CurrencyCodeType.fromValue(store.getCurrency().getCode()));
			orderTotal.setValue(String.valueOf(amount.doubleValue())); 
			paymentDetails.setOrderTotal(orderTotal);
			List<PaymentDetailsType> paymentDetailsList = new ArrayList<PaymentDetailsType>();
			paymentDetailsList.add(paymentDetails);
			
			StringBuilder RETURN_URL = new StringBuilder().append(
					coreConfiguration.getProperty("ORDER_SCHEME", "http")).append("://")
					.append(store.getDomainName()).append("/")
					.append(coreConfiguration.getProperty("CONTEXT_PATH", "sm-shop"))
					;
					


			SetExpressCheckoutRequestDetailsType setExpressCheckoutRequestDetails = new SetExpressCheckoutRequestDetailsType();
			String returnUrl = RETURN_URL.toString() + new StringBuilder().append(Constants.SHOP_URI).append("/paypal/checkout").append(coreConfiguration.getProperty("URL_EXTENSION", ".html")).append("/success").toString();
			String cancelUrl = RETURN_URL.toString() + new StringBuilder().append(Constants.SHOP_URI).append("/paypal/checkout").append(coreConfiguration.getProperty("URL_EXTENSION", ".html")).append("/cancel").toString();
			
			setExpressCheckoutRequestDetails.setReturnURL(returnUrl);
			setExpressCheckoutRequestDetails.setCancelURL(cancelUrl);

			
			setExpressCheckoutRequestDetails.setPaymentDetails(paymentDetailsList);

			SetExpressCheckoutRequestType setExpressCheckoutRequest = new SetExpressCheckoutRequestType(setExpressCheckoutRequestDetails);
			setExpressCheckoutRequest.setVersion("104.0");

			SetExpressCheckoutReq setExpressCheckoutReq = new SetExpressCheckoutReq();
			setExpressCheckoutReq.setSetExpressCheckoutRequest(setExpressCheckoutRequest);

			
			String mode = "sandbox";
			String env = configuration.getEnvironment();
			if(Constants.PRODUCTION_ENVIRONMENT.equals(env)) {
				mode = "production";
			}

			Map<String,String> configurationMap = new HashMap<String,String>();
			configurationMap.put("mode", mode);
			configurationMap.put("acct1.UserName", configuration.getIntegrationKeys().get("username"));
			configurationMap.put("acct1.Password", configuration.getIntegrationKeys().get("api"));
			configurationMap.put("acct1.Signature", configuration.getIntegrationKeys().get("signature"));
			
			PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(configurationMap);
			SetExpressCheckoutResponseType setExpressCheckoutResponse = service.setExpressCheckout(setExpressCheckoutReq);
			
			String token = setExpressCheckoutResponse.getToken();
			String correlationID = setExpressCheckoutResponse.getCorrelationID();
			String ack = setExpressCheckoutResponse.getAck().getValue();
			
			if(!"Success".equals(ack)) {
				LOGGER.error("Wrong value from init transaction " + ack);
				throw new IntegrationException("Wrong paypal ack from init transaction " + ack);
			}
			
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			//transaction.setOrder(order);
			transaction.setTransactionDate(new Date());
			transaction.setTransactionType(TransactionType.INIT);
			transaction.setPaymentType(PaymentType.PAYPAL);
			transaction.getTransactionDetails().put("TOKEN", token);
			transaction.getTransactionDetails().put("CORRELATION", correlationID);
			

			return transaction;
			
			//redirect user to 
			//https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-5LL13394G30048922
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new IntegrationException(e);
		}
		
		
	}

	@Override
	public Transaction authorizeAndCapture(MerchantStore store,
			Customer customer, List<ShoppingCartItem> items, BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {

		com.salesmanager.core.business.payments.model.PaypalPayment paypalPayment = (com.salesmanager.core.business.payments.model.PaypalPayment)payment;
		Validate.notNull(paypalPayment.getPaymentToken(), "A paypal payment token is required to process this transaction");
		
		return processTransaction(store, customer, items, amount, paypalPayment, configuration, module);

		
	}

	@Override
	public Transaction refund(boolean partial, MerchantStore store,
			Transaction transaction, BigDecimal amount,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Transaction processTransaction(MerchantStore store,
			Customer customer, List<ShoppingCartItem> items, BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		
		
		com.salesmanager.core.business.payments.model.PaypalPayment paypalPayment = (com.salesmanager.core.business.payments.model.PaypalPayment)payment;
		
		try {
			
			
			String mode = "sandbox";
			String env = configuration.getEnvironment();
			if(Constants.PRODUCTION_ENVIRONMENT.equals(env)) {
				mode = "production";
			}
			
	  
			 //get token from url and return the user to generate a payerid
			   
			 GetExpressCheckoutDetailsRequestType getExpressCheckoutDetailsRequest = new GetExpressCheckoutDetailsRequestType(paypalPayment.getPaymentToken());
			 getExpressCheckoutDetailsRequest.setVersion("104.0");

			 GetExpressCheckoutDetailsReq getExpressCheckoutDetailsReq = new GetExpressCheckoutDetailsReq();
			 getExpressCheckoutDetailsReq.setGetExpressCheckoutDetailsRequest(getExpressCheckoutDetailsRequest);

			 Map<String,String> configurationMap = new HashMap<String,String>();
			 configurationMap.put("mode", mode);
			 configurationMap.put("acct1.UserName", configuration.getIntegrationKeys().get("username"));
			 configurationMap.put("acct1.Password", configuration.getIntegrationKeys().get("api"));
			 configurationMap.put("acct1.Signature", configuration.getIntegrationKeys().get("signature"));
				
			 
			 PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(configurationMap);
			 GetExpressCheckoutDetailsResponseType getExpressCheckoutDetailsResponse = service.getExpressCheckoutDetails(getExpressCheckoutDetailsReq);

				
			 String token = getExpressCheckoutDetailsResponse.getGetExpressCheckoutDetailsResponseDetails().getToken();
			 String correlationID = getExpressCheckoutDetailsResponse.getCorrelationID();
			 String ack = getExpressCheckoutDetailsResponse.getAck().getValue();
			 String payerId = getExpressCheckoutDetailsResponse.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getPayerID();
			 
			//TOKEN=EC-9VT64354BS889423P&CHECKOUTSTATUS=PaymentActionNotInitiated&TIMESTAMP=2014-01-26T17:30:17Z&CORRELATIONID=84dfe1d0939cc&ACK=Success&VERSION=104.0&BUILD=9285531&EMAIL=csamson777-facilitator@yahoo.com&PAYERID=XURV79Z6URDV4&PAYERSTATUS=verified&BUSINESS=facilitator account's Test Store&FIRSTNAME=facilitator&LASTNAME=account&COUNTRYCODE=US&SHIPTONAME=facilitator account's Test Store&SHIPTOSTREET=1 Main St&SHIPTOCITY=San Jose&SHIPTOSTATE=CA&SHIPTOZIP=95131&SHIPTOCOUNTRYCODE=US&SHIPTOCOUNTRYNAME=United States&ADDRESSSTATUS=Confirmed&CURRENCYCODE=USD&AMT=1.00&ITEMAMT=1.00&SHIPPINGAMT=0.00&HANDLINGAMT=0.00&TAXAMT=0.00&INSURANCEAMT=0.00&SHIPDISCAMT=0.00&L_NAME0=item&L_QTY0=1&L_TAXAMT0=0.00&L_AMT0=1.00&L_ITEMWEIGHTVALUE0=   0.00000&L_ITEMLENGTHVALUE0=   0.00000&L_ITEMWIDTHVALUE0=   0.00000&L_ITEMHEIGHTVALUE0=   0.00000&PAYMENTREQUEST_0_CURRENCYCODE=USD&PAYMENTREQUEST_0_AMT=1.00&PAYMENTREQUEST_0_ITEMAMT=1.00&PAYMENTREQUEST_0_SHIPPINGAMT=0.00&PAYMENTREQUEST_0_HANDLINGAMT=0.00&PAYMENTREQUEST_0_TAXAMT=0.00&PAYMENTREQUEST_0_INSURANCEAMT=0.00&PAYMENTREQUEST_0_SHIPDISCAMT=0.00&PAYMENTREQUEST_0_INSURANCEOPTIONOFFERED=false&PAYMENTREQUEST_0_SHIPTONAME=facilitator account's Test Store&PAYMENTREQUEST_0_SHIPTOSTREET=1 Main St&PAYMENTREQUEST_0_SHIPTOCITY=San Jose&PAYMENTREQUEST_0_SHIPTOSTATE=CA&PAYMENTREQUEST_0_SHIPTOZIP=95131&PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE=US&PAYMENTREQUEST_0_SHIPTOCOUNTRYNAME=United States&PAYMENTREQUEST_0_ADDRESSSTATUS=Confirmed&PAYMENTREQUEST_0_ADDRESSNORMALIZATIONSTATUS=None&L_PAYMENTREQUEST_0_NAME0=item&L_PAYMENTREQUEST_0_QTY0=1&L_PAYMENTREQUEST_0_TAXAMT0=0.00&L_PAYMENTREQUEST_0_AMT0=1.00&L_PAYMENTREQUEST_0_ITEMWEIGHTVALUE0=   0.00000&L_PAYMENTREQUEST_0_ITEMLENGTHVALUE0=   0.00000&L_PAYMENTREQUEST_0_ITEMWIDTHVALUE0=   0.00000&L_PAYMENTREQUEST_0_ITEMHEIGHTVALUE0=   0.00000&PAYMENTREQUESTINFO_0_ERRORCODE=0
				
			 if(!"Success".equals(ack)) {
				LOGGER.error("Wrong value from anthorize and capture transaction " + ack);
				throw new IntegrationException("Wrong paypal ack from init transaction " + ack);
			 }
			
 
			 PaymentDetailsType paymentDetail = new PaymentDetailsType();
			 /** IPN **/
			 //paymentDetail.setNotifyURL("http://replaceIpnUrl.com");
			 BasicAmountType orderTotal = new BasicAmountType();
			 orderTotal.setValue(String.valueOf(amount));
			 orderTotal.setCurrencyID(urn.ebay.apis.eBLBaseComponents.CurrencyCodeType.fromValue(payment.getCurrency().getCode()));
			 paymentDetail.setOrderTotal(orderTotal);
			 paymentDetail.setButtonSource("Shopizer_Cart_AP");
			 /** sale or pre-auth **/
			 paymentDetail.setPaymentAction(urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType.SALE);
			 List<PaymentDetailsType> paymentDetails = new ArrayList<PaymentDetailsType>();
			 paymentDetails.add(paymentDetail);
								
			 DoExpressCheckoutPaymentRequestDetailsType doExpressCheckoutPaymentRequestDetails = new DoExpressCheckoutPaymentRequestDetailsType();
			 doExpressCheckoutPaymentRequestDetails.setToken(token);
			 doExpressCheckoutPaymentRequestDetails.setPayerID(payerId);
			 doExpressCheckoutPaymentRequestDetails.setPaymentDetails(paymentDetails);
				
			 DoExpressCheckoutPaymentRequestType doExpressCheckoutPaymentRequest = new DoExpressCheckoutPaymentRequestType(doExpressCheckoutPaymentRequestDetails);
			 doExpressCheckoutPaymentRequest.setVersion("104.0");
				
			 DoExpressCheckoutPaymentReq doExpressCheckoutPaymentReq = new DoExpressCheckoutPaymentReq();
			 doExpressCheckoutPaymentReq.setDoExpressCheckoutPaymentRequest(doExpressCheckoutPaymentRequest);
				

			 DoExpressCheckoutPaymentResponseType doExpressCheckoutPaymentResponse = service.doExpressCheckoutPayment(doExpressCheckoutPaymentReq); 
			 String commitAck = doExpressCheckoutPaymentResponse.getAck().getValue();
			 
			 
			 if(!"Success".equals(commitAck)) {
				LOGGER.error("Wrong value from transaction commit " + ack);
				throw new IntegrationException("Wrong paypal ack from init transaction " + ack);
			 }
			 
			 
			 List<PaymentInfoType> paymentInfoList =  doExpressCheckoutPaymentResponse.getDoExpressCheckoutPaymentResponseDetails().getPaymentInfo();
			 String transactionId = null;
			 
			 for(PaymentInfoType paymentInfo : paymentInfoList) {
				 transactionId = paymentInfo.getTransactionID();
			 }
			 
			 
			 //TOKEN=EC-90U93956LU4997256&SUCCESSPAGEREDIRECTREQUESTED=false&TIMESTAMP=2014-02-16T15:41:03Z&CORRELATIONID=39d4ab666c1d7&ACK=Success&VERSION=104.0&BUILD=9720069&INSURANCEOPTIONSELECTED=false&SHIPPINGOPTIONISDEFAULT=false&PAYMENTINFO_0_TRANSACTIONID=4YA742984J1256935&PAYMENTINFO_0_TRANSACTIONTYPE=expresscheckout&PAYMENTINFO_0_PAYMENTTYPE=instant&PAYMENTINFO_0_ORDERTIME=2014-02-16T15:41:03Z&PAYMENTINFO_0_AMT=1.00&PAYMENTINFO_0_FEEAMT=0.33&PAYMENTINFO_0_TAXAMT=0.00&PAYMENTINFO_0_CURRENCYCODE=USD&PAYMENTINFO_0_PAYMENTSTATUS=Completed&PAYMENTINFO_0_PENDINGREASON=None&PAYMENTINFO_0_REASONCODE=None&PAYMENTINFO_0_PROTECTIONELIGIBILITY=Eligible&PAYMENTINFO_0_PROTECTIONELIGIBILITYTYPE=ItemNotReceivedEligible,UnauthorizedPaymentEligible&PAYMENTINFO_0_SECUREMERCHANTACCOUNTID=TWLK53YN7GDM6&PAYMENTINFO_0_ERRORCODE=0&PAYMENTINFO_0_ACK=Success
			 
			 Transaction transaction = new Transaction();
			 transaction.setAmount(amount);
			 transaction.setTransactionDate(new Date());
			 transaction.setTransactionType(TransactionType.AUTHORIZECAPTURE);
			 transaction.setPaymentType(PaymentType.PAYPAL);
			 transaction.getTransactionDetails().put("TOKEN", token);
			 transaction.getTransactionDetails().put("PAYERID", payerId);
			 transaction.getTransactionDetails().put("TRANSACTIONID", transactionId);
			 transaction.getTransactionDetails().put("CORRELATION", correlationID);
				
			

			return transaction;
			
			
		} catch(Exception e) {
			throw new IntegrationException(e);
		}

		
		
	}

}
