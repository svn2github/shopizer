package com.salesmanager.core.business.reference.zone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.reference.zone.dao.ZoneDao;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.model.ZoneDescription;
import com.salesmanager.core.business.reference.zone.model.Zone_;

@Service("zoneService")
public class ZoneServiceImpl extends SalesManagerEntityServiceImpl<Long, Zone> implements
		ZoneService {

	@Autowired
	public ZoneServiceImpl(ZoneDao zoneDao) {
		super(zoneDao);
	}

	@Override
	public Zone getByCode(String code) {
		return getByField(Zone_.code, code);
	}

	@Override
	public void addDescription(Zone zone, ZoneDescription description) throws ServiceException {
		if (!zone.getDescriptions().contains(description)) {
			zone.getDescriptions().add(description);
			update(zone);
		}
	}

}
