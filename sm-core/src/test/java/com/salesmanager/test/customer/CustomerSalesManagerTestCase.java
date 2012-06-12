package com.salesmanager.test.customer;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.test.core.AbstractSalesManagerCoreTestCase;

public class CustomerSalesManagerTestCase extends AbstractSalesManagerCoreTestCase {
	
	@Test
	public void createCustomer() throws ServiceException {
/*		Customer customer = new Customer();
		customer.setFirstname("Leonardo");
		customer.setLastname("Ribeiro");
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
		
		Country country = countryService.getByCode(FRA_COUNTRY_CODE);
		customer.setCountry(country);
		customerService.create(customer);
		
		Assert.assertTrue(customerService.count() == 1);
		Assert.assertNotNull(customerService.getByName("Leonardo"));*/
	}
}
