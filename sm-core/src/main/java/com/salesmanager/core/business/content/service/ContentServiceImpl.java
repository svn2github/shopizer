package com.salesmanager.core.business.content.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.content.dao.ContentDao;
import com.salesmanager.core.business.content.model.content.Content;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.modules.cms.content.ContentFileManager;

@Service("contentService")
public class ContentServiceImpl extends SalesManagerEntityServiceImpl<Long, Content> implements ContentService {
	
	private ContentDao contentDao;
	
	@Autowired
	ContentFileManager contentFileManager;
	

	
	@Autowired
	public ContentServiceImpl(ContentDao contentDao) {
		super(contentDao);
		
		this.contentDao = contentDao;
	}



	@Override
	public List<Content> listByType(String contentType, MerchantStore store, Language language) throws ServiceException {

		return contentDao.listByType(contentType, store, language);
	}



	@Override
	public List<Content> listByType(List<String> contentType, MerchantStore store, Language language)
			throws ServiceException {

		return contentDao.listByType(contentType, store, language);
	}
	
	@Override
	public Content getByCode(String code, MerchantStore store)
	throws ServiceException {
		
		return contentDao.getByCode(code, store);
		
	}
	
	@Override
	public void saveOrUpdate(Content content) throws ServiceException {
		
		
		//save or update (persist and attach entities
		if(content.getId()!=null && content.getId()>0) {
			super.update(content);
		} else {
			super.save(content);
		}
		
	}
	
	@Override
	public Content getByCode(String code, MerchantStore store, Language language)
			throws ServiceException {
		return contentDao.getByCode(code, store, language);
	}
	
	@Override
	public void addContentImage(MerchantStore store, InputStream inputStream) throws ServiceException {
		
		//TODO look at ProductImageServiceImpl to create an InputContentImage from the stream
		//and invoke
		
		//contentFileManager.addImage(store, image)
		
	}
	


}
