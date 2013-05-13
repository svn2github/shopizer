package com.salesmanager.web.admin.controller.content;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.web.admin.controller.ControllerConstants;

@Controller
public class ContentController {
	
	
	@Secured("CONTENT")
	@RequestMapping(value="/admin/content/pages/listContent.html", method=RequestMethod.GET)
	public String listContentPages(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//get list of content for contenttype page
		
		//put in model
		
		return ControllerConstants.Tiles.Content.contentPages;
		
		
	}
	

}
