package com.salesmanager.core.business.catalog.product.service.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.review.ProductReviewDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("productReviewService")
public class ProductReviewServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductReview> implements
		ProductReviewService {

	
	@SuppressWarnings("unused")
	private ProductReviewDao productReviewDao;
	
	@Autowired
	public ProductReviewServiceImpl(
			ProductReviewDao productReviewDao) {
			super(productReviewDao);
			this.productReviewDao = productReviewDao;
	}

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
