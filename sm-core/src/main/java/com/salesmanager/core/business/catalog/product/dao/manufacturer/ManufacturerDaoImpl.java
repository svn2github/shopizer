package com.salesmanager.core.business.catalog.product.dao.manufacturer;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("manufacturerDao")
public class ManufacturerDaoImpl extends SalesManagerEntityDaoImpl<Long, Manufacturer>
		implements ManufacturerDao {



}
