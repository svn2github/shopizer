package com.personalization.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.personalization.services.search.IndexKeywordRequest;
import com.personalization.services.search.SearchResponse;
import com.personalization.services.search.SearchService;
import com.personalization.services.search.impl.SearchServiceImpl;

@Controller
public class IndexControler {
	
	/**
	 * Rest method for indexing a document
	 * @param json
	 * @param collection
	 * @param object
	 * @param id
	 */
	@RequestMapping(value="/index/{collection}/{object}/",method=RequestMethod.POST)
	public @ResponseBody Response index(@RequestBody String json,@PathVariable("collection") String collection,@PathVariable("object") String object) {

			IndexResponse resp = new IndexResponse();
			try {
				
				resp.setJsonString(json);
				SearchService service = new SearchService();
				service.index(json, collection, object);
				resp.setResult(1);
				
			} catch (Exception e) {
				e.printStackTrace();
				resp.setMessage(e.getMessage());
			}
			
			return resp;

	}
	
	//@RequestMapping(value="/index2/{collection}/{object}/",method=RequestMethod.POST)
	//public @ResponseBody Response index2(@RequestBody String json,@PathVariable("collection") String collection,@PathVariable("object") String object) {
	@RequestMapping(value="/index/delete/{collection}/{object}/{id}/",method=RequestMethod.GET)
	public @ResponseBody Response deleteIndex(@PathVariable("collection") String collection,@PathVariable("object") String object,@PathVariable("id") String id) {

			IndexResponse resp = new IndexResponse();
			try {
				
				SearchService service = new SearchService();
				service.deleteObject(collection, object, id);
				resp.setResult(1);
				
			} catch (Exception e) {
				e.printStackTrace();
				resp.setMessage(e.getMessage());
			}
			
			return resp;

	}
	

}
