package com.salesmanager.core.business.reference.language.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.reference.language.dao.LanguageDao;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.model.Language_;

@Service("languageService")
public class LanguageServiceImpl extends SalesManagerEntityServiceImpl<Integer, Language>
	implements LanguageService {
	
	@Autowired
	public LanguageServiceImpl(LanguageDao languageDao) {
		super(languageDao);
	}
	
	@Override
	public Language getByCode(String code) throws ServiceException {
		return getByField(Language_.code, code);
	}

}
