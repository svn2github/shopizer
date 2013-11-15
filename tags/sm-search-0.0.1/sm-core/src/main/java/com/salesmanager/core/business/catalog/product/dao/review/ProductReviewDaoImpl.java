package com.salesmanager.core.business.catalog.product.dao.review;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.QProduct;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.catalog.product.model.review.QProductReview;
import com.salesmanager.core.business.catalog.product.model.review.QProductReviewDescription;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.QCustomer;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("productReviewDao")
public class ProductReviewDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductReview>
		implements ProductReviewDao {



	@Override
	public List<ProductReview> getByCustomer(Customer customer) {
		
		QProductReview qEntity = QProductReview.productReview;
		QProductReviewDescription qDescription = QProductReviewDescription.productReviewDescription;
		QCustomer qCustomer = QCustomer.customer;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
		.join(qEntity.customer,qCustomer).fetch()
		.join(qEntity.product,qProduct).fetch()
		.leftJoin(qProduct.merchantStore).fetch()
		.leftJoin(qEntity.descriptions,qDescription).fetch()
		.where(qCustomer.id.eq(customer.getId()));
		
		return query.list(qEntity);
		
		
		
	}

	@Override
	public List<ProductReview> getByProduct(Product product) {
		
		
		QProductReview qEntity = QProductReview.productReview;
		QProductReviewDescription qDescription = QProductReviewDescription.productReviewDescription;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
		.join(qEntity.customer).fetch()
		.join(qEntity.product,qProduct).fetch()
		.leftJoin(qProduct.merchantStore).fetch()
		.leftJoin(qEntity.descriptions,qDescription).fetch()
		.where(qProduct.id.eq(product.getId()));
		
		return query.list(qEntity);
		
		
	}
	
	@Override
	public ProductReview getById(Long id) {

		QProductReview qEntity = QProductReview.productReview;
		QProductReviewDescription qDescription = QProductReviewDescription.productReviewDescription;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qEntity)
			.join(qEntity.customer).fetch()
			.join(qEntity.product,qProduct).fetch()
			.leftJoin(qProduct.merchantStore).fetch()
			.leftJoin(qEntity.descriptions,qDescription).fetch()
			.where(qEntity.id.eq(id));
		
		return query.uniqueResult(qEntity);
		
		
	}



}
