package com.personalization.services.search.worker;

import com.personalization.services.search.SearchRequest;
import com.personalization.services.search.SearchResponse;
import com.personalization.utils.SearchClient;

public interface KeywordSearchWorker {
	
	public SearchResponse execute(SearchClient client,String collection,String json,int size, ExecutionContext context) throws Exception;

}
