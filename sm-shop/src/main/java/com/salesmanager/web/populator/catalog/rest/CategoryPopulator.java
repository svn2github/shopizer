package com.salesmanager.web.populator.catalog.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.catalog.rest.category.CategoryDescription;
import com.salesmanager.web.entity.catalog.rest.category.CategoryEntity;


public class CategoryPopulator extends AbstractDataPopulator<Category, CategoryEntity> {
	
	
	private CategoryService categoryService;
	private LanguageService languageService;


	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	public LanguageService getLanguageService() {
		return languageService;
	}

	/**
	 * Populate from DB entity
	 */
	@Override
	public CategoryEntity populateFromEntity(Category source,
			CategoryEntity target, MerchantStore store, Language language)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Populate DB entity
	 */
	@Override
	public Category populateToEntity(Category target, CategoryEntity source,
			MerchantStore store) throws ServiceException {
		Validate.notNull(categoryService, "Requires to set CategoryService");
		Validate.notNull(languageService, "Requires to set LanguageService");
		
		target.setMerchantStore(store);
		target.setCode(source.getCode());
		target.setSortOrder(source.getSortOrder());
		target.setVisible(source.isVisible());
		
		//get parent
		
		if(source.getParent()==null) {

			target.setParent(null);
			target.setLineage("/");
			target.setDepth(0);

		} else {
			
			Category parent = categoryService.getById(source.getParent());
			if(parent.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				throw new ServiceException("Store id does not belong to specified parent id");
			}
			
			target.setParent(parent);
			
			String lineage = parent.getLineage();
			int depth = parent.getDepth();

			target.setDepth(depth+1);
			target.setLineage(new StringBuilder().append(lineage).append(parent.getId()).append("/").toString());
			
			
			
		}
		

		
		if(!CollectionUtils.isEmpty(source.getDescriptions())) {
			List<com.salesmanager.core.business.catalog.category.model.CategoryDescription> descriptions = new ArrayList<com.salesmanager.core.business.catalog.category.model.CategoryDescription>();
			for(CategoryDescription description : source.getDescriptions()) {
				com.salesmanager.core.business.catalog.category.model.CategoryDescription desc = new com.salesmanager.core.business.catalog.category.model.CategoryDescription();
				desc.setCategory(target);
				desc.setCategoryHighlight(description.getHighlights());
				desc.setDescription(description.getDescription());
				desc.setName(description.getName());
				desc.setMetatagDescription(description.getMetaDescription());
				desc.setMetatagTitle(description.getTitle());
				desc.setSeUrl(description.getFriendlyUrl());
				Language lang = languageService.getByCode(description.getLanguage());
				if(lang==null) {
					throw new ServiceException("Language is null for code " + description.getLanguage() + " use language ISO code [en, fr ...]");
				}
				desc.setLanguage(lang);
				descriptions.add(desc);
			}
			target.setDescriptions(descriptions);
		}
	
		
		return target;
	}

}
