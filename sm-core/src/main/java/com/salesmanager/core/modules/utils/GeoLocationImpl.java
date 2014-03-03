package com.salesmanager.core.modules.utils;

import java.io.InputStream;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.salesmanager.core.business.common.model.Address;


public class GeoLocationImpl implements GeoLocation {
	
	private static GeoLocationImpl geoLocation = null;
	private DatabaseReader reader = null;
	private static final Logger LOGGER = LoggerFactory.getLogger( GeoLocationImpl.class );
	private GeoLocationImpl() {
		try {
			InputStream in =
                    this.getClass().getClassLoader().getResourceAsStream("/reference/GeoLite2-Country.mmdb");
			reader = new DatabaseReader.Builder(in).build();
		} catch(Exception e) {
			LOGGER.error("Cannot instantiate IP database",e);
		}
		
	}

	
	public static GeoLocationImpl getInstance() {
		
		if(geoLocation==null) {
			geoLocation = new GeoLocationImpl();

		}
		
		return geoLocation;
		
		
	}

	@Override
	public Address getAddress(String ipAddress) throws Exception {
		
			Address address = new Address();

			
			CityResponse response = reader.city(InetAddress.getByName(ipAddress));

			address.setCountry(response.getCountry().getIsoCode());
			address.setPostalCode(response.getPostal().getCode());
			address.setZone(response.getMostSpecificSubdivision().getIsoCode());
			address.setCity(response.getCity().getName());
			


		
			return address;
		
		
	}


}
