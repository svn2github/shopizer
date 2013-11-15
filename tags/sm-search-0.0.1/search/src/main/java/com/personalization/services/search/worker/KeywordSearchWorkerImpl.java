package com.personalization.services.search.worker;

import java.util.Collection;

import com.personalization.services.search.SearchRequest;
import com.personalization.services.search.SearchResponse;
import com.personalization.services.search.impl.SearchServiceImpl;
import com.personalization.utils.SearchClient;

public class KeywordSearchWorkerImpl implements KeywordSearchWorker {

	public SearchResponse execute(SearchClient client, String collection,String json,int size, ExecutionContext context) throws Exception{

		
		
		SearchServiceImpl service = new SearchServiceImpl(client);
		Collection<String> hits = service.searchAutocomplete(collection, json, size);
		SearchResponse resp = new SearchResponse();

		String[] array = (String[])hits.toArray(new String[hits.size()]);
		
		
		resp.setInlineSearchList(array);
		
	    return resp; 

	}

}
