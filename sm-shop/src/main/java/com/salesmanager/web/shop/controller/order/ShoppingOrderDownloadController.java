package com.salesmanager.web.shop.controller.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/shop/orders")
public class ShoppingOrderDownloadController {
	
	/**
	 * Virtual product(s) download link
	 * @param id
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Secured("CUSTOMER_AUTH")
	@RequestMapping("/download/{id}")
	public String downloadOrder(@PathVariable Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//generate a download token by download item
		//put the download item in the http session in a map
		
		return "";
	}
	


}
