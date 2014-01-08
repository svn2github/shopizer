package com.salesmanager.core.business.catalog.product.service.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.review.ProductReviewDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.reference.language.model.Language;

@Service("productReviewService")
public class ProductReviewServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductReview> implements
		ProductReviewService {


	private ProductReviewDao productReviewDao;
	
	@Autowired
	public ProductReviewServiceImpl(
			ProductReviewDao productReviewDao) {
			super(productReviewDao);
			this.productReviewDao = productReviewDao;
	}

	@Override
	public List<ProductReview> getByCustomer(Customer customer) {
		return productReviewDao.getByCustomer(customer);
	}

	@Override
	public List<ProductReview> getByProduct(Product product) {
		return productReviewDao.getByProduct(product);
	}
	
	@Override
	public List<ProductReview> getByProduct(Product product, Language language) {
		return productReviewDao.getByProduct(product, language);
	}


}
