package com.salesmanager.core.business.customer.dao.attribute;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.customer.model.attribute.CustomerOptionValue;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("customerOptionValueDao")
public class CustomerOptionValueDaoImpl extends SalesManagerEntityDaoImpl<Long, CustomerOptionValue>
		implements CustomerOptionValueDao {
	
	
	@Override
	public List<CustomerOptionValue> listByStore(MerchantStore store, Language language) {
		
/*		QProductOptionValue qProductOption = QProductOptionValue.productOptionValue;
		QProductOptionValueDescription qDescription = QProductOptionValueDescription.productOptionValueDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qProductOption.merchantStore.id.eq(store.getId())
			.and(qDescription.language.id.eq(language.getId())));
			query.orderBy(qProductOption.id.asc());
			
			
		
		return query.listDistinct(qProductOption);*/
		
		return null;
		
	}
	

	

	
	@Override
	public CustomerOptionValue getById(MerchantStore store, Long id) {
/*		QProductOptionValue qProductOption = QProductOptionValue.productOptionValue;
		QProductOptionValueDescription qDescription = QProductOptionValueDescription.productOptionValueDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qProductOption.id.eq(id)
			.and(qProductOption.merchantStore.id.eq(store.getId())));
		
		return query.uniqueResult(qProductOption);*/
		
		return null;
	}
	


}
