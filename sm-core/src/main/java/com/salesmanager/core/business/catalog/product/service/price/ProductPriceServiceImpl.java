package com.salesmanager.core.business.catalog.product.service.price;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.price.ProductPriceDao;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("productPrice")
public class ProductPriceServiceImpl extends SalesManagerEntityServiceImpl<Long, ProductPrice> 
	implements ProductPriceService {

	@Autowired
	public ProductPriceServiceImpl(ProductPriceDao productPriceDao) {
		super(productPriceDao);
	}

	@Override
	public void addDescription(ProductPrice price,
			ProductPriceDescription description) throws ServiceException {
		price.getDescriptions().add(description);
		//description.setPrice(price);
		update(price);
	}
	
	
	@Override
	public void saveOrUpdate(ProductPrice price) throws ServiceException {
		
		if(price.getId()!=null && price.getId()>0) {
			this.update(price);
		} else {
			
			Set<ProductPriceDescription> descriptions = price.getDescriptions();
			price.setDescriptions(null);
			this.create(price);
			for(ProductPriceDescription description : descriptions) {
				description.setProductPrice(price);
				this.addDescription(price, description);
			}
			
		}
		
		
		
	}
	


}
