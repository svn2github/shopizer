package com.salesmanager.core.business.catalog.product.service.review;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface ProductReviewService extends
		SalesManagerEntityService<Long, ProductReview> {
	
	
	List<ProductReview> getByCustomer(Customer customer);
	List<ProductReview> getByProduct(Product product);



}
