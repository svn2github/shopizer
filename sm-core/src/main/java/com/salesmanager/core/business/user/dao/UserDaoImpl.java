package com.salesmanager.core.business.user.dao;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.user.model.User;

@Repository("userDao")
public class UserDaoImpl extends SalesManagerEntityDaoImpl<Long, User> implements
		UserDao {

}
