package com.salesmanager.web.admin.controller.content;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.web.admin.entity.web.Menu;

@Controller
public class ContentController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentController.class);
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	LanguageService languageService;
	


	

	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("content", "content");
		activeMenus.put("content-files", "content-files");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("content");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	

}
