package com.salesmanager.core.business.user.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.user.dao.PermissionDao;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;

@Service("permissionService")
public class PermissionServiceImpl extends
		SalesManagerEntityServiceImpl<Integer, Permission> implements
		PermissionService {

	PermissionDao permissionDao;

	@Autowired
	private LanguageService languageService;

	@Autowired
	private MerchantStoreService merchantService;
	
	@Autowired
	private GroupService groupService;

	@Autowired
	public PermissionServiceImpl(PermissionDao permissionDao) {
		super(permissionDao);
		this.permissionDao = permissionDao;

	}

	@Override
	public List<Permission> getByName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Permission> list() {
		return permissionDao.list();
	}

	@Override
	public Permission getById(Integer permissionId) {
		return permissionDao.getById(permissionId);

	}

	@Override
	public void saveOrUpdate(Permission permission) {
		// TODO Auto-generated method stub

	}
	//@Override
	public void delete(Permission permission) throws ServiceException {
		
		permission = this.getById(permission.getId());//Prevents detached entity error
	
		
		Integer permissionId = permission.getId();

			
		List<Group> groups = groupService.getGroups(permissionId);
		
		for(Group group : groups) {
			
			group.getPermissions().remove(permission);	

//			groupService.removeGroup(group);

			//need to delete a few things
			
			//delete attributes
			
			//delete availabilities
			
			
			
		}
		
			
			permissionDao.delete(permission);

	}


}
