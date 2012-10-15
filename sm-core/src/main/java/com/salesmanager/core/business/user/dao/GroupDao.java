package com.salesmanager.core.business.user.dao;

import java.util.List;
import java.util.Set;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.user.model.Group;

public interface GroupDao extends SalesManagerEntityDao<Integer, Group> {

	List<Group> getGroupsListBypermissions(Set ids);

	List<Group> listGroup();
}
