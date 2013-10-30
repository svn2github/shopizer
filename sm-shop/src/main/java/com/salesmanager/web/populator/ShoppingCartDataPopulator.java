/**
 * 
 */
package com.salesmanager.web.populator;

import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.populator.AbstractDataPopulator;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;


/**
 * @author Umesh A
 *
 */

@Service(value="shoppingCartDataPopulator")
public class ShoppingCartDataPopulator extends AbstractDataPopulator<ShoppingCart,ShoppingCartData>
{

    @Autowired
    private Provider<ShoppingCartData> targetBean;
    
    @Override
    public ShoppingCartData populate(final ShoppingCart shoppingCart,final ShoppingCartData shoppingCartData){
       // logic to populate shopping cart from model 
        return null;
    }

    @Override
    protected ShoppingCartData createTarget()
    {
      return this.targetBean.get();  
    }
}
