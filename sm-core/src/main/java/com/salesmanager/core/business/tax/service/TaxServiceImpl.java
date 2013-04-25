package com.salesmanager.core.business.tax.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.business.tax.model.TaxConfiguration;
import com.salesmanager.core.business.tax.model.TaxItem;
import com.salesmanager.core.business.tax.model.taxclass.TaxClass;

@Service("taxService")
public class TaxServiceImpl 
		implements TaxService {
	
	private final static String TAX_CONFIGURATION = "TAX_CONFIG";
	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;
	
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
		
		
		BigDecimal totalTaxAmount = new BigDecimal(0);
		
		List<ShoppingCartItem> items = orderSummary.getProducts();
		
		if(items!=null) {
			
			//put items in a map by tax class id
			Map<Long,BigDecimal> taxClassAmountMap = new HashMap<Long,BigDecimal>();
			for(ShoppingCartItem item : items) {
				
				BigDecimal itemPrice = item.getItemPrice();
				TaxClass taxClass = item.getProduct().getTaxClass();
				
				BigDecimal subTotal = taxClassAmountMap.get(taxClass.getId());
				if(subTotal==null) {
					subTotal = new BigDecimal(0);
				}
				
				subTotal = subTotal.add(itemPrice);
				taxClassAmountMap.put(taxClass.getId(), subTotal);
				
			}
			
			//iterate through the tax class and get appropriate rates
			
			
		}
		
		
		return null;
	}


}
