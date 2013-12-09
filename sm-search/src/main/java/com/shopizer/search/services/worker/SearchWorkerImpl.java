package com.shopizer.search.services.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacet;
import org.elasticsearch.search.facet.geodistance.GeoDistanceFacet;
import org.elasticsearch.search.facet.histogram.HistogramFacet;
import org.elasticsearch.search.facet.range.RangeFacet;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.terms.TermsFacet.Entry;

import com.shopizer.search.services.SearchRequest;
import com.shopizer.search.services.SearchResponse;
import com.shopizer.search.services.impl.SearchDelegate;


public class SearchWorkerImpl implements SearchWorker {
	
	@Inject
	private SearchDelegate searchDelegate;
	
	private final static String ORIGINAL_RESPONSE = "ORIGINAL_RESPONSE"; 
	
	private static Logger log = Logger.getLogger(SearchWorkerImpl.class);

	public SearchResponse execute(SearchRequest request, ExecutionContext context) throws Exception{

		SearchResponse response = new SearchResponse();
		org.elasticsearch.action.search.SearchResponse resp = searchDelegate.search(request);
		
		
        SearchHit[] docs = resp.getHits().getHits();
		List<com.shopizer.search.services.SearchHit> hits = new ArrayList<com.shopizer.search.services.SearchHit>();
		List<String> ids = new ArrayList<String>();

        response.setCount(docs.length);
        context.setObject(ORIGINAL_RESPONSE, resp);
        for (SearchHit sd : docs) {
          //to get explanation you'll need to enable this when querying:
          //System.out.println(sd.getExplanation().toString());

          // if we use in mapping: "_source" : {"enabled" : false}
          // we need to include all necessary fields in query and then to use doc.getFields()
          // instead of doc.getSource()


        	//log.debug("Found entry " + sd.sourceAsString());
            //System.out.println(sd.getScore());
        	com.shopizer.search.services.SearchHit hit = new com.shopizer.search.services.SearchHit(sd);
        	hits.add(hit);
        	ids.add(sd.getId());
        	//Map source = sd.getSource();
        	//Map highlights = sd.getHighlightFields();
        	//hits.add(sd);
          
          
        }
        
        response.setIds(ids);
        
        Facets facets = resp.getFacets();
        if(facets!=null) {
        	Map<String,com.shopizer.search.services.Facet> facetsMap = new HashMap<String,com.shopizer.search.services.Facet>();
        	for (Facet facet : facets.facets()) {
        		 
        	     if (facet instanceof TermsFacet) {
        	         TermsFacet ff = (TermsFacet) facet;
        	         com.shopizer.search.services.Facet f = new com.shopizer.search.services.Facet();
        	         f.setName(ff.getName());
        	         List<com.shopizer.search.services.Entry> entries = new ArrayList<com.shopizer.search.services.Entry>();
        	         for(Object o : ff) {
        	        	 com.shopizer.search.services.Entry entry = new com.shopizer.search.services.Entry();
        	        	 Entry e = (Entry)o;
        	        	 entry.setName(e.getTerm().string());
        	        	 entry.setCount(e.getCount());
        	        	 entries.add(entry);
        	         }
        	         f.setEntries(entries);
        	         facetsMap.put(ff.getName(), f);
        	    }
        	     else if (facet instanceof RangeFacet) {
        	    	 RangeFacet ff = (RangeFacet) facet;
        	         com.shopizer.search.services.Facet f = new com.shopizer.search.services.Facet();
        	         f.setName(ff.getName());
        	         List<com.shopizer.search.services.Entry> entries = new ArrayList<com.shopizer.search.services.Entry>();
        	         for(Object o : ff) {
        	        	 com.shopizer.search.services.Entry entry = new com.shopizer.search.services.Entry();
        	        	 Entry e = (Entry)o;
        	        	 entry.setName(e.getTerm().string());
        	        	 entry.setCount(e.getCount());
        	        	 entries.add(entry);
        	         }
        	         f.setEntries(entries);
        	         facetsMap.put(ff.getName(), f);
        	    }
        	     else if (facet instanceof HistogramFacet) {
        	    	 HistogramFacet ff = (HistogramFacet) facet;
        	         com.shopizer.search.services.Facet f = new com.shopizer.search.services.Facet();
        	         f.setName(ff.getName());
        	         List<com.shopizer.search.services.Entry> entries = new ArrayList<com.shopizer.search.services.Entry>();
        	         for(Object o : ff) {
        	        	 com.shopizer.search.services.Entry entry = new com.shopizer.search.services.Entry();
        	        	 Entry e = (Entry)o;
        	        	 entry.setName(e.getTerm().string());
        	        	 entry.setCount(e.getCount());
        	        	 entries.add(entry);
        	         }
        	         f.setEntries(entries);
        	         facetsMap.put(ff.getName(), f);
        	    }
        	     else if (facet instanceof DateHistogramFacet) {
        	    	 DateHistogramFacet ff = (DateHistogramFacet) facet;
        	         com.shopizer.search.services.Facet f = new com.shopizer.search.services.Facet();
        	         f.setName(ff.getName());
        	         List<com.shopizer.search.services.Entry> entries = new ArrayList<com.shopizer.search.services.Entry>();
        	         for(Object o : ff) {
        	        	 com.shopizer.search.services.Entry entry = new com.shopizer.search.services.Entry();
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
        	         com.shopizer.search.services.Facet f = new com.shopizer.search.services.Facet();
        	         f.setName(ff.getName());
        	         List<com.shopizer.search.services.Entry> entries = new ArrayList<com.shopizer.search.services.Entry>();
        	         for(Object o : ff) {
        	        	 com.shopizer.search.services.Entry entry = new com.shopizer.search.services.Entry();
        	        	 Entry e = (Entry)o;
        	        	 entry.setName(e.getTerm().string());
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
		response.setInputSearchJson(request.getJson());
		if(context == null) {
			context = new ExecutionContext();
		}
		context.setObject("response", response);
        
		return response;


	}

}
