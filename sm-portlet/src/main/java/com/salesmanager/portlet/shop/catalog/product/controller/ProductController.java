package com.salesmanager.portlet.shop.catalog.product.controller;

import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.salesmanager.portlet.common.controller.SalesManagerController;

@Controller(value="productController")
@RequestMapping(value = "VIEW")
public class ProductController extends SalesManagerController {
	
	@RenderMapping
	public String displayProduct(RenderResponse response) {
		return "product/product";
	}

}
