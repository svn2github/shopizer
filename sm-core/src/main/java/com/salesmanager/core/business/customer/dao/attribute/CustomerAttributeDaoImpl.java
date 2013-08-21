package com.salesmanager.core.business.customer.dao.attribute;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.attribute.CustomerAttribute;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("customerAttributeDao")
public class CustomerAttributeDaoImpl extends SalesManagerEntityDaoImpl<Long, CustomerAttribute> 
	implements CustomerAttributeDao {
	
	
	@Override
	public CustomerAttribute getById(Long id) {
/*		QProductAttribute qEntity = QProductAttribute.productAttribute;
		QProductOption qProductOption = QProductOption.productOption;
		QProductOptionValue qProductOptionValue = QProductOptionValue.productOptionValue;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
			.join(qEntity.product).fetch()
			.leftJoin(qEntity.productOption, qProductOption).fetch()
			.leftJoin(qEntity.productOptionValue, qProductOptionValue).fetch()
			.leftJoin(qProductOption.descriptions).fetch()
			.leftJoin(qProductOptionValue.descriptions).fetch()
			.leftJoin(qProductOption.merchantStore).fetch()
			.where(qEntity.id.eq(id));
		
		return query.uniqueResult(qEntity);*/
		
		return null;
	}


	
	@Override
	public List<CustomerAttribute> getByCustomer(MerchantStore store, Customer customer, Language language) {
/*		QProductAttribute qEntity = QProductAttribute.productAttribute;
		QProductOptionValue qProductOptionValue = QProductOptionValue.productOptionValue;
		QProductOptionValueDescription qProductOptionValueDescription = QProductOptionValueDescription.productOptionValueDescription;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
			.leftJoin(qEntity.productOptionValue, qProductOptionValue).fetch()
			.leftJoin(qProductOptionValue.merchantStore).fetch()
			.join(qEntity.product,qProduct).fetch()
			.leftJoin(qProductOptionValue.descriptions,qProductOptionValueDescription).fetch()
			.where(qProduct.id.eq(product.getId())
			.and(qProductOptionValue.merchantStore.id.eq(store.getId()))
			.and(qProductOptionValueDescription.language.id.eq(language.getId())));
		
		return query.list(qEntity);*/
		
		return null;
	}
	
}
