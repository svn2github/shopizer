package com.salesmanager.web.admin.controller.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.user.model.User;
import com.salesmanager.web.admin.entity.web.Menu;

@Controller
public class ProfileController {
	
	
	
	@RequestMapping(value="/admin/user/user.html", method=RequestMethod.GET)
	public String userProfile(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//get current user from request.getRemoteUser()
		
		setMenu(model,request);
		
		User user = new User();
		
		
		model.addAttribute("user", user);
		
		return "admin-user-profile";
		
		
	}
	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("profile", "profile");
		activeMenus.put("user", "user");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("profile");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	
	

}
