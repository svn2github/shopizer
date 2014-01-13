/**
 *
 */
package com.salesmanager.web.shop.controller.customer.facade;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.entity.customer.CustomerEntity;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;

/**
 * <p>Customer facade working as a bridge between {@link CustomerService} and Controller
 * It will take care about interacting with Service API and doing any pre and post processing
 * </p>
 *
 * @author Umesh Awasthi
 * @version 1/2
 *
 *
 */
public interface CustomerFacade
{

    /**
     * Method used to fetch customer based on the username and storecode.
     * Customer username is unique to each store.
     *
     * @param userName
     * @param storeCode
     * @param store
     * @param language
     * @throws Exception
     *
     */
    public CustomerEntity getCustomerDataByUserName(final String userName,final MerchantStore store, final Language language) throws Exception;


    /**
     * <p>Method responsible for performing customer auto-login, Method will perform following operations
     * <li> Fetch customer based on userName and Store.</li>
     * <li> Merge Customer Shopping Cart with Session Cart if any.</li>
     * <li> Convert Customer to {@link CustomerEntity} </li>
     * </p>
     *
     * @param userName username of Customer
     * @param storeCode storeCode to which user is associated/
     * @param sessionShoppingCartId session shopping cart, if user already have few items in Cart.
     * @throws Exception
     */
    public ShoppingCartData customerAutoLogin(final String userName,final String sessionShoppingCartId,final MerchantStore store,final Language language) throws Exception;
    
    public Customer getCustomerByUserName(final String userName, final MerchantStore store) throws Exception;
}
