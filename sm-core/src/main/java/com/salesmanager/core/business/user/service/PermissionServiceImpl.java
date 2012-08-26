package com.salesmanager.core.business.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.user.dao.PermissionDao;
import com.salesmanager.core.business.user.model.Permission;

@Service("permissionService")
public class PermissionServiceImpl extends SalesManagerEntityServiceImpl<Integer, Permission>
		implements PermissionService {


	PermissionDao permissionDao;
	
	@Autowired
	public PermissionServiceImpl(PermissionDao permissionDao) {
		super(permissionDao);
		this.permissionDao = permissionDao;

	}

}
