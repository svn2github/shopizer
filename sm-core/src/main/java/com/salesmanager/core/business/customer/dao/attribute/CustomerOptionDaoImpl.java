package com.salesmanager.core.business.customer.dao.attribute;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.customer.model.attribute.CustomerOption;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("customerOptionDao")
public class CustomerOptionDaoImpl extends SalesManagerEntityDaoImpl<Long, CustomerOption>
		implements CustomerOptionDao {
	
	@Override
	public List<CustomerOption> listByStore(MerchantStore store, Language language) {
		
/*		QProductOption qProductOption = QProductOption.productOption;
		QProductOptionDescription qDescription = QProductOptionDescription.productOptionDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qProductOption.merchantStore.id.eq(store.getId())
			.and(qDescription.language.id.eq(language.getId())))
			.orderBy(qProductOption.id.asc());
		
		return query.listDistinct(qProductOption);
		*/
		
		return null;
	}
	
	@Override
	public CustomerOption getById(Long id) {
/*		QProductOption qProductOption = QProductOption.productOption;
		QProductOptionDescription qDescription = QProductOptionDescription.productOptionDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qProductOption.id.eq(id));
		
		return query.uniqueResult(qProductOption);*/
		
		return null;
	}
	

	
	@Override
	public void saveOrUpdate(CustomerOption entity) throws ServiceException {

		if(entity.getId()!=null && entity.getId()>0) {

			super.update(entity);
			
		} else {
			
			super.save(entity);
			
		}
		
	}



}
