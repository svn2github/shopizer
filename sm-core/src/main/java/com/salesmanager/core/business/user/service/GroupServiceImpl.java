package com.salesmanager.core.business.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.user.dao.GroupDao;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;

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
	public void removeGroup(Group group) throws ServiceException {
		group = this.getById(group.getId());// Prevents detached entity error

		List<Permission> permissions = group.getPermissions();
		for (Permission p : permissions){
			p.getGroups().remove(group);
		}
		this.delete(group);
	}

	@Override
	public void saveOrUpdate(Group group) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Group> getByName() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}


}
