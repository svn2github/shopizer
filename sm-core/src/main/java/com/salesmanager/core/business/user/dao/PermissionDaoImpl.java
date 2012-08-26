package com.salesmanager.core.business.user.dao;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.user.model.Permission;

@Repository("permissionDao")
public class PermissionDaoImpl extends SalesManagerEntityDaoImpl<Integer, Permission> implements
		PermissionDao {

}
