package com.salesmanager.portlet.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.portlet.ModelAndView;

public abstract class SalesManagerController {
	
	protected static final String ERROR_PAGE_PATH = "common/error/error";
	protected static final String BIND_ERROR_MESSAGE = "exception";
	
	/**
	 * Method responsible to dispatch the user for the error page if an exception is raised.
	 * 
	 * @param exception - Exception or any subclass of it.
	 * @return mav - returns the ModelAndView object that will redirect the user to the error page.
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView handleException(Exception exception) {
		ModelAndView mav = new ModelAndView(ERROR_PAGE_PATH);
		mav.addObject(BIND_ERROR_MESSAGE, exception);
		
		return mav;
	}
}
