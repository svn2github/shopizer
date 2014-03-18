/**
 *
 */
package com.salesmanager.web.shop.controller;

import javax.servlet.http.HttpServletRequest;

import org.jopendocument.util.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

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
    
    
    public void removeAttribute(final String key, HttpServletRequest request) {
    	com.salesmanager.web.utils.SessionUtil.removeSessionAttribute(key, request);
	}
    
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView handleException(Exception ex) {
		
		ModelAndView model = null;
		if(ex instanceof AccessDeniedException) {
			
			model = new ModelAndView("error/access_denied");
			
		} else {
			
			model = new ModelAndView("error/generic_error");
			model.addObject("stackError", ExceptionUtils.getStackTrace(ex));
			model.addObject("errMsg", ex.getMessage());
			
		}
		
		
 
		return model;
 
	}


}
