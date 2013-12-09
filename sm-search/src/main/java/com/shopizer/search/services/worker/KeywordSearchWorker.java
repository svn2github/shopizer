package com.shopizer.search.services.worker;

import com.shopizer.search.services.SearchResponse;

public interface KeywordSearchWorker {
	
	public SearchResponse execute(String collection,String json,int size, ExecutionContext context) throws Exception;

}
