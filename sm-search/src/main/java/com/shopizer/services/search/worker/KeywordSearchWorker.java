package com.shopizer.services.search.worker;

import com.shopizer.search.utils.SearchClient;
import com.shopizer.services.search.SearchRequest;
import com.shopizer.services.search.SearchResponse;

public interface KeywordSearchWorker {
	
	public SearchResponse execute(SearchClient client,String collection,String json,int size, ExecutionContext context) throws Exception;

}
