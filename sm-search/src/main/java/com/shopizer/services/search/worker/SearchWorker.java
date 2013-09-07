package com.shopizer.services.search.worker;

import com.shopizer.search.utils.SearchClient;
import com.shopizer.services.search.SearchRequest;
import com.shopizer.services.search.SearchResponse;

public interface SearchWorker {
	
	public SearchResponse execute(SearchClient client, SearchRequest request, ExecutionContext context) throws Exception;

}
