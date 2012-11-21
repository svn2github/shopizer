package com.salesmanager.web.admin.controller.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.model.PermissionCriteria;
import com.salesmanager.core.business.user.model.PermissionList;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.PermissionService;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.utils.LabelUtils;

@Controller
public class GroupsController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GroupsController.class);

	@Autowired
	LanguageService languageService;

	@Autowired
	protected GroupService groupService;
	
	@Autowired
	PermissionService permissionService;

	@Autowired
	CountryService countryService;

	@Autowired
	LabelUtils messages;

	@RequestMapping(value = "/admin/groups/editGroup.html", method = RequestMethod.GET)
	public String displayGroupEdit(
			@RequestParam("id") Integer groupId, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return displayGroup(groupId, model, request, response);

	}

	private String displayGroup(Integer groupId, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// display menu
		setMenu(model, request);

		Group group = new Group();

		if (groupId != null && groupId != 0) {// edit mode

			// get from DB
			group = groupService.getById(groupId);

			if (group == null) {
				return "admin-user-groups";
			}

			// categories.remove(category); //remove current category from
			// categories

		} else {

			// create a category

			List<Language> languages = languageService.list();// TODO get
																// supported
																// languages
																// from
																// MerchantStore

			// List<Language> languages = store.getLanguages();

			List<CategoryDescription> descriptions = new ArrayList<CategoryDescription>();

			for (Language l : languages) {

				CategoryDescription desc = new CategoryDescription();
				desc.setLanguage(l);
				descriptions.add(desc);

			}

			// category.setVisible(true);
			// category.setDescriptions(descriptions);

		}

		model.addAttribute("group", group);
		// model.addAttribute("categories", categories);

		return "admin-user-group";
	}

	@RequestMapping(value = "/admin/group/save.html", method = RequestMethod.POST)
	public String saveGroup(
			@Valid @ModelAttribute("group") Group group,
			BindingResult result, Model model, HttpServletRequest request)
			throws Exception {

		// TODO Null Pointer exception

		// Language language = (Language)request.getAttribute("LANGUAGE");

		// display menu
		setMenu(model, request);

		// MerchantStore store =
		// (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		if (group.getId() != null && group.getId() > 0) { // edit
																	// entry

			// get from DB
			Group currentGroup = groupService.getById(group
					.getId());

			if (currentGroup == null) {
				return "admin-user-groups";
			}

		}

		if (result.hasErrors()) {
			return "admin-user-group";
		}

		groupService.saveOrUpdate(group);

		// get parent categories
		List<Group> groups = groupService.list();
		model.addAttribute("groups", groups);

		model.addAttribute("success", "success");
		return "admin-user-groups";
	}

	// category list
	@RequestMapping(value = "/admin/groups/groups.html", method = RequestMethod.GET)
	public String displayGroups(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setMenu(model, request);

		List<Group> groups = groupService.listGroup();
		
		model.addAttribute("groups", groups);
		// does nothing, ajax subsequent request

		return "admin-user-groups";
	}

	// @SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/admin/groups/paging.html", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String pagePermissions(HttpServletRequest request,
			HttpServletResponse response) {
		String groupId = request.getParameter("groupId");

		AjaxPageableResponse resp = new AjaxPageableResponse();

		try {
			int startRow = Integer.parseInt(request.getParameter("_startRow"));
			int endRow = Integer.parseInt(request.getParameter("_endRow"));
			
			
			PermissionCriteria criteria = new PermissionCriteria();
			
			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);
			
			
			if(!StringUtils.isBlank(groupId) && !groupId.equals("-1")) {
				
				//get other filters
				Integer IgroupId = 0;
				try {
					IgroupId = Integer.parseInt(groupId);
				} catch (Exception e) {
					LOGGER.error("Permission page cannot parse groupId " + groupId );
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					String returnString = resp.toJSONString();
					return returnString;
				} 
				
				

				if(IgroupId>0) {
				
					Group group = groupService.getById(IgroupId);
	
					if(group==null ) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
						String returnString = resp.toJSONString();
						return returnString;
					}
					
					//get all sub categories
					List<Group> groups = new ArrayList<Group>();
					groups.add(group);
					
					Set<Integer> groupIds = new HashSet<Integer>();
					
					for(Group gro : groups) {
						groupIds.add(gro.getId());
					}
					criteria.setGroupIds(groupIds);
				}
			}
			
		
			PermissionList permissionList = permissionService.listByCriteria(criteria);
			resp.setEndRow(permissionList.getTotalCount());
			resp.setStartRow(startRow);
			List<Permission> plist = permissionList.getPermissions();
			
			for(Permission permission : plist) {
				
				Map entry = new HashMap();
				entry.put("permissionId", permission.getId());
				
				entry.put("name", permission.getPermissionName());
				entry.put("groupId", groupId);
//				entry.put("available", permission.getAvailable());
				resp.addDataEntry(entry);
				
				
				
			}

			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while paging permissions", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}

	@RequestMapping(value = "/admin/groups/remove.html", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String deleteGroup(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("groupId");

		// MerchantStore store =
		// (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();

		try {

			int id = Integer.parseInt(sid);

			Group group = groupService.getById(id);

			if (group == null) {

				resp.setStatusMessage(messages.getMessage(
						"message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);

			} else {

//				groupService.deleteGroup(group);
				groupService.removeGroup(group);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

			}

		} catch (Exception e) {
			LOGGER.error("Error while deleting group", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
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
