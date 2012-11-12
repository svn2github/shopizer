package com.salesmanager.core.business.system.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Object used to contain the integration information with an external gateway
 * Uses simple JSON to encode the object in JSON by implementing JSONAware
 * and uses jackson JSON decode to parse JSON String to an Object
 * @author csamson
 *
 */
public class IntegrationConfiguration implements JSONAware {
	
	private String moduleCode;
	private boolean active;
	private Map<String,String> integrationKeys= new HashMap<String,String>();
	private Map<String,String[]> integrationOptions= new HashMap<String,String[]>();
	public String getModuleCode() {
		return moduleCode;
	}
	@JsonProperty("moduleCode")  
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	public boolean isActive() {
		return active;
	}
	@JsonProperty("active")
	public void setActive(boolean active) {
		this.active = active;
	}
	public Map<String, String> getIntegrationKeys() {
		return integrationKeys;
	}
	@JsonProperty("integrationKeys")
	public void setIntegrationKeys(Map<String, String> integrationKeys) {
		this.integrationKeys = integrationKeys;
	}
	public Map<String, String[]> getIntegrationOptions() {
		return integrationOptions;
	}
	@JsonProperty("integrationOptions")
	public void setIntegrationOptions(Map<String, String[]> integrationOptions) {
		this.integrationOptions = integrationOptions;
	}
	
	protected String getJsonInfo() {
		
		StringBuilder returnString = new StringBuilder();
		returnString.append("{");
		returnString.append("\"moduleCode\"").append(":\"").append(this.getModuleCode()).append("\"");
		returnString.append(",");
		returnString.append("\"active\"").append(":").append(this.isActive());

		return returnString.toString();
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String toJSONString() {
		
		
		StringBuilder returnString = new StringBuilder();
		returnString.append(getJsonInfo());

		if(this.getIntegrationKeys().size()>0) {

				JSONObject data = new JSONObject();
				Set<String> keys = this.getIntegrationKeys().keySet();
				for(String key : keys) {
					data.put(key, this.getIntegrationKeys().get(key));
				}
				String dataField = data.toJSONString();

				returnString.append(",").append("\"integrationKeys\"").append(":");
				returnString.append(dataField.toString());

				
		 }
		
		
		if(this.getIntegrationOptions().size()>0) {

				//JSONObject data = new JSONObject();
				StringBuilder optionDataEntries = new StringBuilder();
				Set<String> keys = this.getIntegrationOptions().keySet();
				int countOptions = 0;
				for(String key : keys) {

					String[] values = this.getIntegrationOptions().get(key);
					StringBuilder optionsEntries = new StringBuilder();
					StringBuilder dataEntries = new StringBuilder();
					
					int count = 0;
					for(int i = 0 ; i < values.length ; i ++) {
						
						dataEntries.append("\"").append(values[i]).append("\"");
						if(count<values.length-1) {
							dataEntries.append(",");
						}
						count++;
					}
					
					optionsEntries.append("[").append(dataEntries.toString()).append("]");
					
					optionDataEntries.append("\"").append(key).append("\":").append(optionsEntries.toString());

					if(countOptions<keys.size()-1) {
						optionDataEntries.append(",");
					}
					countOptions ++;
					
				}
				String dataField = optionDataEntries.toString();

				returnString.append(",").append("\"integrationOptions\"").append(":{");
				returnString.append(dataField.toString());
				returnString.append("}");
				
		 }
			

		returnString.append("}");

		
		return returnString.toString();

	}

}
