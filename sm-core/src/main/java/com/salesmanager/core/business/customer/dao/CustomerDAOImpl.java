package com.salesmanager.core.business.customer.dao;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerCriteria;
import com.salesmanager.core.business.customer.model.CustomerList;
import com.salesmanager.core.business.customer.model.QCustomer;
import com.salesmanager.core.business.customer.model.attribute.QCustomerAttribute;
import com.salesmanager.core.business.customer.model.attribute.QCustomerOption;
import com.salesmanager.core.business.customer.model.attribute.QCustomerOptionValue;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.QMerchantStore;
import com.salesmanager.core.business.reference.country.model.QCountry;
import com.salesmanager.core.business.reference.zone.model.QZone;

@Repository("customerDao")
public class CustomerDAOImpl extends SalesManagerEntityDaoImpl<Long, Customer> implements CustomerDAO {

	public CustomerDAOImpl() {
		super();
	}
	
	
	@Override
	public Customer getById(Long id){
		QCustomer qCustomer = QCustomer.customer;
		QCountry qCountry = QCountry.country;
		QZone qZone = QZone.zone;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.country,qCountry).fetch()
			.leftJoin(qCustomer.zone,qZone).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch()
			.where(qCustomer.id.eq(id));
		
		return query.uniqueResult(qCustomer);
	}
	

	
	@Override
	public List<Customer> getByName(String name){
		QCustomer qCustomer = QCustomer.customer;
		QCountry qCountry = QCountry.country;
		QZone qZone = QZone.zone;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.country,qCountry).fetch()
			.leftJoin(qCustomer.zone,qZone).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch()
			.where(
					qCustomer.firstname.eq(name).or(qCustomer.lastname.eq(name))
					
			);
		
		return query.list(qCustomer);
	}
	
	@Override
	public CustomerList listByStore(MerchantStore store, CustomerCriteria criteria) {
		
		
		
		CustomerList customerList = new CustomerList();
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(c) from Customer as c");
		
		StringBuilder countBuilderWhere = new StringBuilder();
		countBuilderWhere.append(" where c.merchantStore.id=:mId");

		if(!StringUtils.isBlank(criteria.getName())) {
			countBuilderWhere.append(" and c.firstname like:nm");
			countBuilderWhere.append(" or c.lastname like:nm");
		}
		
		if(!StringUtils.isBlank(criteria.getFirstName())) {
			countBuilderWhere.append(" and c.firstname like:fn");
		}
		
		if(!StringUtils.isBlank(criteria.getLastName())) {
			countBuilderWhere.append(" and c.lastname like:ln");
		}
		
		if(!StringUtils.isBlank(criteria.getEmail())) {
			countBuilderWhere.append(" and c.emailAddress like:email");
		}
		
		if(!StringUtils.isBlank(criteria.getCountry())) {
			countBuilderWhere.append(" and c.country.isoCode like:country");
		}

		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

		countQ.setParameter("mId", store.getId());
		

		if(!StringUtils.isBlank(criteria.getName())) {
			countQ.setParameter("nm", criteria.getName());
		}
		
		if(!StringUtils.isBlank(criteria.getFirstName())) {
			countQ.setParameter("fn", criteria.getFirstName());
		}
		
		if(!StringUtils.isBlank(criteria.getLastName())) {
			countQ.setParameter("ln", criteria.getLastName());
		}
		
		if(!StringUtils.isBlank(criteria.getEmail())) {
			countQ.setParameter("email", criteria.getEmail());
		}
		
		if(!StringUtils.isBlank(criteria.getCountry())) {
			countQ.setParameter("country", criteria.getCountry());
		}
		


		Number count = (Number) countQ.getSingleResult ();

		customerList.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return customerList;
		
		
		
		QCustomer qCustomer = QCustomer.customer;
		QCountry qCountry = QCountry.country;
		QZone qZone = QZone.zone;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.country,qCountry).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.zone,qZone).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch();


			
			query.where(qCustomer.merchantStore.id.eq(store.getId()));
			BooleanBuilder pBuilder = null;

		if(!StringUtils.isBlank(criteria.getName())) {
			if(pBuilder==null) {
				pBuilder = new BooleanBuilder();
			}
			pBuilder.and(qCustomer.firstname.like(criteria.getName())
					.or(qCustomer.lastname.like(criteria.getName())));

		}
		
		
		if(!StringUtils.isBlank(criteria.getFirstName())) {
			if(pBuilder==null) {
				pBuilder = new BooleanBuilder();
			}
			pBuilder.and(qCustomer.firstname.like(criteria.getFirstName()));
		}
		
		if(!StringUtils.isBlank(criteria.getLastName())) {
			if(pBuilder==null) {
				pBuilder = new BooleanBuilder();
			}
			pBuilder.and(qCustomer.lastname.like(criteria.getLastName()));
		}
		
		if(!StringUtils.isBlank(criteria.getEmail())) {
			if(pBuilder==null) {
				pBuilder = new BooleanBuilder();
			}
			pBuilder.and(qCustomer.emailAddress.like(criteria.getEmail()));
		}
		
		if(!StringUtils.isBlank(criteria.getCountry())) {
			if(pBuilder==null) {
				pBuilder = new BooleanBuilder();
			}
			pBuilder.and(qCustomer.country.isoCode.like(criteria.getCountry()));
		}
		

		
		if(criteria.getMaxCount()>0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}
		
		
		customerList.setCustomers(query.list(qCustomer));

		return customerList;
		
	}
	

	@Override
	public Customer getByNick(String nick){
		QCustomer qCustomer = QCustomer.customer;
		QCountry qCountry = QCountry.country;
		QZone qZone = QZone.zone;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		

		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.country,qCountry).fetch()
			.leftJoin(qCustomer.zone,qZone).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.groups).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch()
			.where(qCustomer.nick.eq(nick));
		
		return query.uniqueResult(qCustomer);
	}
	
	@Override
	public Customer getByNick(String nick, int storeId){
		QCustomer qCustomer = QCustomer.customer;
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
		QCountry qCountry = QCountry.country;
		QZone qZone = QZone.zone;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		

		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.join(qCustomer.merchantStore, qMerchantStore).fetch()
			.leftJoin(qCustomer.country,qCountry).fetch()
			.leftJoin(qCustomer.zone,qZone).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.groups).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch()
			.where(qCustomer.nick.eq(nick).and(qMerchantStore.id.eq(storeId)));
		
		return query.uniqueResult(qCustomer);
	}
	
	@Override
	public List<Customer> listByStore(MerchantStore store){
		QCustomer qCustomer = QCustomer.customer;
		QCountry qCountry = QCountry.country;
		QZone qZone = QZone.zone;
		QCustomerAttribute qCustomerAttribute = QCustomerAttribute.customerAttribute;
		QCustomerOption qCustomerOption = QCustomerOption.customerOption;
		QCustomerOptionValue qCustomerOptionValue = QCustomerOptionValue.customerOptionValue;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCustomer)
			.join(qCustomer.merchantStore).fetch()
			.leftJoin(qCustomer.country,qCountry).fetch()
			.leftJoin(qCustomer.zone,qZone).fetch()
			.leftJoin(qCustomer.defaultLanguage).fetch()
			.leftJoin(qCustomer.attributes,qCustomerAttribute).fetch()
			.leftJoin(qCustomerAttribute.customerOption, qCustomerOption).fetch()
			.leftJoin(qCustomerAttribute.customerOptionValue, qCustomerOptionValue).fetch()
			.leftJoin(qCustomerOption.descriptions).fetch()
			.leftJoin(qCustomerOptionValue.descriptions).fetch()
			.where(qCustomer.merchantStore.id.eq(store.getId()));
		
		return query.list(qCustomer);
	}

}
