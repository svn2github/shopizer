package com.salesmanager.core.business.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.user.dao.UserDao;
import com.salesmanager.core.business.user.model.User;

@Service("userService")
public class UserServiceImpl extends SalesManagerEntityServiceImpl<Long, User>
		implements UserService {


	UserDao userDao;
	
	@Autowired
	public UserServiceImpl(UserDao userDao) {
		super(userDao);
		this.userDao = userDao;

	}
	
	@Override
	public User getByUserName(String userName) throws ServiceException {
		
		return userDao.getByUserName(userName);
		
	}

}
