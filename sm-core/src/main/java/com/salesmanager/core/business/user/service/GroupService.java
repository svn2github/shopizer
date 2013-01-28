package com.salesmanager.core.business.user.service;

import java.util.List;
import java.util.Set;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.user.model.Group;

public interface GroupService extends SalesManagerEntityService<Integer, Group> {


	List<Group> listGroup() throws ServiceException;
	List<Group> listGroupByIds(Set<Integer> ids) throws ServiceException;

}
