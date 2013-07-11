package com.personalization.services.search;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetField;

public class GetResponse {
	
	
	private org.elasticsearch.action.get.GetResponse response;
	
	public GetResponse(org.elasticsearch.action.get.GetResponse r) {
		response = r;
	}
	
	
	
	public String getResponseAsString() {
		return response.sourceAsString();
	}
	
	public Map<String,Object> getFields()
	{
		return response.getSource();
	}
	public List<Object> getField(String key) {
		GetField f = response.getFields().get(key);
		if(f!=null) {
			return f.getValues();
		}
		return null;
	}

}
