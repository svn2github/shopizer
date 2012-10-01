package com.salesmanager.core.business.reference.zone.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.model.CountryDescription;
import com.salesmanager.core.business.reference.country.service.CountryServiceImpl;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.dao.ZoneDao;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.model.ZoneDescription;
import com.salesmanager.core.business.reference.zone.model.Zone_;
import com.salesmanager.core.utils.CacheUtils;

@Service("zoneService")
public class ZoneServiceImpl extends SalesManagerEntityServiceImpl<Long, Zone> implements
		ZoneService {
	

	private ZoneDao zoneDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZoneServiceImpl.class);

	@Autowired
	public ZoneServiceImpl(ZoneDao zoneDao) {
		super(zoneDao);
		this.zoneDao = zoneDao;
	}

	@Override
	public Zone getByCode(String code) {
		return getByField(Zone_.code, code);
	}

	@Override
	public void addDescription(Zone zone, ZoneDescription description) throws ServiceException {
		if (zone.getDescriptions()!=null) {
				if(!zone.getDescriptions().contains(description)) {
					zone.getDescriptions().add(description);
					update(zone);
				}
		} else {
			List<ZoneDescription> descriptions = new ArrayList<ZoneDescription>();
			descriptions.add(description);
			zone.setDescriptons(descriptions);
			update(zone);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Zone> getZones(Country country, Language language) throws ServiceException {
		
		List<Zone> zones = null;
		try {
			
			CacheUtils cacheUtils = CacheUtils.getInstance();
			
			zones = (List<Zone>) cacheUtils.getFromCache("ZONES_" + country.getIsoCode() + "_" + language.getCode());

		
		
			if(zones==null) {
			
				zones = zoneDao.listByLanguageAndCountry(country, language);
			
				//set names
				for(Zone zone : zones) {
					ZoneDescription description = zone.getDescriptions().get(0);
					zone.setName(description.getName());
					
				}
				cacheUtils.putInCache(zones, "ZONES_" + country.getIsoCode() + "_" + language.getCode());
			}

		} catch (Exception e) {
			LOGGER.error("getZones()", e);
		}
		return zones;
		
		
	}

}
