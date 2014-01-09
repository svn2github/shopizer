/**
 *
 */
package com.salesmanager.web.services.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.customer.CustomerEntity;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.utils.SessionUtil;

/**
 * @author Umesh Awasthi
 *
 */

public class WebshopAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{

    protected final Logger LOG = Logger.getLogger( getClass() );

    @Autowired
    private  CustomerFacade customerFacade;


    @Autowired
    private ShoppingCartService shoppingCartService;

    public WebshopAuthenticationSuccessHandler() {

    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
            final HttpServletResponse response, final Authentication authentication)
            throws IOException, ServletException {

        LOG.info( "Customer authenticated sucessfully.performing post authentication work" );
        try
        {

            final MerchantStore store=SessionUtil.<MerchantStore>getSessionValue( Constants.MERCHANT_STORE );
            final Language language =SessionUtil.<Language>getSessionValue( Constants.LANGUAGE );
            final String userName= request.getParameter( "j_username" );
            //request.getParameter( "storeCode" );
            CustomerEntity customerData= customerFacade.getCustomerDataByUserName(  userName ,store,language);
            if(customerData ==null){
                LOG.error( "Customer Object is null.aborting login process." );
                throw new ServletException("unable to perform login process.");
            }

            LOG.info( "Fetching and merging Shopping Cart data" );
            final String sessionShoppingCartCode= (String)request.getSession().getAttribute( Constants.SHOPPING_CART );
            ShoppingCartData shoppingCartData= customerFacade.customerAutoLogin( userName, sessionShoppingCartCode, store,language );
            // Should change to add customer data in place of Customer object
            request.getSession().setAttribute(Constants.CUSTOMER, customerFacade.getCustomerByUserName( userName ));

            String shoppingCartID="";
            if(shoppingCartData !=null){
                shoppingCartID=shoppingCartData.getCode();
                request.getSession().setAttribute(Constants.SHOPPING_CART, shoppingCartData.getCode());
            }

            try
            {
                response.setContentType("application/json; charset=UTF-8");
                JSONObject jsonObject=new JSONObject();
                jsonObject.put( Constants.RESPONSE_STATUS, Constants.RESPONSE_SUCCESS );
                jsonObject.put( Constants.SHOPPING_CART, shoppingCartID );
                jsonObject.writeJSONString(response.getWriter());

            }
            catch ( IOException e )
            {
                // handle exception...
            }


        }
        catch ( Exception e )
        {
            LOG.error( "Error while fetching customer with give username.." +e);
            throw new ServletException(e);
        }





    }
}
