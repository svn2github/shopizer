package com.salesmanager.web.admin.controller.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.service.PermissionService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.utils.LabelUtils;

@Controller
public class PermissionController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PermissionController.class);

	@Autowired
	LanguageService languageService;

	@Autowired
	protected PermissionService permissionService;

	@Autowired
	CountryService countryService;

	@Autowired
	LabelUtils messages;

	
	@RequestMapping(value="/admin/permissions/editPermission.html", method=RequestMethod.GET)
	public String displayPermissionEdit(@RequestParam("id") Integer permissionId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayPermission(permissionId,model,request,response);

	}
	private String displayPermission(Integer permissionId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//display menu
		setMenu(model,request);
		
		Permission permission = new Permission();
	
		if(permissionId!=null && permissionId!=0) {//edit mode
			
			//get from DB
			permission = permissionService.getById(permissionId);
			
			if(permission==null ) {
				return "admin-user-permissions";
			}

			
//			categories.remove(category); //remove current category from categories
		
			
		} else {
		
			//create a category
			
			List<Language> languages = languageService.list();//TODO get supported languages from MerchantStore
			
			//List<Language> languages = store.getLanguages();
			
			List<CategoryDescription> descriptions = new ArrayList<CategoryDescription>();

			for(Language l : languages) {
				
				CategoryDescription desc = new CategoryDescription();
				desc.setLanguage(l);
				descriptions.add(desc);
				
			}
			
			
			
//			category.setVisible(true);
//			category.setDescriptions(descriptions);
		
		}
		


		
		model.addAttribute("permission", permission);
//		model.addAttribute("categories", categories);
		

		
		return "admin-user-permission";
	}
	
	@RequestMapping(value="/admin/permission/save.html", method=RequestMethod.POST)
	public String savePermission(@Valid @ModelAttribute("permission") Permission permission, BindingResult result, Model model, HttpServletRequest request) throws Exception {
		
		//TODO Null Pointer exception
		
//		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//display menu
		setMenu(model,request);
		
//		MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
				

		if(permission.getId() != null && permission.getId() >0) { //edit entry
			
			//get from DB
			Permission currentPermission = permissionService.getById(permission.getId());

			if(currentPermission==null) {
				return "admin-user-permissions";
			}
			

			
		}

			

		
		if (result.hasErrors()) {
			return "admin-user-permission";
		}
		
		
		permissionService.saveOrUpdate(permission);

			
		//get parent categories
		List<Permission> permissions = permissionService.list();
		model.addAttribute("permissions", permissions);
		

		model.addAttribute("success","success");
		return "admin-user-permissions";
	}

	// category list
	@RequestMapping(value = "/admin/permissions/permissions.html", method = RequestMethod.GET)
	public String displayPermissions(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setMenu(model, request);

		// does nothing, ajax subsequent request

		return "admin-user-permissions";
	}

	// @SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/admin/permissions/paging.html", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String pagePermissions(HttpServletRequest request,
			HttpServletResponse response) {
		String permissionName = request.getParameter("name");

		AjaxResponse resp = new AjaxResponse();

		try {

			List<Permission> permissions = null;

			if (!StringUtils.isBlank(permissionName)) {

				permissions = permissionService.getByName();

			} else {
				permissions = permissionService.list();
			}

			for (Permission permission : permissions) {

				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("permissionId", permission.getId());
				entry.put("name", permission.getPermissionName());
				resp.addDataEntry(entry);

			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("Error while paging categories", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();

		return returnString;
	}

	private void setMenu(Model model, HttpServletRequest request)
			throws Exception {

		// display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("profile", "profile");
		activeMenus.put("security", "security");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request
				.getAttribute("MENUMAP");

		Menu currentMenu = (Menu) menus.get("profile");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
