package com.shopizer.services.search.worker;

import com.shopizer.services.search.SearchRequest;
import com.shopizer.services.search.SearchResponse;
import com.shopizer.services.search.impl.SearchServiceImpl;
import com.shopizer.utils.SearchClient;

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
