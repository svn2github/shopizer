package com.salesmanager.core.business.catalog.product.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
	
	
	@Override
	/**
	 * This query is used for category listings. All collections are not fully loaded, only the required objects
	 * so the listing page can display everything related to all products
	 */
	public List<Product> getProductsForLocale(Set categoryIds, Language language, Locale locale) {
		
				//TODO accept min max for pagination
				List regionList = new ArrayList();
				regionList.add("*");
				regionList.add(locale.getCountry());


				
				StringBuilder qs = new StringBuilder();
				//qs.append("select distinct p from Product as p ");
				qs.append("select p from Product as p ");
				qs.append("join fetch p.availabilities pa ");
				qs.append("join fetch pa.prices pap ");
				
				qs.append("join fetch p.descriptions pd ");
				qs.append("join fetch p.categories categs ");
				
				
				//not necessary
				//qs.append("join fetch pap.descriptions papd ");
				
				
				//images
				qs.append("left join fetch p.images images ");
				
				//options (do not need attributes for listings)
				//qs.append("left join fetch p.attributes pattr ");
				//qs.append("left join fetch pattr.productOption po ");
				//qs.append("left join fetch po.descriptions pod ");
				//qs.append("left join fetch pattr.productOptionValue pov ");
				//qs.append("left join fetch pov.descriptions povd ");
				
				//other lefts
				qs.append("left join fetch p.manufacturer manuf ");
				qs.append("left join fetch p.type type ");
				qs.append("left join fetch p.taxClass tx ");
				
				//qs.append("where pa.region in (:lid) ");
				qs.append("where categs.id in (:cid) and pa.region in (:lid) ");
				qs.append("and pd.language.id=:lang");


		    	String hql = qs.toString();
				Query q = super.getEntityManager().createQuery(hql);

		    	q.setParameter("cid", categoryIds);
		    	q.setParameter("lid", regionList);
		    	q.setParameter("lang", language.getId());
		    	
		    	List<Product> products =  q.getResultList();
		    	
		    	return products;

		
	}
	
	@Override
	public Product getProductForLocale(long productId, Language language, Locale locale) {


				
				List regionList = new ArrayList();
				regionList.add("*");
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
				//other lefts
				qs.append("left join fetch p.manufacturer manuf ");
				qs.append("left join fetch p.type type ");
				qs.append("left join fetch p.taxClass tx ");
				
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
