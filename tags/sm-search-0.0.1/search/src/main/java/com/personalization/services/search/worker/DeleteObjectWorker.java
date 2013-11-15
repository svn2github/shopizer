package com.personalization.services.search.worker;

import com.personalization.utils.SearchClient;

/**
 * Deletes an object from the index
 * @author Carl Samson
 *
 */
public interface DeleteObjectWorker {
	
	public void deleteObject(SearchClient client,String collection, String object, String id, ExecutionContext context) throws Exception;

}
