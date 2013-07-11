package com.personalization.services.search.workflow;

import java.util.Collection;
import java.util.List;

import com.personalization.services.search.SearchRequest;
import com.personalization.services.search.SearchResponse;
import com.personalization.services.search.impl.SearchServiceImpl;
import com.personalization.services.search.worker.KeywordSearchWorker;
import com.personalization.services.search.worker.SearchWorker;


public class SearchWorkflow extends Workflow{
	
	private List searchWorkflow;
	private List searchKeywordWorkflow;


	


	public List getSearchKeywordWorkflow() {
		return searchKeywordWorkflow;
	}

	public void setSearchKeywordWorkflow(List searchKeywordWorkflow) {
		this.searchKeywordWorkflow = searchKeywordWorkflow;
	}

	public List getSearchWorkflow() {
		return searchWorkflow;
	}

	public void setSearchWorkflow(List searchWorkflow) {
		this.searchWorkflow = searchWorkflow;
	}

	public SearchResponse searchAutocomplete(String collection,String json,int size) throws Exception {
	
		
		SearchResponse response = null;

		
		if(searchKeywordWorkflow!=null) {
			for(Object o : searchKeywordWorkflow) {
				
				//String className = (String)o;
				//SearchWorker search = (SearchWorker)Class.forName(className).newInstance();
				//search.execute(request.getJson(), request.getCollection());
				KeywordSearchWorker sw = (KeywordSearchWorker)o;
				response = sw.execute(super.getSearchClient(),collection,json,size, null);
			}
		}
		
		return response;
		
	}
	
	public SearchResponse search(SearchRequest request) throws Exception {
		
		//SearchServiceImpl service = new SearchServiceImpl();
		//SearchResponse response = service.search(request);

		//response.setInputSearchJson(request.getJson());
		SearchResponse response = null;

		
		if(searchWorkflow!=null) {
			for(Object o : searchWorkflow) {
				
				//String className = (String)o;
				//SearchWorker search = (SearchWorker)Class.forName(className).newInstance();
				//search.execute(request.getJson(), request.getCollection());
				SearchWorker sw = (SearchWorker)o;
				response = sw.execute(super.getSearchClient(),request, null);
			}
		}
		
		return response;
		
	}

}
