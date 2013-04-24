package com.salesmanager.core.business.tax.dao.taxrate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.tax.model.taxclass.TaxClass;
import com.salesmanager.core.business.tax.model.taxrate.QTaxRate;
import com.salesmanager.core.business.tax.model.taxrate.QTaxRateDescription;
import com.salesmanager.core.business.tax.model.taxrate.TaxRate;

@Repository("taxRateDao")
public class TaxRateDaoImpl extends SalesManagerEntityDaoImpl<Long, TaxRate> implements TaxRateDao{
	
	public TaxRateDaoImpl() {
		super();
	}
	
	@Override
	public List<TaxRate> listByStore(MerchantStore store) {
		return null;
	}
	
	@Override
	public List<TaxRate> listByCountryZoneAndTaxClass(Country country, Zone zone, TaxClass taxClass, MerchantStore store, Language language) {
		
		
		QTaxRate qTax = QTaxRate.taxRate1;
		QTaxRateDescription qTaxDescription = QTaxRateDescription.taxRateDescription;

		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qTax)
			.leftJoin(qTax.merchantStore).fetch()
			.leftJoin(qTax.descriptions,qTaxDescription).fetch()
			.join(qTax.country).fetch()
			.leftJoin(qTax.zone).fetch()
			.leftJoin(qTax.parent).fetch()
			.where(qTax.merchantStore.id.eq(store.getId())
			.and(qTax.zone.id.eq(zone.getId()))
			.and(qTax.country.id.eq(country.getId())
			.and(qTaxDescription.language.id.eq(language.getId())))
			);
		
		List<TaxRate> taxes = query.list(qTax);
		return taxes;

	}
}