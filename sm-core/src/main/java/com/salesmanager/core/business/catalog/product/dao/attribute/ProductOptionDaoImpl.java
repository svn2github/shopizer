package com.salesmanager.core.business.catalog.product.dao.attribute;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.QProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.QProductOptionDescription;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("productOptionDao")
public class ProductOptionDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductOption>
		implements ProductOptionDao {
	
	@Override
	public List<ProductOption> listByStore(MerchantStore store, Language language) {
		
		QProductOption qProductOption = QProductOption.productOption;
		QProductOptionDescription qDescription = QProductOptionDescription.productOptionDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qProductOption)
			.leftJoin(qProductOption.descriptions, qDescription).fetch()
			.leftJoin(qProductOption.merchantSore).fetch()
			.where(qProductOption.merchantSore.id.eq(store.getId())
			.and(qDescription.language.id.eq(language.getId())))
			.orderBy(qProductOption.id.asc());
		
		return query.listDistinct(qProductOption);
		
	}



}
