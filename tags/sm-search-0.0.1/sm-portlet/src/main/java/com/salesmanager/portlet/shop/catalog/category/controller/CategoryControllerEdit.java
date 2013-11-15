package com.salesmanager.portlet.shop.catalog.category.controller;

import java.util.List;

import javax.portlet.PortletSession;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.portlet.common.controller.SalesManagerController;

@Controller(value="editCategoryController")
@RequestMapping(value = "EDIT")
public class CategoryControllerEdit extends SalesManagerController {
	
	@Autowired
	CategoryService categoryService;
	

	
	@RenderMode(name="EDIT")
	public String displayPreferences(RenderRequest request, RenderResponse response) throws Exception {
		
		//get all category codes
		//categoryService.get
		
		MerchantStore store = (MerchantStore)request.getPortletSession().getAttribute("MERCHANT_STORE",PortletSession.APPLICATION_SCOPE);
		
		List<Category> categories = categoryService.listByStore(store);
		
		request.setAttribute("CATEGORIES", categories);
		
		return "category/category-edit";
	}

}
