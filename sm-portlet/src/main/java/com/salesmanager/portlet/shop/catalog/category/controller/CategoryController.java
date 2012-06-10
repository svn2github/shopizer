package com.salesmanager.portlet.shop.catalog.category.controller;

import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.salesmanager.portlet.common.controller.SalesManagerController;

@Controller(value="categoryController")
@RequestMapping(value = "VIEW")
public class CategoryController extends SalesManagerController {
	
	@RenderMapping
	public String displayCategory(RenderResponse response) {
		return "category/category";
	}

}
