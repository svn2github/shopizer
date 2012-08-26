package com.salesmanager.core.modules.cms;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;


public class ProductFileManagerImpl extends ProductFileManager {
	
	//load all workers for each workflow
	private List<ProductImagePut> uploadImageWorkflow;
	private ProductImageGet getImage;
	private ProductImageRemove removeImage;


	public ProductImageRemove getRemoveImage() {
		return removeImage;
	}


	public void setRemoveImage(ProductImageRemove removeImage) {
		this.removeImage = removeImage;
	}


	public void uploadProductImage(ProductImage productImage, InputContentImage contentImage)
			throws ServiceException {
			if(uploadImageWorkflow!=null) {
				for(ProductImagePut imagePut : uploadImageWorkflow) {
					imagePut.uploadProductImage(productImage, contentImage);
				}
			}
	}

	
	public OutputContentImage getProductImage(ProductImage productImage) throws ServiceException {
		// TODO Auto-generated method stub
		return getImage.getProductImage(productImage);
	}

	
	public List<OutputContentImage> getImages(MerchantStore store)
			throws ServiceException {
		// TODO Auto-generated method stub
		return getImage.getImages(store);
	}
	
	@Override
	public List<OutputContentImage> getImages(Product product)
			throws ServiceException {
		return getImage.getImages(product);
	}


	public List<ProductImagePut> getUploadImageWorkflow() {
		return uploadImageWorkflow;
	}


	public void setUploadImageWorkflow(List<ProductImagePut> uploadImageWorkflow) {
		this.uploadImageWorkflow = uploadImageWorkflow;
	}


	public ProductImageGet getGetImage() {
		return getImage;
	}


	public void setGetImage(ProductImageGet getImage) {
		this.getImage = getImage;
	}


	@Override
	public void removeProductImage(ProductImage productImage)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removeProductImages(Product product) throws ServiceException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removeImages(MerchantStore store) throws ServiceException {
		// TODO Auto-generated method stub
		
	}


	







}
