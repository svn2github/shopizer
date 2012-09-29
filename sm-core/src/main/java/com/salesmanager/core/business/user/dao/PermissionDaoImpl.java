package com.salesmanager.core.business.user.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.model.QPermission;

@Repository("permissionDao")
public class PermissionDaoImpl extends SalesManagerEntityDaoImpl<Integer, Permission> implements
		PermissionDao {

	@Override
	public List<Permission> listByStore() {
		QPermission qPermission = QPermission.permission;
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qPermission)
			.orderBy(qPermission.id.asc());
		
		return query.listDistinct(qPermission);
		}

}
