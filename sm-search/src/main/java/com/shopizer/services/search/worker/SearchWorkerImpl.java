package com.shopizer.services.search.worker;

import com.shopizer.search.services.SearchRequest;
import com.shopizer.search.services.SearchResponse;
import com.shopizer.search.utils.SearchClient;
import com.shopizer.services.search.impl.SearchServiceImpl;

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
