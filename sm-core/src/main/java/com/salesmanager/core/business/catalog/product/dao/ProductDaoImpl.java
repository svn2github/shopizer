package com.salesmanager.core.business.catalog.product.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("productDao")
public class ProductDaoImpl extends SalesManagerEntityDaoImpl<Long, Product> implements ProductDao {

	public ProductDaoImpl() {
		super();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Product> getProductsForLocale(Set categoryIds, Language language, Locale locale) {
		
		ProductList products = this.getProductsListForLocale(categoryIds, language, locale, 0, -1);
		
		return products.getProducts();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Product> getProductsListByCategories(Set categoryIds) {
		

		//List regionList = new ArrayList();
		//regionList.add("*");
		//regionList.add(locale.getCountry());
		

		//TODO Test performance 
		/**
		 * Testing in debug mode takes a long time with this query
		 * running in normal mode is fine
		 */

		
		StringBuilder qs = new StringBuilder();
		qs.append("select p from Product as p ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("join fetch pa.prices pap ");
		
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");
		
		
		
		qs.append("join fetch pap.descriptions papd ");
		
		
		//images
		qs.append("left join fetch p.images images ");
		
		//options (do not need attributes for listings)
		qs.append("left join fetch p.attributes pattr ");
		qs.append("left join fetch pattr.productOption po ");
		qs.append("left join fetch po.descriptions pod ");
		qs.append("left join fetch pattr.productOptionValue pov ");
		qs.append("left join fetch pov.descriptions povd ");
		
		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch p.type type ");
		qs.append("left join fetch p.taxClass tx ");
		
		//qs.append("where pa.region in (:lid) ");
		qs.append("where categs.id in (:cid)");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("cid", categoryIds);


    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();

    	
    	return products;


}
	
	
	
	/**
	 * This query is used for category listings. All collections are not fully loaded, only the required objects
	 * so the listing page can display everything related to all products
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ProductList getProductsListForLocale(Set categoryIds, Language language, Locale locale, int first, int max) {
		

				List regionList = new ArrayList();
				regionList.add("*");
				regionList.add(locale.getCountry());
				
				ProductList productList = new ProductList();

		        
				Query countQ = super.getEntityManager().createQuery(
							"select count(p) from Product as p INNER JOIN p.availabilities pa INNER JOIN p.categories categs where categs.id in (:cid) and pa.region in (:lid) and p.available=1 and p.dateAvailable<=:dt");
							//"select p from Product as p join fetch p.availabilities pa join fetch p.categories categs where categs.id in (:cid) and pa.region in (:lid) and p.available=1 and p.dateAvailable<=:dt");
				
				countQ.setParameter("cid", categoryIds);
				countQ.setParameter("lid", regionList);
				countQ.setParameter("dt", new Date());
				
				//List<Product> ps =  countQ.getResultList();

				Number count = (Number) countQ.getSingleResult ();

				
		        if(count.intValue()==0)
		        	return productList;
		        
		        productList.setTotalCount(count.intValue());

				
				StringBuilder qs = new StringBuilder();
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
				qs.append("and p.available=true and p.dateAvailable<=:dt and pd.language.id=:lang");


		    	String hql = qs.toString();
				Query q = super.getEntityManager().createQuery(hql);

		    	q.setParameter("cid", categoryIds);
		    	q.setParameter("lid", regionList);
		    	q.setParameter("dt", new Date());
		    	q.setParameter("lang", language.getId());
		    	
		    	if(max>0) {
		    		q.setFirstResult(first);
		    		q.setMaxResults(max);
		    	}
		    	
		    	List<Product> products =  q.getResultList();
		    	productList.setProducts(products);
		    	
		    	return productList;

		
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
				qs.append("and p.available=true and p.dateAvailable<=:dt ");
				//this cannot be done on child elements from left join
				//qs.append("and pod.languageId=:lang and povd.languageId=:lang");

		    	String hql = qs.toString();
				Query q = super.getEntityManager().createQuery(hql);

		    	q.setParameter("pid", productId);
		    	q.setParameter("lid", regionList);
		    	q.setParameter("dt", new Date());
		    	q.setParameter("lang", language.getId());

		    	Product p = (Product)q.getSingleResult();


				return p;
				
	}

	@Override
	public ProductList getProductsForLocale(Set categoryIds, Language language,
			Locale locale, int startIndex, int maxCount) {
		// TODO Auto-generated method stub
		return this.getProductsListForLocale(categoryIds, language, locale, startIndex, maxCount);
	}

	
	
}
