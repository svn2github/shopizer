package com.salesmanager.core.business.user.service;

import java.util.List;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.user.model.Permission;

public interface PermissionService extends SalesManagerEntityService<Integer, Permission> {

	List<Permission> getByName();

	List<Permission> listByStore() throws ServiceException;

}
