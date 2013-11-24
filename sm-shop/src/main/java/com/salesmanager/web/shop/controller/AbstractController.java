/**
 * 
 */
package com.salesmanager.web.shop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;

/**
 * @author Umesh A
 *
 */
public abstract class AbstractController {

	 public ShoppingCartData getShoppingCartFromSession( final HttpServletRequest request){
	    	return (ShoppingCartData)request.getSession().getAttribute(Constants.SHOPPING_CART);  
	  }
	    
	    
	    
	  public void setCartDataToSession(final HttpServletRequest request,final ShoppingCartData shoppingCartData){
	     HttpSession session=request.getSession();
	    	synchronized (session) {
	    		session.setAttribute(Constants.SHOPPING_CART, shoppingCartData);
			}
	    }
}
