package com.salesmanager.test.shop.controller.category.rest;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.client.RestTemplate;

/**
 * ** Slowlow decommisionned for CategoryManagementAPITest **
 * @author casams1
 *
 */
public class ShoppingCategoryControllerTest {
	
	private RestTemplate restTemplate;
	
	private Long testCategoryID;
	
	private Long testProductID;

	@Test
	@Ignore
	public void categoryRESTTest() throws Exception {
		

		//create category
		//postCategory();
		
		//getCategories();
		

		//putCategory();

		//delete customer
		//deleteCategory();
		
	}
	
	public HttpHeaders getHeader(){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//Basic Authentication
		String authorisation = "admin" + ":" + "password";
		byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes());
		headers.add("Authorization", "Basic " + new String(encodedAuthorisation));
		return headers;
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
	
/*	public void putCategory() throws Exception {
		restTemplate = new RestTemplate();
	
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
		
		HttpEntity<String> entity = new HttpEntity<String>(jsonString, getHeader());
		
		restTemplate.put("http://localhost:8080/sm-shop/shop/services/rest/category/DEFAULT/en/"+testCategoryID, entity);
		System.out.println("Category "+testCategoryID+" Updated.");
	}*/
	
/*	public void postCategory() throws Exception {
		restTemplate = new RestTemplate();
		
		String jsonString = ("{"+
				   " \"sortOrder\": \"1\","+
				   " \"code\": \"movie"+Math.random()+"\","+
				   " \"visible\": \"true\","+
				   " \"descriptions\": [{"+
				   "     \"metatagTitle\": \"Hollywood Movie\","+
				   "     \"name\":\"Hollywood\","+
				   "     \"description\":\"Hollywood movie\","+
				   "     \"friendlyUrl\":\"hollywood-movie\","+
				   "     \"language\":\"en\""+
				   " }]"+
				"}");
		
		HttpEntity<String> entity = new HttpEntity<String>(jsonString, getHeader());

		ResponseEntity response = restTemplate.postForEntity("http://localhost:8080/sm-shop/shop/services/rest/category/DEFAULT/en", entity, Category.class);

		Category cat = (Category) response.getBody();
		System.out.println("New Category ID : " + cat.getId());
		testCategoryID = cat.getId();
	}*/
	
/*	public void deleteCategory() throws Exception {
		restTemplate = new RestTemplate();
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(getHeader());
		
		restTemplate.exchange("http://localhost:8080/sm-shop/shop/services/category/DEFAULT/en/"+testCategoryID, HttpMethod.DELETE, httpEntity, Category.class);
		System.out.println("Category "+testCategoryID+" Deleted.");
	}*/
	
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
	
/*	@Test
	@Ignore
	public void putProduct() throws Exception {
		restTemplate = new RestTemplate();
		
		//TODO: Put Product
		
	}*/
	
/*	@Test
	@Ignore
	public void postProduct() throws Exception {
		restTemplate = new RestTemplate();
		
		//TODO: Post Product

		
	}*/
	
/*	@Test
	@Ignore
	public void deleteProduct() throws Exception {
		restTemplate = new RestTemplate();
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(getHeader());
		
		restTemplate.exchange("http://localhost:8080/sm-shop/shop/services/rest/products/DEFAULT/en/"+testCategoryID+"/"+testProductID, HttpMethod.DELETE, httpEntity, Product.class);
		System.out.println("Product "+testProductID+" Deleted.");
	}*/
	
}
