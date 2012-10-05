package com.salesmanager.core.business.customer.dao;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.QCustomer;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.reference.country.model.QCountry;
import com.salesmanager.core.business.reference.zone.model.QZone;

@Repository("customerDao")
public class CustomerDAOImpl extends SalesManagerEntityDaoImpl<Long, Customer> implements CustomerDAO {

	public CustomerDAOImpl() {
		super();
	}
	
	public Customer getById(Long id){
		QCustomer qCustomer = QCustomer.customer;
		QCountry qCountry = QCountry.country;
		QZone qZone = QZone.zone;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.leftJoin(qCustomer.country,qCountry).fetch()
			.leftJoin(qCustomer.zone,qZone).fetch()
			.where(qCustomer.id.eq(id));
		
		return query.uniqueResult(qCustomer);
	}

}
