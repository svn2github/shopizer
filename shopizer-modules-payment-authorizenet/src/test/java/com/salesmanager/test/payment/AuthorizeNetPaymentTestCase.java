package com.salesmanager.test.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.salesmanager.core.business.catalog.product.model.type.ProductType;
import com.salesmanager.core.business.common.model.Billing;
import com.salesmanager.core.business.common.model.Delivery;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.init.service.InitializationDatabase;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.shipping.model.ShippingProduct;
import com.salesmanager.core.business.system.model.Environment;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;


@ContextConfiguration(locations = {
		"classpath:spring/test-spring-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class
})
public class AuthorizeNetPaymentTestCase {
	
	@Autowired
	protected LanguageService languageService;
	
	@Autowired
	protected CountryService countryService;
	
	@Autowired
	protected ZoneService zoneService;
	
	@Autowired
	protected MerchantStoreService merchantService;
	
	@Autowired
	protected CustomerService customerService;
	
	@Autowired
	protected InitializationDatabase                initializationDatabase;
	
	@Before
	public void init() throws ServiceException {
		
		initializationDatabase.populate("TEST");
	}
	
	

	
	@Test
	public void testAuthorize() {
		System.out.println("Hello world");
	}
	
	
	@Test
	public void testAuthorizeAndCapture() {
		
		
		try {
			

		    Language en = languageService.getByCode("en");
		    Country country = countryService.getByCode("CA");
		    Zone zone = zoneService.getByCode("QC");
	
		    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
	
		    
		    IntegrationConfiguration configuration = new IntegrationConfiguration();
		    configuration.setActive(true);
		    configuration.setEnvironment(Environment.TEST.name());
		    configuration.setModuleCode("authorizenet");
		    
		    //configure module
	
		    configuration.getIntegrationKeys().put("userId", "csamson777");
		    configuration.getIntegrationKeys().put("accessKey", "AC66279FF8020AE0");
		    configuration.getIntegrationKeys().put("password", "william");
		    
		    Order order = new Order();
		    
	
		    
			Customer customer = new Customer();
			customer.setFirstname("Test");
			customer.setMerchantStore(store);
			customer.setLastname("User");
			customer.setCity("city");
			customer.setEmailAddress("test@test.com");
			customer.setGender("M");
			customer.setTelephone("00000");
			customer.setAnonymous(true);
			customer.setCompany("ifactory");
			customer.setDateOfBirth(new Date());
			customer.setFax("fax");
			customer.setNewsletter('c');
			customer.setNick("My nick");
			customer.setPassword("123456");
			customer.setPostalCode("000");
			customer.setState("state");
			customer.setStreetAddress("Street 1");
			customer.setTelephone("123123");
			customer.setCountry(country);
			customer.setZone(zone);
			
		    Delivery delivery = new Delivery();
		    delivery.setAddress("Shipping address");
		    delivery.setCity("Boucherville");
		    delivery.setCountry(country);
		    delivery.setZone(zone);
		    delivery.setPostalCode("J4B-8J9");
		    
	
		    
		    
		    Billing billing = new Billing();
		    billing.setAddress("Billing address");
		    billing.setCountry(country);
		    billing.setZone(zone);
		    billing.setPostalCode("J4B-8J9");
		    
		    customer.setBilling(billing);
		    customer.setDelivery(delivery);
			
			customerService.create(customer);
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	public void testRefund() {
		
	}

}
