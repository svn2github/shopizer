package com.personalization.services.search.workflow;

import com.personalization.services.search.impl.SearchServiceImpl;
import com.personalization.utils.SearchClient;

public class GetWorkflow extends Workflow {
	
	
	public com.personalization.services.search.GetResponse getObject(String collection, String object, String id) throws Exception {
		
		SearchServiceImpl search = new SearchServiceImpl(super.getSearchClient());
		return search.getObject(collection, object, id);
		
	}

}
