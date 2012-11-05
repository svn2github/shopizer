package com.salesmanager.core.business.catalog.product.dao.relationship;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.QProduct;
import com.salesmanager.core.business.catalog.product.model.description.QProductDescription;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.relationship.QProductRelationship;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("productRelationshipDao")
public class ProductRelationshipDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductRelationship>
		implements ProductRelationshipDao {
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, String type, Language language) {
		QProductRelationship qEntity = QProductRelationship.productRelationship;
		QProduct qProduct = QProduct.product;
		QProduct qRelatedProduct = QProduct.product;
		//QProductDescription qProductDescription = QProductDescription.productDescription;
		QProductDescription qRelatedProductDescription = QProductDescription.productDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
			.join(qEntity.store).fetch()
			.leftJoin(qEntity.product,qProduct).fetch()
			.join(qEntity.relatedProduct,qRelatedProduct).fetch()
			//.leftJoin(qProduct.descriptions).fetch()
			.leftJoin(qRelatedProduct.descriptions,qRelatedProductDescription).fetch()
			.where(qEntity.store.id.eq(store.getId())
					.and(qRelatedProductDescription.language.id.eq(language.getId()))
					.and(qEntity.code.eq(type)));

		
		return query.list(qEntity);
	}



}
