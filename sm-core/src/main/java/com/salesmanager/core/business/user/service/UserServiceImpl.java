package com.salesmanager.core.business.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.user.dao.UserDao;
import com.salesmanager.core.business.user.model.User;


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

	@Override
	public List<User> listUser() throws ServiceException {
		try {
			return userDao.listUser();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<User> listByStore(MerchantStore store) throws ServiceException {
		try {
			return userDao.listUserByStore(store);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void saveOrUpdate(User user) throws ServiceException {
		
		if(user.getId()==null || user.getId().longValue()==0) {
			userDao.save(user);
		} else {
			userDao.update(user);
		}
		
	}

}
