package com.salesmanager.core.business.user.dao;

import java.util.List;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.user.model.Permission;

public interface PermissionDao extends SalesManagerEntityDao<Integer, Permission> {

	List<Permission> listByStore();



}
