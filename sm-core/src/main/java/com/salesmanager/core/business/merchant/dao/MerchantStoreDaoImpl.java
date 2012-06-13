package com.salesmanager.core.business.merchant.dao;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.QMerchantStore;


@Repository("merchantStoreDao")
public class MerchantStoreDaoImpl extends SalesManagerEntityDaoImpl<Integer, MerchantStore> implements MerchantStoreDao {

	public MerchantStoreDaoImpl() {
		super();
	}
	
	@Override
	public Collection<Product> getProducts(MerchantStore merchantStore) throws ServiceException {
		

		

		StringBuilder qs = new StringBuilder();
	
		
		qs.append("from ProductMerchant as pm, Product as p ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch pa.prices pap ");
		qs.append("join fetch pap.descriptions papd ");
		//images
		qs.append("left join fetch p.images images ");
		//options
		qs.append("left join fetch p.attributes pattr ");
		qs.append("left join fetch pattr.productOption po ");
		qs.append("left join fetch po.descriptions pod ");
		qs.append("left join fetch pattr.productOptionValue pov ");
		qs.append("left join fetch pov.descriptions povd ");
		
		qs.append("where pm.merchantId=:mid ");
		qs.append("and pm.productId=p.productId ");

		
		
		
		
		// TODO : WTF?
    	String hql = qs.toString();
		//Query q = this.entityManager.createQuery(hql);
    	
		Session session = (Session)super.getEntityManager().getDelegate();
		org.hibernate.Query q = session.createQuery(qs.toString());
		q.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		

    	q.setInteger("mid", merchantStore.getId());
    	//q.setParameterList("lid", regionList);
    	//@TODO languageUtil
    	//q.setParameter("lang", 1);
		
		Collection<Product> results = q.list();
    	//MerchantStore s = (MerchantStore)q.getSingleResult();
    	//Collection<Product> results = s.getProducts();

		return results;

		
		
		
	}
	
	public MerchantStore getMerchantStore(Integer merchantStoreId)  {
		
		
		QMerchantStore qMerchantStore = QMerchantStore.merchantStore;

		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qMerchantStore)
			.innerJoin(qMerchantStore.defaultLanguage)
			.leftJoin(qMerchantStore.languages)
			.where(qMerchantStore.id.eq(merchantStoreId));
		
		return query.uniqueResult(qMerchantStore);
	}

}
