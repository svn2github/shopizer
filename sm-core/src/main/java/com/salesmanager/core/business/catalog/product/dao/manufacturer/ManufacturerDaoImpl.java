package com.salesmanager.core.business.catalog.product.dao.manufacturer;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.QManufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.QManufacturerDescription;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("manufacturerDao")
public class ManufacturerDaoImpl extends SalesManagerEntityDaoImpl<Long, Manufacturer>
		implements ManufacturerDao {
	
	@Override
	public List<Manufacturer> listByStore(MerchantStore store, Language language) {
		QManufacturer qManufacturer = QManufacturer.manufacturer;
		QManufacturerDescription qManufacturerDescription = QManufacturerDescription.manufacturerDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qManufacturer)
			.leftJoin(qManufacturer.descriptions, qManufacturerDescription).fetch()
			.leftJoin(qManufacturer.merchantStore).fetch()
			.where(qManufacturerDescription.language.id.eq(language.getId())
			.and(qManufacturer.merchantStore.id.eq(store.getId())));
		

		
		List<Manufacturer> manufacturers = query.list(qManufacturer);
		return manufacturers;
	}



}
