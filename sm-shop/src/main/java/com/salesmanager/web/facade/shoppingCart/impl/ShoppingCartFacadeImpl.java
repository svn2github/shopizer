/**
 * 
 */
package com.salesmanager.web.facade.shoppingCart.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.facade.shoppingCart.ShoppingCartFacade;
import com.salesmanager.web.populator.ShoppingCartDataPopulator;


/**
 * @author Umesh Awasthi
 * @version 1.2
 * @since 1.2
 * 
 */
@Service(value="shoppingCartFacade")
public class ShoppingCartFacadeImpl implements ShoppingCartFacade {

	protected final Logger LOG = Logger.getLogger(getClass());

	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private ShoppingCartDataPopulator shoppingCartDataPopulator;

	@Override
	public ShoppingCartData getShoppingCartData(final Customer customer, final MerchantStore store,
			final Language language, final String shoppingCartId) throws Exception {

		ShoppingCart cart = null;
		try {
			if (customer != null) {
				LOG.info("Reteriving customer shopping cart...");

				cart = shoppingCartService.getShoppingCart(customer);

			}

			else {
				if (StringUtils.isNotBlank(shoppingCartId) && cart == null) {
					cart = shoppingCartService.getByCode(shoppingCartId, store);
				}

			}
		}
		catch (ServiceException ex) {
			LOG.error( "Error while retriving cart from customer", ex );
		}
		
		if(cart !=null){
		    LOG.info( "Cart model found." );
		   ShoppingCartData shoppingCart=this.shoppingCartDataPopulator.populate( cart);
		}

		return null;
	}

}
