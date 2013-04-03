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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.service.OrderServiceImpl;
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
import com.salesmanager.core.utils.CreditCardUtils;
import com.salesmanager.core.utils.ProductPriceUtils;

public class BeanStreamPayment implements PaymentModule {
	
	@Autowired
	private ProductPriceUtils productPriceUtils;
	
	@Autowired
	private MerchantLogService merchantLogService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanStreamPayment.class);

	@Override
	public Transaction initTransaction(Customer customer, Order order,
			BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction authorize(Customer customer, Order order,
			BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction capture(Customer customer, Order order,
			BigDecimal amount, Payment payment, Transaction transaction, 
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
				
				String amnt = productPriceUtils.getAdminFormatedAmount(order.getMerchant(), order.getTotal());
				
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
		
				
			
				
				URL postURL = new URL(server.toString());
				conn = (HttpURLConnection) postURL.openConnection();
				
	
				Transaction response = this.sendTransaction(messageString.toString(), "PAC", order, conn);
				
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

	@Override
	public Transaction authorizeAndCapture(Customer customer, Order order,
			BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
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
	
	
	private Transaction sendTransaction(String transaction, String type, Order order, HttpURLConnection conn) throws IntegrationException {
		
		String agent = "Mozilla/4.0";
		String respText = "";
		Map<String,String> nvp = null;
		DataOutputStream output = null;
		DataInputStream in = null;
		BufferedReader is = null;
		try {
			

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
						new MerchantLog(order.getMerchant(),
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

		}

		
	}
	
	
	
	private Transaction processTransaction(TransactionType type,
			Order order,
			BigDecimal amount, Payment payment,
			IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
		

		
		
		
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
		
		HttpURLConnection conn = null;
		
		try {
			
		
		String orderNumber = new StringBuilder().append(order.getId()).append(new Date().getTime()).toString();
		
		String amnt = productPriceUtils.getAdminFormatedAmount(order.getMerchant(), order.getTotal());
		
		
		StringBuilder messageString = new StringBuilder();
		

		messageString.append("requestType=BACKEND&");
		messageString.append("merchant_id=").append(configuration.getIntegrationKeys().get("MERCHANT_ID")).append("&");
		messageString.append("trnType=").append(type).append("&");
		messageString.append("orderNumber=").append(orderNumber).append("&");
		messageString.append("trnCardOwner=").append(order.getCcOwner()).append("&");
		messageString.append("trnCardNumber=").append(order.getCcNumber()).append("&");
		messageString.append("trnExpMonth=").append(order.getCcExpires().substring(0, 2)).append("&");
		messageString.append("trnExpYear=").append(order.getCcExpires().substring(2,
				order.getCcExpires().length())).append("&");
		messageString.append("trnCardCvd=").append(order.getCcCvv()).append("&");
		messageString.append("trnAmount=").append(amnt).append("&");
		messageString.append("ordName=").append(order.getBilling().getName()).append("&");
		messageString.append("ordAddress1=").append(order.getBilling().getAddress()).append("&");
		messageString.append("ordCity=").append(order.getBilling().getCity()).append("&");
		

		
		messageString.append("ordProvince=").append(order.getBilling().getState()).append("&");
		messageString.append("ordPostalCode=").append(order.getBilling().getPostalCode()).append("&");
		messageString.append("ordCountry=").append(order.getCustomerCountry()).append("&");
		messageString.append("ordPhoneNumber=").append(order.getCustomerTelephone()).append("&");
		messageString.append("ordEmailAddress=").append(order.getCustomerEmailAddress());
		
		
		
		
		/**
		 * 	purchase (P)
		 *  -----------
				REQUEST -> merchant_id=123456789&requestType=BACKEND&trnType=P&trnOrderNumber=1234TEST&trnAmount=5.00&trnCardOwner=Joe+Test&trnCardNumber=4030000010001234&trnExpMonth=10&trnExpYear=10&ordName=Joe+Test&ordAddress1=123+Test+Street&ordCity=Victoria&ordProvince=BC&ordCountry=CA&ordPostalCode=V8T2E7&ordPhoneNumber=5555555555&ordEmailAddress=joe%40testemail.com
				RESPONSE-> trnApproved=1&trnId=10003067&messageId=1&messageText=Approved&trnOrderNumber=E40089&authCode=TEST&errorType=N&errorFields=&responseType=T&trnAmount=10%2E00&trnDate=1%2F17%2F2008+11%3A36%3A34+AM&avsProcessed=0&avsId=0&avsResult=0&avsAddrMatch=0&avsPostalMatch=0&avsMessage=Address+Verification+not+performed+for+this+transaction%2E&rspCodeCav=0&rspCavResult=0&rspCodeCredit1=0&rspCodeCredit2=0&rspCodeCredit3=0&rspCodeCredit4=0&rspCodeAddr1=0&rspCodeAddr2=0&rspCodeAddr3=0&rspCodeAddr4=0&rspCodeDob=0&rspCustomerDec=&trnType=P&paymentMethod=CC&ref1=&ref2=&ref3=&ref4=&ref5=
		
			pre authorization (PA)
			----------------------

			Prior to processing a pre-authorization through the API, you must modify the transaction settings in your Beanstream merchant member area to allow for this transaction type.
			- Log in to the Beanstream online member area at www.beanstream.com/admin/sDefault.asp.
			- Navigate to administration - account admin - order settings in the left menu.
			Under the heading �Restrict Internet Transaction Processing Types,� select either of the last two options. The �Purchases or Pre-Authorization Only� option will allow you to process both types of transaction through your web interface. De-selecting the �Restrict Internet Transaction Processing Types� checkbox will allow you to process all types of transactions including returns, voids and pre-auth completions.
		
			capture (PAC) -> requires trnId
			-------------
		
			refund (R)
			-------------
				REQUEST -> merchant_id=123456789&requestType=BACKEND&trnType=R&username=user1234&password=pass1234&trnOrderNumber=1234&trnAmount=1.00&adjId=10002115
				RESPONSE-> trnApproved=1&trnId=10002118&messageId=1&messageText=Approved&trnOrderNumber=1234R&authCode=TEST&errorType=N&errorFields=&responseType=T&trnAmount=1%2E00&trnDate=8%2F17%2F2009+1%3A44%3A56+PM&avsProcessed=0&avsId=0&avsResult=0&avsAddrMatch=0&avsPostalMatch=0&avsMessage=Address+Verification+not+performed+for+this+transaction%2E&cardType=VI&trnType=R&paymentMethod=CC&ref1=&ref2=&ref3=&ref4=&ref5=
		

			//notes
			//On receipt of the transaction response, the merchant must display order amount, transaction ID number, bank authorization code (authCode), currency, date and �messageText� to the customer on a confirmation page.
		*/
		

		//String agent = "Mozilla/4.0";
		//String respText = "";
		//Map nvp = null;
		
		
		/** debug **/
		
		

			StringBuffer messageLogString = new StringBuffer();
			
			
			messageLogString.append("requestType=BACKEND&");
			messageLogString.append("merchant_id=").append(configuration.getIntegrationKeys().get("MERCHANT_ID")).append("&");
			messageLogString.append("trnType=").append(type).append("&");
			messageLogString.append("orderNumber=").append(orderNumber).append("&");
			messageLogString.append("trnCardOwner=").append(order.getCcOwner()).append("&");
			messageLogString.append("trnCardNumber=").append(CreditCardUtils.maskCardNumber(order.getCcNumber())).append("&");
			messageLogString.append("trnExpMonth=").append(order.getCcExpires().substring(0, 2)).append("&");
			messageString.append("trnExpYear=").append(order.getCcExpires().substring(2,
					order.getCcExpires().length())).append("&");
			messageLogString.append("trnCardCvd=").append(order.getCcCvv()).append("&");
			messageLogString.append("trnAmount=").append(amnt).append("&");
			messageLogString.append("ordName=").append(order.getBilling().getName()).append("&");
			messageLogString.append("ordAddress1=").append(order.getBilling().getAddress()).append("&");
			messageLogString.append("ordCity=").append(order.getBilling().getCity()).append("&");
			

			
			messageLogString.append("ordProvince=").append(order.getBilling().getState()).append("&");
			messageLogString.append("ordPostalCode=").append(order.getBilling().getPostalCode()).append("&");
			messageLogString.append("ordCountry=").append(order.getCustomerCountry()).append("&");
			messageLogString.append("ordPhoneNumber=").append(order.getCustomerTelephone()).append("&");
			messageLogString.append("ordEmailAddress=").append(order.getCustomerEmailAddress());
			
		

			/** debug **/
	
	
			LOGGER.debug("REQUEST SENT TO BEANSTREAM -> " + messageLogString.toString());

		

		
		
		//DataOutputStream output = null;
		//DataInputStream in = null;
		//BufferedReader is = null;
	
			
			URL postURL = new URL(server.toString());
			conn = (HttpURLConnection) postURL.openConnection();
			


			
			Transaction response = this.sendTransaction(messageString.toString(), type.name(), order, conn);
			
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
	
	
	
	private Transaction parseResponse(TransactionType transactionType,
			PaymentType paymentType, Map<String,String> nvp,
			Order order, BigDecimal amount) throws Exception {
		
		
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setOrder(order);
		transaction.setTransactionDate(new Date());
		transaction.setTransactionType(transactionType);
		transaction.setPaymentType(paymentType);
		transaction.getTransactionDetails().put("TRNAPPROVED", (String)nvp.get("TRNAPPROVED"));
		transaction.getTransactionDetails().put("TRNORDERNUMBER", (String)nvp.get("TRNORDERNUMBER"));
		transaction.getTransactionDetails().put("MESSAGETEXT", (String)nvp.get("MESSAGETEXT"));
		
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



}
