package com.salesmanager.core.business.catalog.product.service.image;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.image.ProductImageDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.service.ProductImageEnum;
import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.InputContentImage;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.modules.cms.product.ProductFileManager;
import com.salesmanager.core.utils.CoreConfiguration;

@Service("productImage")
public class ProductImageServiceImpl extends SalesManagerEntityServiceImpl<Long, ProductImage> 
	implements ProductImageService {

	@Autowired
	public ProductImageServiceImpl(ProductImageDao productImageDao) {
		super(productImageDao);
	}
	
	@Autowired
	ProductFileManager productFileManager;
	
	@Autowired
	CoreConfiguration configuration;
	
	
	@Override
	public void addProductImage(Product product, ProductImage productImage, File file) throws ServiceException {
		
		productImage.setProduct(product);
		
		//upload the image in the CMS
		InputContentImage contentImage = new InputContentImage(ImageContentType.PRODUCT);
		contentImage.setFile(file);
		contentImage.setDefaultImage(productImage.isDefaultImage());
		contentImage.setImageName(productImage.getProductImage());
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentType = fileNameMap.getContentTypeFor(file.getName());
		contentImage.setImageContentType(contentType);
	
		
		productFileManager.uploadProductImage(configuration, productImage, contentImage);

		//insert ProductImage
		if(productImage.getId()!=null && productImage.getId()>0) {
			this.update(productImage);
		} else {
			this.create(productImage);
		}
		
		
	}
	
	//TODO get default product image

	
	@Override
	public OutputContentImage getProductImage(ProductImage productImage, ProductImageEnum size) throws ServiceException {

		
		ProductImage pi = new ProductImage();
		String imageName = productImage.getProductImage();
		if(size == ProductImageEnum.LARGE) {
			imageName = "L-" + imageName;
		}
		
		if(size == ProductImageEnum.SMALL) {
			imageName = "S-" + imageName;
		}
		
		pi.setProductImage(imageName);
		pi.setProduct(productImage.getProduct());
		
		OutputContentImage outputImage = productFileManager.getProductImage(pi);
		
		return outputImage;
		
	}
	
	@Override
	public List<OutputContentImage> getProductImages(Product product) throws ServiceException {
		return productFileManager.getImages(product);
	}
	
	@Override
	public void removeProductImage(ProductImage productImage) throws ServiceException {

		productFileManager.removeProductImage(productImage);
		this.delete(productImage);
		
	}
}
