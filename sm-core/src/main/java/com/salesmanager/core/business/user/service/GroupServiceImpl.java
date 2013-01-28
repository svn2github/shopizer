package com.salesmanager.core.business.user.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.user.dao.GroupDao;
import com.salesmanager.core.business.user.model.Group;

@Service("groupService")
public class GroupServiceImpl extends
		SalesManagerEntityServiceImpl<Integer, Group> implements GroupService {

	GroupDao groupDao;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	public GroupServiceImpl(GroupDao groupDao) {
		super(groupDao);
		this.groupDao = groupDao;

	}






	@Override
	public List<Group> listGroup() throws ServiceException {
		try {
			return groupDao.listGroup();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public List<Group> listGroupByIds(Set<Integer> ids) throws ServiceException {
		try {
			return groupDao.listGroupByIds(ids);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


}
