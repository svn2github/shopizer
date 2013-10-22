package com.shopizer.search.services.worker;



import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.collections.CollectionUtils;

import com.shopizer.search.services.SearchRequest;
import com.shopizer.search.services.SearchResponse;
import com.shopizer.search.services.impl.SearchServiceImpl;
import com.shopizer.search.utils.CustomIndexConfiguration;
import com.shopizer.search.utils.DynamicIndexNameUtil;
import com.shopizer.search.utils.SearchClient;


public class DeleteKeywordsImpl implements DeleteObjectWorker {
	
	
	//private CustomIndexConfiguration indexConfiguration = null;
	private List<CustomIndexConfiguration> indexConfigurations = null;

/*	public CustomIndexConfiguration getIndexConfiguration() {
		return indexConfiguration;
	}


	public void setIndexConfiguration(CustomIndexConfiguration indexConfiguration) {
		this.indexConfiguration = indexConfiguration;
	}*/


	public void deleteObject(SearchClient client,String collection, String object, String id, ExecutionContext context) throws Exception {
		// TODO Auto-generated method stub
		
		@SuppressWarnings("rawtypes")
		Map indexData = (Map)context.getObject("indexData");
		
		if(indexData!=null) {
			
			
			if(!CollectionUtils.isEmpty(this.getIndexConfigurations())) {
		
				for(CustomIndexConfiguration indexConfiguration : this.getIndexConfigurations()) {
				
					String indexName = DynamicIndexNameUtil.getIndexName(indexConfiguration.getCollectionName(),indexData);
					StringTokenizer t = new StringTokenizer(indexConfiguration.getCollectionName(),"_");
					int countToken = t.countTokens();
					if(countToken>1) {
						
						StringBuilder iName = new StringBuilder();
						int count = 1;
						while(t.hasMoreTokens()) {
							
							String elem = t.nextToken();
							iName.append(DynamicIndexNameUtil.getIndexName(elem,indexData));
							if(count<countToken) {
								iName.append("_");
							}
							count++;
						}
						indexName = iName.toString();
					} 
					
					
					//build a search string for retrieving the internal id of the keyword
					String query = new StringBuilder()
						.append("{\"query\":{\"term\" : {\"_id_\" : \"")
						.append(id)
						.append("\" }}}").toString();
					
					SearchRequest sr = new SearchRequest();
					sr.setCollection(indexName);
					sr.setJson(query);
					
					SearchServiceImpl s = new SearchServiceImpl(client);
					SearchResponse r = s.search(sr);
					if(r!=null) {
						Collection<String> ids = r.getIds();
						
						s.bulkDeleteIndex(ids, indexName);
					}
		
				}
				
			}
			
		}
	}
	

	public void deleteObject(SearchClient client,String collection, String id) throws Exception {
		
		String query = new StringBuilder()
		.append("{\"query\":{\"term\" : {\"_id_\" : \"")
		.append(id)
		.append("\" }}}").toString();
	
		SearchRequest sr = new SearchRequest();
		sr.setCollection(collection);
		sr.setJson(query);
		
		SearchServiceImpl s = new SearchServiceImpl(client);
		SearchResponse r = s.search(sr);
		if(r!=null) {
			Collection<String> ids = r.getIds();
		
			s.bulkDeleteIndex(ids, collection);
		}
		
		
	}

	public List<CustomIndexConfiguration> getIndexConfigurations() {
		return indexConfigurations;
	}

	public void setIndexConfigurations(List<CustomIndexConfiguration> indexConfigurations) {
		this.indexConfigurations = indexConfigurations;
	}

}
