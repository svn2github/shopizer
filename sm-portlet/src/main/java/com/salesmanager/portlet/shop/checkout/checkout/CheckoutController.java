package com.salesmanager.portlet.shop.checkout.checkout;

import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.salesmanager.portlet.common.controller.SalesManagerController;

@Controller(value="checkoutController")
@RequestMapping(value = "VIEW")
public class CheckoutController extends SalesManagerController {
	
	@RenderMapping
	public String displayCheckout(RenderResponse response) {
		return "checkout/checkout";
	}

}
