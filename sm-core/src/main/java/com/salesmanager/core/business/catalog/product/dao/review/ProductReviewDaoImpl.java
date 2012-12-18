package com.salesmanager.core.business.catalog.product.dao.review;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("productReviewDao")
public class ProductReviewDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductReview>
		implements ProductReviewDao {



	@Override
	public List<ProductReview> getByCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductReview> getByProduct(Product product) {
		// TODO Auto-generated method stub
		return null;
	}



}
