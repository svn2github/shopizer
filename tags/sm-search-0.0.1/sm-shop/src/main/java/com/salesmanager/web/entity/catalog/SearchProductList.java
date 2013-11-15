package com.salesmanager.web.entity.catalog;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representing the results of a search query
 * @author Carl Samson
 *
 */
public class SearchProductList extends ProductList {
	

	private static final long serialVersionUID = 1L;
	private List<Category> categoryFacets = new ArrayList<Category>();
	public void setCategoryFacets(List<Category> categoryFacets) {
		this.categoryFacets = categoryFacets;
	}
	public List<Category> getCategoryFacets() {
		return categoryFacets;
	}

}
