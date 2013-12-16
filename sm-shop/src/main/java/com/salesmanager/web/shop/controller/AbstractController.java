/**
 *
 */
package com.salesmanager.web.shop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;

/**
 * @author Umesh A
 *
 */
public abstract class AbstractController {


    /**
     * Method which will help to retrieving values from Session
     * based on the key being passed to the method.
     * @param key
     * @return value stored in session corresponding to the key
     */
    @SuppressWarnings( "unchecked" )
    public <T> T getSessionValue(final String key) {
	      ServletRequestAttributes reqAttr = (ServletRequestAttributes) RequestContextHolder
	                          .currentRequestAttributes();
	          return (T) reqAttr.getRequest().getAttribute(key);
	  }



	  public ShoppingCartData getShoppingCartFromSession( final HttpServletRequest request){
	    	return (ShoppingCartData)request.getSession().getAttribute(Constants.SHOPPING_CART);
	  }

	  public void setCartDataToSession(final HttpServletRequest request,final ShoppingCartData shoppingCartData){
	     HttpSession session=request.getSession();
	    		session.setAttribute(Constants.SHOPPING_CART, shoppingCartData);
	    }
	  
	  public void removeCartDataFromSession(final HttpServletRequest request){
		     HttpSession session=request.getSession();
		    		session.removeAttribute(Constants.SHOPPING_CART);
		    }
}
