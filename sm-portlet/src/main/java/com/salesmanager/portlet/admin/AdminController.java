package com.salesmanager.portlet.admin;

import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.salesmanager.portlet.common.controller.SalesManagerController;

@Controller(value="adminController")
@RequestMapping(value = "VIEW")
public class AdminController extends SalesManagerController {
	
	@RenderMapping
	public String displayHome(RenderResponse response) {
		return "admin/home";
	}

}
