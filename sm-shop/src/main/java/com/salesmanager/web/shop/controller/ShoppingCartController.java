package com.salesmanager.web.shop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class ShoppingCartController {
	
	
	@RequestMapping(value={"/shop/shoppingCart.html"}, method=RequestMethod.GET)
	public String displayShoppingCart(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return ControllerConstants.Tiles.ShoppingCart.shoppingCart;
		
		
	}
		

}
