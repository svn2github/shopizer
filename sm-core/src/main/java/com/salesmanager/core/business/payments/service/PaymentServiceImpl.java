package com.salesmanager.core.business.payments.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.payments.model.Payment;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.payments.model.TransactionType;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.business.system.service.ModuleConfigurationService;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.payment.model.PaymentModule;
import com.salesmanager.core.utils.reference.ConfigurationModulesLoader;

@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {
	
	

	private final static String PAYMENT_MODULES = "PAYMENT";
	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;
	
	@Autowired
	private ModuleConfigurationService moduleConfigurationService;
	
	@Autowired
	private TransactionService transactionService;;
	
	@Autowired
	@Resource(name="paymentModules")
	private Map<String,PaymentModule> paymentModules;
	
	@Override
	public List<IntegrationModule> getPaymentMethods(MerchantStore store) throws ServiceException {
		
		List<IntegrationModule> modules =  moduleConfigurationService.getIntegrationModules(PAYMENT_MODULES);
		List<IntegrationModule> returnModules = new ArrayList<IntegrationModule>();
		
		for(IntegrationModule module : modules) {
			if(module.getRegionsSet().contains(store.getCountry().getIsoCode())
					|| module.getRegionsSet().contains("*")) {
				
				returnModules.add(module);
			}
		}
		
		return returnModules;
	}
	
	@Override
	public IntegrationModule getPaymentMethod(MerchantStore store, String moduleName) throws ServiceException {
		List<IntegrationModule> modules =  getPaymentMethods(store);

		for(IntegrationModule module : modules) {
			if(module.getModule().equals(moduleName)) {
				
				return module;
			}
		}
		
		return null;
	}
	
	@Override
	public IntegrationConfiguration getPaymentConfiguration(String moduleCode, MerchantStore store) throws ServiceException {

		
		Map<String,IntegrationConfiguration> configuredModules = getPaymentModulesConfigured(store);
		if(configuredModules!=null) {
			for(String key : configuredModules.keySet()) {
				if(key.equals(moduleCode)) {
					return configuredModules.get(key);	
				}
			}
		}
		
		return null;
		
	}
	
	@Override
	public Map<String,IntegrationConfiguration> getPaymentModulesConfigured(MerchantStore store) throws ServiceException {
		
		try {
		
			Map<String,IntegrationConfiguration> modules = new HashMap<String,IntegrationConfiguration>();
			MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(PAYMENT_MODULES, store);
			if(merchantConfiguration!=null) {
				if(!StringUtils.isBlank(merchantConfiguration.getValue())) {
					
					modules = ConfigurationModulesLoader.loadIntegrationConfigurations(merchantConfiguration.getValue());
					
					
				}
			}
			return modules;
		
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void savePaymentModuleConfiguration(IntegrationConfiguration configuration, MerchantStore store) throws ServiceException {
		
		//validate entries
		try {
			
			String moduleCode = configuration.getModuleCode();
			PaymentModule module = (PaymentModule)paymentModules.get(moduleCode);
			if(module==null) {
				throw new ServiceException("Payment module " + moduleCode + " does not exist");
			}
			module.validateModuleConfiguration(configuration, store);
			
		} catch (IntegrationException ie) {
			throw ie;
		}
		
		try {
			Map<String,IntegrationConfiguration> modules = new HashMap<String,IntegrationConfiguration>();
			MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(PAYMENT_MODULES, store);
			if(merchantConfiguration!=null) {
				if(!StringUtils.isBlank(merchantConfiguration.getValue())) {
					modules = ConfigurationModulesLoader.loadIntegrationConfigurations(merchantConfiguration.getValue());
				}
			} else {
				merchantConfiguration = new MerchantConfiguration();
				merchantConfiguration.setMerchantStore(store);
				merchantConfiguration.setKey(PAYMENT_MODULES);
			}
			modules.put(configuration.getModuleCode(), configuration);
			
			String configs =  ConfigurationModulesLoader.toJSONString(modules);
			merchantConfiguration.setValue(configs);
			merchantConfigurationService.saveOrUpdate(merchantConfiguration);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
}
	

	


	@Override
	public Transaction processPayment(Order order, Customer customer,
			MerchantStore store, Payment payment, BigDecimal amount)
			throws ServiceException {

		Validate.notNull(order);
		Validate.notNull(customer);
		Validate.notNull(store);
		Validate.notNull(payment);
		Validate.notNull(amount);
		
		
		//must have a shipping module configured
		Map<String, IntegrationConfiguration> modules = this.getPaymentModulesConfigured(store);
		if(modules==null){
			throw new ServiceException("No payment module configured");
		}
		
		IntegrationConfiguration configuration = modules.get(payment.getModuleName());
		
		if(configuration==null) {
			throw new ServiceException("Payment module " + payment.getModuleName() + " is not configured");
		}
		
		if(!configuration.isActive()) {
			throw new ServiceException("Payment module " + payment.getModuleName() + " is not active");
		}
		
		
		PaymentModule module = this.paymentModules.get(payment.getModuleName());
		
		if(module==null) {
			throw new ServiceException("Payment module " + payment.getModuleName() + " does not exist");
		}
		
		IntegrationModule integrationModule = getPaymentMethod(store,payment.getModuleName());
		
		TransactionType transactionType = payment.getTransactionType();
		Transaction transaction = null;
		if(transactionType == TransactionType.AUTHORIZE)  {
			transaction = module.authorize(customer, order, amount, payment, configuration, integrationModule);
		} else if(transactionType == TransactionType.AUTHORIZECAPTURE)  {
			transaction = module.authorizeAndCapture(customer, order, amount, payment, configuration, integrationModule);
		} else if(transactionType == TransactionType.CAPTURE)  {
			//get the previous transaction
			Transaction trx = transactionService.getCapturableTransaction(order);
			if(trx==null) {
				throw new ServiceException("No capturable transaction for order id " + order.getId());
			}
			transaction = module.capture(customer, order, amount, payment, trx, configuration, integrationModule);
		} else if(transactionType == TransactionType.INIT)  {
			transaction = module.initTransaction(customer, order, amount, payment, configuration, integrationModule);
		}
		
		if(transactionType != TransactionType.INIT) {
			transactionService.create(transaction);
		}
		
		return transaction;

		

	}

	@Override
	public Transaction processRefund(Order order, Customer customer,
			MerchantStore store, Payment payment, BigDecimal amount)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void validateCreditCard(String number, int type, String month, String date)
	throws ServiceException {

		try {
			Integer.parseInt(month);
			Integer.parseInt(date);
		} catch (NumberFormatException nfe) {
			throw new ServiceException("Invalid date format");
		}
		
		if (number.equals("")) {
			throw new ServiceException("Invalid number");
		}
		
		Matcher m = Pattern.compile("[^\\d\\s.-]").matcher(number);
		
		if (m.find()) {
			throw new ServiceException("Invalid number");
		}
		
		Matcher matcher = Pattern.compile("[\\s.-]").matcher(number);
		
		number = matcher.replaceAll("");
		validateCreditCardDate(Integer.parseInt(month), Integer.parseInt(date));
		validateCreditCardNumber(number, "xyz");
	}

	private void validateCreditCardDate(int m, int y) throws ServiceException {
		java.util.Calendar cal = new java.util.GregorianCalendar();
		int monthNow = cal.get(java.util.Calendar.MONTH) + 1;
		int yearNow = cal.get(java.util.Calendar.YEAR);
		if (yearNow > y) {
			throw new ServiceException("Invalid date");
		}
		// OK, change implementation
		if (yearNow == y && monthNow > m) {
			throw new ServiceException("Invalid date");
		}
	
	}
	
	private void validateCreditCardNumber(String number, String type)
	throws ServiceException {

		
/*		case MASTERCARD:
			if (number.length() != 16
					|| Integer.parseInt(number.substring(0, 2)) < 51
					|| Integer.parseInt(number.substring(0, 2)) > 55) {
				throw new CreditCardUtilException(LabelUtil.getInstance()
						.getText("errors.creditcard.invalidnumber"));
			}
			break;
		
		case VISA:
			if ((number.length() != 13 && number.length() != 16)
					|| Integer.parseInt(number.substring(0, 1)) != 4) {
				throw new CreditCardUtilException(LabelUtil.getInstance()
						.getText("errors.creditcard.invalidnumber"));
			}
			break;
		
		case AMEX:
			if (number.length() != 15
					|| (Integer.parseInt(number.substring(0, 2)) != 34 && Integer
							.parseInt(number.substring(0, 2)) != 37)) {
				throw new CreditCardUtilException(LabelUtil.getInstance()
						.getText("errors.creditcard.invalidnumber"));
			}
			break;
		
		case DISCOVER:
			if (number.length() != 16
					|| Integer.parseInt(number.substring(0, 5)) != 6011) {
				throw new CreditCardUtilException(LabelUtil.getInstance()
						.getText("errors.creditcard.invalidnumber"));
			}
			break;
		
		case DINERS:
			if (number.length() != 14
					|| ((Integer.parseInt(number.substring(0, 2)) != 36 && Integer
							.parseInt(number.substring(0, 2)) != 38)
							&& Integer.parseInt(number.substring(0, 3)) < 300 || Integer
							.parseInt(number.substring(0, 3)) > 305)) {
				throw new CreditCardUtilException(LabelUtil.getInstance()
						.getText("errors.creditcard.invalidnumber"));
			}
			break;
		}*/
		luhnValidate(number);
	}

	// The Luhn algorithm is basically a CRC type
	// system for checking the validity of an entry.
	// All major credit cards use numbers that will
	// pass the Luhn check. Also, all of them are based
	// on MOD 10.
	
	private void luhnValidate(String numberString)
			throws ServiceException {
		char[] charArray = numberString.toCharArray();
		int[] number = new int[charArray.length];
		int total = 0;
	
		for (int i = 0; i < charArray.length; i++) {
			number[i] = Character.getNumericValue(charArray[i]);
		}
	
		for (int i = number.length - 2; i > -1; i -= 2) {
			number[i] *= 2;
	
			if (number[i] > 9)
				number[i] -= 9;
		}
	
		for (int i = 0; i < number.length; i++)
			total += number[i];
	
		if (total % 10 != 0)
			throw new ServiceException("Invalid number");
	
	}
	


}
