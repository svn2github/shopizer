package com.salesmanager.core.business.catalog.category.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.QCategory;
import com.salesmanager.core.business.catalog.category.model.QCategoryDescription;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("categoryDao")
public class CategoryDaoImpl extends SalesManagerEntityDaoImpl<Long, Category> implements CategoryDao {

	public CategoryDaoImpl() {
		super();
	}

	@Override
	public List<Category> listBySeUrl(String seUrl) {
		QCategory qCategory = QCategory.category;
		QCategoryDescription qDescription = QCategoryDescription.categoryDescription;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qCategory)
			.leftJoin(qCategory.descriptions, qDescription)
			.where(qDescription.seUrl.like(seUrl))
			.orderBy(qDescription._super.title.desc(), qDescription._super.name.desc());
		
		return query.list(qCategory);
	}

	@Override
	public List<Category> listByByParent(Category category) {
		QCategory qCategory = QCategory.category;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		if (category == null) {
			query.from(qCategory)
			.where(qCategory.parent.isNull())
			.orderBy(qCategory.id.desc());
		} else {
			query.from(qCategory)
				.where(qCategory.parent.eq(category))
				.orderBy(qCategory.id.desc());
		}
		
		return query.list(qCategory);
	}
}
