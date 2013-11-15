package com.salesmanager.portlet.shop.checkout.cart;

import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.salesmanager.portlet.common.controller.SalesManagerController;

@Controller(value="shoppingCartController")
@RequestMapping(value = "VIEW")
public class ShoppingCartController extends SalesManagerController {
	
	@RenderMapping
	public String displayMiniCart(RenderResponse response) {
		return "cart/cart";
	}

}
