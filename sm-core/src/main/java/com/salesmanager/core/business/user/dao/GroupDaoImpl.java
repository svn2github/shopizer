package com.salesmanager.core.business.user.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.category.model.QCategory;
import com.salesmanager.core.business.catalog.category.model.QCategoryDescription;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.QGroup;

@Repository("groupDao")
public class GroupDaoImpl extends SalesManagerEntityDaoImpl<Integer, Group> implements
		GroupDao {

	@SuppressWarnings("rawtypes")
	@Override
	public List<Group> getGroupsListBypermissions(Set permissionIds) {
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
		qs.append("where perms.id in (:cid) ");
//		qs.append("and g.groupName !=:gn" );


    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("cid", permissionIds);
 //   	q.setParameter("gn","SUPERADMIN");

    	
    	@SuppressWarnings("unchecked")
		List<Group> groups =  q.getResultList();

    	
    	return groups;
	}

	@Override
	public List<Group> listGroup() {
		QGroup qGroup = QGroup.group;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qGroup)
			.orderBy(qGroup.id.asc());
		
		return query.listDistinct(qGroup);
	}

}
