package com.salesmanager.core.business.catalog.product.service.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.relationship.ProductRelationshipDao;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("productRelationshipService")
public class ProductRelationshipServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductRelationship> implements
		ProductRelationshipService {

	
	private ProductRelationshipDao productRelationshipDao;
	
	@Autowired
	public ProductRelationshipServiceImpl(
			ProductRelationshipDao productRelationshipDao) {
			super(productRelationshipDao);
			this.productRelationshipDao = productRelationshipDao;
	}
	
	@Override
	public void saveOrUpdate(ProductRelationship relationship) throws ServiceException {
		
		if(relationship.getId()!=null && relationship.getId()>0) {
			
			this.update(relationship);
			
		} else {
			this.create(relationship);
		}
		
	}



}
