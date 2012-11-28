package com.salesmanager.core.business.catalog.product.dao.attribute;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.QProduct;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.attribute.QProductAttribute;
import com.salesmanager.core.business.catalog.product.model.attribute.QProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.QProductOptionValue;
import com.salesmanager.core.business.catalog.product.model.attribute.QProductOptionValueDescription;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("productAttributeDao")
public class ProductAttributeDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductAttribute> 
	implements ProductAttributeDao {
	
	
	@Override
	public ProductAttribute getById(Long id) {
		QProductAttribute qEntity = QProductAttribute.productAttribute;
		QProductOption qProductOption = QProductOption.productOption;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
			.leftJoin(qEntity.productOption, qProductOption).fetch()
			.leftJoin(qProductOption.merchantSore).fetch()
			.where(qEntity.id.eq(id));
		
		return query.uniqueResult(qEntity);
	}
	
	@Override
	public List<ProductAttribute> getByOptionId(MerchantStore store, Long id) {
		QProductAttribute qEntity = QProductAttribute.productAttribute;
		QProductOption qProductOption = QProductOption.productOption;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
			.leftJoin(qEntity.productOption, qProductOption).fetch()
			.leftJoin(qProductOption.merchantSore).fetch()
			.where(qProductOption.id.eq(id)
			.and(qProductOption.merchantSore.id.eq(store.getId())));
		
		return query.list(qEntity);
	}
	
	@Override
	public List<ProductAttribute> getByOptionValueId(MerchantStore store, Long id) {
		QProductAttribute qEntity = QProductAttribute.productAttribute;
		QProductOptionValue qProductOptionValue = QProductOptionValue.productOptionValue;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
			.leftJoin(qEntity.productOptionValue, qProductOptionValue).fetch()
			.leftJoin(qProductOptionValue.merchantSore).fetch()
			.where(qProductOptionValue.id.eq(id)
			.and(qProductOptionValue.merchantSore.id.eq(store.getId())));
		
		return query.list(qEntity);
	}

	
	@Override
	public List<ProductAttribute> getByProduct(MerchantStore store, Product product, Language language) {
		QProductAttribute qEntity = QProductAttribute.productAttribute;
		QProductOptionValue qProductOptionValue = QProductOptionValue.productOptionValue;
		QProductOptionValueDescription qProductOptionValueDescription = QProductOptionValueDescription.productOptionValueDescription;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
			.leftJoin(qEntity.productOptionValue, qProductOptionValue).fetch()
			.leftJoin(qProductOptionValue.merchantSore).fetch()
			.leftJoin(qEntity.product,qProduct).fetch()
			.leftJoin(qProductOptionValue.descriptions,qProductOptionValueDescription).fetch()
			.where(qProduct.id.eq(product.getId())
			.and(qProductOptionValue.merchantSore.id.eq(store.getId()))
			.and(qProductOptionValueDescription.language.id.eq(language.getId())));
		
		return query.list(qEntity);
	}
	
}
