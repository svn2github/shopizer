package com.salesmanager.core.business.user.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.model.PermissionCriteria;
import com.salesmanager.core.business.user.model.PermissionList;
import com.salesmanager.core.business.user.model.QPermission;

@Repository("permissionDao")
public class PermissionDaoImpl extends SalesManagerEntityDaoImpl<Integer, Permission> implements
		PermissionDao {

	@Override
	public List<Permission> list() {
		QPermission qPermission = QPermission.permission;
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qPermission)
			.orderBy(qPermission.id.asc());
		
		return query.listDistinct(qPermission);
		}

	@Override
	public Permission getById(Integer permissionId) {
		QPermission qPermission = QPermission.permission;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qPermission)
			.where(qPermission.id.eq(permissionId));
		
		return query.uniqueResult(qPermission);
	}


	@Override
	public List<Permission> getPermissionsListByGroups(Set groupIds) {
		StringBuilder qs = new StringBuilder();
		qs.append("select p from Permission as p ");
		qs.append("join fetch p.groups grous ");
		qs.append("where grous.id in (:cid)");

    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("cid", groupIds);
    	
    	@SuppressWarnings("unchecked")
		List<Permission> permissions =  q.getResultList();
    	
    	return permissions;
	}

	@Override
	public PermissionList listByCriteria(PermissionCriteria criteria) {
		PermissionList permissionList = new PermissionList();

        
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(p) from Permission as p");
		
		StringBuilder countBuilderWhere = new StringBuilder();
//		countBuilderWhere.append(" where p.merchantStore.id=:mId");
		
		//"select count(p) from Product as p INNER JOIN p.availabilities pa INNER JOIN p.categories categs where p.merchantSore.id=:mId and categs.id in (:cid) and pa.region in (:lid) and p.available=1 and p.dateAvailable<=:dt");

//		if(!StringUtils.isBlank(criteria.getPermissionName())) {
//			countBuilderSelect.append(" INNER JOIN p.descriptions pd");
//			countBuilderWhere.append(" and pd.language.id=:lang and pd.name like : nm");
//		}
		
		
		if(criteria.getGroupIds()!=null && criteria.getGroupIds().size()>0) {
			countBuilderSelect.append(" INNER JOIN p.groups grous");
			countBuilderWhere.append(" where grous.id in (:cid)");
		}
		
//		if(!StringUtils.isBlank(criteria.getCode())) {
//			countBuilderWhere.append(" and p.sku like :sku");
//		}
		
//		if(criteria.getAvailable()!=null) {
//			if(criteria.getAvailable().booleanValue()) {
//				countBuilderWhere.append(" and p.available=true and p.dateAvailable<=:dt");
//			} else {
//				countBuilderWhere.append(" and p.available=false or p.dateAvailable>:dt");
//			}
//		}
	
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

//		countQ.setParameter("mId", store.getId());
		
		if(criteria.getGroupIds()!=null && criteria.getGroupIds().size()>0) {
			countQ.setParameter("cid", criteria.getGroupIds());
		}
		

//		if(criteria.getAvailable()!=null) {
//			countQ.setParameter("dt", new Date());
//		}
		
//		if(!StringUtils.isBlank(criteria.getCode())) {
//			countQ.setParameter("sku", "%" + criteria.getCode() + "%");
//		}
		
//		if(!StringUtils.isBlank(criteria.getProductName())) {
//			countQ.setParameter("nm", "%" + criteria.getProductName() + "%");
//		}

		Number count = (Number) countQ.getSingleResult ();

		permissionList.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return permissionList;

		
		StringBuilder qs = new StringBuilder();
		qs.append("select p from Permission as p ");
//		qs.append("join fetch p.merchantStore merch ");
//		qs.append("join fetch p.availabilities pa ");
//		qs.append("join fetch pa.prices pap ");
		
//		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.groups grous ");
		

		
		//images
//		qs.append("left join fetch p.images images ");
		

		//other lefts
//		qs.append("left join fetch p.manufacturer manuf ");
//		qs.append("left join fetch p.type type ");
//		qs.append("left join fetch p.taxClass tx ");

//		qs.append("where merch.id=:mId");
//		qs.append(" and pd.language.id=:lang");
		
		if(criteria.getGroupIds()!=null && criteria.getGroupIds().size()>0) {
			qs.append(" where grous.id in (:cid)");
		}
		

		
//		if(criteria.getAvailable()!=null) {
//			if(criteria.getAvailable().booleanValue()) {
//				qs.append(" and p.available=true and p.dateAvailable<=:dt");
//			} else {
//				qs.append(" and p.available=false and p.dateAvailable>:dt");
//			}
//		}
//		
//		if(!StringUtils.isBlank(criteria.getProductName())) {
//			qs.append(" and pd.name like:nm");
//		}
//		
//		if(!StringUtils.isBlank(criteria.getCode())) {
//			qs.append(" and p.sku like :sku");
//		}


    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);


  //  	q.setParameter("lang", language.getId());
//    	q.setParameter("mId", store.getId());
    	
    	if(criteria.getGroupIds()!=null && criteria.getGroupIds().size()>0) {
    		q.setParameter("cid", criteria.getGroupIds());
    	}
    	

		
//		if(criteria.getAvailable()!=null) {
//			q.setParameter("dt", new Date());
//		}
//		
//		if(!StringUtils.isBlank(criteria.getCode())) {
//			q.setParameter("sku", "%" + criteria.getCode() + "%");
//		}
//		
//		if(!StringUtils.isBlank(criteria.getProductName())) {
//			q.setParameter("nm", "%" + criteria.getProductName() + "%");
//		}
    	
    	if(criteria.getMaxCount()>0) {
    		
    		
	    	q.setFirstResult(criteria.getStartIndex());
	    	if(criteria.getMaxCount()<count.intValue()) {
	    		q.setMaxResults(criteria.getMaxCount());
	    		permissionList.setTotalCount(criteria.getMaxCount());
	    	}
	    	else {
	    		q.setMaxResults(count.intValue());
	    		permissionList.setTotalCount(count.intValue());
	    	}
    	}
    	
    	@SuppressWarnings("unchecked")
		List<Permission> permissions =  q.getResultList();
    	permissionList.setPermissions(permissions);
    	
    	return permissionList;
	}

}
