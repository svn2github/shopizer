package com.salesmanager.web.admin.controller.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.PermissionService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.utils.LabelUtils;

@Controller
public class PermissionController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PermissionController.class);

	@Autowired
	protected PermissionService permissionService;

	@Autowired
	protected GroupService groupService;

	@Autowired
	CountryService countryService;

	@Autowired
	LabelUtils messages;

	@RequestMapping(value = "/admin/permissions/editPermission.html", method = RequestMethod.GET)
	public String displayPermissionEdit(
			@RequestParam("id") Integer permissionId, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return displayPermission(permissionId, model, request, response);

	}

	private String displayPermission(Integer permissionId, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// display menu
		setMenu(model, request);

		Permission permission = new Permission();

		if (permissionId != null && permissionId != 0) {// edit mode

			// get from DB
			permission = permissionService.getById(permissionId);

			if (permission == null) {
				return "admin-user-permissions";
			}
 
		}

		model.addAttribute("permission", permission);
		// model.addAttribute("categories", categories);

		return "admin-user-permission";
	}

	@RequestMapping(value = "/admin/permission/save.html", method = RequestMethod.POST)
	public String savePermission(
			@Valid @ModelAttribute("permission") Permission permission,
			BindingResult result, Model model, HttpServletRequest request)
			throws Exception {


		// display menu
		setMenu(model, request);


		if (permission.getId() != null && permission.getId() > 0) { // edit
																	// entry

			// get from DB
			Permission currentPermission = permissionService.getById(permission
					.getId());

			if (currentPermission == null) {
				return "admin-user-permissions";
			}

		}

		if (result.hasErrors()) {
			return "admin-user-permission";
		}

		permissionService.saveOrUpdate(permission);

		// get parent categories
		List<Permission> permissions = permissionService.list();
		model.addAttribute("permissions", permissions);

		model.addAttribute("success", "success");
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
				permissions = permissionService.listPermission();
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

	@RequestMapping(value = "/admin/permissions/remove.html", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String deletePermission(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("permissionId");

		// MerchantStore store =
		// (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();

		try {

			int id = Integer.parseInt(sid);

			Permission permission = permissionService.getById(id);

			if (permission == null) {

				resp.setStatusMessage(messages.getMessage(
						"message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);

			} else {
				permissionService.deletePermission(permission);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

			}

		} catch (Exception e) {
			LOGGER.error("Error while deleting permission", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();

		return returnString;
	}

	@RequestMapping(value = "/admin/permissions/removePermission.html", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String removePermission(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("permissionId");
		String gid = request.getParameter("groupId");
	
		AjaxResponse resp = new AjaxResponse();

		try {

			int id = Integer.parseInt(sid);
			int groupId=Integer.parseInt(gid);

			Permission permission = permissionService.getById(id);
			Group group=groupService.getById(groupId);

			if (permission == null) {

				resp.setStatusMessage(messages.getMessage(
						"message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);

			} else {
				permissionService.removePermission(permission,group);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

			}

		} catch (Exception e) {
			LOGGER.error("Error while deleting permission", e);
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
