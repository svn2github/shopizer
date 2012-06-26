package com.salesmanager.core.business.reference.zone.service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.model.ZoneDescription;

public interface ZoneService extends SalesManagerEntityService<Long, Zone> {
	
	Zone getByCode(String code);

	void addDescription(Zone zone, ZoneDescription description) throws ServiceException;
}
