package com.salesmanager.core.business.catalog.product.service.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.image.ProductImageDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.image.ProductImageDescription;
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
	public void addProductImage(Product product, ProductImage productImage) throws ServiceException {
		
		
		
		
		productImage.setProduct(product);
		
		ByteArrayOutputStream baos = null;
		try {
			
		
			//upload the image in the CMS
			InputContentImage contentImage = new InputContentImage(ImageContentType.PRODUCT);
			//contentImage.setFile(productImage.getImage());
			contentImage.setDefaultImage(productImage.isDefaultImage());
			contentImage.setImageName(productImage.getProductImage());
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			String contentType = fileNameMap.getContentTypeFor(productImage.getProductImage());
			contentImage.setImageContentType(contentType);
			

			String extension = contentType.substring(contentType.indexOf("/")+1,contentType.length());
			BufferedImage image = ImageIO.read(productImage.getImage());
			baos = new ByteArrayOutputStream();
			ImageIO.write( image, extension, baos );
			contentImage.setBufferedImage(image);
			contentImage.setFile(baos);


			
			productFileManager.uploadProductImage(configuration, productImage, contentImage);
	
			//insert ProductImage
			this.saveOrUpdate(productImage);
		
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			try {
				baos.flush();
				baos.close();
			} catch(Exception e) {
				
			}
		}
		
		
	}
	
	@Override
	public void saveOrUpdate(ProductImage productImage) throws ServiceException {
		
		
		//save or update (persist and attach entities
		if(productImage.getId()!=null && productImage.getId()>0) {

			super.update(productImage);
			
		} else {
			
			List<ProductImageDescription> descriptions = productImage.getDescriptions();
			productImage.setDescriptions(null);
			super.save(productImage);
			
			if(descriptions!=null && descriptions.size()>0) {
				for(ProductImageDescription description : descriptions) {
					this.addProductImageDescription(productImage, description);
				}
			}
			
		}
		
	}
	
	public void addProductImageDescription(ProductImage productImage, ProductImageDescription description)
	throws ServiceException {

		
			if(productImage.getDescriptions()==null) {
				productImage.setDescriptions(new ArrayList<ProductImageDescription>());
			}
			
			productImage.getDescriptions().add(description);
			description.setProductImage(productImage);
			update(productImage);


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
	public OutputContentImage getProductImage(final Integer storeId, final Long productId, final String fileName) throws ServiceException {
		OutputContentImage outputImage = productFileManager.getProductImage(storeId,productId,fileName);
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
