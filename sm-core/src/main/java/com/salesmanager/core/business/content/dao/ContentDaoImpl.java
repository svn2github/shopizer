package com.salesmanager.core.business.content.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.content.model.content.Content;
import com.salesmanager.core.business.content.model.content.ContentType;
import com.salesmanager.core.business.content.model.content.QContent;
import com.salesmanager.core.business.content.model.content.QContentDescription;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Repository("contentDao")
public class ContentDaoImpl extends SalesManagerEntityDaoImpl<Long, Content> implements ContentDao {

	public ContentDaoImpl() {
		super();
	}
	
	@Override
	public List<Content> listByType(ContentType contentType, MerchantStore store, Language language) throws ServiceException {

		QContent qContent = QContent.content;
		QContentDescription qContentDescription = QContentDescription.contentDescription;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qContent)
			.leftJoin(qContent.descriptions, qContentDescription).fetch()
			.leftJoin(qContent.merchantStore).fetch()
			.where(qContentDescription.language.id.eq(language.getId())
			.and(qContent.merchantStore.id.eq(store.getId()))
			.and(qContent.contentType.eq(contentType.name()))
			);
		
		List<Content> contents = query.list(qContent);
		
		return contents;
	}
	
	@Override
	public List<Content> listByType(ContentType contentType, MerchantStore store) throws ServiceException {

		QContent qContent = QContent.content;
		QContentDescription qContentDescription = QContentDescription.contentDescription;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qContent)
			.leftJoin(qContent.descriptions, qContentDescription).fetch()
			.leftJoin(qContent.merchantStore).fetch()
			.where(qContent.merchantStore.id.eq(store.getId())
			.and(qContent.contentType.eq(contentType.name()))
			);
		
		List<Content> contents = query.list(qContent);
		
		return contents;
	}
	
	
	@Override
	public List<Content> listByType(List<String> contentType, MerchantStore store, Language language) throws ServiceException {

		QContent qContent = QContent.content;
		QContentDescription qContentDescription = QContentDescription.contentDescription;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qContent)
			.leftJoin(qContent.descriptions, qContentDescription).fetch()
			.leftJoin(qContent.merchantStore).fetch()
			.where(qContentDescription.language.id.eq(language.getId())
			.and(qContent.merchantStore.id.eq(store.getId()))
			.and(qContent.contentType.in(contentType))
			);
		
		List<Content> contents = query.list(qContent);
		
		return contents;
	}
	
	@Override
	public List<Content> listByType(List<String> contentType, MerchantStore store) throws ServiceException {

		QContent qContent = QContent.content;
		QContentDescription qContentDescription = QContentDescription.contentDescription;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qContent)
			.leftJoin(qContent.descriptions, qContentDescription).fetch()
			.leftJoin(qContent.merchantStore).fetch()
			.where(qContent.merchantStore.id.eq(store.getId())
			.and(qContent.contentType.in(contentType))
			);
		
		List<Content> contents = query.list(qContent);
		
		return contents;
	}
	
	@Override
	public Content getByCode(String code, MerchantStore store) throws ServiceException {

		QContent qContent = QContent.content;
		QContentDescription qContentDescription = QContentDescription.contentDescription;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qContent)
			.leftJoin(qContent.descriptions, qContentDescription).fetch()
			.leftJoin(qContent.merchantStore).fetch()
			.where(qContent.merchantStore.id.eq(store.getId())
			.and(qContent.code.eq(code))
			);
		
		Content content = query.singleResult(qContent);
		
		return content;
	}
	
	@Override
	public Content getByCode(String code, MerchantStore store, Language language) throws ServiceException {

		QContent qContent = QContent.content;
		QContentDescription qContentDescription = QContentDescription.contentDescription;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qContent)
			.leftJoin(qContent.descriptions, qContentDescription).fetch()
			.leftJoin(qContent.merchantStore).fetch()
			.where(qContentDescription.language.id.eq(language.getId())
			.and(qContent.merchantStore.id.eq(store.getId())
			.and(qContent.code.eq(code)))
			);
		
		Content content = query.singleResult(qContent);
		
		return content;
	}
	

}
