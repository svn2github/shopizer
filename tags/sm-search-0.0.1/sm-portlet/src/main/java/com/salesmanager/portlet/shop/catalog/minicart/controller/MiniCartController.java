package com.salesmanager.portlet.shop.catalog.minicart.controller;

import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.salesmanager.portlet.common.controller.SalesManagerController;

@Controller(value="miniCartController")
@RequestMapping(value = "VIEW")
public class MiniCartController extends SalesManagerController {
	
	@RenderMapping
	public String displayMiniCart(RenderResponse response) {
		return "minicart/minicart";
	}

}
