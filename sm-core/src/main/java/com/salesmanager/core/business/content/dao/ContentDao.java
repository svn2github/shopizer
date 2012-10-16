package com.salesmanager.core.business.content.dao;

import java.util.List;

import com.salesmanager.core.business.content.model.content.Content;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface ContentDao extends SalesManagerEntityDao<Long, Content> {

	List<Content> listByType(String contentType, MerchantStore store,
			Language language) throws ServiceException;

	List<Content> listByType(List<String> contentType, MerchantStore store,
			Language language) throws ServiceException;

	Content getByCode(String code, MerchantStore store)
			throws ServiceException;



}
