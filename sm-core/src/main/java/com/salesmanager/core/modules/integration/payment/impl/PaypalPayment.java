package com.salesmanager.core.modules.integration.payment.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.payments.model.Payment;
import com.salesmanager.core.business.payments.model.PaymentType;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.payments.model.TransactionType;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.model.MerchantLog;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.business.system.service.MerchantLogService;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.payment.model.PaymentModule;
import com.salesmanager.core.utils.ProductPriceUtils;

public class PaypalPayment implements PaymentModule {
	
	@Autowired
	private ProductPriceUtils productPriceUtils;
	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;
	
	@Autowired
	private MerchantLogService merchantLogService;
	
	
	
	
	

	@Override
	public Transaction initTransaction(MerchantStore store, Customer customer,
			BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
		// TODO Auto-generated method stub
		
		
/*		ConfigurationRequest vo = new ConfigurationRequest(order
				.getMerchantId());
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		String paymentType = "Sale";

		IntegrationProperties properties;

		try {

			ConfigurationResponse config = mservice.getConfigurationByModule(
					vo, PaymentConstants.PAYMENT_PAYPALNAME);
			if (config == null) {
				throw new TransactionException("Payment module "
						+ PaymentConstants.PAYMENT_PAYPALNAME
						+ " cannot be retreived from MerchantConfiguration");
			}
			properties = (IntegrationProperties) config
					.getConfiguration("properties");

		} catch (Exception e) {
			throw new TransactionException(e);
		}

		if (properties == null) {
			throw new TransactionException(
					"Integration properties are null for merchantId "
							+ order.getMerchantId());
		}*/
		
		//Sale - Authorization
		String transactionType = configuration.getIntegrationKeys().get("TRANSACTION_TYPE");

		/*
		 * '------------------------------------ ' The currencyCodeType and
		 * paymentType ' are set to the selections made on the Integration
		 * Assistant '------------------------------------
		 */

		

		/*
		 * Construct the parameter string that describes the PayPal payment the
		 * varialbes were set in the web form, and the resulting string is
		 * stored in $nvpstr
		 */

		String nvpstr = null;
		
		try {
		
		//TODO add $ ?
		String sAmount = productPriceUtils.getAdminFormatedAmount(store, amount);


		MerchantConfiguration returnUrl = merchantConfigurationService.getMerchantConfiguration("PAYPAL_RETURN_URL", store);

		if(returnUrl==null) {
			throw new IntegrationException("Paypal return url not configured");
		}
		

		String sLocale = "US";// default to US english

		/**
		 * supports AU DE FR GB IT ES JP US
		 **/


			String country = store.getCountry().getIsoCode();
			if ("AU".equals(country)) {
				sLocale = "AU";
			} else if ("DE".equals(country)) {
				sLocale = "DE";
			} else if ("FR".equals(country)) {
				sLocale = "FR";
			} else if ("GB".equals(country)) {
				sLocale = "GB";
			} else if ("IT".equals(country)) {
				sLocale = "IT";
			} else if ("ES".equals(country)) {
				sLocale = "ES";
			} else if ("JP".equals(country)) {
				sLocale = "JP";
			}


		

			String cancelUrl = "";

			String sReturnUrl = returnUrl.getValue();

			nvpstr = "&METHOD=SetExpressCheckout&Amt="
					+ amount
					+ "&PAYMENTACTION="
					+ transactionType
					+ "&LOCALECODE="
					+ sLocale
					+ "&ReturnUrl="
					+ sReturnUrl
					+ "&CANCELURL="
					+ cancelUrl
					+ "&CURRENCYCODE=" + store.getCurrency();
		} catch (Exception e) {
			throw new IntegrationException(e);
		}

		/*
		 * Make the call to PayPal to get the Express Checkout token If the API
		 * call succeded, then redirect the buyer to PayPal to begin to
		 * authorize payment. If an error occured, show the resulting errors
		 */

		Map<String,String> nvp = null;

		try {
			nvp = httpcall(configuration, module, "SetExpressCheckout",
					nvpstr);
		} catch (Exception e) {
			//log.error(e);
			throw new IntegrationException(e);
		}

		String strAck = nvp.get("ACK").toString();
		if (strAck != null && strAck.equalsIgnoreCase("Success")) {
			nvp.put("PAYMENTTYPE", transactionType);
			
			Transaction transaction = new Transaction();
			transaction.setTransactionDetails(nvp);
			
			return transaction;
		} else {
			//LogMerchantUtil.log(order.getMerchantId(), (String) nvp
			//		.get("L_LONGMESSAGE0"));
			//log.error("Error with paypal transaction "
			//		+ (String) nvp.get("L_LONGMESSAGE0"));
			return null;
		}
		

	}

	@Override
	public Transaction authorize(MerchantStore store, Customer customer,
			List<ShoppingCartItem> items, BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction capture(MerchantStore store, Customer customer,
			List<ShoppingCartItem> items, BigDecimal amount, com.salesmanager.core.business.payments.model.Payment payment, Transaction trx, IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
		// TODO Auto-generated method stub
		
		
		
		

/*		ConfigurationRequest vo = new ConfigurationRequest(order
				.getMerchantId());
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		IntegrationProperties properties;

		String nvpstr = "";

		try {

			ConfigurationResponse config = mservice.getConfigurationByModule(
					vo, PaymentConstants.PAYMENT_PAYPALNAME);
			if (config == null) {
				throw new TransactionException("Payment module "
						+ PaymentConstants.PAYMENT_PAYPALNAME
						+ " cannot be retreived from MerchantConfiguration");
			}
			properties = (IntegrationProperties) config
					.getConfiguration("properties");

		} catch (Exception e) {
			throw new TransactionException(e);
		}

		if (properties == null) {
			throw new TransactionException(
					"Integration properties are null for merchantId "
							+ order.getMerchantId());
		}*/

		/*
		 * '------------------------------------ ' The currencyCodeType and
		 * paymentType ' are set to the selections made on the Integration
		 * Assistant '------------------------------------
		 */
		
		String nvpstr = "";

		//PREAUTH - SALE
		String transactionType = configuration.getIntegrationKeys().get("TRANSACTION_TYPE");

		TransactionType trType = TransactionType.CAPTURE;
		
		if(transactionType.equals("PREAUTH")) {
			trType = TransactionType.AUTHORIZE;
		}

		/*
		 * '------------------------------------ ' The currencyCodeType and
		 * paymentType ' are set to the selections made on the Integration
		 * Assistant '------------------------------------
		 */
		String currencyCodeType = store.getCurrency().getCode();

		com.salesmanager.core.business.payments.model.PaypalPayment paypalPayment = ((com.salesmanager.core.business.payments.model.PaypalPayment)payment);

		
		String token = paypalPayment.getPaymentToken();
		String payerID = paypalPayment.getPayerId();

		if (token == null) {
			throw new IntegrationException(
					"processTransaction Paypal TOKEN is null in PaypalPayment");
		}

		if (payerID == null) {
			throw new IntegrationException(
					"processTransaction Paypal PAYERID is null in PaypalPayment");
		}
		
		try {
		
		String sAmount = productPriceUtils.getAdminFormatedAmount(store, amount);

/*		String amount = CurrencyUtil.displayFormatedAmountNoCurrency(order
				.getTotal(), Constants.CURRENCY_CODE_USD);
		if (order.getTotal().toString().equals(
				new BigDecimal("0.00").toString())) {
			// check if recuring
			if (order.getRecursiveAmount().floatValue() > 0) {
				amount = CurrencyUtil.displayFormatedAmountNoCurrency(order
						.getRecursiveAmount(), order.getCurrency());
			} else {
				amount = "0";
			}
		}*/

		

			// InetAddress addr = InetAddress.getLocalHost();

			// Get IP Address
			// byte[] ipAddr = addr.getAddress();

			// String ip = new String(ipAddr);

			// IPN
			// String ipnUrl = ReferenceUtil.buildSecureServerUrl() +
			// (String)conf.getString("core.salesmanager.checkout.paypalIpn");
		
			String uniqueId = UUID.randomUUID().toString();


			nvpstr = "&TOKEN=" + token + "&PAYERID=" + payerID
					+ "&PAYMENTACTION=" + transactionType + "&AMT=" + sAmount
					+ "&INVNUM=" + uniqueId;

			// nvpstr = nvpstr + "&CURRENCYCODE=" + currencyCodeType +
			// "&IPADDRESS=" + ip.toString();
			nvpstr = nvpstr + "&CURRENCYCODE=" + currencyCodeType;

			
			Map<String,String> nvp = httpcall(configuration, module, "DoExpressCheckoutPayment",
					nvpstr);
			

			String strAck = nvp.get("ACK").toString();
			
			String transactionId = (String) nvp.get("TRANSACTIONID");
			String correlationId = (String) nvp.get("CORRELATIONID");
			
			StringBuilder valueBuffer = new StringBuilder();
			for (String key : nvp.keySet()) {
				valueBuffer.append("[").append(key).append("=").append(
						(String) nvp.get(key)).append("]");
			}
			

			

			
			if (strAck != null && strAck.equalsIgnoreCase("Success")) {
				
				
				
				Transaction transaction = new Transaction();
				transaction.setAmount(amount);
				//transaction.setOrder(order);
				transaction.setTransactionDate(new Date());
				transaction.setTransactionType(trType);
				transaction.setPaymentType(payment.getPaymentType());
				transaction.getTransactionDetails().put("TRANSACTIONID", transactionId);
				transaction.getTransactionDetails().put("CORRELATIONID", correlationId);
				
				return transaction;

			} else {

				String ErrorCode = nvp.get("L_ERRORCODE0").toString();
				String ErrorShortMsg = nvp.get("L_SHORTMESSAGE0").toString();
				String ErrorLongMsg = nvp.get("L_LONGMESSAGE0").toString();
				String ErrorSeverityCode = nvp.get("L_SEVERITYCODE0")
						.toString();

				IntegrationException te = new IntegrationException(
						"Paypal transaction refused " + ErrorLongMsg);
						te.setErrorCode(IntegrationException.TRANSACTION_EXCEPTION);
						
						
				merchantLogService.save(
								new MerchantLog(store,
								"Paypal transaction refused "
										+ ErrorLongMsg));

				if (ErrorCode.equals("10415")) {// transaction already submited
					te = new IntegrationException("Paypal transaction refused "
							+ ErrorLongMsg);
					te
							.setErrorCode(IntegrationException.TRANSACTION_EXCEPTION);
				}

				throw te;
			}
			


		} catch (Exception e) {
			if (e instanceof IntegrationException) {
				throw (IntegrationException) e;
			}
			throw new IntegrationException(e);
		}
		

	}

	@Override
	public Transaction authorizeAndCapture(MerchantStore store, Customer customer,
			List<ShoppingCartItem> items, BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction refund(boolean partial, MerchantStore store, Transaction transaction,
			BigDecimal amount, 
			IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationException {
		
		
		
		try {

			/*
			 * '------------------------------------ ' The currencyCodeType and
			 * paymentType ' are set to the selections made on the Integration
			 * Assistant '------------------------------------
			 */



			// REQUEST
			// requiredSecurityParameters]&METHOD=RefundTransaction&AUTHORIZATIONID=
			// &AMT=99.12&REFUNDTYPE=Full|Partial

			// IPN
			// String ipnUrl = ReferenceUtil.buildSecureServerUrl() +
			// (String)conf.getString("core.salesmanager.checkout.paypalIpn");

			String refundType = "Full";
			if (partial) {
				refundType = "Partial";
			}

			// String nvpstr = "&TRANSACTIONID=" + transactionId +
			// "&REFUNDTYPE=" + refundType + "&IPADDRESS=" + ip.toString();
			String nvpstr = "&TRANSACTIONID=" + transaction.getTransactionDetails().get("TRANSACTIONID") + "&REFUNDTYPE="
					+ refundType + "&CURRENCYCODE=" + store.getCurrency() + "&IPADDRESS=";


			if (partial) {
				nvpstr = nvpstr + "&AMT=" + amount + "&NOTE=Partial refund";
			}

			Map<String,String> nvp = httpcall(configuration, module, "RefundTransaction",
					nvpstr);
			String strAck = nvp.get("ACK").toString();
			

			
			StringBuilder valueBuffer = new StringBuilder();
			for (String key : nvp.keySet()) {
				valueBuffer.append("[").append(key).append("=").append(
						(String) nvp.get(key)).append("]");
			}
			

			if (strAck != null && strAck.equalsIgnoreCase("Success")) {
				
				
				String responseTransactionId = (String) nvp
				.get("REFUNDTRANSACTIONID");
				
				/**
				 * RESPONSE
				 * [successResponseFields]&AUTHORIZATIONID=
				 * &TRANSACTIONID
				 * =&PARENTTRANSACTIONID=
				 * &RECEIPTID
				 * =&TRANSACTIONTYPE=express-checkout
				 * &PAYMENTTYPE=instant&ORDERTIME=2006-08-15T17:31:38Z&AMT=99.12
				 * &CURRENCYCODE=USD&FEEAMT=3.29&TAXAMT=0.00&PAYMENTSTATUS=
				 * Completed &PENDINGREASON=None&REASONCODE=None
				 **/
				
				Transaction refund = new Transaction();
				refund.setAmount(amount);
				//refund.setOrder(order);
				refund.setTransactionDate(new Date());
				refund.setTransactionType(TransactionType.REFUND);
				refund.setPaymentType(PaymentType.PAYPAL);
				refund.getTransactionDetails().put("REFUNDTRANSACTIONID", responseTransactionId);
			
				return transaction;

			} else {

				String ErrorCode = nvp.get("L_ERRORCODE0").toString();
				String ErrorShortMsg = nvp.get("L_SHORTMESSAGE0").toString();
				String ErrorLongMsg = nvp.get("L_LONGMESSAGE0").toString();
				String ErrorSeverityCode = nvp.get("L_SEVERITYCODE0")
						.toString();

				IntegrationException te = new IntegrationException(
						"Paypal transaction refused " + ErrorLongMsg);
						te.setErrorCode(IntegrationException.TRANSACTION_EXCEPTION);
						
						
				merchantLogService.save(
								new MerchantLog(store,
								"Paypal transaction refused "
										+ ErrorLongMsg));

				if (ErrorCode.equals("10415")) {// transaction already submited
					te = new IntegrationException("Paypal transaction refused "
							+ ErrorLongMsg);
					te
							.setErrorCode(IntegrationException.TRANSACTION_EXCEPTION);
				}

				throw te;
			}
			


		} catch (Exception e) {
			if (e instanceof IntegrationException) {
				throw (IntegrationException) e;
			}
			throw new IntegrationException(e);
		}
			

	}
	
	
	public Map<String,String> httpcall(IntegrationConfiguration keys, IntegrationModule module,
			String methodName, String nvpStr) throws Exception {

		// return null;

		boolean bSandbox = false;
		if (keys.getEnvironment().equals("TEST")) {// sandbox
			bSandbox = true;
		}

		String gv_APIEndpoint = "";
		String PAYPAL_URL = "";
		String gv_Version = "3.3";

		if (bSandbox == true) {
			gv_APIEndpoint = "https://api-3t.sandbox.paypal.com/nvp";
			//TODO TEST VS PROD
			PAYPAL_URL = new StringBuffer().append(
					module.getModuleConfigs().get("scheme")).append("://")
					.append(module.getModuleConfigs().get("host")).append(
							module.getModuleConfigs().get("uri")).append(
							"?cmd=_express-checkout&token=").toString();
			// PAYPAL_URL =
			// "https://www.sandbox.paypal.com/webscr?cmd=_express-checkout&token=";
		} else {
			gv_APIEndpoint = "https://api-3t.paypal.com/nvp";
			//TODO TEST VS PROD
			PAYPAL_URL = new StringBuffer().append(
					module.getModuleConfigs().get("scheme")).append("://")
					.append(module.getModuleConfigs().get("host")).append(
							module.getModuleConfigs().get("uri")).append(
							"?cmd=_express-checkout&token=").toString();
			// PAYPAL_URL =
			// "https://www.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=";
		}

		String agent = "Mozilla/4.0";
		String respText = "";
		Map<String,String> nvp = null;

		// deformatNVP( nvpStr );
		String encodedData = "METHOD=" + methodName + "&VERSION=" + gv_Version
				+ "&PWD=" + keys.getIntegrationKeys().get("PASSWORD") + "&USER="
				+ keys.getIntegrationKeys().get("USER")  + "&SIGNATURE=" + keys.getIntegrationKeys().get("SIGNATURE") 
				+ nvpStr + "&BUTTONSOURCE=" + "PP-ECWizard";
		
		//log.debug("REQUEST SENT TO PAYPAL -> " + encodedData);

		HttpURLConnection conn = null;
		DataOutputStream output = null;
		DataInputStream in = null;
		BufferedReader is = null;
		try {
			URL postURL = new URL(gv_APIEndpoint);
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

			// conn.setRequestProperty( "Content-Type", type );
			conn.setRequestProperty("Content-Length", String
					.valueOf(encodedData.length()));
			conn.setRequestMethod("POST");

			// get the output stream to POST to.
			output = new DataOutputStream(conn.getOutputStream());
			output.writeBytes(encodedData);
			output.flush();
			// output.close ();

			// Read input from the input stream.
			in = new DataInputStream(conn.getInputStream());
			int rc = conn.getResponseCode();
			if (rc != -1) {
				is = new BufferedReader(new InputStreamReader(conn
						.getInputStream()));
				String _line = null;
				while (((_line = is.readLine()) != null)) {
					//log.debug("Response line from Paypal -> " + _line);
					respText = respText + _line;
				}
				nvp = formatUrlResponse(respText);
			} else {
				throw new Exception("Invalid response from paypal, return code is " + rc);
			}

			nvp.put("PAYPAL_URL", PAYPAL_URL);
			nvp.put("NVP_URL", gv_APIEndpoint);

			return nvp;

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



}
