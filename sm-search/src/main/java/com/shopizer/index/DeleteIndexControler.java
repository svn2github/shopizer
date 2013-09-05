package com.shopizer.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DeleteIndexControler {
	
	//@RequestMapping(value="/deleteIndex/{collection}/{object}/{id}/",method=RequestMethod.GET)
	//public @ResponseBody Response deleteIndex(@PathVariable("collection") String collection,@PathVariable("object") String object,@PathVariable("id") String id) {
/*	@RequestMapping(value="/index2/{collection}/{object}/",method=RequestMethod.POST)
	public @ResponseBody Response index2(@RequestBody String json,@PathVariable("collection") String collection,@PathVariable("object") String object) {


			IndexResponse resp = new IndexResponse();
			try {
				
				//SearchService service = new SearchService();
				//service.deleteObject(collection, object, id);
				resp.setResult(1);
				
			} catch (Exception e) {
				e.printStackTrace();
				resp.setMessage(e.getMessage());
			}
			
			return resp;

	}*/

}
