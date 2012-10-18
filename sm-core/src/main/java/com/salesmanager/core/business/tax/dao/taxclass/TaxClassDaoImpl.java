package com.salesmanager.core.business.tax.dao.taxclass;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.tax.model.taxclass.QTaxClass;
import com.salesmanager.core.business.tax.model.taxclass.TaxClass;

@Repository("taxClassDao")
public class TaxClassDaoImpl extends SalesManagerEntityDaoImpl<Long, TaxClass> implements TaxClassDao{
	
	public TaxClassDaoImpl() {
		super();
	}
	
	
	@Override
	public List<TaxClass> listByStore(MerchantStore store) {
		QTaxClass qTax = QTaxClass.taxClass;

		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qTax)
			.leftJoin(qTax.merchantSore).fetch()
			.where(qTax.merchantSore.id.eq(store.getId())
			.or(qTax.merchantSore.isNull()));
		
		List<TaxClass> taxes = query.list(qTax);
		return taxes;
	}
}