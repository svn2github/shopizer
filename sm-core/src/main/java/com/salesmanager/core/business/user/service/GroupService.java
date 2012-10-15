package com.salesmanager.core.business.user.service;

import java.util.List;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;

public interface GroupService extends SalesManagerEntityService<Integer, Group> {

//	List<Group> getGroups(List<Integer> permissionIds) throws ServiceException;

	void removeGroup(Group group) throws ServiceException;

	void saveOrUpdate(Group group) throws ServiceException;

	List<Group> getByName() throws ServiceException;

	List<Group> listGroup() throws ServiceException;

}
