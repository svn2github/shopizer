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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.secutity.Password;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.admin.security.SecurityQuestion;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.LabelUtils;

@Controller
public class UserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	LanguageService languageService;
	
	@Autowired
	UserService userService;

	@Autowired
	GroupService groupService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	MerchantStoreService merchantStoreService;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Secured("STORE_ADMIN")
	@RequestMapping(value="/admin/users/list.html", method=RequestMethod.GET)
	public String displayUsers(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		//The users are retrieved from the paging method
		setMenu(model,request);
		return ControllerConstants.Tiles.User.users;
	}
	
	/**
	 * Displays a list of users that can be managed by admins
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Secured("STORE_ADMIN")
	@RequestMapping(value = "/admin/users/paging.html", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String pageUsers(HttpServletRequest request,
			HttpServletResponse response) {

		AjaxResponse resp = new AjaxResponse();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		try {

			List<User> users = userService.listUserByStore(store);

			for (User user : users) {

				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("userId", user.getId());
				entry.put("name", user.getFirstName() + " " + user.getLastName());
				entry.put("email", user.getAdminEmail());
				entry.put("active", user.isActive());
				resp.addDataEntry(entry);

			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();

		return returnString;
	}


	@RequestMapping(value="/admin/users/password.html", method=RequestMethod.GET)
	public String displayChangePassword(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		setMenu(model,request);
		String userName = request.getRemoteUser();
		User user = userService.getByUserName(userName);
		
		Password password = new Password();
		password.setUser(user);
		
		model.addAttribute("password",password);
		model.addAttribute("user",user);
		return ControllerConstants.Tiles.User.password;
	}
	
	
	
	@RequestMapping(value="/admin/users/savePassword.html", method=RequestMethod.POST)
	public String changePassword(@ModelAttribute("password") Password password, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		setMenu(model,request);
		String userName = request.getRemoteUser();
		User dbUser = userService.getByUserName(userName);
		

		if(password.getUser().getId().longValue()!= dbUser.getId().longValue()) {
				return "redirect:/admin/users/displayUser.html";
		}
		
		//validate password not empty
		if(StringUtils.isBlank(password.getPassword())) {
			ObjectError error = new ObjectError("password",new StringBuilder().append(messages.getMessage("label.generic.password", locale)).append(" ").append(messages.getMessage("message.cannot.empty", locale)).toString());
			result.addError(error);
			return ControllerConstants.Tiles.User.password;
		}
		

		String tempPass = passwordEncoder.encodePassword(password.getPassword(), null);
		
		//password match
		if(!tempPass.equals(dbUser.getAdminPassword())) {
			ObjectError error = new ObjectError("password",messages.getMessage("message.password.invalid", locale));
			result.addError(error);
			return ControllerConstants.Tiles.User.password;
		}


		
		if(StringUtils.isBlank(password.getNewPassword())) {
			ObjectError error = new ObjectError("newPassword",new StringBuilder().append(messages.getMessage("label.generic.newpassword", locale)).append(" ").append(messages.getMessage("message.cannot.empty", locale)).toString());
			result.addError(error);
		}
		
		if(StringUtils.isBlank(password.getRepeatPassword())) {
			ObjectError error = new ObjectError("newPasswordAgain",new StringBuilder().append(messages.getMessage("label.generic.newpassword.repeat", locale)).append(" ").append(messages.getMessage("message.cannot.empty", locale)).toString());
			result.addError(error);
		}
		
		if(!password.getRepeatPassword().equals(password.getNewPassword())) {
			ObjectError error = new ObjectError("newPasswordAgain",messages.getMessage("message.password.different", locale));
			result.addError(error);
		}
		
		if(password.getNewPassword().length()<6) {
			ObjectError error = new ObjectError("newPassword",messages.getMessage("message.password.length", locale));
			result.addError(error);
		}
		
		if (result.hasErrors()) {
			return ControllerConstants.Tiles.User.password;
		}
		
		
		
		String pass = passwordEncoder.encodePassword(password.getNewPassword(), null);
		dbUser.setAdminPassword(pass);
		userService.update(dbUser);
		
		model.addAttribute("success","success");
		return ControllerConstants.Tiles.User.password;
	}
	
	@RequestMapping(value="/admin/users/createUser.html", method=RequestMethod.GET)
	public String displayUserCreate(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		return displayUser(null,model,request,response,locale);
	}
	
	@RequestMapping(value="/admin/users/displayStoreUser.html", method=RequestMethod.GET)
	public String displayUserEdit(@ModelAttribute("id") Long id, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		User dbUser = userService.getById(id);
		
		if(dbUser==null) {
			LOGGER.info("User is null for id " + id);
			return "redirect://admin/users/list.html";
		}
		
		
		return displayUser(dbUser,model,request,response,locale);

	}
	
	@RequestMapping(value="/admin/users/displayUser.html", method=RequestMethod.GET)
	public String displayUserEdit(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		
		String userName = request.getRemoteUser();
		User user = userService.getByUserName(userName);
		return displayUser(user,model,request,response,locale);

	}
	
	private void populateUserObjects(User user, MerchantStore store, Model model, Locale locale) throws Exception {
		
		//get groups
		List<Group> groups = groupService.listGroup();
		
		List<MerchantStore> stores = new ArrayList<MerchantStore>();
		stores.add(store);
		
		//String remoteUser = request.getRemoteUser();
		
		if(user!=null && user.getId()!=null) {
			User logedInUser = userService.getByUserName(user.getAdminName());
			
			//check groups
			List<Group> logedInUserGroups = logedInUser.getGroups();
			for(Group group : logedInUserGroups) {
				if(group.getGroupName().equals(Constants.GROUP_SUPERADMIN)) {
					stores = merchantStoreService.list();
				}
			}
		}
		
		//questions
		List<SecurityQuestion> questions = new ArrayList<SecurityQuestion>();
		
		SecurityQuestion question = new SecurityQuestion();
		question.setId("1");
		question.setLabel(messages.getMessage("security.question.1", locale));
		questions.add(question);
		
		question = new SecurityQuestion();
		question.setId("2");
		question.setLabel(messages.getMessage("security.question.2", locale));
		questions.add(question);
		
		question = new SecurityQuestion();
		question.setId("3");
		question.setLabel(messages.getMessage("security.question.3", locale));
		questions.add(question);
		
		question = new SecurityQuestion();
		question.setId("4");
		question.setLabel(messages.getMessage("security.question.4", locale));
		questions.add(question);
		
		question = new SecurityQuestion();
		question.setId("5");
		question.setLabel(messages.getMessage("security.question.5", locale));
		questions.add(question);
		
		question = new SecurityQuestion();
		question.setId("6");
		question.setLabel(messages.getMessage("security.question.6", locale));
		questions.add(question);
		
		question = new SecurityQuestion();
		question.setId("7");
		question.setLabel(messages.getMessage("security.question.7", locale));
		questions.add(question);
		
		question = new SecurityQuestion();
		question.setId("8");
		question.setLabel(messages.getMessage("security.question.8", locale));
		questions.add(question);
		
		question = new SecurityQuestion();
		question.setId("9");
		question.setLabel(messages.getMessage("security.question.9", locale));
		questions.add(question);
		
		model.addAttribute("questions", questions);
		model.addAttribute("stores", stores);
		model.addAttribute("languages", store.getLanguages());
		model.addAttribute("groups", groups);
		
		
	}
	
	
	
	private String displayUser(User user, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		

		//display menu
		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);



		
		if(user==null) {
			user = new User();
		} else {
			user.setAdminPassword("TRANSIENT");
		}
		
		this.populateUserObjects(user, store, model, locale);
		

		model.addAttribute("user", user);
		
		

		return ControllerConstants.Tiles.User.profile;
	}
	
	@RequestMapping(value="/admin/users/checkUserCode.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String checkUserCode(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String code = request.getParameter("code");
		String id = request.getParameter("id");

		AjaxResponse resp = new AjaxResponse();
		
		try {
			
		User user = userService.getByUserName(code);
		
		
		if(!StringUtils.isBlank(id)) {
			try {
				Long lid = Long.parseLong(id);
				
				if(user.getAdminName().equals(code) && user.getId()==lid) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
					return resp.toJSONString();
				}
			} catch (Exception e) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				return resp.toJSONString();
			}

		}

			
			if(StringUtils.isBlank(code)) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				return resp.toJSONString();
			}

			if(user!=null) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				return resp.toJSONString();
			}

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while getting user", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	@RequestMapping(value="/admin/users/save.html", method=RequestMethod.POST)
	public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {


		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		
		this.populateUserObjects(user, store, model, locale);
		
		Language language = user.getDefaultLanguage();
		
		Language l = languageService.getById(language.getId());
		
		user.setDefaultLanguage(l);
		
		
		
		User dbUser = null;
		
		//edit mode, need to get original user important information
		if(user.getId()!=null) {
			dbUser = userService.getByUserName(user.getAdminName());
			if(dbUser==null) {
				return "redirect://admin/users/displayUser.html";
			}
		}
		
		List<Group> submitedGroups = user.getGroups();
		Set<Integer> ids = new HashSet<Integer>();
		for(Group group : submitedGroups) {
			ids.add(Integer.parseInt(group.getGroupName()));
		}
		

		
		//validate security questions not empty
		if(StringUtils.isBlank(user.getAnswer1())) {
			ObjectError error = new ObjectError("answer1",messages.getMessage("security.answer.question1.message", locale));
			result.addError(error);
		}
		
		if(StringUtils.isBlank(user.getAnswer2())) {
			ObjectError error = new ObjectError("answer2",messages.getMessage("security.answer.question2.message", locale));
			result.addError(error);
		}
		
		if(StringUtils.isBlank(user.getAnswer3())) {
			ObjectError error = new ObjectError("answer3",messages.getMessage("security.answer.question3.message", locale));
			result.addError(error);
		}
		
		if(user.getQuestion1().equals(user.getQuestion2()) || user.getQuestion1().equals(user.getQuestion3())
				|| user.getQuestion2().equals(user.getQuestion1()) || user.getQuestion1().equals(user.getQuestion3())
				|| user.getQuestion3().equals(user.getQuestion1()) || user.getQuestion1().equals(user.getQuestion2()))
		
		
		{
			ObjectError error = new ObjectError("question1",messages.getMessage("security.questions.differentmessages", locale));
			result.addError(error);
		}
		
		

		
		if(user.getId()!=null && user.getId()>0) {
			if(user.getId().longValue()!=dbUser.getId().longValue()) {
				return "redirect://admin/users/displayUser.html";
			}
			
			List<Group> groups = dbUser.getGroups();
			boolean removeSuperAdmin = true;
			for(Group group : groups) {
				//can't revoke super admin
				if(group.getGroupName().equals("SUPERADMIN")) {
					Integer groupId = group.getId();
					List<Group> userGroups = user.getGroups();
					for(Group g : userGroups) {
						int submitedGroup = Integer.parseInt(g.getGroupName());
						if(submitedGroup==groupId) {
							removeSuperAdmin = false;
							break;
						}
					}
				}
			}
			
			if(removeSuperAdmin) {
				ObjectError error = new ObjectError("groups",messages.getMessage("message.security.cannotrevoke.superadmin", locale));
				result.addError(error);
			}
		}
		

		

		
		List<Group> newGroups = groupService.listGroupByIds(ids);

		//set actual user groups
		user.setGroups(newGroups);
		
		if (result.hasErrors()) {
			return ControllerConstants.Tiles.User.profile;
		}
		
		if(user.getId()!=null && user.getId()>0) {
			user.setAdminPassword(dbUser.getAdminPassword());
		}
		//save or update user
		userService.saveOrUpdate(user);
		//model.addAttribute("user", user);

		model.addAttribute("success","success");
		return ControllerConstants.Tiles.User.profile;
	}
	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("profile", "profile");
		activeMenus.put("user", "create-user");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("profile");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
