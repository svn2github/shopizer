package com.salesmanager.core.business.catalog.product.service.relationship;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.relationship.ProductRelationshipDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationshipType;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

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
	
	public void delete(ProductRelationship relationship) throws ServiceException {
		
		//throws detached exception so need to query first
		relationship = this.getById(relationship.getId());
		super.delete(relationship);
		
		
	}
	
	@Override
	public List<ProductRelationship> getByType(MerchantStore store, Product product, ProductRelationshipType type, Language language) throws ServiceException {
		
		if(product==null) {
			return productRelationshipDao.getByType(store, type.name(), language);
		} else {
			return null;
		}
		
	}
	
	@Override
	public ProductRelationship getByType(MerchantStore store, Product product, ProductRelationshipType type) throws ServiceException {
		

		return productRelationshipDao.getByType(store, type.name(), product.getId().longValue());
				
		
	}



}
