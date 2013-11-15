package com.personalization.services.search.worker;

import com.personalization.utils.SearchClient;

public interface IndexWorker {
	
	public void execute(SearchClient client, String json, String collection, String object, String id, ExecutionContext context) throws Exception;

}
