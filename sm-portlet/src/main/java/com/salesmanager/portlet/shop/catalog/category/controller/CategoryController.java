package com.salesmanager.portlet.shop.catalog.category.controller;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.portlet.common.controller.SalesManagerController;

@Controller(value="categoryController")
@RequestMapping(value = "VIEW")
public class CategoryController extends SalesManagerController {
	
	@Autowired
	CategoryService categoryService;
	
	@RenderMapping
	public String displayCategory(RenderRequest request, RenderResponse response) throws Exception {
		return "category/category";
	}
	


}
