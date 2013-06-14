package com.salesmanager.web.shop.controller.orders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ShoppingOrderDownloadController {
	
	//secure this resource for a known customer downloading the order
	@RequestMapping("/shop/orders/download.html")
	public String displayOrderDownload(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//generate a download token by download item
		//put the download item in the http session in a map
		
		return "";
	}
	
	@RequestMapping("/shop/orders/{storeCode}/{orderUrl}/download.html")
	public String displayPublicOrderDownload(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		
		return "";
	}

}
