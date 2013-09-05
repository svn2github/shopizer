package com.shopizer.test;

import org.codehaus.jackson.map.ObjectMapper;

import com.shopizer.services.search.SearchRequest;
import com.shopizer.services.search.SearchResponse;
import com.shopizer.services.search.SearchService;

public class TestSearchInline {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestSearchInline ti = new TestSearchInline();
		ti.testSearch2();

	}
	
/*	public void testSearch() {
		
		try {
			
			
			ShortSearchResponse response;
			SearchService service = new SearchService();
			response = service.searchAutoComplete("m", "keyword", "skuba", 10);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}*/
	
	public void testSearch2() {
		
		try {
			
			
			
			//String json="{ \"query\": { \"filtered\" : { \"query\" : {\"query_string\" : {\"query\" : \"dynamic\"}},\"filter\" : {\"numeric_range\" : {\"age\" : {\"from\" : \"35\",\"to\" : \"40\",\"include_lower\" : true,\"include_upper\" : true}}}  }}}"; 
			//String json="{ \"filtered\" : { \"query\" : {\"wildcard\" : {\"keyword\" : \"skub*\"}},\"filter\" : {\"numeric_range\" : {\"age\" : {\"from\" : \"30\",\"to\" : \"44\",\"include_lower\" : true,\"include_upper\" : true}}}  }}";
			             //{"filtered" : {"query" : {"wildcard" : {"keyord" : "d*" }},"filter" : {"numeric_range" : {"age" : {"from" : "25","to" : "45","include_lower" : true,"include_upper" : true}}}}}
			//String json="{\"wildcard\":{\"keyword\":\"dyn*\"}}";
			
			String json ="{\"query\":{\"filtered\":{\"query\":{\"text\":{\"_all\":\"beach\"}},\"filter\":{\"numeric_range\":{\"age\":{\"from\":\"22\",\"to\":\"45\",\"include_lower\":true,\"include_upper\":true}}}}},\"highlight\":{\"fields\":{\"description\":{}}},\"facets\":{\"tags\":{\"terms\":{\"field\":\"tags\"}}}}";

			
			System.out.println(json);
			
			
			SearchRequest request = new SearchRequest();
			request.setCollection("profile_m");
			request.setJson(json);
			request.setSize(20);
			request.setStart(0);
			
			SearchResponse response;
			SearchService service = new SearchService();
			SearchResponse resp= service.search(request);
			
			ObjectMapper mapper = new ObjectMapper();
			String indexData = mapper.writeValueAsString(resp);
			System.out.println(indexData);
			
			//response = service.searchAutoComplete("m",json, 10);
			
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}

}
