package com.salesmanager.core.business.user.dao;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.user.model.Group;

@Repository("groupDao")
public class GroupDaoImpl extends SalesManagerEntityDaoImpl<Integer, Group> implements
		GroupDao {

}
