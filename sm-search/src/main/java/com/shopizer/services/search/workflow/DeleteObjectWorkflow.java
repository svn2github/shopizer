package com.shopizer.services.search.workflow;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.shopizer.services.search.SearchService;
import com.shopizer.services.search.worker.DeleteObjectWorker;
import com.shopizer.services.search.worker.ExecutionContext;
import com.shopizer.services.search.worker.IndexWorker;
import com.shopizer.services.search.worker.SearchWorker;

@Component
public class DeleteObjectWorkflow extends Workflow{
	
	private static Logger log = Logger.getLogger(DeleteObjectWorkflow.class);
	
	private List deleteObjectWorkflow;


	public List getDeleteObjectWorkflow() {
		return deleteObjectWorkflow;
	}


	public void setDeleteObjectWorkflow(List deleteObjectWorkflow) {
		this.deleteObjectWorkflow = deleteObjectWorkflow;
	}


	public void deleteObject(String collection, String object, String id) throws Exception {

		
		if(deleteObjectWorkflow!=null) {
			ExecutionContext context = new ExecutionContext();
			for(Object o : deleteObjectWorkflow) {
				DeleteObjectWorker iw = (DeleteObjectWorker)o;
				iw.deleteObject(super.getSearchClient(),collection, object, id, context);
			}
		}
	}

}
