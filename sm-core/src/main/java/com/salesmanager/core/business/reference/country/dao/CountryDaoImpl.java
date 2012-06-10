package com.salesmanager.core.business.reference.country.dao;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.reference.country.model.Country;

@Repository("countryDao")
public class CountryDaoImpl extends SalesManagerEntityDaoImpl<Integer, Country> implements CountryDao {

}
