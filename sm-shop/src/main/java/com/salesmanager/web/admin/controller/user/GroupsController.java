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
import com.salesmanager.core.business.user.service.GroupService;
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
		// (MerchantStore)request.getAttribute("MERCHANT_STORE");

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
	public String displayPermissions(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setMenu(model, request);

		// does nothing, ajax subsequent request

		return "admin-user-groups";
	}

	// @SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/admin/groups/paging.html", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String pagePermissions(HttpServletRequest request,
			HttpServletResponse response) {
		String groupName = request.getParameter("name");

		AjaxResponse resp = new AjaxResponse();

		try {

			List<Group> groups = null;

			if (!StringUtils.isBlank(groupName)) {

				groups = groupService.getByName();

			} else {
				groups = groupService.list();
			}

			for (Group group : groups) {

				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("groupId", group.getId());
				entry.put("name", group.getGroupName());
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

	@RequestMapping(value = "/admin/groups/remove.html", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String deleteGroup(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("groupId");

		// MerchantStore store =
		// (MerchantStore)request.getAttribute("MERCHANT_STORE");

		AjaxResponse resp = new AjaxResponse();

		try {

			int id = Integer.parseInt(sid);

			Group group = groupService.getById(id);

			if (group == null) {

				resp.setStatusMessage(messages.getMessage(
						"message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);

			} else {

				groupService.deleteGroup(group);
//				groupService.removeGroup(group);
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
