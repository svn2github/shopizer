package com.salesmanager.test.shop.controller.customer.rest;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.salesmanager.web.entity.customer.Customer;


public class CustomerRESTControllerTest {
	
	private RestTemplate restTemplate;
	
	private Long testCustmerID;
	
	@Test
	public void customerRESTTest() throws Exception {
		
		//create customer
		postCustomer();
		
		//get All customers
		getCustomers();
		
		//get a single customer
		getCustomer();
		
		//update customer
		putCustomer();
		
		//delete customer
		deleteCustomer();
		
	}
	
	public void postCustomer() throws Exception {
		restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String jsonString = ("{"+
			   " \"password\": \"5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8\","+
			   " \"firstname\": \"Tapas\","+
			   " \"lastname\": \"Jena\","+
			   " \"emailAddress\": \"tapasfs.friends@gmail.com\","+
			   " \"telephone\": \"9703517026\","+
			   " \"streetAddress\": \"Hitech City\","+
			   " \"postalCode\": \"500008\","+
			   " \"city\": \"Hyderabad\","+
			   " \"country\": \"IN\","+
			   " \"billing\": {"+
			   "     \"country\":\"US\""+
			   " },"+
			   " \"delivery\": {"+
			   "     \"country\": \"IN\","+
			   "     \"state\":\"a state\""+
			   " },"+
			   " \"zone\": {"+
			   "	\"country\": \"IN\""+
			   " }"+
			"}");

		HttpEntity<String> entity = new HttpEntity<String>(jsonString, headers);

		ResponseEntity response = restTemplate.postForEntity("http://localhost:8080/sm-shop/shop/services/customers/DEFAULT", entity, Customer.class);

		Customer cust = (Customer) response.getBody();
		System.out.println("New Customer ID : " + cust.getId());
		testCustmerID = cust.getId();
	}
	
	public void getCustomers() throws Exception {
		restTemplate = new RestTemplate();
		ResponseEntity<Customer[]> response = restTemplate.getForEntity("http://localhost:8080/sm-shop/shop/services/customers/DEFAULT", Customer[].class);
		
		if(response.getStatusCode() != HttpStatus.OK){
			throw new Exception();
		}else{
			System.out.println(response.getBody().length + " Customer records found.");
		}
	}
	
	public void getCustomer() throws Exception {
		restTemplate = new RestTemplate();
		ResponseEntity<Customer> response = restTemplate.getForEntity("http://localhost:8080/sm-shop/shop/services/customers/DEFAULT/"+testCustmerID, Customer.class);
		
		if(response.getStatusCode() != HttpStatus.OK){
			throw new Exception();
		}else{
			System.out.println("Returned username is : " + response.getBody().getUserName());
		}
	}
	
	public void putCustomer() throws Exception {
		restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String jsonString = ("{"+
				   " \"password\": \"5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8\","+
				   " \"firstname\": \"Tapas\","+
				   " \"lastname\": \"22222\","+
				   " \"emailAddress\": \"tapasfs.friends@gmail.com\","+
				   " \"telephone\": \"9703517026\","+
				   " \"streetAddress\": \"Hitech City\","+
				   " \"postalCode\": \"500008\","+
				   " \"city\": \"Hyderabad123\","+
				   " \"country\": \"IN\","+
				   " \"billing\": {"+
				   "     \"country\":\"US\""+
				   " },"+
				   " \"delivery\": {"+
				   "     \"country\": \"IN\","+
				   "     \"state\":\"a state\""+
				   " },"+
				   " \"zone\": {"+
				   "	\"country\": \"IN\""+
				   " }"+
				"}");
		
		HttpEntity<String> entity = new HttpEntity<String>(jsonString, headers);
		restTemplate.put("http://localhost:8080/sm-shop/shop/services/customers/DEFAULT/"+testCustmerID, entity);
		System.out.println("Customer "+testCustmerID+" Updated.");
	}
	
	public void deleteCustomer() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete("http://localhost:8080/sm-shop/shop/services/customers/DEFAULT/"+testCustmerID);
		System.out.println("Customer "+testCustmerID+" Deleted.");
	}
	
}
