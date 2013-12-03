package com.salesmanager.test.shop.controller.product.rest;

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

import com.salesmanager.web.entity.catalog.category.CategoryDescription;
import com.salesmanager.web.entity.catalog.category.PersistableCategory;
import com.salesmanager.web.entity.catalog.manufacturer.ManufacturerDescription;
import com.salesmanager.web.entity.catalog.manufacturer.PersistableManufacturer;
import com.salesmanager.web.entity.catalog.product.ReadableProduct;

public class ProductManagementAPITest {
	
	private RestTemplate restTemplate;
	
	private Long testCategoryID;
	
	private Long testProductID;

	@Test
	@Ignore
	public void categoryRESTTest() throws Exception {
		

		
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
	
	@Test
	public void createManufacturer() throws Exception {
		
		ManufacturerDescription description = new ManufacturerDescription();
		description.setLanguage("en");
		description.setName("Tag Heuer");
		description.setFriendlyUrl("tag-watches");
		description.setTitle("Tag Heuer");
		
		List<ManufacturerDescription> descriptions = new ArrayList<ManufacturerDescription>();
		descriptions.add(description);
		
		PersistableManufacturer manufacturer = new PersistableManufacturer();
		manufacturer.setOrder(1);
		manufacturer.setDescriptions(descriptions);
		

		ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = writer.writeValueAsString(manufacturer);
		
		System.out.println(json);
		
/*		{
			  "descriptions" : [ {
			    "name" : "Tag Heuer",
			    "description" : null,
			    "friendlyUrl" : "tag-watches",
			    "keyWords" : null,
			    "highlights" : null,
			    "metaDescription" : null,
			    "title" : "Tag Heuer",
			    "language" : "en",
			    "id" : 0
			  } ],
			  "order" : 1,
			  "id" : 0
			}*/
		
		restTemplate = new RestTemplate();

		
		HttpEntity<String> entity = new HttpEntity<String>(json, getHeader());

		ResponseEntity response = restTemplate.postForEntity("http://localhost:8080/sm-shop/shop/services/private/manufacturer/DEFAULT/create", entity, PersistableManufacturer.class);

		PersistableManufacturer manuf = (PersistableManufacturer) response.getBody();
		System.out.println("New Manufacturer ID : " + manuf.getId());
		
		
	}
		
	
	@Test
	@Ignore
	public void getProducts() throws Exception {
		restTemplate = new RestTemplate();
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(getHeader());
		
		ResponseEntity<ReadableProduct[]> response = restTemplate.exchange("http://localhost:8080/sm-shop/shop/services/rest/products/DEFAULT/en/"+testCategoryID, HttpMethod.GET, httpEntity, ReadableProduct[].class);
		
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
		
		//TODO: Put Product
		
	}
	
	@Test
	@Ignore
	public void postProduct() throws Exception {
		restTemplate = new RestTemplate();
		
		//TODO: Post Product

		
	}
	
	@Test
	@Ignore
	public void deleteProduct() throws Exception {
		restTemplate = new RestTemplate();
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(getHeader());
		
		restTemplate.exchange("http://localhost:8080/sm-shop/shop/services/rest/products/DEFAULT/en/"+testCategoryID+"/"+testProductID, HttpMethod.DELETE, httpEntity, ReadableProduct.class);
		System.out.println("Product "+testProductID+" Deleted.");
	}
	
}
