package com.shopizer.test;

import com.shopizer.search.services.SearchService;

public class TestIndex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestIndex ti = new TestIndex();
		ti.testIndex();

	}
	
	public void testIndex() {
		
		try {
			
			String jsonData = "{\"id\":\"3\",\"name\":\"zorino2\",\"sex\":\"m\",\"age\":\"32\",\"description\":\"Nice looking guy enjoying life and doing skuba diving. I love red wine and i enjoy sport\", \"tags\":[\"honnest\",\"sportive\",\"skuba diving\",\"web\"]}";
			

			
			
			SearchService service = new SearchService();
			service.index(jsonData, "profile_m", "profile");
			
			
			//service.getObject("product_en", "product", "1");
			
/*			jsonData = "{\"id\":\"2\",\"name\":\"lola\",\"sex\":\"f\",\"age\":\"36\",\"description\":\"Cultured woman enjoys shopping in Ney-York. Spend a lot of time training. I play tennis a lot\", \"tags\":[\"tennis\",\"sport\",\"training\",\"mature\"]}";
			

			service.index(jsonData, "profile_f", "profile", "2");*/
			
			System.out.println("done");
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}

}
