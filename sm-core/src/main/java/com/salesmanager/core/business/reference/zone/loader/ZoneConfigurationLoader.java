package com.salesmanager.core.business.reference.zone.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.salesmanager.core.business.reference.zone.model.ZonePojo;

public class ZoneConfigurationLoader {
	
	public Map<String, List<ZonePojo>> loadZoneConfigurations(){
		
		ObjectMapper mapper = new ObjectMapper();
		InputStream in = this.getClass().getResourceAsStream("/zoneconfig.json");
		Map<String,List<ZonePojo>> zoneConfigurations = null ;
		try {
			zoneConfigurations = mapper.readValue(in, new TypeReference<Map<String,List<ZonePojo>>>() { });
						
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return zoneConfigurations;

	}

}
