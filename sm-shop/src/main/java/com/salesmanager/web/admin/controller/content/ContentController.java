package com.salesmanager.web.admin.controller.content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.content.model.content.Content;
import com.salesmanager.core.business.content.model.content.ContentDescription;
import com.salesmanager.core.business.content.model.content.ContentType;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;

@Controller
public class ContentController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StaticContentController.class);
	
	@Autowired
	private ContentService contentService;
	
	
	@Secured("CONTENT")
	@RequestMapping(value="/admin/content/pages/listContent.html", method=RequestMethod.GET)
	public String listContentPages(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);

		return ControllerConstants.Tiles.Content.contentPages;
		
		
	}
	
	@SuppressWarnings({ "unchecked"})
	@Secured("CONTENT")
	@RequestMapping(value="/admin/content/page.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageStaticContent(@ModelAttribute String contentType, HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();

		try {
			

			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

			Language language = (Language)request.getAttribute("LANGUAGE");

			List<Content> contentList = contentService.listByType(ContentType.PAGE, store);
			
			if(contentList!=null) {

				for(Content content : contentList) {
					
					List<ContentDescription> descriptions = content.getDescriptions();
					ContentDescription description = descriptions.get(0);
					for(ContentDescription desc : descriptions) {
						if(desc.getLanguage().getCode().equals(language.getCode())) {
							description = desc;
							break;
						}
					}
					

					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					entry.put("id", content.getId());
					entry.put("code", content.getCode());
					entry.put("name", description.getName());
					resp.addDataEntry(entry);

				}
			
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("Error while paging content", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	
	@Secured("CONTENT")
	@RequestMapping(value="/admin/content/pages/addContent.html", method=RequestMethod.POST)
	public String addContent(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		
		//get list of content for contenttype page
		
		//put in model
		
		return ControllerConstants.Tiles.Content.contentPages;
		
		
	}
	
	
	
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
