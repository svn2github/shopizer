package com.shopizer.services.search.worker;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;


import com.shopizer.search.services.SearchHit;
import com.shopizer.search.services.SearchRequest;
import com.shopizer.search.services.SearchResponse;
import com.shopizer.search.utils.SearchClient;
import com.shopizer.services.search.impl.SearchServiceImpl;

public class DeleteObjectImpl implements DeleteObjectWorker {

	public void deleteObject(SearchClient client, String collection, String object, String id, ExecutionContext context) throws Exception {
		// TODO Auto-generated method stub
		
		//need to get the original entry
		SearchServiceImpl s = new SearchServiceImpl(client);
		com.shopizer.search.services.GetResponse r = s.getObject(collection, object, id);
		
		if(r!=null) {
			
			if(context==null) {
				context = new ExecutionContext();
			}
			
			
			if(r.getFields()!=null && r.getFields().size()>0) {
			
				//System.out.println(r.toString());
	
				
				//ObjectMapper mapper = new ObjectMapper();
				//Map indexData = mapper.readValue(r.getResponseAsString(), Map.class);
				
				context.setObject("indexData", r.getFields());
			
			}
			
		}
		
/*		String query = new StringBuilder()
		.append("{\"query\":{\"term\" : {\"_id\" : \"")
		.append(id)
		.append("\" }}}").toString();
		
		SearchRequest r = new SearchRequest();
		r.setCollection(collection);
		r.setJson(query);*/
	
		
		//SearchResponse resp =s.search(r);
		
		//if(resp.getSearchHits()!=null && resp.getSearchHits().size()>0) {
			
			//List<SearchHit> hits = (List<SearchHit>)resp.getSearchHits();
		
			//SearchHit hit = hits.get(0);
			//Map obj = hit.getSource();
			
/*			Map<String,Object> indexData = (Map)context.getObject("indexData");
			if(indexData==null) {
				ObjectMapper mapper = new ObjectMapper();
				indexData = mapper.readValue(json, Map.class);
			}*/
			
			//if(context==null) {
			//	context = new ExecutionContext();
			//}
			
			//context.setObject("indexData", obj);
		
		//}
		
		SearchServiceImpl search = new SearchServiceImpl(client);
		search.delete(collection, object, id);

	}

}
