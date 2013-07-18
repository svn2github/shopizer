package com.personalization.services.search.impl;

import static org.elasticsearch.node.NodeBuilder.*; 

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.index.query.XContentFilterBuilder;
//import org.elasticsearch.index.query.XContentQueryBuilder;

//??????
import static org.elasticsearch.index.query.FilterBuilders.*; 
import static org.elasticsearch.index.query.QueryBuilders.*; 

import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacet;
import org.elasticsearch.search.facet.filter.FilterFacet;
import org.elasticsearch.search.facet.geodistance.GeoDistanceFacet;
import org.elasticsearch.search.facet.histogram.HistogramFacet;
import org.elasticsearch.search.facet.query.QueryFacet;
import org.elasticsearch.search.facet.range.RangeFacet;
import org.elasticsearch.search.facet.statistical.StatisticalFacet;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.terms.TermsFacet.Entry;

import com.personalization.services.search.IndexKeywordRequest;
import com.personalization.services.search.SearchField;
import com.personalization.services.search.SearchRequest;
import com.personalization.services.search.SearchResponse;
import com.personalization.services.search.field.BooleanField;
import com.personalization.services.search.field.DateField;
import com.personalization.services.search.field.DoubleField;
import com.personalization.services.search.field.Field;
import com.personalization.services.search.field.IntegerField;
import com.personalization.services.search.field.ListField;
import com.personalization.services.search.field.StringField;
import com.personalization.services.search.worker.ExecutionContext;
import com.personalization.utils.SearchClient;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class SearchServiceImpl {
	
	
	private SearchClient searchClient = null;
	
	public SearchServiceImpl(SearchClient searchClient) {
		this.searchClient = searchClient;
	}
	
	/**
	 * Creates a structure that represents the object and the content to be indexed
	 */
	public void createIndice(String json,String collection,String object) throws Exception {
		
	
		
		Client client = searchClient.getClient();
		
		
/*		Map map = client.admin().cluster().state(new
				ClusterStateRequest()).actionGet().getState().getMetaData().getIndices();

		if(map!=null && map.size()>0) {
			
			if(map.containsKey("collection")) {
				return;
			}
			
		}*/


		//maintain a list of created index
		
		client.admin().indices().  
	    create(new CreateIndexRequest(collection).mapping(object, json)).
	    actionGet();



		
/*		client.admin().indices().  
        create(new CreateIndexRequest(collection).mapping(object, json)).
        actionGet();
		*/
		
	}
	
	
    /**
     * Will index an object in json format in a collection
     * of indexes
     * @param collection
     * @param object
     * @param id
     */
	public void index(String json, String collection, String object, String id) {
		

		
		Client client = searchClient.getClient();
		
		//System.out.println(collection + " " + object + " " + id + "  " + json);
		
        IndexResponse r = client.prepareIndex(collection, object, id) 
        .setSource(json) 
        .execute() 
        .actionGet();
		
	}
	
	public void delete(String collection, String object, String id) throws Exception {
		

		
		Client client = searchClient.getClient();
		
		DeleteResponse r = client.prepareDelete(collection, object, id) 
        .execute() 
        .actionGet();
		

	}
	
	public void bulkDeleteIndex(Collection<String> ids, String collection) throws Exception {
		

		Client client = searchClient.getClient();
		
		if(ids!=null && ids.size()>0) {
			
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			
			for(String s : ids) {
				
				
				DeleteRequest dr = new DeleteRequest();
				dr.type("keyword").index(collection).id(s);
				
				System.out.println(dr.toString());
				
				bulkRequest.add(dr);
				
			}
			
			BulkResponse bulkResponse = bulkRequest.execute().actionGet(); 
			if (bulkResponse.hasFailures()) { 
			    // process failures by iterating through each bulk response item 
				System.out.println("has failures");
			}
			
		}
		

	}
	

	
	/**
	 * Index keywords in bulk
	 * @param bulks
	 * @param collection
	 * @param object
	 * @param id
	 */
	public void bulkIndexKeywords(Collection<IndexKeywordRequest> bulks, String collection, String object) {
		
		
		try {
			

			Client client = searchClient.getClient();
			BulkRequestBuilder bulkRequest = client.prepareBulk(); 
			
			//@todo, index in appropriate Locale
			for(IndexKeywordRequest key : bulks) {
			
				// either use client#prepare, or use Requests# to directly build index/delete requests 
				//bulkRequest.add(client.prepareIndex(collection, object, key.getId()) 
				
				String id = key.getKey();
				if(id.length()>25) {
					id = id.substring(0,25);
				}
				id = id.trim().toLowerCase();
				
				XContentBuilder b = jsonBuilder() 
                .startObject() 
                	.field("id", id)//index name is the value trimed and lower cased
                    .field("keyword", key.getKey())
                    .field("_id_", key.getId());
                    
                    Collection fields = key.getFilters();
                    if(fields.size()>0) {
                    	
                    	for(Object o : fields) {
                    		
                    		if(o instanceof BooleanField) {
                    			
                    			Boolean val = ((BooleanField)o).getValue();
                    			b.field(((Field)o).getName(), val.booleanValue());
                    			
                    		} else if(o instanceof IntegerField) {
                    			
                    			Integer val = ((IntegerField)o).getValue();
                    			b.field(((Field)o).getName(), val.intValue());
                    			
                    			
                    		} else if(o instanceof ListField) {
                    			
                    			List val = ((ListField)o).getValue();
                    			b.field(((Field)o).getName(), val);
                    			
                    		} else if(o instanceof DoubleField) {
                    			
                    			Double val = ((DoubleField)o).getValue();
                    			b.field(((Field)o).getName(), val.doubleValue());
                    			
                    		} else if(o instanceof DateField) {
                    			
                    			Date val = ((DateField)o).getValue();
                    			b.field(((Field)o).getName(), val);
                    			
                    		} else {
                    			
                    			String val = ((StringField)o).getValue();
                    			b.field(((Field)o).getName(), val);
                    			
                    		}
                    	}
                    }
                    
                b.endObject();

				
				bulkRequest.add(client.prepareIndex(collection, object).setSource(b));
			}
			 
	
			         
			BulkResponse bulkResponse = bulkRequest.execute().actionGet(); 
			if (bulkResponse.hasFailures()) { 
			    // process failures by iterating through each bulk response item 
				System.out.println("Has failures");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	public com.personalization.services.search.GetResponse getObject(String collection, String object, String id) throws Exception {
		
		
	
		
		/**
		 * This method throws an exception
		 */
		GetResponse response = searchClient.getClient().prepareGet(collection, object, id)
		.setOperationThreaded(true) 
		.setFields("_source")
        .execute() 
        .actionGet();
				
		
		com.personalization.services.search.GetResponse r = null;
		if(response!=null) {
			
			r = new com.personalization.services.search.GetResponse(response);
			
		}
		
		return r;
		
	}
	
	/**
	 * Search for a term
	 * @param term
	 * @param collection
	 * @param field
	 * @return
	 */
	//public Collection<SearchHit> searchTerm(String collection,String field,String term) {
	public SearchResponse search(SearchRequest request) throws Exception {

		SearchResponse response = new SearchResponse();
		try {
			
			//SearchClient searchClient = new SearchClient("seniorcarlos","127.0.0.1",9300);

			

			
			org.elasticsearch.action.search.SearchRequestBuilder builder = searchClient.getClient().prepareSearch(request.getCollection())
	        //.setQuery("{ \"term\" : { \"keyword\" : \"dynamic\" }}")
			//.setQuery(request.getJson())
			//.addHighlightedField("description")
			//.addHighlightedField("tags")
			//with extra you can set everything
			.setExtraSource(request.getJson())
	        .setExplain(false);
			
			builder.setFrom(request.getStart());
			if(request.getSize()>-1) {
				builder.setSize(request.getSize());
			}
	        
/*	        Collection<SearchField> queryStringColl = request.getTerms();
	        StringBuilder queryBuild = new StringBuilder();
	        if(queryStringColl==null) {
	        	throw new Exception("terms cannot be null");
	        }
	        
	        int i = 1;
	        for(SearchField field : queryStringColl) {
	        	
	        	queryBuild.append(field.getField()).append(":").append(request.getSearchString());
	        	if(i<queryStringColl.size()) {
	        		queryBuild.append(",");
	        	}
	        	i++;
	        }*/
	        
	        
	        //try this block (filters)
/*	        XContentFilterBuilder queryFilter = FilterBuilders.matchAllFilter(); 
	        XContentQueryBuilder query = 
	        QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), 
	        queryFilter); 
	        builder.setQuery(query);*/

	        
	        //                                                    _all
	        //XContentQueryBuilder qb = QueryBuilders.queryString(field + ":" + term);
	        //XContentQueryBuilder qb = QueryBuilders.queryString("_all" + ":" + "Tom");
	        //XContentQueryBuilder qb = QueryBuilders.queryString(queryBuild.toString());
	        //builder.setQuery(qb);
	        

	        //builder.setQuery(termQuery(field, term));
	
	        org.elasticsearch.action.search.SearchResponse rsp = builder.execute().actionGet();
	        SearchHit[] docs = rsp.getHits().getHits();
	        List<com.personalization.services.search.SearchHit> hits = new ArrayList();
	        List ids = new ArrayList();
	        response.setCount(docs.length);
	        for (SearchHit sd : docs) {
	          //to get explanation you'll need to enable this when querying:
	          //System.out.println(sd.getExplanation().toString());
	
	          // if we use in mapping: "_source" : {"enabled" : false}
	          // we need to include all necessary fields in query and then to use doc.getFields()
	          // instead of doc.getSource()
	        	//com.personalization.services.search.SearchHit hit = new com.personalization.services.search.SearchHit();
	        	//hit.setSource(sd.sourceAsString());
	        	//hit.setScore(sd.getScore());
	        	//hit.setHighligts(sd.highlightFields());
	        	//hits.add(hit);
	        	

	            System.out.println(sd.sourceAsString());
	            //System.out.println(sd.getScore());
	        	com.personalization.services.search.SearchHit hit = new com.personalization.services.search.SearchHit(sd);
	        	hits.add(hit);
	        	ids.add(sd.getId());
	        	//Map source = sd.getSource();
	        	//Map highlights = sd.getHighlightFields();
	        	//hits.add(sd);
	          
	          
	        }
	        
	        response.setIds(ids);
	        
	        Facets facets = rsp.getFacets();
	        if(facets!=null) {
	        	Map facetsMap = new HashMap();
	        	for (Facet facet : facets.facets()) {
	        		 
	        	     if (facet instanceof TermsFacet) {
	        	         TermsFacet ff = (TermsFacet) facet;
	        	         com.personalization.services.search.Facet f = new com.personalization.services.search.Facet();
	        	         f.setName(ff.getName());
	        	         List entries = new ArrayList();
	        	         for(Object o : ff) {
	        	        	 com.personalization.services.search.Entry entry = new com.personalization.services.search.Entry();
	        	        	 Entry e = (Entry)o;
	        	        	 entry.setName(e.getTerm().string());//TODO
	        	        	 entry.setCount(e.getCount());
	        	        	 entries.add(entry);
	        	         }
	        	         f.setEntries(entries);
	        	         facetsMap.put(ff.getName(), f);
	        	    }
	        	     else if (facet instanceof RangeFacet) {
	        	    	 RangeFacet ff = (RangeFacet) facet;
	        	         com.personalization.services.search.Facet f = new com.personalization.services.search.Facet();
	        	         f.setName(ff.getName());
	        	         List entries = new ArrayList();
	        	         for(Object o : ff) {
	        	        	 com.personalization.services.search.Entry entry = new com.personalization.services.search.Entry();
	        	        	 Entry e = (Entry)o;
	        	        	 entry.setName(e.getTerm().string());//TODO
	        	        	 entry.setCount(e.getCount());
	        	        	 entries.add(entry);
	        	         }
	        	         f.setEntries(entries);
	        	         facetsMap.put(ff.getName(), f);
	        	    }
	        	     else if (facet instanceof HistogramFacet) {
	        	    	 HistogramFacet ff = (HistogramFacet) facet;
	        	         com.personalization.services.search.Facet f = new com.personalization.services.search.Facet();
	        	         f.setName(ff.getName());
	        	         List entries = new ArrayList();
	        	         for(Object o : ff) {
	        	        	 com.personalization.services.search.Entry entry = new com.personalization.services.search.Entry();
	        	        	 Entry e = (Entry)o;
	        	        	 entry.setName(e.getTerm().string());//TODO
	        	        	 entry.setCount(e.getCount());
	        	        	 entries.add(entry);
	        	         }
	        	         f.setEntries(entries);
	        	         facetsMap.put(ff.getName(), f);
	        	    }
	        	     else if (facet instanceof DateHistogramFacet) {
	        	    	 DateHistogramFacet ff = (DateHistogramFacet) facet;
	        	         com.personalization.services.search.Facet f = new com.personalization.services.search.Facet();
	        	         f.setName(ff.getName());
	        	         List entries = new ArrayList();
	        	         for(Object o : ff) {
	        	        	 com.personalization.services.search.Entry entry = new com.personalization.services.search.Entry();
	        	        	 Entry e = (Entry)o;
	        	        	 entry.setName(e.getTerm().string());//TODO
	        	        	 entry.setCount(e.getCount());
	        	        	 entries.add(entry);
	        	         }
	        	         f.setEntries(entries);
	        	         facetsMap.put(ff.getName(), f);
	        	    }
	        	     else if (facet instanceof GeoDistanceFacet) {
	        	    	 GeoDistanceFacet ff = (GeoDistanceFacet) facet;
	        	         com.personalization.services.search.Facet f = new com.personalization.services.search.Facet();
	        	         f.setName(ff.getName());
	        	         List entries = new ArrayList();
	        	         for(Object o : ff) {
	        	        	 com.personalization.services.search.Entry entry = new com.personalization.services.search.Entry();
	        	        	 Entry e = (Entry)o;
	        	        	 entry.setName(e.getTerm().string());//TODO
	        	        	 entry.setCount(e.getCount());
	        	        	 entries.add(entry);
	        	         }
	        	         f.setEntries(entries);
	        	         facetsMap.put(ff.getName(), f);
	        	    }
	        	}
	        	response.setFacets(facetsMap);
	        }
	        
	        response.setSearchHits(hits);
	        return response;
        
		} catch (Exception e) {
			throw e;
		}
		
		
	}

	public Set<String> searchAutocomplete(String collection,String json,int size) {
		
		Set<String> returnList = new HashSet();
		
		
		try {
			

	       

			//SearchResponse searchResponse = client.prepareSearch().setQuery("{ \"term\" : { \"field1\" : \"value1_1\" }}").execute().actionGet();

			
			SearchRequestBuilder builder = searchClient.getClient().prepareSearch(collection)
	        //.setQuery("{ \"term\" : { \"keyword\" : \"dynamic\" }}")
			.setQuery(json)
	        .setExplain(true);
	        
	        
	        //XContentQueryBuilder qb = QueryBuilders.queryString(json);
	        
	        //XContentQueryBuilder qb = QueryBuilders.queryString(field + ":*" + term + "*");
	        //this one is working
	        //PrefixQueryBuilder qb = new PrefixQueryBuilder(field, term.toLowerCase());
	        //builder.setQuery(qb);
			if(size>-1) {
				builder.setFrom(0).setSize(size);
			}
	        //builder.setQuery(json);
	        
	        //SearchResponse searchResponse = client.prepareSearch().setQuery("{ \"term\" : { \"field1\" : \"value1_1\" }}").execute().actionGet();

	        
	
	        org.elasticsearch.action.search.SearchResponse rsp = builder.execute().actionGet();
	        SearchHit[] docs = rsp.getHits().getHits();
	        for (SearchHit sd : docs) {
	          //to get explanation you'll need to enable this when querying:
	          //System.out.println(sd.getExplanation().toString());
	
	          // if we use in mapping: "_source" : {"enabled" : false}
	          // we need to include all necessary fields in query and then to use doc.getFields()
	          // instead of doc.getSource()
	        	//System.out.println(sd.getIndex());
	        	Map source = sd.getSource();
	        	//System.out.println(sd.getType());
	        	//System.out.println(sd.getExplanation().toString());
	        	//System.out.println(sd.fields().toString());
	        	//System.out.println(sd.getMatchedFilters().length);
	        	//SearchHitField f = sd.field("keyword");
	        	String f = (String)source.get("keyword");
	            //System.out.println(sd.sourceAsString());
	            //System.out.println(sd.getScore());
	        	
	        	//returnList.add(sd.sourceAsString());
	        	returnList.add(f.toLowerCase());
	        }
        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return returnList;
		
	}
	
/*	private void search() {
		
		System.out.println("Started");
		
		//Node node = nodeBuilder().node(); 
		//Client client = node.client();
		
		Client client = new TransportClient() 
        .addTransportAddress(new InetSocketTransportAddress("localhost", 9300)); 

		
		
		 ListenableActionFuture laf = client.prepareGet("twitter", "tweet", "1") .execute();
		
		 GetResponse response = (GetResponse) laf.actionGet();

		
		if(response!=null) {
			System.out.println(response.toString());
		}
		
		
		client.close();
		//node.close();
		
		System.out.println("Finished");
		
		
		try {
			
		System.out.println("Before");
		
		*//**
		 * Connecting to the server. In the configuration files i have set
		 * a cluster with the name of seniorcarlos
		 *//*
        Settings s = ImmutableSettings.settingsBuilder().put("cluster.name", "seniorcarlos").build();
        Client client = new TransportClient(s).
        addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
        
        
        
        1) Seems to have created an index template called an indice [SCHEMA MAPPING]
  		String fileAsString = "{" 
        	+"\"tweet\" : {" 
        	+"    \"properties\" : {" 
        	+"         \"longval\" : { \"type\" : \"long\", \"null_value\" : -1}" 
        	+"}}}";  

        
        client.admin().indices().  
        create(new CreateIndexRequest("tweetindex").mapping("tweet", fileAsString)).
        actionGet(); 
        
        *//**
         * 2) Create an index
         *//*


        //client.admin().indices().create(new CreateIndexRequest(indexName)).actionGet();

        //Index a new document
        											//index   //doc type //id
        IndexResponse response = client.prepareIndex("twitter", "tweet", "1") 
        .setSource(jsonBuilder() 
                    .startObject() 
                        .field("user", "kimchy") 
                        .field("postDate", new Date()) 
                        .field("message", "trying out Elastic Search") 
                    .endObject() 
                  ) 
        .execute() 
        .actionGet();
        
        *//**3)**//*

 		String file = "{\"user\": \"csamson\", \"post_date\": \"2009-11-15T13:12:00\", \"message\": \"Complicated this thing\" }";

        IndexResponse r = client.prepareIndex("twitter", "tweet", "2") 
        .setSource(file
                  ) 
        .execute() 
        .actionGet();
        
        
        *//** 4) DOES NOT WORK **//*
        *//**
        GetResponse response = client.prepareGet("twitter", "tweet", "1") 
        .execute() 
        .actionGet();
        **//*
        
        *//** 5) Search**//*
        
        QueryBuilder qb2 = boolQuery() 
        .must(termQuery("content", "test1")) 
        .must(termQuery("content", "test4")) 
        .mustNot(termQuery("content", "test2")) 
        .should(termQuery("content", "test3")); 

        QueryBuilder qb3 = filteredQuery( 
        		termQuery("name.first", "shay"),  
        		rangeFilter("age") 
        		.from(23) 
        		.to(54) 
        		.includeLower(true) 
        		.includeUpper(false) 
        );
        
        SearchRequestBuilder builder = client.prepareSearch("twitter");
        
        //********************************
        //XContentQueryBuilder qb = QueryBuilders.queryString(queryString);
        
          //.defaultOperator(Operator.AND).
          // field("tweetText").field("userName", 0).
          // allowLeadingWildcard(false).useDisMax(true);
        
        
        //builder.addSort("createdAt", SortOrder.DESC);
        //builder.setFrom(page * hitsPerPage).setSize(hitsPerPage);
        //builder.setQuery(qb);
        //********************************
        
        builder.setQuery(termQuery("message", "Search"));

        org.elasticsearch.action.search.SearchResponse rsp = builder.execute().actionGet();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
          //to get explanation you'll need to enable this when querying:
          //System.out.println(sd.getExplanation().toString());

          // if we use in mapping: "_source" : {"enabled" : false}
          // we need to include all necessary fields in query and then to use doc.getFields()
          // instead of doc.getSource()
        	System.out.println(sd.sourceAsString());
          //MyTweet tw = readDoc(sd.getSource(), sd.getId());
          //tweets.add(tw);
        }

        
        
        System.out.println("Finish");
        
        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}*/
	
/*	public static void main(String[] args) {
		
		SearchServiceImpl ss = new SearchServiceImpl();
		//ss.search();
		//ss.searchTerm("twitter", "user", "kimchy");
		
		SearchRequest req = new SearchRequest();
		req.setCollection("books");//need to specify json
		//req.setSearchString("Tom");
		
		try {
			
			ss.search(req);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//ss.searchTerm("books", "author", "Tom");
		//ss.searchTerm("keywords", "keywords", "book");
		//ss.searchInlineTerm("keywords", "value", "Bo",3);
		
		//ss.index("{\"book\": {\"id\":\"123\",\"author\":\"Tom Jones\",\"description\":\"Musical biography of Tom Jones\"}}", "books", "book", "111");
		
		
	}*/

}
