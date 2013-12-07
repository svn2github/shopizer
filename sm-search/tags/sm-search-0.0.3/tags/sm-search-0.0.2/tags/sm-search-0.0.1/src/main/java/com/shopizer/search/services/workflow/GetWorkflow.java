package com.shopizer.search.services.workflow;

import org.springframework.stereotype.Component;

import com.shopizer.search.services.impl.SearchServiceImpl;



@Component
public class GetWorkflow extends Workflow {
	
	
	public com.shopizer.search.services.GetResponse getObject(String collection, String object, String id) throws Exception {
		
		SearchServiceImpl search = new SearchServiceImpl(super.getSearchClient());
		return search.getObject(collection, object, id);
		
	}

}
