package com.salesmanager.core.business.catalog.product.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("productDao")
public class ProductDaoImpl extends SalesManagerEntityDaoImpl<Long, Product> implements ProductDao {

	public ProductDaoImpl() {
		super();
	}
	
	public Product getProduct(long productId, Language language, Locale locale) {


				
				List regionList = new ArrayList();
				regionList.add("*");//depends of the config
				regionList.add(locale.getCountry());
				

				StringBuilder qs = new StringBuilder();
				qs.append("select distinct p from Product as p ");
				qs.append("join fetch p.availabilities pa ");
				qs.append("join fetch p.descriptions pd ");
				qs.append("join fetch pa.prices pap ");
				qs.append("join fetch pap.descriptions papd ");
				//images
				qs.append("left join fetch p.images images ");
				//options
				qs.append("left join fetch p.images images ");
				qs.append("left join fetch p.attributes pattr ");
				qs.append("left join fetch pattr.productOption po ");
				qs.append("left join fetch po.descriptions pod ");
				qs.append("left join fetch pattr.productOptionValue pov ");
				qs.append("left join fetch pov.descriptions povd ");
				//qs.append("where p.id=:pid");
				qs.append("where p.id=:pid and pa.region in (:lid) ");
				qs.append("and pd.language.id=:lang and papd.language.id=:lang ");
				//this cannot be done on child elements from left join
				//qs.append("and pod.languageId=:lang and povd.languageId=:lang");

		    	String hql = qs.toString();
				Query q = super.getEntityManager().createQuery(hql);

		    	q.setParameter("pid", productId);
		    	q.setParameter("lid", regionList);
		    	q.setParameter("lang", language.getId());

		    	Product p = (Product)q.getSingleResult();


				return p;
				
			}

	
	
}
