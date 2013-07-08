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
	public List<Category> getByName(MerchantStore store, String name, Language language) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qDescription.name.like("%" + name + "%")
			.and(qDescription.language.id.eq(language.getId()))
			.and(qCategory.merchantStore.id.eq(store.getId())))
			.orderBy(qCategory.sortOrder.asc());
		

		
		List<Category> categories = query.list(qCategory);
		return categories;
	}
	


	@Override
	public List<Category> listBySeUrl(MerchantStore store,String seUrl) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qDescription.seUrl.like(seUrl)
			.and(qCategory.merchantStore.id.eq(store.getId())))
			.orderBy(qDescription._super.title.desc(), qDescription._super.name.desc(), qDescription.language.id.desc()).orderBy(qCategory.sortOrder.asc());
		
		return query.list(qCategory);
	}
	
	@Override
	public Category getBySeUrl(MerchantStore store,String seUrl) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qDescription.seUrl.eq(seUrl)
			.and(qCategory.merchantStore.id.eq(store.getId())));

		
		return query.uniqueResult(qCategory);
	}
	
	@Override
	public Category getByCode(MerchantStore store, String code) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.code.eq(code)
			.and(qCategory.merchantStore.id.eq(store.getId())));
			//.orderBy(qDescription._super.title.desc(), qDescription._super.name.desc(), qDescription.language.id.desc());
			//.orderBy(qDescription.language.id.desc());
		
		return query.uniqueResult(qCategory);
	}
	
	@Override
	public Category getByCode(String merchantStoreCode, String code) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.code.eq(code)
			.and(qCategory.merchantStore.code.eq(merchantStoreCode)));

		
		return query.uniqueResult(qCategory);
	}
	
	@Override
	public Category getById(Long id) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.id.eq(id));
			//.orderBy(qDescription.language.id.desc());
			//.orderBy(qDescription._super.title.desc(), qDescription._super.name.desc(), qDescription.language.id.desc());
		
		return query.uniqueResult(qCategory);
	}
	
	@Override
	public List<Category> listByLineage(MerchantStore store, String lineage) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.lineage.like(new StringBuilder().append(lineage).append("%").toString())
			.and(qCategory.merchantStore.id.eq(store.getId())))
			.orderBy(qCategory.sortOrder.asc(), qCategory.lineage.asc(), qCategory.lineage.asc(), qCategory.depth.asc(), qDescription.language.id.desc());
		
		return query.listDistinct(qCategory);
	}
	
	@Override
	public List<Category> listByLineage(String merchantStoreCode, String lineage) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.lineage.like(new StringBuilder().append(lineage).append("%").toString())
			.and(qCategory.merchantStore.code.eq(merchantStoreCode)))
			.orderBy(qCategory.sortOrder.asc(),qCategory.lineage.asc(), qCategory.lineage.asc(), qCategory.depth.asc(), qDescription.language.id.desc());
		
		return query.listDistinct(qCategory);
	}
	
	@Override
	public List<Category> listByDepth(MerchantStore store, int depth) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.depth.eq(depth)
			.and(qCategory.merchantStore.id.eq(store.getId())))
			.orderBy(qCategory.sortOrder.asc(), qCategory.lineage.asc(), qCategory.lineage.asc(), qCategory.depth.asc(), qDescription.language.id.desc());
		
		return query.listDistinct(qCategory);
	}
	
	@Override
	public List<Category> listByDepth(MerchantStore store, int depth, Language language) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.depth.eq(depth)
			.and(qCategory.merchantStore.id.eq(store.getId()))
			.and(qDescription.language.id.eq(language.getId())))
			.orderBy(qCategory.sortOrder.asc(), qCategory.lineage.asc(), qCategory.lineage.asc(), qCategory.depth.asc(), qDescription.language.id.desc());
		
		return query.listDistinct(qCategory);
	}
	
	@Override
	public List<Category> listByParent(Category category, Language language) {
		
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.leftJoin(qCategory.parent).fetch()
			.where(qCategory.parent.id.eq(category.getId())
			.and(qCategory.merchantStore.id.eq(category.getMerchantStore().getId())))
			.orderBy(qCategory.lineage.asc(), qCategory.lineage.asc(), qCategory.depth.asc(), qDescription.language.id.desc());
		
		return query.listDistinct(qCategory);
		
	}
	
	

	@Override
	public List<Category> listByStoreAndParent(MerchantStore store, Category category) {
		//TODO join descriptions and merchantstore
		QCategory qCategory = QCategory.category;
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		if (store == null) {
			if (category == null) {
				query.from(qCategory)
					.where(qCategory.parent.isNull())
					.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			} else {
				query.from(qCategory)
					.where(qCategory.parent.eq(category))
					.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			}
		} else {
			if (category == null) {
				query.from(qCategory)
					.where(qCategory.parent.isNull()
						.and(qCategory.merchantStore.eq(store)))
					.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			} else {
				query.from(qCategory)
					.where(qCategory.parent.eq(category)
						.and(qCategory.merchantStore.eq(store)))
					.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
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
			.leftJoin(qCategory.merchantStore).fetch()
			.where(qCategory.merchantStore.id.eq(store.getId()))
			.orderBy(qCategory.sortOrder.asc(),qCategory.id.asc());
		
		return query.listDistinct(qCategory);
	}
	
	@Override
	public List<Category> listByStore(MerchantStore store, Language language) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription).fetch()
			.leftJoin(qCategory.merchantStore).fetch()
			.where((qCategory.merchantStore.id.eq(store.getId()))
			.and(qDescription.language.id.eq(language.getId())))
			.orderBy(qCategory.sortOrder.asc(),qCategory.id.asc());
		
		return query.listDistinct(qCategory);
	}
}
