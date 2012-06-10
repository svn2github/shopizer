package com.salesmanager.core.business.catalog.product.service.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.price.ProductPriceDao;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.common.model.Description;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.reference.language.model.Language;

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
	public Description getDescription(
			ProductPrice price, Language language) {
		//for (Description description : price.getDescriptions()) {
			//if (description.getLanguage().equals(language)) {
			//	return description;
			//}
		//}
		return null;
	}

}
