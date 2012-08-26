package com.salesmanager.core.business.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.user.dao.GroupDao;
import com.salesmanager.core.business.user.model.Group;

@Service("groupService")
public class GroupServiceImpl extends SalesManagerEntityServiceImpl<Integer, Group>
		implements GroupService{


	GroupDao groupDao;
	
	@Autowired
	public GroupServiceImpl(GroupDao groupDao) {
		super(groupDao);
		this.groupDao = groupDao;

	}

}
