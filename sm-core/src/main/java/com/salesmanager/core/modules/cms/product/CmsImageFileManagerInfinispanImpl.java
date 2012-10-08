package com.salesmanager.core.modules.cms.product;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.infinispan.tree.Fqn;
import org.infinispan.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.content.model.image.InputContentImage;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.modules.cms.common.CmsFileManagerInfinispan;
import com.salesmanager.core.utils.CoreConfiguration;


/**
 * Manager for storing in retrieving image files from the CMS
 * This is a layer on top of Infinispan
 * https://docs.jboss.org/author/display/ISPN/Tree+API+Module
 * @author csamson
 *
 */
public class CmsImageFileManagerInfinispanImpl extends CmsFileManagerInfinispan implements ProductImagePut, ProductImageGet, ProductImageRemove {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CmsImageFileManagerInfinispanImpl.class);
	
	private static CmsImageFileManagerInfinispanImpl fileManager = null; 
	

	
	/**
	 *Requires to stop the engine
	 *when image servlet un-deploys
	 */
	public void stopFileManager() {
		
        try {
        	manager.stop();
            LOGGER.info("Stopping CMS");
        } catch (Exception e) {
        	LOGGER.error("Error while stopping CmsImageFileManager",e);
        }
	}
	
	
	public static CmsImageFileManagerInfinispanImpl getInstance() {
		
		if(fileManager==null) {
			fileManager = new CmsImageFileManagerInfinispanImpl();
			try {
				fileManager.initFileManager();
			} catch (Exception e) {
				LOGGER.error("Error while instantiating CmsImageFileManager",e);
			}
		}
		
		return fileManager;
		
		
		
	}
	
	private CmsImageFileManagerInfinispanImpl(){
		
	}

	/**
	 * root
	 * 		-productFiles
	 * 				-merchant-id
	 * 					     -product-id
	 * 								-<IMAGE NAME>
	 */

	@SuppressWarnings("unchecked")
	@Override
	public void uploadProductImage(CoreConfiguration configuration, ProductImage productImage, InputContentImage contentImage)
			throws ServiceException {
		
        if(treeCache==null) {
        	throw new ServiceException("CmsImageFileManagerInfinispan has a null treeCache");
        }
        InputStream input = null;
        ByteArrayOutputStream output = null;
		try {
        	

			StringBuilder merchantPath = new StringBuilder();
			merchantPath.append("/productFiles/")
			.append("merchant-").append(String.valueOf(productImage.getProduct().getMerchantStore().getId()));

			Fqn merchantFiles = Fqn.fromString(merchantPath.toString());
			
			Node<String, Object> merchantFilesTree = treeCache.getRoot().getChild(merchantFiles);
			
			if(merchantFilesTree==null) {
				treeCache.getRoot().addChild(merchantFiles);
				merchantFilesTree = treeCache.getRoot().getChild(merchantFiles);
			}
			
			//merchantPath.append("/").append(String.valueOf(productImage.getProduct().getId()));
			
			Fqn productFiles = Fqn.fromString("product-"+ String.valueOf(productImage.getProduct().getId()));
			
			Node<String, Object> productFilesTree = merchantFilesTree.getChild(productFiles);
			
			if(productFilesTree==null) {
				merchantFilesTree.addChild(productFiles);
				productFilesTree = merchantFilesTree.getChild(productFiles);
			}
			
            input = new BufferedInputStream(new FileInputStream(contentImage.getFile()));
            output = new ByteArrayOutputStream(); 
            IOUtils.copy(input, output);

            byte[] imageBytes = output.toByteArray();
            
            productFilesTree.put(contentImage.getImageName(), imageBytes);

	
        } catch(Exception e) {
        	
        	throw new ServiceException(e);
	        
		} finally {
			
			if(input!=null) {
				try {
					input.close();
				} catch (Exception ignore) {}
			}
			
			if(output!=null) {
				try {
					output.close();
				} catch (Exception ignore) {}
			}
			
		}

		
	}

	@SuppressWarnings("unchecked")
	@Override
	public OutputContentImage getProductImage(ProductImage productImage) throws ServiceException {

        if(treeCache==null) {
        	throw new ServiceException("CmsImageFileManagerInfinispan has a null treeCache");
        }
        InputStream input = null;
		OutputContentImage contentImage = new OutputContentImage();
		try {
        	
			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append("merchant-").append(String.valueOf(productImage.getProduct().getMerchantStore().getId())).append("/")
			.append("product-").append(String.valueOf(productImage.getProduct().getId()));
			
			Fqn productFiles = Fqn.fromString(filePath.toString());
			
			Node<String, Object> productFilesTree = treeCache.getRoot().getChild(productFiles);
			
			if(productFilesTree==null) {
				return null;
			}
			
			byte[] imageBytes = (byte[])productFilesTree.get(productImage.getProductImage());
			
			if(imageBytes==null) {
				return null;
			}
			
			input = new ByteArrayInputStream(imageBytes);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			IOUtils.copy(input, output);
			
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			String contentType = fileNameMap.getContentTypeFor(productImage.getProductImage());
			
			contentImage.setImage(output);
			contentImage.setImageContentType(contentType);
			contentImage.setImageName(productImage.getProductImage());
			

        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			if(input!=null) {
				try {
					input.close();
				} catch (Exception ignore) {}
			}
		}
		
		return contentImage;

	}
	
	@SuppressWarnings("unchecked")
	public List<OutputContentImage> getImages(MerchantStore store) throws ServiceException {

        if(treeCache==null) {
        	throw new ServiceException("CmsImageFileManagerInfinispan has a null treeCache");
        }
		List<OutputContentImage> images = new ArrayList<OutputContentImage>();
		FileNameMap fileNameMap = URLConnection.getFileNameMap();

        try {


			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append("mechant-").append(String.valueOf(store.getId()));
			
			Fqn merchantFiles = Fqn.fromString(filePath.toString());
			
			Node<String, Object> merchantFilesTree = treeCache.getRoot().getChild(merchantFiles);
			
			if(merchantFilesTree==null) {
				return null;
			}
			
			Set<Node<String, Object>> childNodes = merchantFilesTree.getChildren();
			if(childNodes!=null) {
				for(@SuppressWarnings("rawtypes") Node node : childNodes) {//productId
					Set<String> names = node.getChildrenNames();//imageNames
					if(names!=null && names.size()>0) {
						for(String name : names) {
							OutputContentImage contentImage = new OutputContentImage();
							byte[] imageBytes = (byte[])node.get(name);

							InputStream input = new ByteArrayInputStream(imageBytes);
							ByteArrayOutputStream output = new ByteArrayOutputStream();
							IOUtils.copy(input, output);
							
							String contentType = fileNameMap.getContentTypeFor(name);
							
							contentImage.setImage(output);
							contentImage.setImageContentType(contentType);
							contentImage.setImageName(name);
							
							images.add(contentImage);
						}
					}
				}
			}
	        
			
			
        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			
		}
		
		return images;

	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OutputContentImage> getImages(Product product)
			throws ServiceException {

        if(treeCache==null) {
        	throw new ServiceException("CmsImageFileManagerInfinispan has a null treeCache");
        }

		List<OutputContentImage> images = new ArrayList<OutputContentImage>();
		FileNameMap fileNameMap = URLConnection.getFileNameMap();

        try {


			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append("merchant-").append(String.valueOf(product.getMerchantStore().getId())).append("/")
			.append("product-").append(String.valueOf(product.getId()));
			
			Fqn productFiles = Fqn.fromString(filePath.toString());
			
			Node<String, Object> productFilesTree = treeCache.getRoot().getChild(productFiles);
			

			
			if(productFilesTree==null) {
				return null;
			}
			


			Set<String> names = productFilesTree.getKeys();//imageNames
			if(names!=null && names.size()>0) {
						
						for(String name : names) {
							OutputContentImage contentImage = new OutputContentImage();
							byte[] imageBytes = (byte[])productFilesTree.get(name);

							InputStream input = new ByteArrayInputStream(imageBytes);
							ByteArrayOutputStream output = new ByteArrayOutputStream();
							IOUtils.copy(input, output);
							
							String contentType = fileNameMap.getContentTypeFor(name);
							
							contentImage.setImage(output);
							contentImage.setImageContentType(contentType);
							contentImage.setImageName(name);
							
							images.add(contentImage);
							
						}

			}
	        
			
			
        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			
		}
		
		return images;
	}
	




	@Override
	public void removeImages(MerchantStore store) throws ServiceException {
        if(treeCache==null) {
        	throw new ServiceException("CmsImageFileManagerInfinispan has a null treeCache");
        }


        try {


			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append("merchant-").append(String.valueOf(store.getId()));
			
			Fqn merchantFiles = Fqn.fromString(filePath.toString());
			
			
			treeCache.removeNode(merchantFiles);
			

			
        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			
		}

		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeProductImage(ProductImage productImage)
			throws ServiceException {
		
		
        if(treeCache==null) {
        	throw new ServiceException("CmsImageFileManagerInfinispan has a null treeCache");
        }


        try {


			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append("merchant-").append(String.valueOf(productImage.getProduct().getMerchantStore().getId())).append("/")
			.append("product-").append(String.valueOf(productImage.getProduct().getId()));
			
			Fqn pi = Fqn.fromString(filePath.toString());
			
			Node<String, Object> productFilesTree = treeCache.getRoot().getChild(pi);
			
			
			productFilesTree.remove(productImage.getProductImage());
			

			
        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			
		}

		
	}

	@Override
	public void removeProductImages(Product product) throws ServiceException {
		
        if(treeCache==null) {
        	throw new ServiceException("CmsImageFileManagerInfinispan has a null treeCache");
        }


        try {


			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append("merchant-").append(String.valueOf(product.getMerchantStore().getId())).append("/")
			.append("product-").append(String.valueOf(product.getId()));
			
			Fqn productFiles = Fqn.fromString(filePath.toString());
			
			
			treeCache.removeNode(productFiles);
			

			
        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			
		}

		
	}
	
	public String getRepositoryFileName() {
		return repositoryFileName;
	}

	public void setRepositoryFileName(String repositoryFileName) {
		this.repositoryFileName = repositoryFileName;
	}


	
	

}
