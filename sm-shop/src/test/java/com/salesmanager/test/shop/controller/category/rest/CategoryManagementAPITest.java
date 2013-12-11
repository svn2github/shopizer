package com.salesmanager.test.shop.controller.category.rest;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.client.RestTemplate;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.web.entity.catalog.category.Category;
import com.salesmanager.web.entity.catalog.category.CategoryDescription;
import com.salesmanager.web.entity.catalog.category.PersistableCategory;
import com.salesmanager.web.entity.catalog.category.ReadableCategory;

public class CategoryManagementAPITest {
	
	private RestTemplate restTemplate;
	
	private Long testCategoryID;
	
	private Long testProductID;

	@Test
	@Ignore
	public void categoryRESTTest() throws Exception {
		
		//getCategory();
		
		//create customer
		postCategory();
		
		//get All customers
		//getCategories();
		
		//update customer
		//putCategory();
		
		//delete customer
		//deleteCategory();
		
	}
	
	public HttpHeaders getHeader(){
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
		//MediaType.APPLICATION_JSON //for application/json
		headers.setContentType(mediaType);
		//Basic Authentication
		String authorisation = "admin" + ":" + "password";
		byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes());
		headers.add("Authorization", "Basic " + new String(encodedAuthorisation));
		return headers;
	}
	
	public void getCategory() throws Exception {
		restTemplate = new RestTemplate();
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(getHeader());
		
		ResponseEntity<ReadableCategory> response = restTemplate.exchange("http://localhost:8080/sm-shop/shop/services/public/category/DEFAULT/en/4", HttpMethod.GET, httpEntity, ReadableCategory.class);
		
		if(response.getStatusCode() != HttpStatus.OK){
			throw new Exception();
		}else{
			System.out.println(response.getBody() + " Category record found.");
		}
	}
	
/*	public void getCategories() throws Exception {
		restTemplate = new RestTemplate();
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(getHeader());
		
		ResponseEntity<Category[]> response = restTemplate.exchange("http://localhost:8080/sm-shop/shop/services/rest/category/DEFAULT/en", HttpMethod.GET, httpEntity, Category[].class);
		
		if(response.getStatusCode() != HttpStatus.OK){
			throw new Exception();
		}else{
			System.out.println(response.getBody().length + " Category records found.");
		}
	}*/
	

	
	public void postCategory() throws Exception {
		restTemplate = new RestTemplate();
		
/*		String jsonString = ("{"+
				   " \"sortOrder\": \"0\","+
				   " \"depth\": \"2\","+
				   " \"code\": \"movie"+Math.random()+"\","+
				   " \"lineage\": \"/movie\","+
				   " \"categoryStatus\": \"true\","+
				   " \"visible\": \"true\","+
				   " \"descriptions\": [{"+
				   "     \"metatagTitle\": \"Hollywood Movie\","+
				   "     \"name\":\"Hollywood\""+
				   " }],"+
				   " \"categoryImage\": \"image.jpg\""+
				"}");*/
		
		PersistableCategory newCategory = new PersistableCategory();
		newCategory.setCode("javascript");
		newCategory.setSortOrder(1);
		newCategory.setVisible(true);
		
		Category parent = new Category();
		parent.setId(4L);
		
		newCategory.setParent(parent);
		
		CategoryDescription description = new CategoryDescription();
		description.setLanguage("en");
		description.setName("Javascript");
		description.setFriendlyUrl("javascript");
		description.setTitle("Javascript");
		
		List<CategoryDescription> descriptions = new ArrayList<CategoryDescription>();
		descriptions.add(description);
		
		newCategory.setDescriptions(descriptions);
		
		ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = writer.writeValueAsString(newCategory);
		
		System.out.println(json);
		
		
		HttpEntity<String> entity = new HttpEntity<String>(json, getHeader());

		ResponseEntity response = restTemplate.postForEntity("http://localhost:8080/sm-shop/shop/services/private/category/DEFAULT", entity, PersistableCategory.class);

		PersistableCategory cat = (PersistableCategory) response.getBody();
		System.out.println("New Category ID : " + cat.getId());
		testCategoryID = cat.getId();
	}
	
	public void deleteCategory() throws Exception {
		restTemplate = new RestTemplate();
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(getHeader());
		
		restTemplate.exchange("http://localhost:8080/sm-shop/shop/services/category/DEFAULT/en/"+testCategoryID, HttpMethod.DELETE, httpEntity, Category.class);
		System.out.println("Category "+testCategoryID+" Deleted.");
	}
	
/*	@Test
	@Ignore
	public void getProducts() throws Exception {
		restTemplate = new RestTemplate();
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(getHeader());
		
		ResponseEntity<Product[]> response = restTemplate.exchange("http://localhost:8080/sm-shop/shop/services/rest/products/DEFAULT/en/"+testCategoryID, HttpMethod.GET, httpEntity, Product[].class);
		
		if(response.getStatusCode() != HttpStatus.OK){
			throw new Exception();
		}else{
			System.out.println(response.getBody().length + " Product records found.");
		}
	}*/
	

	
}
