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
    public <T> T getSessionAttribute(final String key, HttpServletRequest request) {
	          return (T) com.salesmanager.web.utils.SessionUtil.getSessionAttribute(key, request);

	  }
    
    public void setSessionAttribute(final String key, final Object value, HttpServletRequest request) {
    	com.salesmanager.web.utils.SessionUtil.setSessionAttribute(key, value, request);
	}


}
