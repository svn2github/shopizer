package com.salesmanager.core.business.content.service;

import java.io.InputStream;
import java.util.List;

import com.salesmanager.core.business.content.model.content.Content;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface ContentService extends SalesManagerEntityService<Long, Content> {
	
	
	public List<Content> listByType(String contentType, MerchantStore store, Language language) throws ServiceException;
	public List<Content> listByType(List<String> contentType, MerchantStore store, Language language) throws ServiceException;
	Content getByCode(String code, MerchantStore store)
			throws ServiceException;
	void saveOrUpdate(Content content) throws ServiceException;
	Content getByCode(String code, MerchantStore store, Language language)
			throws ServiceException;
	void addContentImage(MerchantStore store, InputStream inputStream)
			throws ServiceException;



}
