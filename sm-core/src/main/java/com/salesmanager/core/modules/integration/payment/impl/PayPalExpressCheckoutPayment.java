package com.salesmanager.core.modules.integration.payment.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutReq;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutRequestType;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsItemType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsType;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction authorize(MerchantStore store, Customer customer,
			List<ShoppingCartItem> items, BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction capture(MerchantStore store, Customer customer,
			List<ShoppingCartItem> items, BigDecimal amount, Payment payment, Transaction transaction,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Transaction initPaypalTransaction(MerchantStore store,
			Customer customer, List<ShoppingCartItem> items, BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		
		

		try {
			
			
			PaymentDetailsType paymentDetails = new PaymentDetailsType();
			paymentDetails.setPaymentAction(urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType.SALE);

			List<PaymentDetailsItemType> lineItems = new ArrayList<PaymentDetailsItemType>();
			
			
			for(ShoppingCartItem cartItem : items) {
			
				PaymentDetailsItemType item = new PaymentDetailsItemType();
				BasicAmountType amt = new BasicAmountType();
				amt.setCurrencyID(urn.ebay.apis.eBLBaseComponents.CurrencyCodeType.fromValue(store.getCurrency().getCode()));
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
					.append("/order/");
					


			SetExpressCheckoutRequestDetailsType setExpressCheckoutRequestDetails = new SetExpressCheckoutRequestDetailsType();
			setExpressCheckoutRequestDetails.setReturnURL(RETURN_URL.append("/paypal/").append(coreConfiguration.getProperty("URL_EXTENSION", ".html")).append("/success").toString());
			setExpressCheckoutRequestDetails.setCancelURL(RETURN_URL.append("/paypal/").append(coreConfiguration.getProperty("URL_EXTENSION", ".html")).append("/cancel").toString());

			setExpressCheckoutRequestDetails.setPaymentDetails(paymentDetailsList);

			SetExpressCheckoutRequestType setExpressCheckoutRequest = new SetExpressCheckoutRequestType(setExpressCheckoutRequestDetails);
			setExpressCheckoutRequest.setVersion("104.0");

			SetExpressCheckoutReq setExpressCheckoutReq = new SetExpressCheckoutReq();
			setExpressCheckoutReq.setSetExpressCheckoutRequest(setExpressCheckoutRequest);

			/*		
	  		Map<String, String> sdkConfig = new HashMap<String, String>();
			sdkConfig.put("mode", "sandbox");
			sdkConfig.put("acct1.UserName", "jb-us-seller_api1.paypal.com");
			sdkConfig.put("acct1.Password", "WX4WTU3S8MY44S7F");
			sdkConfig.put("acct1.Signature","AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy");
			*/
			
			String mode = "sandbox";
			String env = configuration.getEnvironment();
			if(Constants.PRODUCTION_ENVIRONMENT.equals(env)) {
				mode = "production";
			}
			
			Properties sdkConfig = new Properties();
			sdkConfig.put("mode", mode);
			sdkConfig.put("acct1.UserName", configuration.getIntegrationKeys().get("account"));
			sdkConfig.put("acct1.Password", configuration.getIntegrationKeys().get("api"));
			sdkConfig.put("acct1.Signature", configuration.getIntegrationKeys().get("signature"));
			
			PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(sdkConfig);
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
			
		} catch(Exception e) {
			throw new IntegrationException(e);
		}
		
		
	}

	@Override
	public Transaction authorizeAndCapture(MerchantStore store,
			Customer customer, List<ShoppingCartItem> items, BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {

		
		
		try {
			
			
			PaymentDetailsType paymentDetails = new PaymentDetailsType();
			paymentDetails.setPaymentAction(urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType.SALE);
			
			

			List<PaymentDetailsItemType> lineItems = new ArrayList<PaymentDetailsItemType>();
			
			
			for(ShoppingCartItem cartItem : items) {
			
				PaymentDetailsItemType item = new PaymentDetailsItemType();
				BasicAmountType amt = new BasicAmountType();
				amt.setCurrencyID(urn.ebay.apis.eBLBaseComponents.CurrencyCodeType.fromValue(store.getCurrency().getCode()));
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
					.append(coreConfiguration.getProperty("CONTEXT_PATH", "sm-shop"));
					//.append("/paypal/expresscheckout?success=true");
					


			SetExpressCheckoutRequestDetailsType setExpressCheckoutRequestDetails = new SetExpressCheckoutRequestDetailsType();
			setExpressCheckoutRequestDetails.setReturnURL(RETURN_URL.append("/paypal/expresscheckout.htmk/success").toString());
			setExpressCheckoutRequestDetails.setCancelURL(RETURN_URL.append("/paypal/expresscheckout?cancel.html/cancel").toString());

			setExpressCheckoutRequestDetails.setPaymentDetails(paymentDetailsList);

			SetExpressCheckoutRequestType setExpressCheckoutRequest = new SetExpressCheckoutRequestType(setExpressCheckoutRequestDetails);
			setExpressCheckoutRequest.setVersion("104.0");

			SetExpressCheckoutReq setExpressCheckoutReq = new SetExpressCheckoutReq();
			setExpressCheckoutReq.setSetExpressCheckoutRequest(setExpressCheckoutRequest);

			/*		
	  		Map<String, String> sdkConfig = new HashMap<String, String>();
			sdkConfig.put("mode", "sandbox");
			sdkConfig.put("acct1.UserName", "jb-us-seller_api1.paypal.com");
			sdkConfig.put("acct1.Password", "WX4WTU3S8MY44S7F");
			sdkConfig.put("acct1.Signature","AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy");
			*/
			
			String mode = "sandbox";
			String env = configuration.getEnvironment();
			if(Constants.PRODUCTION_ENVIRONMENT.equals(env)) {
				mode = "production";
			}
			
			Properties sdkConfig = new Properties();
			sdkConfig.put("mode", mode);
			sdkConfig.put("acct1.UserName", configuration.getIntegrationKeys().get("account"));
			sdkConfig.put("acct1.Password", configuration.getIntegrationKeys().get("api"));
			sdkConfig.put("acct1.Signature", configuration.getIntegrationKeys().get("signature"));
			
			PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(sdkConfig);
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
			
			
			
			
			/**
			 * RETURN URL
			 * 
			 *  get token from url and return the user to generate a payerid
			 *  
			 *  GetExpressCheckoutDetailsRequestType getExpressCheckoutDetailsRequest = new GetExpressCheckoutDetailsRequestType("EC-9VT64354BS889423P");
				getExpressCheckoutDetailsRequest.setVersion("104.0");

				GetExpressCheckoutDetailsReq getExpressCheckoutDetailsReq = new GetExpressCheckoutDetailsReq();
				getExpressCheckoutDetailsReq.setGetExpressCheckoutDetailsRequest(getExpressCheckoutDetailsRequest);

				Map<String, String> sdkConfig = new HashMap<String, String>();
				sdkConfig.put("mode", "sandbox");
				sdkConfig.put("acct1.UserName", "jb-us-seller_api1.paypal.com");
				sdkConfig.put("acct1.Password", "WX4WTU3S8MY44S7F");
				sdkConfig.put("acct1.Signature","AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy");
				PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(sdkConfig);
				GetExpressCheckoutDetailsResponseType getExpressCheckoutDetailsResponse = service.getExpressCheckoutDetails(getExpressCheckoutDetailsReq);
				TOKEN=EC-9VT64354BS889423P&CHECKOUTSTATUS=PaymentActionNotInitiated&TIMESTAMP=2014-01-26T17:30:17Z&CORRELATIONID=84dfe1d0939cc&ACK=Success&VERSION=104.0&BUILD=9285531&EMAIL=csamson777-facilitator@yahoo.com&PAYERID=XURV79Z6URDV4&PAYERSTATUS=verified&BUSINESS=facilitator account's Test Store&FIRSTNAME=facilitator&LASTNAME=account&COUNTRYCODE=US&SHIPTONAME=facilitator account's Test Store&SHIPTOSTREET=1 Main St&SHIPTOCITY=San Jose&SHIPTOSTATE=CA&SHIPTOZIP=95131&SHIPTOCOUNTRYCODE=US&SHIPTOCOUNTRYNAME=United States&ADDRESSSTATUS=Confirmed&CURRENCYCODE=USD&AMT=1.00&ITEMAMT=1.00&SHIPPINGAMT=0.00&HANDLINGAMT=0.00&TAXAMT=0.00&INSURANCEAMT=0.00&SHIPDISCAMT=0.00&L_NAME0=item&L_QTY0=1&L_TAXAMT0=0.00&L_AMT0=1.00&L_ITEMWEIGHTVALUE0=   0.00000&L_ITEMLENGTHVALUE0=   0.00000&L_ITEMWIDTHVALUE0=   0.00000&L_ITEMHEIGHTVALUE0=   0.00000&PAYMENTREQUEST_0_CURRENCYCODE=USD&PAYMENTREQUEST_0_AMT=1.00&PAYMENTREQUEST_0_ITEMAMT=1.00&PAYMENTREQUEST_0_SHIPPINGAMT=0.00&PAYMENTREQUEST_0_HANDLINGAMT=0.00&PAYMENTREQUEST_0_TAXAMT=0.00&PAYMENTREQUEST_0_INSURANCEAMT=0.00&PAYMENTREQUEST_0_SHIPDISCAMT=0.00&PAYMENTREQUEST_0_INSURANCEOPTIONOFFERED=false&PAYMENTREQUEST_0_SHIPTONAME=facilitator account's Test Store&PAYMENTREQUEST_0_SHIPTOSTREET=1 Main St&PAYMENTREQUEST_0_SHIPTOCITY=San Jose&PAYMENTREQUEST_0_SHIPTOSTATE=CA&PAYMENTREQUEST_0_SHIPTOZIP=95131&PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE=US&PAYMENTREQUEST_0_SHIPTOCOUNTRYNAME=United States&PAYMENTREQUEST_0_ADDRESSSTATUS=Confirmed&PAYMENTREQUEST_0_ADDRESSNORMALIZATIONSTATUS=None&L_PAYMENTREQUEST_0_NAME0=item&L_PAYMENTREQUEST_0_QTY0=1&L_PAYMENTREQUEST_0_TAXAMT0=0.00&L_PAYMENTREQUEST_0_AMT0=1.00&L_PAYMENTREQUEST_0_ITEMWEIGHTVALUE0=   0.00000&L_PAYMENTREQUEST_0_ITEMLENGTHVALUE0=   0.00000&L_PAYMENTREQUEST_0_ITEMWIDTHVALUE0=   0.00000&L_PAYMENTREQUEST_0_ITEMHEIGHTVALUE0=   0.00000&PAYMENTREQUESTINFO_0_ERRORCODE=0 
			 */
			
			/**
			 * COMMIT TRANSACTION
			 * 
			 *  commit transaction
			 *  
			 *  PaymentDetailsType paymentDetail = new PaymentDetailsType();
				paymentDetail.setNotifyURL("http://replaceIpnUrl.com");
				BasicAmountType orderTotal = new BasicAmountType();
				orderTotal.setValue(1.00);
				orderTotal.setCurrencyID(CurrencyCodeType.fromValue("USD"));
				paymentDetail.setOrderTotal(orderTotal);
				paymentDetail.setPaymentAction(PaymentActionCodeType.fromValue("Sale"));
				List<PaymentDetailsType> paymentDetails = new ArrayList<PaymentDetailsType>();
				paymentDetails.add(paymentDetail);
								
				DoExpressCheckoutPaymentRequestDetailsType doExpressCheckoutPaymentRequestDetails = new DoExpressCheckoutPaymentRequestDetailsType();
				doExpressCheckoutPaymentRequestDetails.setToken("EC-9VT64354BS889423P");
				doExpressCheckoutPaymentRequestDetails.setPayerID("XURV79Z6URDV4");
				doExpressCheckoutPaymentRequestDetails.setPaymentDetails(paymentDetails);
				
				DoExpressCheckoutPaymentRequestType doExpressCheckoutPaymentRequest = new DoExpressCheckoutPaymentRequestType(doExpressCheckoutPaymentRequestDetails);
				doExpressCheckoutPaymentRequest.setVersion("104.0");
				
				DoExpressCheckoutPaymentReq doExpressCheckoutPaymentReq = new DoExpressCheckoutPaymentReq();
				doExpressCheckoutPaymentReq.setDoExpressCheckoutPaymentRequest(doExpressCheckoutPaymentRequest);
				
				Map<String, String> sdkConfig = new HashMap<String, String>();
				sdkConfig.put("mode", "sandbox");
				sdkConfig.put("acct1.UserName", "jb-us-seller_api1.paypal.com");
				sdkConfig.put("acct1.Password", "WX4WTU3S8MY44S7F");
				sdkConfig.put("acct1.Signature", "AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy");
				PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(sdkConfig);
				DoExpressCheckoutPaymentResponseType doExpressCheckoutPaymentResponse = service.doExpressCheckoutPayment(doExpressCheckoutPaymentReq); 
			 * 
			 * 
			 */
			

			return transaction;
			
			
		} catch(Exception e) {
			throw new IntegrationException(e);
		}

		
	}

	@Override
	public Transaction refund(boolean partial, MerchantStore store,
			Transaction transaction, BigDecimal amount,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}

}
