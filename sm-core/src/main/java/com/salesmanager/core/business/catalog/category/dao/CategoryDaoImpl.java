package com.salesmanager.core.business.catalog.category.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.QCategory;
import com.salesmanager.core.business.catalog.category.model.QCategoryDescription;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("categoryDao")
public class CategoryDaoImpl extends SalesManagerEntityDaoImpl<Long, Category> implements CategoryDao {

	public CategoryDaoImpl() {
		super();
	}
	
	@Override
	public Category getByName(MerchantStore store, String name) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.where(qDescription.name.like(name)
			.and(qCategory.merchantSore.id.eq(store.getId())));
		

		
		List<Category> categories = query.list(qCategory);
		if(categories.size()>0) {
			return categories.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Category> listBySeUrl(MerchantStore store,String seUrl) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.where(qDescription.seUrl.like(seUrl)
			.and(qCategory.merchantSore.id.eq(store.getId())))
			.orderBy(qDescription._super.title.desc(), qDescription._super.name.desc());
		
		return query.list(qCategory);
	}
	
	@Override
	public Category getByCode(MerchantStore store, String code) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.where(qCategory.code.eq(code)
			.and(qCategory.merchantSore.id.eq(store.getId())))
			.orderBy(qDescription._super.title.desc(), qDescription._super.name.desc());
		
		return query.uniqueResult(qCategory);
	}
	
	@Override
	public List<Category> listByLineage(MerchantStore store, String lineage) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.where(qCategory.lineage.like(new StringBuilder().append(lineage).append("%").toString())
			.and(qCategory.merchantSore.id.eq(store.getId())))
			.orderBy(qCategory.lineage.asc(),qCategory.lineage.asc());
		
		return query.listDistinct(qCategory);
	}

	@Override
	public List<Category> listByStoreAndParent(MerchantStore store, Category category) {
		QCategory qCategory = QCategory.category;
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		if (store == null) {
			if (category == null) {
				query.from(qCategory)
					.where(qCategory.parent.isNull())
					.orderBy(qCategory.id.desc());
			} else {
				query.from(qCategory)
					.where(qCategory.parent.eq(category))
					.orderBy(qCategory.id.desc());
			}
		} else {
			if (category == null) {
				query.from(qCategory)
					.where(qCategory.parent.isNull()
						.and(qCategory.merchantSore.eq(store)))
					.orderBy(qCategory.id.desc());
			} else {
				query.from(qCategory)
					.where(qCategory.parent.eq(category)
						.and(qCategory.merchantSore.eq(store)))
					.orderBy(qCategory.id.desc());
			}
		}
		
		return query.list(qCategory);
	}

	@Override
	public List<Category> listByStore(MerchantStore store) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.where(qCategory.merchantSore.id.eq(store.getId()))
			.orderBy(qCategory.code.asc(),qCategory.code.asc());
		
		return query.listDistinct(qCategory);
	}
	
	@Override
	public List<Category> listByStore(MerchantStore store, Language language) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.where((qCategory.merchantSore.id.eq(store.getId()))
			.and(qDescription.language.id.eq(language.getId())))
			.orderBy(qCategory.code.asc(),qCategory.code.asc());
		
		return query.listDistinct(qCategory);
	}
}
