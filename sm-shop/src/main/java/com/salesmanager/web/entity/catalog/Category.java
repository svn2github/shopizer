package com.salesmanager.web.entity.catalog;

import java.io.Serializable;


/**
 * Used when invoking /service type urls
 * Contains simplified and minimal structure
 * @author casams1
 *
 */
public class Category implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String code;
	private int order;
	private int depth;
	
	private String name;
	private String description;
	private String friendlyUrl;
	private String keyWords;
	private String highlights;
	private String metaDescription;

	private String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFriendlyUrl() {
		return friendlyUrl;
	}
	public void setFriendlyUrl(String friendlyUrl) {
		this.friendlyUrl = friendlyUrl;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public void setHighlights(String highlights) {
		this.highlights = highlights;
	}
	public String getHighlights() {
		return highlights;
	}
	
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	

}
