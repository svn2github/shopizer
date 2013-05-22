package com.salesmanager.core.modules.integration.payment.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.aim.Result;
import net.authorize.data.creditcard.CreditCard;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.payments.model.CreditCardPayment;
import com.salesmanager.core.business.payments.model.Payment;
import com.salesmanager.core.business.payments.model.PaymentType;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.payments.model.TransactionType;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.business.system.model.MerchantLog;
import com.salesmanager.core.business.system.service.MerchantLogService;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.payment.model.PaymentModule;
import com.salesmanager.core.utils.ProductPriceUtils;

public class AuthorizeNetPayment implements PaymentModule {

	
	@Autowired
	private ProductPriceUtils productPriceUtils;
	
	@Autowired
	private MerchantLogService merchantLogService;
	

	
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanStreamPayment.class);

	
	

	public Transaction initTransaction(MerchantStore store, Customer customer,
			BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}


	public Transaction authorize(MerchantStore store, Customer customer,
			BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		// TODO Auto-generated method stub
		return processTransaction(
				store,
				TransactionType.AUTHORIZE,
				amount,
				payment,
				configuration,
				module);
	}


	public Transaction capture(MerchantStore store, Customer customer,
			BigDecimal amount, Payment payment, Transaction transaction, 
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {


			try {
				

				
				//authorize a preauth 

		
				String trnID = transaction.getTransactionDetails().get("TRANSACTIONID");
				
				String amnt = productPriceUtils.getAdminFormatedAmount(store, amount);
				
				/**
				merchant_id=123456789&requestType=BACKEND
				&trnType=PAC&username=user1234&password=pass1234&trnID=1000
				2115 --> requires also adjId [not documented]
				**/
				
				StringBuilder messageString = new StringBuilder();
				messageString.append("requestType=BACKEND&");
				messageString.append("merchant_id=").append(configuration.getIntegrationKeys().get("MERCHANT_ID")).append("&");
				messageString.append("trnType=").append("PAC").append("&");
				messageString.append("username=").append(configuration.getIntegrationKeys().get("USER_ID")).append("&");
				messageString.append("password=").append(configuration.getIntegrationKeys().get("PASSWORD")).append("&");
				messageString.append("trnAmount=").append(amnt).append("&");
				messageString.append("adjId=").append(trnID).append("&");
				messageString.append("trnID=").append(trnID);
				
				LOGGER.debug("REQUEST SENT TO BEANSTREAM -> " + messageString.toString());
		


				Transaction response = this.sendTransaction(store, messageString.toString(), "PAC", TransactionType.CAPTURE, payment.getPaymentType(), amount, configuration, module);
				
				return response;
				
			} catch(Exception e) {
				
				if(e instanceof IntegrationException)
					throw (IntegrationException)e;
				throw new IntegrationException("Error while processing BeanStream transaction",e);
	
			} 

	}


	public Transaction authorizeAndCapture(MerchantStore store, Customer customer,
			BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		return processTransaction(
				store,
				TransactionType.AUTHORIZECAPTURE,
				amount,
				payment,
				configuration,
				module);
	}


	public Transaction refund(boolean partial, MerchantStore store, Transaction transaction,
			BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {

		
		
		
		HttpURLConnection conn = null;
		
		try {
			
			
			boolean bSandbox = false;
			if (configuration.getEnvironment().equals("TEST")) {// sandbox
				bSandbox = true;
			}

			String server = "";


			if (bSandbox == true) {
				server = new StringBuffer().append(
						module.getModuleConfigs().get("scheme")).append("://")
						.append(module.getModuleConfigs().get("host")).append(
								module.getModuleConfigs().get("uri")).toString();
			} else {

				server = new StringBuffer().append(
						module.getModuleConfigs().get("scheme")).append("://")
						.append(module.getModuleConfigs().get("host")).append(
								module.getModuleConfigs().get("uri")).toString();
			}
			
			
			//authorize a preauth 

	
			String trnID = transaction.getTransactionDetails().get("TRANSACTIONID");
			
			String amnt = productPriceUtils.getAdminFormatedAmount(store, amount);
			
			/**
			merchant_id=123456789&requestType=BACKEND
			&trnType=R&username=user1234&password=pass1234
			&trnOrderNumber=1234&trnAmount=1.00&adjId=1000
			2115
			**/
			StringBuilder messageString = new StringBuilder();



			messageString.append("requestType=BACKEND&");
			messageString.append("merchant_id=").append(configuration.getIntegrationKeys().get("MERCHANT_ID")).append("&");
			messageString.append("trnType=").append("R").append("&");
			messageString.append("username=").append(configuration.getIntegrationKeys().get("USER_ID")).append("&");
			messageString.append("password=").append(configuration.getIntegrationKeys().get("PASSWORD")).append("&");
			messageString.append("trnOrderNumber=").append(transaction.getTransactionDetails().get("TRNORDERNUMBER")).append("&");
			messageString.append("trnAmount=").append(amnt).append("&");
			messageString.append("adjId=").append(trnID);
			
			LOGGER.debug("REQUEST SENT TO BEANSTREAM -> " + messageString.toString());
	
			
		
			
			URL postURL = new URL(server.toString());
			conn = (HttpURLConnection) postURL.openConnection();
			


			
			Transaction response = this.sendTransaction(store, messageString.toString(), "R", TransactionType.REFUND, payment.getPaymentType(), amount, configuration, module);
			
			return response;
			
		} catch(Exception e) {
			
			if(e instanceof IntegrationException)
				throw (IntegrationException)e;
			throw new IntegrationException("Error while processing BeanStream transaction",e);

		} finally {
			
			
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}
		}
		
		
		
	}
	
	
	private Transaction sendTransaction(
			MerchantStore store,
			String transaction, 
			String beanstreamType, 
			TransactionType transactionType,
			PaymentType paymentType,
			BigDecimal amount,
			IntegrationConfiguration configuration,
			IntegrationModule module
			) throws IntegrationException {
		
		String agent = "Mozilla/4.0";
		String respText = "";
		Map<String,String> nvp = null;
		DataOutputStream output = null;
		DataInputStream in = null;
		BufferedReader is = null;
		HttpURLConnection conn =null;
		try {
			
			boolean bSandbox = false;
			if (configuration.getEnvironment().equals("TEST")) {// sandbox
				bSandbox = true;
			}

			String server = "";


			if (bSandbox == true) {
				server = new StringBuffer().append(
						module.getModuleConfigs().get("scheme")).append("://")
						.append(module.getModuleConfigs().get("host")).append(
								module.getModuleConfigs().get("uri")).toString();
			} else {

				server = new StringBuffer().append(
						module.getModuleConfigs().get("scheme")).append("://")
						.append(module.getModuleConfigs().get("host")).append(
								module.getModuleConfigs().get("uri")).toString();
			}
			
			
			URL postURL = new URL(server.toString());
			conn = (HttpURLConnection) postURL.openConnection();
			

			// Set connection parameters. We need to perform input and output,
			// so set both as true.
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// Set the content type we are POSTing. We impersonate it as
			// encoded form data
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("User-Agent", agent);

			conn.setRequestProperty("Content-Length", String
					.valueOf(transaction.length()));
			conn.setRequestMethod("POST");

			// get the output stream to POST to.
			output = new DataOutputStream(conn.getOutputStream());
			output.writeBytes(transaction);
			output.flush();


			// Read input from the input stream.
			in = new DataInputStream(conn.getInputStream());
			int rc = conn.getResponseCode();
			if (rc != -1) {
				is = new BufferedReader(new InputStreamReader(conn
						.getInputStream()));
				String _line = null;
				while (((_line = is.readLine()) != null)) {
					respText = respText + _line;
				}
				
				LOGGER.debug("BeanStream response -> " + respText.trim());
				
				nvp = formatUrlResponse(respText.trim());
			} else {
				throw new IntegrationException("Invalid response from BeanStream, return code is " + rc);
			}
			
			//check
			//trnApproved=1&trnId=10003067&messageId=1&messageText=Approved&trnOrderNumber=E40089&authCode=TEST&errorType=N&errorFields=

			String transactionApproved = (String)nvp.get("TRNAPPROVED");
			String transactionId = (String)nvp.get("TRNID");
			String messageId = (String)nvp.get("MESSAGEID");
			String messageText = (String)nvp.get("MESSAGETEXT");
			String orderId = (String)nvp.get("TRNORDERNUMBER");
			String authCode = (String)nvp.get("AUTHCODE");
			String errorType = (String)nvp.get("ERRORTYPE");
			String errorFields = (String)nvp.get("ERRORFIELDS");
			
			
			if(StringUtils.isBlank(transactionApproved)) {
				throw new IntegrationException("Required field transactionApproved missing from BeanStream response");
			}
			
			//errors
			if(transactionApproved.equals("0")) {

				merchantLogService.save(
						new MerchantLog(store,
						"Can't process BeanStream message "
								 + messageText + " return code id " + messageId));
	
				IntegrationException te = new IntegrationException(
						"Can't process BeanStream message " + messageText);
				te
				.setErrorCode(IntegrationException.TRANSACTION_EXCEPTION);
				throw te;
			}
			
			//create transaction object

			//return parseResponse(type,transaction,respText,nvp,order);
			//return this.parseResponse(transactionType, paymentType, nvp, amount);
			return null;
			
			
		} catch(Exception e) {
			if(e instanceof IntegrationException) {
				throw (IntegrationException)e;
			}
			
			throw new IntegrationException("Error while processing BeanStream transaction",e);

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

			if (output != null) {
				try {
					output.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}
			
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

		}

		
	}
	
	
	
	
	private Transaction processTransaction(MerchantStore store, TransactionType type,
			BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
		

		
		
		
		boolean bSandbox = false;
		if (configuration.getEnvironment().equals("TEST")) {// sandbox
			bSandbox = true;
		}

		
		try {
			
		
		//String orderNumber = new StringBuilder().append(order.getId()).append(new Date().getTime()).toString();
		String orderNumber =UUID.randomUUID().toString();

		
		Environment env = Environment.PRODUCTION;
		if(bSandbox) {
			env = Environment.SANDBOX;
		}
		
		net.authorize.TransactionType trx = net.authorize.TransactionType.AUTH_CAPTURE;
		if(type.name().equals(TransactionType.AUTHORIZE)) {
			trx = net.authorize.TransactionType.AUTH_ONLY;
		}
		
	    String apiLoginID = configuration.getIntegrationKeys().get("appLoginId");
	    String transactionKey = configuration.getIntegrationKeys().get("transactionId");
	    Merchant merchant = Merchant.createMerchant(env,
	        apiLoginID, transactionKey);
	    
	    CreditCardPayment creditCardPayment = (CreditCardPayment)payment;

	    // create credit card
	    CreditCard creditCard = CreditCard.createCreditCard();
	    creditCard.setCreditCardNumber(creditCardPayment.getCreditCardNumber());
	    creditCard.setExpirationMonth(creditCardPayment.getExpirationMonth());
	    creditCard.setExpirationYear(creditCardPayment.getExpirationYear());

	    // create transaction
	    net.authorize.aim.Transaction transaction = merchant.createAIMTransaction(
	        trx, amount);
	    transaction.setCreditCard(creditCard);

	    Result<Transaction> result = (Result<Transaction>)merchant
	        .postTransaction(transaction);


	    
	    if(result.isApproved()) {
	      System.out.println("Approved!</br>");
	      System.out.println("Transaction Id: " + result.getTarget().getId());
	    } else if (result.isDeclined()) {
	    	System.out.println("Declined.</br>");
	    	System.out.println(result.getReasonResponseCode() + " : " + result.getResponseText());
	    } else {
	      System.out.println("Error.</br>");
	      System.out.println(result.getReasonResponseCode() + " : " + result.getResponseText());
	    }

			
			//Transaction response = this.sendTransaction(messageString.toString(), transactionType, type, payment.getPaymentType(), order, amount, configuration, module);
			
			//return response;
	    	return null;


			
		} catch(Exception e) {
			
			if(e instanceof IntegrationException)
				throw (IntegrationException)e;
			throw new IntegrationException("Error while processing AuthorizeNet transaction",e);

		} 

	}
	
	
	
	private Transaction parseResponse(TransactionType transactionType,
			PaymentType paymentType, Result result,
			Order order, BigDecimal amount) throws Exception {
		
		
		//result.
		
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setOrder(order);
		transaction.setTransactionDate(new Date());
		transaction.setTransactionType(transactionType);
		transaction.setPaymentType(paymentType);
		//transaction.getTransactionDetails().put("TRANSACTIONID", result.);
		//transaction.getTransactionDetails().put("TRNAPPROVED", (String)nvp.get("TRNAPPROVED"));
		//transaction.getTransactionDetails().put("TRNORDERNUMBER", (String)nvp.get("TRNORDERNUMBER"));
		//transaction.getTransactionDetails().put("MESSAGETEXT", (String)nvp.get("MESSAGETEXT"));
		
		return transaction;
		
	}

	private Map formatUrlResponse(String payload) throws Exception {
		HashMap<String,String> nvp = new HashMap<String,String> ();
		StringTokenizer stTok = new StringTokenizer(payload, "&");
		while (stTok.hasMoreTokens()) {
			StringTokenizer stInternalTokenizer = new StringTokenizer(stTok
					.nextToken(), "=");
			if (stInternalTokenizer.countTokens() == 2) {
				String key = URLDecoder.decode(stInternalTokenizer.nextToken(),
						"UTF-8");
				String value = URLDecoder.decode(stInternalTokenizer
						.nextToken(), "UTF-8");
				nvp.put(key.toUpperCase(), value);
			}
		}
		return nvp;
	}


	public void validateModuleConfiguration(
			IntegrationConfiguration integrationConfiguration,
			MerchantStore store) throws IntegrationException {
		// TODO Auto-generated method stub
		
	}


}
