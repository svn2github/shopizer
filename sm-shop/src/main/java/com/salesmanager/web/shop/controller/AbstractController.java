/**
 *
 */
package com.salesmanager.web.shop.controller;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
	          return (T) reqAttr.getRequest().getSession().getAttribute( key );
	  }


}
