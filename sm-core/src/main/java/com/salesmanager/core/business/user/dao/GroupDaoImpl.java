package com.salesmanager.core.business.user.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.user.model.Group;

@Repository("groupDao")
public class GroupDaoImpl extends SalesManagerEntityDaoImpl<Integer, Group> implements
		GroupDao {

	@Override
	public List<Group> getGroupsListByPermission(Integer permissionId) {
		StringBuilder qs = new StringBuilder();
		qs.append("select g from Group as g ");
//		qs.append("join fetch p.merchantStore merch ");
//		qs.append("join fetch p.availabilities pa ");
//		qs.append("join fetch pa.prices pap ");
		
//		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch g.permissions perms ");
		
		
		
//		qs.append("join fetch pap.descriptions papd ");
		
		
		//images
//		qs.append("left join fetch p.images images ");
		
		//options (do not need attributes for listings)
//		qs.append("left join fetch p.attributes pattr ");
//		qs.append("left join fetch pattr.productOption po ");
//		qs.append("left join fetch po.descriptions pod ");
//		qs.append("left join fetch pattr.productOptionValue pov ");
//		qs.append("left join fetch pov.descriptions povd ");
		
		//other lefts
//		qs.append("left join fetch p.manufacturer manuf ");
//		qs.append("left join fetch p.type type ");
//		qs.append("left join fetch p.taxClass tx ");
		
		//qs.append("where pa.region in (:lid) ");
		qs.append("where perms.id =:gid");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("gid", permissionId);


    	
    	@SuppressWarnings("unchecked")
		List<Group> groups =  q.getResultList();

    	
    	return groups;
	}

}
