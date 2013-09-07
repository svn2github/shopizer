package com.shopizer.search.services;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.search.highlight.HighlightField;

public class SearchHit {
	
	private org.elasticsearch.search.SearchHit searchit;
	
	private Map metaEntries = new HashMap();
	
	public Map getMetaEntries() {
		return metaEntries;
	}

	public SearchHit(org.elasticsearch.search.SearchHit searchit) {
		
		this.searchit = searchit;
		metaEntries.put("source", searchit.getSource());
		if(searchit.getHighlightFields()!=null && searchit.getHighlightFields().size()>0) {
			metaEntries.put("highlightFields", searchit.getHighlightFields());
		}
		
	}
	
	public Map<String, Object> getSource() {
		return searchit.getSource();
	}
	
	public String getId() {
		return searchit.getId();
	}
	
	public String getIndex() {
		return searchit.getIndex();
	}
	
	public float getScore() {
		return searchit.getScore();
	}
	
	public Map<String, HighlightField> getHighlightFields() {
		return searchit.getHighlightFields();
	}
	
	

}
