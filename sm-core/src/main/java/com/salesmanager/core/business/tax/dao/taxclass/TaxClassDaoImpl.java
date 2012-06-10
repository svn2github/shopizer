package com.salesmanager.core.business.tax.dao.taxclass;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.tax.model.taxclass.TaxClass;

@Repository("taxClassDao")
public class TaxClassDaoImpl extends SalesManagerEntityDaoImpl<Long, TaxClass> implements TaxClassDao{
	
	public TaxClassDaoImpl() {
		super();
	}
}