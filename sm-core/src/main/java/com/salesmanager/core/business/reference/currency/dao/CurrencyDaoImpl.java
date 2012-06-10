package com.salesmanager.core.business.reference.currency.dao;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.reference.currency.model.Currency;

@Repository("currencyDao")
public class CurrencyDaoImpl extends SalesManagerEntityDaoImpl<Long, Currency> 
	implements CurrencyDao {

}
