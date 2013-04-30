package com.salesmanager.core.business.tax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.common.model.Billing;
import com.salesmanager.core.business.common.model.Delivery;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.shipping.model.ShippingConfiguration;
import com.salesmanager.core.business.shipping.service.ShippingService;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.business.tax.model.TaxBasisCalculation;
import com.salesmanager.core.business.tax.model.TaxConfiguration;
import com.salesmanager.core.business.tax.model.TaxItem;
import com.salesmanager.core.business.tax.model.taxclass.TaxClass;
import com.salesmanager.core.business.tax.model.taxrate.TaxRate;

@Service("taxService")
public class TaxServiceImpl 
		implements TaxService {
	
	private final static String TAX_CONFIGURATION = "TAX_CONFIG";
	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;
	
	@Autowired
	private TaxRateService taxRateService;
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private ShippingService shippingService;
	
	@Autowired
	private TaxClassService taxClassService;
	
	@Override
	public TaxConfiguration getTaxConfiguration(MerchantStore store) throws ServiceException {
		
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(TAX_CONFIGURATION, store);
		TaxConfiguration taxConfiguration = null;
		if(configuration!=null) {
			String value = configuration.getValue();
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				taxConfiguration = mapper.readValue(value, TaxConfiguration.class);
			} catch(Exception e) {
				throw new ServiceException("Cannot parse json string " + value);
			}
		}
		return taxConfiguration;
	}
	
	
	@Override
	public void saveTaxConfiguration(TaxConfiguration shippingConfiguration, MerchantStore store) throws ServiceException {
		
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(TAX_CONFIGURATION, store);

		if(configuration==null) {
			configuration = new MerchantConfiguration();
			configuration.setMerchantStore(store);
			configuration.setKey(TAX_CONFIGURATION);
		}
		
		String value = shippingConfiguration.toJSONString();
		configuration.setValue(value);
		merchantConfigurationService.saveOrUpdate(configuration);
		
	}
	
	public List<TaxItem> calculateTax(OrderSummary orderSummary, Customer customer, MerchantStore store, Locale locale) throws ServiceException {
		

		Language language = languageService.getByCode(locale.getLanguage());
		
		if(language==null){
			language = store.getDefaultLanguage();
		}


		BigDecimal totalTaxAmount = new BigDecimal(0);
		
		List<ShoppingCartItem> items = orderSummary.getProducts();
		
		List<TaxItem> taxLines = new ArrayList<TaxItem>();
		
		if(items==null) {
			return taxLines;
		}
		
		//determine tax calculation basis
		TaxConfiguration taxConfiguration = this.getTaxConfiguration(store);
		if(taxConfiguration==null) {
			taxConfiguration = new TaxConfiguration();
			taxConfiguration.setTaxBasisCalculation(TaxBasisCalculation.SHIPPINGADDRESS);
		}
		
		Country country = customer.getCountry();
		Zone zone = customer.getZone();
		String stateProvince = customer.getState();
		
		TaxBasisCalculation taxBasisCalculation = taxConfiguration.getTaxBasisCalculation();
		if(taxBasisCalculation.name().equals(TaxBasisCalculation.SHIPPINGADDRESS)){
			Delivery shipping = customer.getDelivery();
			if(shipping!=null) {
				country = shipping.getCountry();
				zone = shipping.getZone();
				stateProvince = shipping.getState();
			}
		} else if(taxBasisCalculation.name().equals(TaxBasisCalculation.BILLINGADDRESS)){
			Billing billing = customer.getBilling();
			if(billing!=null) {
				country = billing.getCountry();
				zone = billing.getZone();
				stateProvince = billing.getState();
			}
		} else if(taxBasisCalculation.name().equals(TaxBasisCalculation.STOREADDRESS)){
			country = store.getCountry();
			zone = store.getZone();
			stateProvince = store.getStorestateprovince();
		}
		
		Map<Long,TaxClass> taxClasses =  new HashMap<Long,TaxClass>();
			
		//put items in a map by tax class id
		Map<Long,BigDecimal> taxClassAmountMap = new HashMap<Long,BigDecimal>();
		for(ShoppingCartItem item : items) {
				
				BigDecimal itemPrice = item.getItemPrice();
				TaxClass taxClass = item.getProduct().getTaxClass();
				int quantity = item.getQuantity();
				itemPrice = itemPrice.multiply(new BigDecimal(quantity));
				if(taxClass!=null) {
					BigDecimal subTotal = taxClassAmountMap.get(taxClass.getId());
					if(subTotal==null) {
						subTotal = new BigDecimal(0);
					}
					
					subTotal = subTotal.add(itemPrice);
					taxClassAmountMap.put(taxClass.getId(), subTotal);
					taxClasses.put(taxClass.getId(), taxClass);
				}
				
		}
		
		//tax on shipping ?
		ShippingConfiguration shippingConfiguration = shippingService.getShippingConfiguration(store);	
		
		if(shippingConfiguration!=null) {
			if(shippingConfiguration.isTaxOnShipping()){
				//get default tax class
				TaxClass defaultTaxClass = taxClassService.getByCode(TaxClass.DEFAULT_TAX_CLASS);
				BigDecimal amount = taxClassAmountMap.get(defaultTaxClass.getId());
				if(amount==null) {
					amount = new BigDecimal(0);
				}
				amount = amount.add(orderSummary.getShipping());
				taxClassAmountMap.put(defaultTaxClass.getId(), amount);
				taxClasses.put(defaultTaxClass.getId(), defaultTaxClass);
			}
		}
		
		
		
		
		//iterate through the tax class and get appropriate rates
		for(Long taxClassId : taxClassAmountMap.keySet()) {
			
			//get taxRate by tax class
			List<TaxRate> taxRates = null; 
			if(!StringUtils.isBlank(stateProvince)&& zone==null) {
				taxRates = taxRateService.listByCountryStateProvinceAndTaxClass(country, stateProvince, taxClasses.get(taxClassId), store, language);
			} else {
				taxRates = taxRateService.listByCountryZoneAndTaxClass(country, zone, taxClasses.get(taxClassId), store, language);
			}
			
			if(taxRates==null || taxRates.size()==0){
				continue;
			}
			BigDecimal afterTaxAmount = new BigDecimal(0);
			BigDecimal beforeTaxAmount = taxClassAmountMap.get(taxClassId);
			for(TaxRate taxRate : taxRates) {
				
				double taxRateDouble = taxRate.getTaxRate().doubleValue();
				
				if(taxRate.isPiggyback()) {
					beforeTaxAmount.add(afterTaxAmount);
				}
				
				double value  = (afterTaxAmount.doubleValue() * taxRateDouble)/100;
				afterTaxAmount = new BigDecimal(value);
				
			}
			
		}
			
			

		
		
		return null;
	}


}
