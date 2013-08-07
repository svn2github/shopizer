package com.salesmanager.core.business.catalog.product.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.utils.ProductPriceUtils;

/**
 * Contains all the logic required to calculate product price
 * @author casams1
 *
 */
@Service("pricingService")
public class PricingServiceImpl implements PricingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PricingServiceImpl.class);
	

	@Autowired
	private ProductPriceUtils priceUtil;
	


	
}
