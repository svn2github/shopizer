package com.salesmanager.test.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.test.core.AbstractSalesManagerCoreTestCase;


public class OrderSalesManagerTestCase extends AbstractSalesManagerCoreTestCase {

	@Test
	public void createOrder() throws ServiceException {
		

	    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
	    
	    //get product create during population
	    List<Product> products = productService.listByStore(store);
	    Product product = products.listIterator().next();
	    
	    //create a Customer
	    //see customer test case

		
		Currency currency = currencyService.getByCode(EURO_CURRENCY_CODE);

		Order order = new Order();
		order.setDatePurchased(new Date());
		order.setCurrency(currency);
		order.setLastModified(new Date());
		//all required fields
		
		//Delivery
		//Billing
		
		//requires 
		//OrderProduct
		//OrderProductPrice
		//OrderTotal
		
		orderService.create(order);
		Assert.assertTrue(orderService.count() >= 1);
	}
	
	
	@Test
	public void getMerchantOrders() throws ServiceException {
		
		List<Order> merchantOrders= new ArrayList<Order>();
		
		Language language = languageService.getByCode(ENGLISH_LANGUAGE_CODE);
		Currency currency = currencyService.getByCode(EURO_CURRENCY_CODE);
		Country country = countryService.getByCode(FR_COUNTRY_CODE);
		
		MerchantStore merchant = new MerchantStore();
		
		merchant.setCurrency(currency);
		merchant.setStorename("Test Store");
		merchant.setCountry(country);
		merchant.setDefaultLanguage(language);

		
		merchant.setStorecity("Test Store City");
		merchant.setCode( merchantService.count()+"");
		Language en = languageService.getByCode("en");
		Language fr = languageService.getByCode("fr");
		List<Language> supportedLanguages = new ArrayList<Language>();
		supportedLanguages.add(en);
		supportedLanguages.add(fr);
		merchant.setLanguages( supportedLanguages );
		merchant.setStoreEmailAddress("store_email@email.com");
		merchant.setStorephone("Merchant Store Phone");
		merchant.setStorepostalcode("12061");
		
		merchantService.create(merchant);
		
		Customer customer = new Customer();
		
		customer.setCountry(country);
		customer.setFirstname("Ahmed");
		customer.setLastname("Faraz");
		customer.setCity("Dubai");
		customer.setEmailAddress("email@email.com");
		customer.setPostalCode("63839");
		
		customer.setStreetAddress("Customer Address");
		
		customer.setTelephone("Customer Phone");
		
//		customerService.create(customer);
		
		
		Order order = new Order();

		order.setDatePurchased(new Date());

		//order.setCustomer(customer);
		order.setCurrency(currency);
		order.setMerchant(merchant);
		order.setLastModified(new Date());
		
		orderService.create(order);
		
		try {
			merchantOrders = orderService.listByStore(merchant);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertTrue("Merchant Orders are null." , merchantOrders != null);
		Assert.assertTrue("Merchant Orders count is not one." , (merchantOrders != null && merchantOrders.size() == 1) );
	}

}