package com.salesmanager.web.admin.controller.user;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
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
	LabelUtils messages;

	
//	@RequestMapping(value="/admin/categories/editCategory.html", method=RequestMethod.GET)
//	public String displayUserEdit(@RequestParam("id") long categoryId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		return displayUser(categoryId,model,request,response);
//
//	}
	
	@RequestMapping(value="/admin/users/createUser.html", method=RequestMethod.GET)
	public String displayUserCreate(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayUser(null,model,request,response);
	}
	
	@RequestMapping(value="/admin/users/displayUser.html", method=RequestMethod.GET)
	public String displayUserEdit(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		String userName = request.getRemoteUser();
		
		User user = userService.getByUserName(userName);
		
		return displayUser(user,model,request,response);

	}
	
	
	
	private String displayUser(User user, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		//display menu
		setMenu(model,request);

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		//get groups
		List<Group> groups = groupService.listGroup();
		
		if(user==null) {
			user = new User();
		}
		

		model.addAttribute("user", user);
		model.addAttribute("groups", groups);
		

		return "admin-users-user";
	}
	
	@RequestMapping(value="/admin/users/checkUserCode.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String checkUserCode(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String code = request.getParameter("code");
		String id = request.getParameter("id");


//		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		
		AjaxResponse resp = new AjaxResponse();
		
		try {
			
		User user = userService.getByUserName(code);
		
		
		if(id!=null) {
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
	public String saveUser(@Valid @ModelAttribute("category") Category category, BindingResult result, Model model, HttpServletRequest request) throws Exception {

//		Language language = (Language)request.getAttribute("LANGUAGE");
//		
//		//display menu
//		setMenu(model,request);
//		
//		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
//
//		if(category.getId() != null && category.getId() >0) { //edit entry
//			
//			//get from DB
//			Category currentCategory = categoryService.getById(category.getId());
//			
//			if(currentCategory==null || currentCategory.getMerchantStore().getId()!=store.getId()) {
//				return "catalogue-categories";
//			}
//
//		}
//
//			
//			Map<String,Language> langs = languageService.getLanguagesMap();
//			
//
//
//			List<CategoryDescription> descriptions = category.getDescriptions();
//			if(descriptions!=null) {
//				
//				for(CategoryDescription description : descriptions) {
//					
//					String code = description.getLanguage().getCode();
//					Language l = langs.get(code);
//					description.setLanguage(l);
//					description.setCategory(category);
//					
//					
//				}
//				
//			}
//			
//			//save to DB
//			category.setMerchantStore(store);
//		//}
//		
//		if (result.hasErrors()) {
//			return "catalogue-categories-category";
//		}
//		
//		//check parent
//		if(category.getParent()!=null) {
//			if(category.getParent().getId()==-1) {//this is a root category
//				category.setParent(null);
//				category.setLineage("/");
//				category.setDepth(0);
//			}
//		}
//		
//		
//		categoryService.saveOrUpdate(category);
//
//			
//		//ajust lineage and depth
//		if(category.getParent()!=null && category.getParent().getId()!=-1) { 
//		
//			Category parent = new Category();
//			parent.setId(category.getParent().getId());
//			parent.setMerchantStore(store);
//			
//			categoryService.addChild(parent, category);
//		
//		}
//		
//		
//		//get parent categories
//		List<Category> categories = categoryService.listByStore(store,language);
//		model.addAttribute("categories", categories);
//		
//
		model.addAttribute("success","success");
		return "catalogue-categories-category";
	}
	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("profile", "profile");
		activeMenus.put("create-user", "create-user");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("profile");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
