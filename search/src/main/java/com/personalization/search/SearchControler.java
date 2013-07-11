package com.personalization.search;

import java.util.Collection;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.personalization.services.search.SearchRequest;
import com.personalization.services.search.SearchResponse;
import com.personalization.services.search.SearchService;
import com.personalization.services.search.impl.SearchServiceImpl;

@Controller
//@RequestMapping("search/*")
public class SearchControler {
	
/*	@RequestMapping(value="/autocomplete", method=RequestMethod.GET)
	//public String searchInline(@PathVariable String search) {
	public @ResponseBody ShortSearchResponse readString(@RequestParam String search) {
			//System.out.println("*********************" + search);
			
		
			ShortSearchResponse resp = null;
		
			try {
				
				
				SearchService service = new SearchService();
				resp = service.searchAutoComplete("keywords", "value", search,10);

			    
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return resp;  
		


	}*/ 
	
	@RequestMapping(value="/search/autocomplete/{collection}",method=RequestMethod.POST)
	public @ResponseBody SearchResponse autocomplete(@RequestBody String json,@PathVariable("collection") String collection) {

		SearchResponse resp = null;
		
		try {
			
			
			SearchService service = new SearchService();
			resp = service.searchAutoComplete(collection,json,10);

		    
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resp;  
			
			
			
	}
	
	
	@RequestMapping(value="/search/{collection}",method=RequestMethod.POST)
	public @ResponseBody SearchResponse search(@RequestBody String json,@PathVariable("collection") String collection) {

	

			SearchService service = new SearchService();
			
			SearchRequest request = new SearchRequest();
			request.setCollection(collection);
			request.setJson(json);
			request.setSize(20);
			request.setStart(0);
			//request.setTerms("");
			
			try {
				SearchResponse resp = service.search(request);
				
				ObjectMapper mapper = new ObjectMapper();
				String indexData = mapper.writeValueAsString(resp);
				System.out.println(indexData);
				
				
				return resp;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;

	} 

	

}
