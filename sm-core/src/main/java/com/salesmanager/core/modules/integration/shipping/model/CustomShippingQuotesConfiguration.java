package com.salesmanager.core.modules.integration.shipping.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.salesmanager.core.business.system.model.CustomIntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;

public class CustomShippingQuotesConfiguration extends IntegrationConfiguration implements CustomIntegrationConfiguration, Serializable {
	
	/**
	 * 
	 */
	private String moduleCode;
	
	private List<CustomShippingQuotesRegion> regions = new ArrayList<CustomShippingQuotesRegion>();
	
	
	private static final long serialVersionUID = 1L;

	
	@SuppressWarnings("unchecked")
	public String toJSONString() {
		JSONObject data = new JSONObject();
		

		data.put("moduleCode", this.getModuleCode());
		StringBuilder regionsList = new StringBuilder();
		int countRegion = 0;
		regionsList.append("[");
		for(CustomShippingQuotesRegion region : regions) {
			regionsList.append(region.toJSONString());
			countRegion ++;
			if(countRegion<regions.size()) {
				regionsList.append(",");
			}
		}

		return data.toJSONString();

	}

	@Override
	public String getModuleCode() {
		return moduleCode;
	}

	@Override
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
		
	}

	public void setRegions(List<CustomShippingQuotesRegion> regions) {
		this.regions = regions;
	}

	public List<CustomShippingQuotesRegion> getRegions() {
		return regions;
	}

}
