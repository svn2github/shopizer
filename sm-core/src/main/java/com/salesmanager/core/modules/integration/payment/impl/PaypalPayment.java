package com.salesmanager.core.modules.integration.payment.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.payments.model.Payment;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.payment.model.PaymentModule;
import com.salesmanager.core.utils.ProductPriceUtils;

public class PaypalPayment implements PaymentModule {
	
	@Autowired
	private ProductPriceUtils productPriceUtils;
	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;

	@Override
	public Transaction initTransaction(Customer customer, Order order,
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
		String sAmount = productPriceUtils.getAdminFormatedAmount(order.getMerchant(), amount);


		MerchantConfiguration returnUrl = merchantConfigurationService.getMerchantConfiguration("PAYPAL_RETURN_URL", order.getMerchant());

		if(returnUrl==null) {
			throw new IntegrationException("Paypal return url not configured");
		}
		

		String sLocale = "US";// default to US english

		/**
		 * supports AU DE FR GB IT ES JP US
		 **/


			String country = order.getMerchant().getCountry().getIsoCode();
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
					+ returnUrl
					+ "&CANCELURL="
					+ cancelUrl
					+ "&CURRENCYCODE=" + order.getCurrency();
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
	
	
	public Map httpcall(IntegrationConfiguration keys, IntegrationModule module,
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



}
