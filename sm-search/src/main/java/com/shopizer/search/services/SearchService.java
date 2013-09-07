package com.shopizer.search.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopizer.search.services.workflow.DeleteObjectWorkflow;
import com.shopizer.search.services.workflow.GetWorkflow;
import com.shopizer.search.services.workflow.IndexWorkflow;
import com.shopizer.search.services.workflow.SearchWorkflow;


/**
 * This is the main class for indexing and searching services
 * @author Carl Samson
 *
 */
@Service
public class SearchService {
	
	//workflow to be executed when indexing
	//private static List indexWorkflow = new ArrayList();
	
	//private static List searchWorkflow = new ArrayList();
	
	private static Logger log = Logger.getLogger(SearchService.class);
	
	@Autowired
	private DeleteObjectWorkflow deleteWorkflow;
	
	@Autowired
	private IndexWorkflow indexWorkflow;
	
	@Autowired
	private GetWorkflow getWorkflow;
	
	@Autowired
	private SearchWorkflow searchWorkflow;
	
	//@Autowired
	//private SearchWorkflow searchAutocompleteWorkflow;

	
/*	//loads configuration
	static {
		try {
			//PropertiesConfiguration config = new PropertiesConfiguration("personalization.properties");
			//config.setReloadingStrategy(new FileChangedReloadingStrategy());
			Config config = Config.getInstance();
			indexWorkflow = config.getConfiguration().getList("search.indexer.flow");
			//indexWorkflow = config.getList("search.indexer.flow");
			
			//insert metadata for Object index
			List metadataList = config.getConfiguration().getList("index.collection.name");
			if(metadataList!=null) {
				SearchServiceImpl service = new SearchServiceImpl();
				for(Object o : metadataList) {
					
					String coll = (String)o;
					//get metadata (will be taken from file)
					//String metadata = config.getConfiguration().getString("index.collection.indice." + coll);
					
					
					String metadata = FileUtil.readFileAsString(coll + ".json");
					String object = config.getConfiguration().getString("index.collection." + coll+ ".object");
					if(!StringUtils.isBlank(metadata) && !StringUtils.isBlank(object)) {
						
						service.createIndice(metadata, coll, object);
						
					}
				}
			}
			searchWorkflow = config.getConfiguration().getList("search.flow");

			
		} catch (Exception e) {
			log.error("****************************************");
			log.error(e);
			log.error("****************************************");
		}
		
	}*/
	
	
	public void deleteObject(String collection, String object, String id) throws Exception {
		
		String name = new StringBuilder().append("delete-").append(object).append("-workflow").toString();
		

		
		deleteWorkflow.deleteObject(collection, object, id);
		
		
	}
	
	
	public com.shopizer.search.services.GetResponse getObject(String collection, String object, String id) throws Exception {
		

		//GetWorkflow workflow = (GetWorkflow)BeanUtil.getBean("get-worflow");
		
		//if(workflow==null) {
		//	throw new Exception("No bean defined with name get-workflow");
		//}
		
		return getWorkflow.getObject(collection,object,id);

	}
	
	/**
	 * Index a document
	 * @param json
	 * @param collection
	 * @param object
	 * @param id
	 */
	public void index(String json, String collection, String object) throws Exception {
		
		String name = new StringBuilder().append("index-").append(object).append("-workflow").toString();
		
		
		//IndexWorkflow workflow = (IndexWorkflow)BeanUtil.getBean(name);
		
		//if(workflow==null) {
		//	throw new Exception("No bean defined with name " + na6me);
		//}
		
		indexWorkflow.index(json, collection, object);
		
		
/*		//validate valid json object
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> indexData = mapper.readValue(json, Map.class);
		
		//get id


		String _id = (String)indexData.get("id");
		if(StringUtils.isBlank(_id)) {
			log.warn("No id exist for object " + json + " will create a generic one");
			UUID uid = UUID.randomUUID();
			_id = uid.toString();
			indexData.put("id", _id);
			json = mapper.writeValueAsString(indexData);
		}
		
		if(indexWorkflow==null) {
			IndexWorker index = (IndexWorker)Class.forName("com.personalization.services.search.worker.ObjectIndexer").newInstance();
			index.execute(json, collection, object, _id, null);
		} else {
		
			for(Object o : indexWorkflow) {
				
				String className = (String)o;
	
				IndexWorker index = (IndexWorker)Class.forName(className).newInstance();
				index.execute(json, collection, object, _id, null);

			}
		
		}*/
		
	}
	
	/**
	 * This method will use autocomplete search
	 * @param collection
	 * @param field
	 * @param term
	 * @param size
	 * @return
	 */
/*	public ShortSearchResponse searchAutoComplete(String collection,String field,String term,int size) throws Exception {

		
		SearchServiceImpl service = new SearchServiceImpl();
		//Collection<String> hits = service.searchInlineTerm("keywords", "value", term, size);
		Collection<String> hits = service.searchInlineTerm(collection, field, term, size);
		ShortSearchResponse resp = new ShortSearchResponse();
		
		//Object[] objectArray = hits.toArray();
		String[] array = (String[])hits.toArray(new String[hits.size()]);
		
		
		resp.setInlineSearchList(array);
		resp.setInputSearch(term);
	    return resp;  
		
		
	}*/
	
	public SearchResponse searchAutoComplete(String collection,String json,int size) throws Exception {

		
		//SearchWorkflow workflow = (SearchWorkflow)BeanUtil.getBean("search-autocomplete-workflow");
		return searchWorkflow.searchAutocomplete(collection,json,size);
		
		/**
		SearchServiceImpl service = new SearchServiceImpl();
		//Collection<String> hits = service.searchInlineTerm("keywords", "value", term, size);
		Collection<String> hits = service.searchAutocomplete(collection, json, size);
		SearchResponse resp = new SearchResponse();
		
		//Object[] objectArray = hits.toArray();
		String[] array = (String[])hits.toArray(new String[hits.size()]);
		
		
		resp.setInlineSearchList(array);
		//resp.setInputSearch(term);
	    return resp;
	    **/  
		
		
	}
	
	public SearchResponse search(SearchRequest request) throws Exception {
		

/*		SearchServiceImpl service = new SearchServiceImpl();
		SearchResponse response = service.search(request);

		response.setInputSearchJson(request.getJson());
		
		if(searchWorkflow!=null) {

			for(Object o : searchWorkflow) {
				
				String className = (String)o;
	
				SearchWorker search = (SearchWorker)Class.forName(className).newInstance();
				search.execute(request.getJson(), request.getCollection());

			}
		
		}
		
		return response;*/
				
		//SearchWorkflow workflow = (SearchWorkflow)BeanUtil.getBean("search-workflow");
		return searchWorkflow.search(request);
	}
}
