package com.salesmanager.web.admin.controller.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.content.model.content.Content;
import com.salesmanager.core.business.content.model.content.ContentDescription;
import com.salesmanager.core.business.content.model.content.ContentType;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;

@Controller
public class ContentBoxesController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentBoxesController.class);
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	LanguageService languageService;
	
	
	@Secured("CONTENT")
	@RequestMapping(value="/admin/content/boxes/listContent.html", method=RequestMethod.GET)
	public String listContentBoxes(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);

		return ControllerConstants.Tiles.Content.contentBoxes;
		
		
	}
	
	@Secured("CONTENT")
	@RequestMapping(value="/admin/content/boxes/createBox.html", method=RequestMethod.GET)
	public String createBox(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Content content = new Content();
		content.setMerchantStore(store);
		content.setContentType(ContentType.PAGE);
		
		
		List<Language> languages = store.getLanguages();
		
		
		for(Language l : languages) {
			
			ContentDescription description = new ContentDescription();
			description.setLanguage(l);
			content.getDescriptions().add(description);
		}
		
		
		
		model.addAttribute("content",content);
		

		return ControllerConstants.Tiles.Content.contentPagesDetails;
		
		
	}
	
	@Secured("CONTENT")
	@RequestMapping(value="/admin/content/boxes/contentDetails.html", method=RequestMethod.GET)
	public String getContentDetails(@RequestParam("id") Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Content content = contentService.getById(id);
		

		
		if(content==null) {
			LOGGER.error("Content entity null for id " + id);
			return "/admin/content/boxes/listContent.html";
		}
		
		if(content.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			LOGGER.error("Content id " + id + " does not belong to merchant " + store.getId());
			return "/admin/content/boxes/listContent.html";
		}
		
		if(!content.getContentType().name().equals(ContentType.PAGE)) {
			LOGGER.error("This controller does not handle content type " + content.getContentType().name());
			return "/admin/content/boxes/listContent.html";
		}
		
		List<Language> languages = store.getLanguages();
		
		List<ContentDescription> descriptions = new ArrayList<ContentDescription>();
		for(Language l : languages) {
			for(ContentDescription description : content.getDescriptions()) {
				if(description.getLanguage().getCode().equals(l.getCode())) {
					descriptions.add(description);
				}
			}
		}
		content.setDescriptions(descriptions);
		
		model.addAttribute("content",content);
		

		return ControllerConstants.Tiles.Content.contentPagesDetails;
		
		
	}
	

	
	@SuppressWarnings({ "unchecked"})
	@Secured("CONTENT")
	@RequestMapping(value="/admin/content/boxes/page.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageContentBoxes(@ModelAttribute String contentType, HttpServletRequest request, HttpServletResponse response) {
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
	@RequestMapping(value="/admin/content/boxes/saveContent.html", method=RequestMethod.POST)
	public String saveContent(@Valid @ModelAttribute Content content, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		if (result.hasErrors()) {
			return ControllerConstants.Tiles.Content.contentPagesDetails;
		}
		
		Map<String,Language> langs = languageService.getLanguagesMap();
		
		List<ContentDescription> descriptions = content.getDescriptions();
		for(ContentDescription description : descriptions) {
			Language l = langs.get(description.getLanguage().getCode());
			description.setLanguage(l);
			description.setContent(content);
		}
		
		
		content.setMerchantStore(store);
		contentService.saveOrUpdate(content);
		
		
		model.addAttribute("content",content);
		model.addAttribute("success","success");
		return ControllerConstants.Tiles.Content.contentPagesDetails;
		
		
	}
	
	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("content", "content");
		activeMenus.put("content-boxes", "content-boxes");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("content");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	

}
