package com.salesmanager.core.modules.cms.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CacheAttribute implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5110875925988368254L;

	private String entityId;
	
	private Map<String,byte[]> entities = new HashMap<String,byte[]>();

	public Map<String,byte[]> getEntities() {
		return entities;
	}

	public void setEntities(Map<String,byte[]> entities) {
		this.entities = entities;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	
	
	

}
