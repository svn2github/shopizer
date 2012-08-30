package com.salesmanager.core.modules.cms;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PropertyType;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.nodetype.NodeTypeTemplate;
import javax.jcr.nodetype.PropertyDefinitionTemplate;

import org.apache.commons.io.IOUtils;
import org.modeshape.common.collection.Problem;
import org.modeshape.common.collection.Problems;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;
import org.modeshape.jcr.api.JcrTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.utils.CoreConfiguration;


/**
 * Manager for storing in retrieving image files from the CMS
 * This is a layer on top of JCR and ModeShape/Infinispan
 * @author csamson
 *
 */
public class CmsImageFileManagerModeShapeImpl implements ProductImagePut, ProductImageGet, ProductImageRemove {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CmsImageFileManagerModeShapeImpl.class);
	
	private static CmsImageFileManagerModeShapeImpl fileManager = null; 
	
	//get repository config
	private String repositoryFileName = "cms/shopizer-repository-config.json";
	
	
	private ModeShapeEngine engine = null;
	private RepositoryConfiguration config = null;
	
	private final static String PROPS = "props";
	private final static String MIME_TYPE = "mimeType";
	private final static String NAME = "name";
	private final static String EXTENSION = "extension";
	private final static String DEFAULT = "default";
	private final static String ALT = "alt";

	@SuppressWarnings("unchecked")
	private void initFileManager() throws Exception {
		
		engine = new ModeShapeEngine();
		engine.start();
		
        // Load the configuration for a repository via the classloader (can also use path to a file)...
        Repository repository = null;
        String repositoryName = null;
        try {
            URL url = CmsImageFileManagerModeShapeImpl.class.getClassLoader().getResource(repositoryFileName);
            config = RepositoryConfiguration.read(url);

            // Verify the configuration for the repository ...
            Problems problems = config.validate();
            if (problems.hasErrors()) {

            	while(problems.iterator().hasNext()) {
            		Problem p = problems.iterator().next();
            		LOGGER.error(p.toString());
            	}
                throw new Exception("The repository cannot be started " + problems.toString());
            }

            // Deploy the repository ...
            repository = engine.deploy(config);
            repositoryName = config.getName();
        } catch (Throwable e) {
        	LOGGER.error("Error while instantiating CmsImageFileManager",e);
            stopFileManager();
            //System.exit(-1);
            throw new Exception("The repository cannot be started ",e);

        }

        Session session = null;
        JcrTools tools = new JcrTools();
        try {
            // Get the repository
            repository = engine.getRepository(repositoryName);
            

            // Create a session ...
            session = repository.login("default");
            Workspace workspace = session.getWorkspace();

	         /***** MIXINS ****/
	
	         // Obtain the ModeShape-specific node type manager ...
	         NodeTypeManager nodeTypeManager = workspace.getNodeTypeManager();

	         NodeType nt = null;
	        	 
	         try {
	        	 
	        	 nt = nodeTypeManager.getNodeType("props");
	        	 
	         } catch(Exception e) {
	        	 //do not log
	         }
	         

	         if(nt==null) {
	
		         // Declare a mixin node type named "searchable" (with no namespace)
		         NodeTypeTemplate nodeType = nodeTypeManager.createNodeTypeTemplate();
		         nodeType.setName(PROPS);
		         nodeType.setMixin(true);

		         PropertyDefinitionTemplate property2 = nodeTypeManager.createPropertyDefinitionTemplate();
		         property2.setName(MIME_TYPE);
		         property2.setRequiredType(PropertyType.STRING);
		         nodeType.getPropertyDefinitionTemplates().add(property2);
		         
		         PropertyDefinitionTemplate property3 = nodeTypeManager.createPropertyDefinitionTemplate();
		         property3.setName(EXTENSION);
		         property3.setRequiredType(PropertyType.STRING);
		         nodeType.getPropertyDefinitionTemplates().add(property3);
		         
		         PropertyDefinitionTemplate property4 = nodeTypeManager.createPropertyDefinitionTemplate();
		         property4.setName(NAME);
		         property4.setRequiredType(PropertyType.STRING);
		         nodeType.getPropertyDefinitionTemplates().add(property4);
		         
		         PropertyDefinitionTemplate property5 = nodeTypeManager.createPropertyDefinitionTemplate();
		         property5.setName(DEFAULT);
		         property5.setRequiredType(PropertyType.BOOLEAN);
		         Value fls = session.getValueFactory().createValue(false);
		         Value[] va = new Value[1];
		         va[0] = fls;
		         property5.setDefaultValues(va);
		         nodeType.getPropertyDefinitionTemplates().add(property5);

		         // Register the custom node type
		         nodeTypeManager.registerNodeType(nodeType,false);
	         
	         }
	         
	         Node filesNode = null;   
	         Node root = session.getRootNode(); 
	         
	         try {
	 			
	        	 filesNode = root.getNode("productFiles");   

	        	 
	 		} catch (Exception e) {
	 			//do not log
	 		}
	         
	         
	         if(filesNode==null) {
	             filesNode = root.addNode("productFiles", "nt:folder");
	             session.save(); 
	         }
	         
	         NodeIterator ni = root.getNodes();
	         if(ni!=null) {
	        	 while(ni.hasNext()) {
	        		 Node n = (Node)ni.next();
	        		 LOGGER.debug("CMS has " + n.getName());
	        		 tools.printNode(n);
	        	 }
	         }
	         
	         LOGGER.debug("CMS started");

        } catch (RepositoryException e) {
        	LOGGER.error("Error while instantiating CmsImageFileManager",e);
        } catch (Exception e) {
        	LOGGER.error("Error while instantiating CmsImageFileManager",e);
        } finally {
            if (session != null) session.logout();
        }
		
		
		
	}
	
	/**
	 *Requires to stop the engine
	 *when image servlet un-deploys
	 */
	public void stopFileManager() {
		
        try {
            engine.shutdown().get();
            LOGGER.info("Stopping CMS");
            engine = null;
        } catch (Exception e) {
        	LOGGER.error("Error while stopping CmsImageFileManager",e);
        }
	}
	
	
	public static CmsImageFileManagerModeShapeImpl getInstance() {
		
		if(fileManager==null) {
			fileManager = new CmsImageFileManagerModeShapeImpl();
			try {
				fileManager.initFileManager();
			} catch (Exception e) {
				LOGGER.error("Error while instantiating CmsImageFileManager",e);
			}
		}
		
		return fileManager;
		
		
		
	}
	
	private CmsImageFileManagerModeShapeImpl(){
		
	}

	/**
	 * root
	 * 		-productFiles
	 * 				-merchantId
	 * 					     -productId
	 */

	@Override
	public void uploadProductImage(CoreConfiguration configuration, ProductImage productImage, InputContentImage contentImage)
			throws ServiceException {
		
        Repository repository = null;
        String repositoryName = null;
        Session session = null;
        Node filesNode = null;
        
        JcrTools tools = new JcrTools();

        if(engine==null) {
        	throw new ServiceException("updateProductImage has null CMS engine");
        }

        try {
        	repositoryName = config.getName();
		
        	repository = engine.getRepository(repositoryName);

        	// Create a session ...
        	session = repository.login("default");
        

	        Node root = session.getRootNode();

	        filesNode = root.getNode("productFiles");
	        
	        if(filesNode==null){
	        	throw new ServiceException("updateProductImage filesNode is null");
	        }

	        
	        Node userFiles = null;
	
	        try {
	        	userFiles = filesNode.getNode(String.valueOf(productImage.getProduct().getMerchantSore().getId()));
			} catch (Exception e) {
				//do not log
			}

			if(userFiles==null) {
				filesNode.addNode(String.valueOf(productImage.getProduct().getMerchantSore().getId()), "nt:folder");//storeId
			}
			
			Node productIdFiles = null;
			
	        try {
	        	productIdFiles = filesNode.getNode(String.valueOf(productImage.getProduct().getId()));
			} catch (Exception e) {
				//do not log
			}
			
			if(productIdFiles==null) {
				userFiles.addNode(String.valueOf(productImage.getProduct().getId()), "nt:folder");//productId
			}
			
			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append(String.valueOf(productImage.getProduct().getMerchantSore().getId())).append("/")
			.append(String.valueOf(productImage.getProduct().getId())).append("/")
			.append(contentImage.getFile().getName());
			
			LOGGER.info("Loading content node " + filePath.toString());
			LOGGER.info("Content image " + contentImage.toString());
	
	        tools.uploadFile(session, filePath.toString(), contentImage.getFile());
	
	        Node image = session.getNode(filePath.toString());
	        image.addMixin(PROPS);
	        image.setProperty(MIME_TYPE, contentImage.getImageContentType());
	        image.setProperty(NAME, productImage.getProductImage());
	        image.setProperty(DEFAULT, productImage.isDefaultImage());
	        session.save();
        } catch(Exception e) {
        	
        	throw new ServiceException(e);
	        
		} finally {
			session.logout();
		}

		
	}

	@Override
	public OutputContentImage getProductImage(ProductImage productImage) throws ServiceException {

        Repository repository = null;
        String repositoryName = null;
        Session session = null;
        
        if(engine==null) {
        	throw new ServiceException("updateProductImage has null CMS engine");
        }
        
        @SuppressWarnings("unused")
		OutputContentImage image = new OutputContentImage();

        try {
        	repositoryName = config.getName();
        	repository = engine.getRepository(repositoryName);

        	// Create a session ...
        	session = repository.login("default");
        	
        	//TODO refactor to common

        	
			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append(String.valueOf(productImage.getProduct().getMerchantSore().getId())).append("/")
			.append(String.valueOf(productImage.getProduct().getId())).append("/")
			.append(productImage.getProductImage());

         	
			Node img = null;
			
			try {
				img = session.getNode(filePath.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			if(img!=null) {

	         	Node fileNode = img.getNode("jcr:content");
	
	            String contentType = img.getProperty(MIME_TYPE).getString();
	
	            
	            Binary content = fileNode.getProperty("jcr:data").getBinary();
	            InputStream in = content.getStream();
	            ByteArrayOutputStream out = new ByteArrayOutputStream(); 
	            IOUtils.copy(in, out);
	            image.setImageContentType(contentType);
	            image.setImage(out);
	            image.setDefaultImage(productImage.isDefaultImage());
	            content.dispose();
            
			}

        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			session.logout();
		}

         return image;
	}
	
	public List<OutputContentImage> getImages(MerchantStore store) throws ServiceException {
		
		
        Repository repository = null;
        String repositoryName = null;
        Session session = null;
        
        if(engine==null) {
        	throw new ServiceException("updateProductImage has null CMS engine");
        }
        
		List<OutputContentImage> images = new ArrayList<OutputContentImage>();

        try {
        	repositoryName = config.getName();
        	repository = engine.getRepository(repositoryName);

        	// Create a session ...
        	session = repository.login("default");

			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append(String.valueOf(store.getId())).append("/");

	        
			images = this.getContentImages(session, filePath.toString());
			
        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			session.logout();
		}
		
		return images;

	}
	
	
	@Override
	public List<OutputContentImage> getImages(Product product)
			throws ServiceException {

        Repository repository = null;
        String repositoryName = null;
        Session session = null;
        
        if(engine==null) {
        	throw new ServiceException("updateProductImage has null CMS engine");
        }

		List<OutputContentImage> images = new ArrayList<OutputContentImage>();

        try {
        	repositoryName = config.getName();
        	repository = engine.getRepository(repositoryName);

        	// Create a session ...
        	session = repository.login("default");

			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append(String.valueOf(product.getMerchantSore().getId())).append("/")
			.append(String.valueOf(product.getId())).append("/");

	        
			images = this.getContentImages(session, filePath.toString());
			
        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			session.logout();
		}
		
		return images;
	}
	
	
	private List<OutputContentImage> getContentImages(Session session, String path) throws Exception {
		JcrTools tool = new JcrTools();

		List<OutputContentImage> images = new ArrayList<OutputContentImage>();
        
		Node files = null;
		
		try {
			files = session.getNode(path);
		} catch (Exception e) {
		}
		

         if(files!=null) {
         	
         	NodeIterator niter = files.getNodes();
         	while(niter.hasNext()) {
         		OutputContentImage c = new OutputContentImage();
         		Node n = (Node)niter.next();
         		
         		tool.printNode(n);
         		
         		//getting content
         		Node nContent = n.getNode("jcr:content");
         		
         		
                String name = n.getProperty(NAME).getString();
                String contentType = n.getProperty(MIME_TYPE).getString();
                boolean defaultImage = n.getProperty(DEFAULT).getBoolean();
                
                Binary content = nContent.getProperty("jcr:data").getBinary();
                InputStream in = content.getStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream(); 
                IOUtils.copy(in, out);
                c.setImage(out);
                c.setImageContentType(contentType);
                c.setDefaultImage(defaultImage);
                c.setImageName(name);
                images.add(c);
                
                content.dispose();
         	}
         }
		
		return images;
		
	}

	public String getRepositoryFileName() {
		return repositoryFileName;
	}

	public void setRepositoryFileName(String repositoryFileName) {
		this.repositoryFileName = repositoryFileName;
	}

	@Override
	public void removeImages(MerchantStore store) throws ServiceException {
        Repository repository = null;
        String repositoryName = null;
        Session session = null;
        
        if(engine==null) {
        	throw new ServiceException("updateProductImage has null CMS engine");
        }

        try {
        	repositoryName = config.getName();
        	repository = engine.getRepository(repositoryName);

        	// Create a session ...
        	session = repository.login("default");
        	
			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append(String.valueOf(store.getId())).append("/");

			Node imgs = null;
			
			try {
				imgs = session.getNode(filePath.toString());
			} catch (Exception e) {
				//do not log
			}
			
			
			
			if(imgs!=null) {

				imgs.remove();
            
			}

        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			session.logout();
		}

		
		
		
	}

	@Override
	public void removeProductImage(ProductImage productImage)
			throws ServiceException {
		
		
        Repository repository = null;
        String repositoryName = null;
        Session session = null;
        
        if(engine==null) {
        	throw new ServiceException("updateProductImage has null CMS engine");
        }

        try {
        	repositoryName = config.getName();
        	repository = engine.getRepository(repositoryName);

        	// Create a session ...
        	session = repository.login("default");
        	
			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append(String.valueOf(productImage.getProduct().getMerchantSore().getId())).append("/")
			.append(String.valueOf(productImage.getProduct().getId())).append("/")
			.append(productImage.getProductImage());


			
			Node img = null;
			
			try {
				img = session.getNode(filePath.toString());
			} catch (Exception e) {
				//do not log
			}
			
			
			
			if(img!=null) {

				img.remove();
            
			}

        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			session.logout();
		}

		
		
	}

	@Override
	public void removeProductImages(Product product) throws ServiceException {
        Repository repository = null;
        String repositoryName = null;
        Session session = null;
        
        if(engine==null) {
        	throw new ServiceException("updateProductImage has null CMS engine");
        }

        try {
        	repositoryName = config.getName();
        	repository = engine.getRepository(repositoryName);

        	// Create a session ...
        	session = repository.login("default");
        	

			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append(String.valueOf(product.getMerchantSore().getId())).append("/")
			.append(String.valueOf(product.getId())).append("/");


			
			Node imgs = null;
			
			try {
				imgs = session.getNode(filePath.toString());
			} catch (Exception e) {
				// do not log
			}
			
			
			
			if(imgs!=null) {

				imgs.remove();
            
			}

        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			session.logout();
		}

		
	}


	
	

}
