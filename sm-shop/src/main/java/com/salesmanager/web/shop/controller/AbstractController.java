/**
 *
 */
package com.salesmanager.web.shop.controller;

import javax.servlet.http.HttpServletRequest;

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
    public <T> T getSessionAttribute(final String key, HttpServletRequest req) {
	          return (T) req.getSession().getAttribute( key );
	  }
    
    public void setSessionAttribute(final String key, final Object value, HttpServletRequest req) {
	          req.getSession().setAttribute( key, value );
	  }


}
