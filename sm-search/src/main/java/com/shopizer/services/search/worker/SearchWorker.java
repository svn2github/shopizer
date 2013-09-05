package com.shopizer.services.search.worker;

import com.shopizer.services.search.SearchRequest;
import com.shopizer.services.search.SearchResponse;
import com.shopizer.utils.SearchClient;

public interface SearchWorker {
	
	public SearchResponse execute(SearchClient client, SearchRequest request, ExecutionContext context) throws Exception;

}
