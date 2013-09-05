package com.shopizer.services.search.worker;

import com.shopizer.utils.SearchClient;

public interface IndexWorker {
	
	public void execute(SearchClient client, String json, String collection, String object, String id, ExecutionContext context) throws Exception;

}
