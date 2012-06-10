package com.salesmanager.core.business.reference.zone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.reference.zone.dao.ZoneDao;
import com.salesmanager.core.business.reference.zone.model.Zone;

@Service("zoneService")
public class ZoneServiceImpl extends SalesManagerEntityServiceImpl<Long, Zone> implements
		ZoneService {

	@Autowired
	public ZoneServiceImpl(ZoneDao zoneDao) {
		super(zoneDao);
	}

}
