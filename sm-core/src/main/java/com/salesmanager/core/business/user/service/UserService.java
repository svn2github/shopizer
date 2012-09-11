package com.salesmanager.core.business.user.service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.user.model.User;

public interface UserService extends SalesManagerEntityService<Long, User> {

	User getByUserName(String userName) throws ServiceException;

}
