package com.salesmanager.test.shop.controller.category.rest;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.salesmanager.web.entity.catalog.Category;
import com.salesmanager.web.entity.catalog.Product;

public class ShoppingCategoryControllerTest {
	
	private RestTemplate restTemplate;
	
	private Long testCategoryID;
	
	private Long testProductID;

	@Test
	public void categoryRESTTest() throws Exception {
		
		//create customer
		postCategory();
		
		//get All customers
		getCategories();
		
		//update customer
		//putCategory();
		
		//delete customer
		deleteCategory();
		
	}
	
	public void getCategories() throws Exception {
		restTemplate = new RestTemplate();
		ResponseEntity<Category[]> response = restTemplate.getForEntity("http://localhost:8080/sm-shop/shop/services/category/DEFAULT/en", Category[].class);
		
		if(response.getStatusCode() != HttpStatus.OK){
			throw new Exception();
		}else{
			System.out.println(response.getBody().length + " Category records found.");
		}
	}
	
	public void putCategory() throws Exception {
		restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	
		String jsonString = ("{"+
				   " \"sortOrder\": \"0\","+
				   " \"depth\": \"2\","+
				   " \"code\": \"movie"+Math.random()+"\","+
				   " \"lineage\": \"/movie\","+
				   " \"categoryStatus\": \"true\","+
				   " \"visible\": \"true\","+
				   " \"categories\": [{"+
				   "     \"code\":\"music\""+
				   " },{"+
				   "     \"code\":\"music123\""+
				   " }],"+
				   " \"descriptions\": [{"+
				   "     \"metatagTitle\": \"Hollywood Movie\","+
				   "     \"name\":\"Hollywood\""+
				   " }],"+
				   " \"categoryImage\": \"image.jpg\""+
				"}");
		
		HttpEntity<String> entity = new HttpEntity<String>(jsonString, headers);
		restTemplate.put("http://localhost:8080/sm-shop/shop/services/category/DEFAULT/en/"+testCategoryID, entity);
		System.out.println("Category "+testCategoryID+" Updated.");
	}
	
	public void postCategory() throws Exception {
		restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String jsonString = ("{"+
				   " \"sortOrder\": \"0\","+
				   " \"depth\": \"2\","+
				   " \"code\": \"movie"+Math.random()+"\","+
				   " \"lineage\": \"/movie\","+
				   " \"categoryStatus\": \"true\","+
				   " \"visible\": \"true\","+
				   " \"categories\": [{"+
				   "     \"code\":\"music\""+
				   " },{"+
				   "     \"code\":\"music123\""+
				   " }],"+
				   " \"descriptions\": [{"+
				   "     \"metatagTitle\": \"Hollywood Movie\","+
				   "     \"name\":\"Hollywood\""+
				   " }],"+
				   " \"categoryImage\": \"image.jpg\""+
				"}");
		
		HttpEntity<String> entity = new HttpEntity<String>(jsonString, headers);

		ResponseEntity response = restTemplate.postForEntity("http://localhost:8080/sm-shop/shop/services/category/DEFAULT/en", entity, Category.class);

		Category cat = (Category) response.getBody();
		System.out.println("New Category ID : " + cat.getId());
		testCategoryID = cat.getId();
	}
	
	public void deleteCategory() throws Exception {
		restTemplate = new RestTemplate();
		restTemplate.delete("http://localhost:8080/sm-shop/shop/services/category/DEFAULT/en/"+testCategoryID);
		System.out.println("Category "+testCategoryID+" Deleted.");
	}
	
	@Test
	@Ignore
	public void getProducts() throws Exception {
		restTemplate = new RestTemplate();
		ResponseEntity<Product[]> response = restTemplate.getForEntity("http://localhost:8080/sm-shop/shop/services/products/DEFAULT/en/movie", Product[].class);
		
		if(response.getStatusCode() != HttpStatus.OK){
			throw new Exception();
		}else{
			System.out.println(response.getBody().length + " Product records found.");
		}
	}
	
	@Test
	@Ignore
	public void putProduct() throws Exception {
		restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
	}
	
	@Test
	@Ignore
	public void postProduct() throws Exception {
		restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		
	}
	
	@Test
	@Ignore
	public void deleteProduct() throws Exception {
		restTemplate = new RestTemplate();
		restTemplate.delete("http://localhost:8080/sm-shop/shop/services/products/DEFAULT/en/movie/1");
	}
	
}
