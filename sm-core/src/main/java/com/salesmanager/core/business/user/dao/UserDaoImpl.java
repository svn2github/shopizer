package com.salesmanager.core.business.user.dao;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.user.model.QGroup;
import com.salesmanager.core.business.user.model.QUser;
import com.salesmanager.core.business.user.model.User;

@Repository("userDao")
public class UserDaoImpl extends SalesManagerEntityDaoImpl<Long, User> implements
		UserDao {
	
	@Override
	public User getByUserName(String userName) {
		
		
		QUser qUser = QUser.user;
		QGroup qGroup = QGroup.group;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qUser)
			.innerJoin(qUser.groups, qGroup).fetch()
			.where(qUser.adminName.eq(userName));
		

		
		User user = query.uniqueResult(qUser);
		return user;
	}

}
