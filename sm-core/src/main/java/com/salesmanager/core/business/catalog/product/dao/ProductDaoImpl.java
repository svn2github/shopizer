package com.salesmanager.core.business.catalog.product.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.tax.model.taxclass.TaxClass;
import com.salesmanager.core.constants.Constants;

@Repository("productDao")
public class ProductDaoImpl extends SalesManagerEntityDaoImpl<Long, Product> implements ProductDao {

	public ProductDaoImpl() {
		super();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Product> getProductsForLocale(MerchantStore store, Set categoryIds, Language language, Locale locale) {
		
		ProductList products = this.getProductsListForLocale(store, categoryIds, language, locale, 0, -1);
		
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
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");
		
		
		
		qs.append("left join fetch pap.descriptions papd ");
		
		
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
	
	

	@Override
	public List<Product> listByTaxClass(TaxClass taxClass) {

		
		/**
		 * Testing in debug mode takes a long time with this query
		 * running in normal mode is fine
		 */

		
		StringBuilder qs = new StringBuilder();
		qs.append("select p from Product as p ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");
		
		
		
		qs.append("left join fetch pap.descriptions papd ");
		
		
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
		qs.append("where tx.id=:tid");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("tid", taxClass.getId());


    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();

    	
    	return products;
		
		
	}

	
	
	
	@Override
	public List<Product> listByStore(MerchantStore store) {

		
		/**
		 * Testing in debug mode takes a long time with this query
		 * running in normal mode is fine
		 */

		
		StringBuilder qs = new StringBuilder();
		qs.append("select p from Product as p ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");
		
		
		
		qs.append("left join fetch pap.descriptions papd ");
		
		
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
		qs.append("where merch.id=:mid");



    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("mid", store.getId());


    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();

    	
    	return products;
		
		
	}
	
	
	/**
	 * Used in the admin section
	 * @param store
	 * @param first
	 * @param max
	 * @return
	 */
	@Override
	public ProductList listByStore(MerchantStore store, Language language, ProductCriteria criteria) {

		ProductList productList = new ProductList();

        
		StringBuilder countBuilderSelect = new StringBuilder();
		countBuilderSelect.append("select count(p) from Product as p");
		
		StringBuilder countBuilderWhere = new StringBuilder();
		countBuilderWhere.append(" where p.merchantStore.id=:mId");
		
		//"select count(p) from Product as p INNER JOIN p.availabilities pa INNER JOIN p.categories categs where p.merchantStore.id=:mId and categs.id in (:cid) and pa.region in (:lid) and p.available=1 and p.dateAvailable<=:dt");

		if(!StringUtils.isBlank(criteria.getProductName())) {
			countBuilderSelect.append(" INNER JOIN p.descriptions pd");
			countBuilderWhere.append(" and pd.language.id=:lang and pd.name like:nm");
		}
		
		
		if(criteria.getCategoryIds()!=null && criteria.getCategoryIds().size()>0) {
			countBuilderSelect.append(" INNER JOIN p.categories categs");
			countBuilderWhere.append(" and categs.id in (:cid)");
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			countBuilderWhere.append(" and p.sku like :sku");
		}
		
		if(criteria.getAvailable()!=null) {
			if(criteria.getAvailable().booleanValue()) {
				countBuilderWhere.append(" and p.available=true and p.dateAvailable<=:dt");
			} else {
				countBuilderWhere.append(" and p.available=false or p.dateAvailable>:dt");
			}
		}
	
		Query countQ = super.getEntityManager().createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());

		countQ.setParameter("mId", store.getId());
		
		if(criteria.getCategoryIds()!=null && criteria.getCategoryIds().size()>0) {
			countQ.setParameter("cid", criteria.getCategoryIds());
		}
		

		if(criteria.getAvailable()!=null) {
			countQ.setParameter("dt", new Date());
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			countQ.setParameter("sku", "%" + criteria.getCode() + "%");
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			countQ.setParameter("nm", "%" + criteria.getProductName() + "%");
			countQ.setParameter("lang", language.getId());
		}

		Number count = (Number) countQ.getSingleResult ();

		productList.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return productList;

		
		StringBuilder qs = new StringBuilder();
		qs.append("select p from Product as p ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		
		qs.append("join fetch p.descriptions pd ");
		qs.append("left join fetch p.categories categs ");
		

		
		//images
		qs.append("left join fetch p.images images ");
		

		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch p.type type ");
		qs.append("left join fetch p.taxClass tx ");

		qs.append("where merch.id=:mId");
		qs.append(" and pd.language.id=:lang");
		
		if(criteria.getCategoryIds()!=null && criteria.getCategoryIds().size()>0) {
			qs.append(" and categs.id in (:cid)");
		}
		

		
		if(criteria.getAvailable()!=null) {
			if(criteria.getAvailable().booleanValue()) {
				qs.append(" and p.available=true and p.dateAvailable<=:dt");
			} else {
				qs.append(" and p.available=false and p.dateAvailable>:dt");
			}
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			qs.append(" and pd.name like:nm");
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			qs.append(" and p.sku like :sku");
		}


    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);


    	q.setParameter("lang", language.getId());
    	q.setParameter("mId", store.getId());
    	
    	if(criteria.getCategoryIds()!=null && criteria.getCategoryIds().size()>0) {
    		q.setParameter("cid", criteria.getCategoryIds());
    	}
    	

		
		if(criteria.getAvailable()!=null) {
			q.setParameter("dt", new Date());
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			q.setParameter("sku", "%" + criteria.getCode() + "%");
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			q.setParameter("nm", "%" + criteria.getProductName() + "%");
			q.setParameter("lang", language.getId());
		}
    	
    	if(criteria.getMaxCount()>0) {
    		
    		
	    	q.setFirstResult(criteria.getStartIndex());
	    	if(criteria.getMaxCount()<count.intValue()) {
	    		q.setMaxResults(criteria.getMaxCount());
	    		productList.setTotalCount(criteria.getMaxCount());
	    	}
	    	else {
	    		q.setMaxResults(count.intValue());
	    		productList.setTotalCount(count.intValue());
	    	}
	    	//if(criteria.getMaxCount()>0) {
	    	//		int interval = criteria.getMaxCount() - criteria.getStartIndex();
	    	//
	    	//		if(count.intValue()<interval) {
	    				
	    	//			q.setMaxResults(count.intValue());
	    	//		} else {
	    				
	    	//		}
	    			//int maxCount = criteria.getStartIndex() + criteria.getMaxCount();
	    			//if(maxCount < count.intValue()) {
	    			//	q.setMaxResults(maxCount);
	    			//} else {
	    			//	q.setMaxResults(count.intValue());
	    			//}
	    	//}
    	}
    	
    	@SuppressWarnings("unchecked")
		List<Product> products =  q.getResultList();
    	productList.setProducts(products);
    	
    	return productList;

		
		
		
	}
	
	
	
	/**
	 * This query is used for category listings. All collections are not fully loaded, only the required objects
	 * so the listing page can display everything related to all products
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ProductList getProductsListForLocale(MerchantStore store, Set categoryIds, Language language, Locale locale, int first, int max) {
		

				List regionList = new ArrayList();
				regionList.add(Constants.ALL_REGIONS);
				if(locale!=null) {
					regionList.add(locale.getCountry());
				}
				
				ProductList productList = new ProductList();

		        
				Query countQ = super.getEntityManager().createQuery(
							"select count(p) from Product as p INNER JOIN p.availabilities pa INNER JOIN p.categories categs where p.merchantStore.id=:mId and categs.id in (:cid) and pa.region in (:lid) and p.available=1 and p.dateAvailable<=:dt");
							//"select p from Product as p join fetch p.availabilities pa join fetch p.categories categs where categs.id in (:cid) and pa.region in (:lid) and p.available=1 and p.dateAvailable<=:dt");
				
				countQ.setParameter("cid", categoryIds);
				countQ.setParameter("lid", regionList);
				countQ.setParameter("dt", new Date());
				countQ.setParameter("mId", store.getId());
				
				//List<Product> ps =  countQ.getResultList();

				Number count = (Number) countQ.getSingleResult ();

				
				productList.setTotalCount(count.intValue());
				
				if(count.intValue()==0)
		        	return productList;
		        
		        

				
				StringBuilder qs = new StringBuilder();
				qs.append("select p from Product as p ");
				qs.append("join fetch p.merchantStore merch ");
				qs.append("join fetch p.availabilities pa ");
				qs.append("left join fetch pa.prices pap ");
				
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
				qs.append("where p.merchantStore.id=mId and categs.id in (:cid) and pa.region in (:lid) ");
				qs.append("and p.available=true and p.dateAvailable<=:dt and pd.language.id=:lang");


		    	String hql = qs.toString();
				Query q = super.getEntityManager().createQuery(hql);

		    	q.setParameter("cid", categoryIds);
		    	q.setParameter("lid", regionList);
		    	q.setParameter("dt", new Date());
		    	q.setParameter("lang", language.getId());
		    	q.setParameter("mId", store.getId());
		    	
		    	
		    	q.setFirstResult(first);
		    	if(max>0) {
		    			int maxCount = first + max;

		    			if(maxCount < count.intValue()) {
		    				q.setMaxResults(maxCount);
		    			} else {
		    				q.setMaxResults(count.intValue());
		    			}
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
				qs.append("join fetch p.merchantStore pm ");
				qs.append("left join fetch pa.prices pap ");
				qs.append("left join fetch pap.descriptions papd ");
				
				
				
				
				//images
				qs.append("left join fetch p.images images ");
				//options
				qs.append("left join fetch p.images images ");
				qs.append("left join fetch p.attributes pattr ");
				qs.append("left join fetch pattr.productOption po ");
				qs.append("left join fetch po.descriptions pod ");
				qs.append("left join fetch pattr.productOptionValue pov ");
				qs.append("left join fetch pov.descriptions povd ");
				qs.append("left join fetch p.relationships pr ");
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
	public Product getById(Long productId) {


		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from Product as p ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.descriptions pd ");
		qs.append("left join fetch p.categories categs ");
		qs.append("left join fetch pa.prices pap ");
		qs.append("left join fetch pap.descriptions papd ");
		qs.append("left join fetch categs.descriptions categsd ");
		
		//images
		qs.append("left join fetch p.images images ");
		//options
		qs.append("left join fetch p.images images ");
		qs.append("left join fetch p.attributes pattr ");
		qs.append("left join fetch pattr.productOption po ");
		qs.append("left join fetch po.descriptions pod ");
		qs.append("left join fetch pattr.productOptionValue pov ");
		qs.append("left join fetch pov.descriptions povd ");
		qs.append("left join fetch p.relationships pr ");
		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch p.type type ");
		qs.append("left join fetch p.taxClass tx ");
		
		qs.append("where p.id=:pid");


    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);

    	q.setParameter("pid", productId);


    	Product p = (Product)q.getSingleResult();


		return p;
		
}

	@Override
	public ProductList getProductsForLocale(MerchantStore store, Set categoryIds, Language language,
			Locale locale, int startIndex, int maxCount) {
		// TODO Auto-generated method stub
		return this.getProductsListForLocale(store, categoryIds, language, locale, startIndex, maxCount);
	}

	
	
}
