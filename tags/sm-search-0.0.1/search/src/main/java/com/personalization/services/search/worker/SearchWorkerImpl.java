package com.personalization.services.search.worker;

import com.personalization.services.search.SearchRequest;
import com.personalization.services.search.SearchResponse;
import com.personalization.services.search.impl.SearchServiceImpl;
import com.personalization.utils.SearchClient;

public class SearchWorkerImpl implements SearchWorker {

	public SearchResponse execute(SearchClient client, SearchRequest request, ExecutionContext context) throws Exception{
		SearchServiceImpl service = new SearchServiceImpl(client);
		SearchResponse response = service.search(request);

		response.setInputSearchJson(request.getJson());
		if(context == null) {
			context = new ExecutionContext();
		}
		context.setObject("response", response);
		return response;

	}

}
