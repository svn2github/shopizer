package com.salesmanager.core.modules.integration.shipping.model;

import java.io.Serializable;
import java.util.List;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import com.salesmanager.core.business.system.model.IntegrationConfiguration;

public class CustomShippingQuotesConfiguration extends IntegrationConfiguration implements Serializable, JSONAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String customRegionName;//a name given by the merchant for this custom region
	private List<String> countries;//a list of country code for this region
	
	private List<CustomShippingQuoteWeightItem> quoteItems;//price max weight

	public void setQuoteItems(List<CustomShippingQuoteWeightItem> quoteItems) {
		this.quoteItems = quoteItems;
	}

	public List<CustomShippingQuoteWeightItem> getQuoteItems() {
		return quoteItems;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	public List<String> getCountries() {
		return countries;
	}

	public void setCustomRegionName(String customRegionName) {
		this.customRegionName = customRegionName;
	}

	public String getCustomRegionName() {
		return customRegionName;
	}
	
	@SuppressWarnings("unchecked")
	public String toJSONString() {
		JSONObject data = new JSONObject();
		
		data.put("active", super.isActive());
		data.put("moduleCode", super.getModuleCode());
		data.put("customRegionName", this.getCustomRegionName());
		
		StringBuilder coutriesList = new StringBuilder();
		int countCountry = 0;
		coutriesList.append("[");
		for(String country : countries) {
			coutriesList.append("\"").append(country).append("\"");
			countCountry ++;
			if(countCountry<countries.size()) {
				coutriesList.append(",");
			}
		}
		coutriesList.append("]");
		data.put("countries", coutriesList.toString());
		
		StringBuilder quotesList = new StringBuilder();
		int countQuotes = 0;
		quotesList.append("[");
		for(CustomShippingQuoteWeightItem quote : quoteItems) {
			quotesList.append(quote.toJSONString());
			countQuotes ++;
			if(countQuotes<quoteItems.size()) {
				quotesList.append(",");
			}
		}
		quotesList.append("]");
		data.put("quoteItems", quotesList.toString());
		
		
		return data.toJSONString();
		
		
	}

}
