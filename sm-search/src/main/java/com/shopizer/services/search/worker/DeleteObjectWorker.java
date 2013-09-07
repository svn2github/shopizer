package com.shopizer.services.search.worker;

import com.shopizer.search.utils.SearchClient;

/**
 * Deletes an object from the index
 * @author Carl Samson
 *
 */
public interface DeleteObjectWorker {
	
	public void deleteObject(SearchClient client,String collection, String object, String id, ExecutionContext context) throws Exception;

}
