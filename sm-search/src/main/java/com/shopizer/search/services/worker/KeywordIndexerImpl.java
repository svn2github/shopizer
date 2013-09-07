package com.shopizer.search.services.worker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.shopizer.search.services.IndexKeywordRequest;
import com.shopizer.search.services.field.BooleanField;
import com.shopizer.search.services.field.DoubleField;
import com.shopizer.search.services.field.Field;
import com.shopizer.search.services.field.IntegerField;
import com.shopizer.search.services.field.ListField;
import com.shopizer.search.services.field.StringField;
import com.shopizer.search.services.impl.SearchServiceImpl;
import com.shopizer.search.utils.CustomIndexConfiguration;
import com.shopizer.search.utils.CustomIndexFieldConfiguration;
import com.shopizer.search.utils.DateUtil;
import com.shopizer.search.utils.DynamicIndexNameUtil;
import com.shopizer.search.utils.SearchClient;


public class KeywordIndexerImpl implements IndexWorker {
	
	
	private static Logger log = Logger.getLogger(KeywordIndexerImpl.class);
	
	//private static Map keyWordsToIndex = null;
	//private static Map fieldsToAdd = null;
	
	private static boolean init = false;
	
	//private static String collectionName = null;
	
	private List<CustomIndexConfiguration> indexConfigurations = null;


	public List<CustomIndexConfiguration> getIndexConfigurations() {
		return indexConfigurations;
	}

	public void setIndexConfigurations(
			List<CustomIndexConfiguration> indexConfigurations) {
		this.indexConfigurations = indexConfigurations;
	}

	private static Map<String,CustomIndexConfiguration>  indexConfigurationsMap = null;

	private synchronized void init() {
		
		
		try {
			
			if(indexConfigurations!=null) {
				
				for(Object o : indexConfigurations) {
					
					CustomIndexConfiguration ic = (CustomIndexConfiguration)o;
					String key = ic.getCreateOnIndexName();
					if(indexConfigurationsMap==null) {
						indexConfigurationsMap = new HashMap();
					}
					if(StringUtils.isBlank(key)) {
						log.error("*********************************************");
						log.error("Require property createOnIndexName in keyword indexer");
						log.error("*********************************************");
						continue;
					}
					indexConfigurationsMap.put(key, ic);

				}
				
			}
			
			init = true;
			
		} catch (Exception e) {
			log.error("*********************************************");
			log.error(e);
			log.error("*********************************************");
		}
		
		
		
/*		try {
			//PropertiesConfiguration config = new PropertiesConfiguration("personalization.properties");
			//config.setReloadingStrategy(new FileChangedReloadingStrategy());
			Config config = Config.getInstance();
			Configuration configuration = config.getConfiguration();
			
			//indexes
			List kw = configuration.getList("index.keywords.field");
			if(kw!=null && kw.size()>0) {
				
				for(Object o : kw) {
					
					String fieldName = (String)o;
					
					String fieldType = configuration.getString("index.keywords.type." + fieldName);
					if(fieldType==null) {
						fieldType = "String";
					}
					keyWordsToIndex = new HashMap();
					keyWordsToIndex.put(fieldName, fieldType);
				}
				
			}
			
			//fields
			List fields = configuration.getList("filter.keywords.field");
			if(fields!=null && fields.size()>0) {
				
				for(Object o : fields) {
					
					String fieldName = (String)o;
					
					String fieldType = configuration.getString("filter.keywords.type." + fieldName);
					if(fieldType==null) {
						fieldType = "String";
					}
					fieldsToAdd = new HashMap();
					fieldsToAdd.put(fieldName, fieldType);
				}
				
			}
			
			collectionName = configuration.getString("index.keywords.collection.name");

		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}

	public void execute(SearchClient client, String json, String collection, String object, String id, ExecutionContext context)
			throws Exception {
		
			if(!init) {
				init();
			}
		
			//new logic
			if(indexConfigurationsMap!=null && indexConfigurationsMap.containsKey(object)) {
				
				
				//get json
				Map indexData = (Map)context.getObject("indexData");
				
				SearchServiceImpl service = new SearchServiceImpl(client);
				
				CustomIndexConfiguration conf = indexConfigurationsMap.get(object);
				
				
				String indexName = DynamicIndexNameUtil.getIndexName(conf.getCollectionName(),indexData);
				StringTokenizer t = new StringTokenizer(conf.getCollectionName(),"_");
				int countToken = t.countTokens();
				if(countToken>1) {
					
					StringBuilder iName = new StringBuilder();
					int count = 1;
					while(t.hasMoreTokens()) {
						
						String elem = t.nextToken();
						iName.append(DynamicIndexNameUtil.getIndexName(elem,indexData));
						if(count<countToken) {
							iName.append("_");
						}
						count++;
					}
					indexName = iName.toString();
				} 
				
				//get fields to index
				List fields = conf.getFields();
				if(fields!=null) {
					
					List k = null;
					for(Object o : fields) {
						
						
						CustomIndexFieldConfiguration cifc = (CustomIndexFieldConfiguration)o;
						
						String fieldName = cifc.getFieldName();

						if(fieldName.trim().toLowerCase().equals("id")) {//we have our own id
							continue;
						}
						

						String fieldType = cifc.getFieldType();
						
						
						if(!StringUtils.isBlank(fieldType)) {
							if(fieldType.equals("List")) {

									try {
										List keyWords = (List)indexData.get(fieldName);
										
										if(keyWords!=null) {
										
											if(k==null) {
												k = new ArrayList();
											}
											k.addAll(keyWords);
										}
										
									} catch (Exception e) {//might have one instead of a list
										String keyword = (String)indexData.get(fieldName);
										
										if(keyword!=null) {
										
											if(k==null) {
												k = new ArrayList();
											}
											k.add(keyword);
									
										}
									}

							} else {//String
								
									String keyword = (String)indexData.get(fieldName);
								
									if(keyword!=null) {
									
										if(k==null) {
											k = new ArrayList();
										}
										k.add(keyword);
								
									}
							}
						}//end field type
						
				}//end for


				if(k!=null) {
					
					Collection bulks = new ArrayList();
					
					StringBuilder kw = new StringBuilder();
					int i = 1;
					for(Object o : k) {
	
						
						IndexKeywordRequest kr = new IndexKeywordRequest();
						
						//id is the key trimed in lower case
						String value = (String)o;
						
						if(StringUtils.isBlank(value)) {
							continue;
						}
						
						//kr.setId(value.toLowerCase().trim());
						//get id from original object
						
						String _id = (String)indexData.get("id");
						kr.setId(_id);
						kr.setKey(value);
						
						//check fields to add
						
						if(conf.getFilters()!=null && conf.getFilters().size()>0) {
							
							for(Object oo : conf.getFilters()) {
								
								CustomIndexFieldConfiguration filter = (CustomIndexFieldConfiguration)oo;
								
								String fieldName = filter.getFieldName();
								
								String fieldType = filter.getFieldType();
								
								Field f = null;
								
								if(fieldType.equals("List")) {
									List ooo = (List)indexData.get(fieldName);
									f = new ListField();
									f.setValue(ooo);
									f.setName(fieldName);
									kr.getFilters().add(f);
									
								} else if(fieldType.equals("Boolean")) {
									
									String s = (String)indexData.get(fieldName);
									Boolean ooo = new Boolean(s);
									f = new BooleanField();
									f.setValue(ooo);
									f.setName(fieldName);
									kr.getFilters().add(f);
									
								} else if(fieldType.equals("Integer")) {
									
									String s = (String)indexData.get(fieldName);
									Integer ooo = Integer.parseInt(s);
									f = new IntegerField();
									f.setValue(ooo);
									f.setName(fieldName);
									kr.getFilters().add(f);
									
								} else if(fieldType.equals("Double")) {
									
									String s = (String)indexData.get(fieldName);
									Double ooo = Double.parseDouble(s);
									f = new DoubleField();
									f.setValue(ooo);
									f.setName(fieldName);
									kr.getFilters().add(f);
									
								} else if(fieldType.equals("Date")) {
									
									String d = (String)indexData.get(fieldName);
									//parse date
									try {
										Date dt = DateUtil.formatDate(d);
										f = new DoubleField();
										f.setValue(dt);
										f.setName(fieldName);
										kr.getFilters().add(f);
									} catch (Exception e) {
										log.error("Invalid date format " + d);
									}

									
								} else {
									
									String ooo = (String)indexData.get(fieldName);
									f = new StringField();
									f.setValue(ooo.toLowerCase());
									f.setName(fieldName);
									kr.getFilters().add(f);
									
								}
							}
							
							
						}
						
						
						
						bulks.add(kr);
						
					}

					service.bulkIndexKeywords(bulks, indexName, "keyword");
					
				}
				
				
				
			}
				
			}
		
		
		
		
/*			if(!StringUtils.isBlank(collectionName) && keyWordsToIndex!=null && keyWordsToIndex.size()>0) {
		
				SearchServiceImpl service = new SearchServiceImpl();
	
				
				ObjectMapper mapper = new ObjectMapper();
				Map<String,Object> indexData = mapper.readValue(json, Map.class);
				

				List k = null;
				
				String indexName = getIndexName(collectionName,indexData);
				//tokenize ?
				StringTokenizer t = new StringTokenizer(collectionName,"_");
				if(t.countTokens()>1) {
					
					StringBuilder iName = new StringBuilder();
					int count = 1;
					while(t.hasMoreTokens()) {
						
						
						String elem = t.nextToken();
						iName.append(getIndexName(elem,indexData));
						if(count<t.countTokens()) {
							iName.append("_");
						}
					}
					
					indexName = iName.toString();

				} 
				
				
				for(Object o : keyWordsToIndex.keySet()) {
					
					String fieldName = (String)o;

					
					if(fieldName.trim().toLowerCase().equals("id")) {//we have our own id
						continue;
					}
					

					
					String fieldType = (String)keyWordsToIndex.get(fieldName);
					if(!StringUtils.isBlank(fieldType)) {
						if(fieldType.equals("List")) {

								try {
									List keyWords = (List)indexData.get(fieldName);
									
									if(keyWords!=null) {
									
										if(k==null) {
											k = new ArrayList();
										}
										k.addAll(keyWords);
									
									}
									
								} catch (Exception e) {//might have one instead of a list
									String keyword = (String)indexData.get(fieldName);
									
									if(keyword!=null) {
									
										if(k==null) {
											k = new ArrayList();
										}
										k.add(keyword);
								
									}
								}

						} else {//String
							
								String keyword = (String)indexData.get(fieldName);
							
								if(keyword!=null) {
								
									if(k==null) {
										k = new ArrayList();
									}
									k.add(keyword);
							
								}
							
						}
						
					}
				}//end for
*/					
					

				
	
						
				//}
			
			//}


	}
	

	

	
/*	public static List<IndexConfiguration> getIndexConfigurations() {
		return indexConfigurations;
	}

	public static void setIndexConfigurations(
			List<IndexConfiguration> indexConfigurations) {
		KeywordIndexerImpl.indexConfigurations = indexConfigurations;
	}
	
	public static List<CustomIndexFieldConfiguration> getIndexFieldConfigurations() {
		return indexFieldConfigurations;
	}

	public static void setIndexFieldConfigurations(
			List<CustomIndexFieldConfiguration> indexFieldConfigurations) {
		KeywordIndexerImpl.indexFieldConfigurations = indexFieldConfigurations;
	}*/

}
