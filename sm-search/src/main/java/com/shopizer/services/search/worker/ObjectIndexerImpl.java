package com.shopizer.services.search.worker;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.shopizer.services.search.SearchService;
import com.shopizer.services.search.impl.SearchServiceImpl;
import com.shopizer.utils.FileUtil;
import com.shopizer.utils.IndexConfiguration;
import com.shopizer.utils.SearchClient;

public class ObjectIndexerImpl implements IndexWorker {
	
	private static boolean init = false;
	
	private List<IndexConfiguration> indexConfigurations;

	public List<IndexConfiguration> getIndexConfigurations() {
		return indexConfigurations;
	}

	public void setIndexConfigurations(List<IndexConfiguration> indexConfigurations) {
		this.indexConfigurations = indexConfigurations;
	}
	
	private static Logger log = Logger.getLogger(ObjectIndexerImpl.class);
	
	private synchronized void init(SearchClient client) {
		
		//get the list of configuration
		//get the collection name and index name
		//get the mapping file
		if(getIndexConfigurations()!=null && getIndexConfigurations().size()>0) {
			
			SearchServiceImpl service = new SearchServiceImpl(client);

			
				

				for(Object o : indexConfigurations) {
					
					IndexConfiguration config = (IndexConfiguration)o;
					
					if(!StringUtils.isBlank(config.getMappingFileName())) {
						try {
							String metadata = FileUtil.readFileAsString(config.getMappingFileName());
							if(!StringUtils.isBlank(metadata) && !StringUtils.isBlank(config.getIndexName())) {
							
								service.createIndice(metadata, config.getCollectionName(), config.getIndexName());
							}
						} catch (Exception e) {
							log.error("*********************************************");
							log.error(e);
							log.error("*********************************************");
						}
					}
				}
			
	
			
			init = true;
		}
	}

	public void execute(SearchClient client, String json, String collection, String object, String id, ExecutionContext context)
			throws Exception {
		// TODO Auto-generated method stub
		if(init==false) {
			init(client);
		}
		
		//get json object
		Map<String,Object> indexData = (Map)context.getObject("indexData");
		if(indexData==null) {
			ObjectMapper mapper = new ObjectMapper();
			indexData = mapper.readValue(json, Map.class);
		}
		
		if(context==null) {
			context = new ExecutionContext();
		}
		
		context.setObject("indexData", indexData);
		
		SearchServiceImpl service = new SearchServiceImpl(client);
		service.index(json, collection, object, id);
		

	}

}
