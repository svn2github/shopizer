package com.shopizer.services.search.workflow;

import org.springframework.stereotype.Component;

import com.shopizer.services.search.impl.SearchServiceImpl;
import com.shopizer.utils.SearchClient;

@Component
public class GetWorkflow extends Workflow {
	
	
	public com.shopizer.services.search.GetResponse getObject(String collection, String object, String id) throws Exception {
		
		SearchServiceImpl search = new SearchServiceImpl(super.getSearchClient());
		return search.getObject(collection, object, id);
		
	}

}
