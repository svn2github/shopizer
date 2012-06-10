package com.salesmanager.core.business.reference.zone.dao;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.reference.zone.model.Zone;

@Repository("zoneDao")
public class ZoneDaoImpl extends SalesManagerEntityDaoImpl<Long, Zone> implements ZoneDao {

}
