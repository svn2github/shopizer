package com.salesmanager.core.constants;

import java.util.List;
import java.util.Map;

import com.salesmanager.core.business.reference.zone.loader.ZoneConfigurationLoader;
import com.salesmanager.core.business.reference.zone.model.ZonePojo;

public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

			Map<String,List<ZonePojo>> result = new ZoneConfigurationLoader().loadZoneConfigurations();
			
			System.out.println( "result  - " + result);
			 
		
			List<ZonePojo> list  = result.get("en");
			
			for (ZonePojo zonePojo : list) {
				System.out.println("Country - "  + zonePojo.getCountryCode());
				System.out.println("ZoneCode - "  + zonePojo.getZoneCode());
				System.out.println("ZoneName - "  + zonePojo.getZoneName());
			}

	}

}
