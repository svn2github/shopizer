package com.salesmanager.web.shop.model.search;

import org.json.simple.JSONObject;

public class AutoCompleteRequest {
	
	//private String collectionName;
	//private String query;
	//private String filter;
	private String merchantCode;
	private String languageCode;
	
	
	private final static String WILDCARD_QUERY = "wildcard";
	private final static String KEYWORD = "keyword";
	private final static String UNDERSCORE = "_";
	private final static String ALL = "*";
	
	public AutoCompleteRequest(String merchantCode, String languageCode) {
		this.merchantCode = merchantCode;
		this.languageCode = languageCode;
	}

	@SuppressWarnings("unchecked")
	public String toJSONString(String query) {

		JSONObject wildcard = new JSONObject();
		JSONObject q = new JSONObject();
		

		
		StringBuilder qValueBuilder = new StringBuilder();
		qValueBuilder.append(query).append(ALL);
		
		q.put(KEYWORD, qValueBuilder.toString());
		wildcard.put(WILDCARD_QUERY, q);
		
		
		return wildcard.toJSONString();
	}
	
	/** keyword_en_default **/
	public String getCollectionName() {
		StringBuilder qBuilder = new StringBuilder();
		qBuilder.append(KEYWORD).append(UNDERSCORE).append(getLanguageCode()).append(UNDERSCORE)
		.append(getMerchantCode());
		
		return qBuilder.toString().toLowerCase();
	}
	
	

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

}
