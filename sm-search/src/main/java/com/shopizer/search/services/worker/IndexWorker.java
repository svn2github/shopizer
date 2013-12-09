package com.shopizer.search.services.worker;


public interface IndexWorker {
	
	public void init();
	public void execute(String json, String collection, String object, String id, ExecutionContext context) throws Exception;

}
