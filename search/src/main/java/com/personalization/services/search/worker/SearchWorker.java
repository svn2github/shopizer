package com.personalization.services.search.worker;

import com.personalization.services.search.SearchRequest;
import com.personalization.services.search.SearchResponse;
import com.personalization.utils.SearchClient;

public interface SearchWorker {
	
	public SearchResponse execute(SearchClient client, SearchRequest request, ExecutionContext context) throws Exception;

}
