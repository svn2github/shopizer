package com.personalization.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.personalization.services.search.SearchService;

@Controller
public class IndexControler {
	
	
	
	@Autowired
	private SearchService searchService;
	
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

				searchService.index(json, collection, object);
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
				
				searchService.deleteObject(collection, object, id);
				resp.setResult(1);
				
			} catch (Exception e) {
				e.printStackTrace();
				resp.setMessage(e.getMessage());
			}
			
			return resp;

	}
	

}
