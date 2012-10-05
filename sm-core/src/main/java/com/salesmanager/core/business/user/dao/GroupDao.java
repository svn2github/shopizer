package com.salesmanager.core.business.user.dao;

import java.util.List;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.user.model.Group;

public interface GroupDao extends SalesManagerEntityDao<Integer, Group> {

	List<Group> getGroupsListByPermission(Integer permissionId);



}
