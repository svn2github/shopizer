package com.salesmanager.core.business.user.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.user.dao.GroupDao;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;

@Service("groupService")
public class GroupServiceImpl extends SalesManagerEntityServiceImpl<Integer, Group>
		implements GroupService{


	GroupDao groupDao;
	
	@Autowired
	public GroupServiceImpl(GroupDao groupDao) {
		super(groupDao);
		this.groupDao = groupDao;

	}

	@Override
	public List<Group> getGroups(Integer permissionId) {
		return groupDao.getGroupsListByPermission(permissionId);
	}

	@Override
	public void removeGroup(Group group) throws ServiceException {
		group = this.getById(group.getId());//Prevents detached entity error
		group.setPermissions(null);
		
		
		this.delete(group);		
	}


}
