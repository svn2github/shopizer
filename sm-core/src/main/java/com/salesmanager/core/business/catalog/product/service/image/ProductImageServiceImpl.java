package com.salesmanager.core.business.catalog.product.service.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.image.ProductImageDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.image.ProductImageDescription;
import com.salesmanager.core.business.catalog.product.service.ProductImageEnum;
import com.salesmanager.core.business.content.model.content.FileContentType;
import com.salesmanager.core.business.content.model.content.ImageContentFile;
import com.salesmanager.core.business.content.model.content.OutputContentFile;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.modules.cms.product.ProductFileManager;
import com.salesmanager.core.utils.CoreConfiguration;

@Service("productImage")
public class ProductImageServiceImpl extends SalesManagerEntityServiceImpl<Long, ProductImage> 
	implements ProductImageService {
	
	private ProductImageDao productImageDao;

	@Autowired
	public ProductImageServiceImpl(ProductImageDao productImageDao) {
		super(productImageDao);
		this.productImageDao = productImageDao;
	}
	
	@Autowired
	private ProductFileManager productFileManager;
	
	@Autowired
	private CoreConfiguration configuration;
	
	
	public ProductImage getById(Long id) {
		
		
		return productImageDao.getProductImageById(id);
	}
	
	
	@Override
	public void addProductImages(Product product, List<ProductImage> productImages) throws ServiceException {
		
		try {
			for(ProductImage productImage : productImages) {
				
				Assert.notNull(productImage.getImage());
				
		        InputStream inputStream = productImage.getImage();
		        ImageContentFile cmsContentImage = new ImageContentFile();
		        cmsContentImage.setFileName( productImage.getProductImage() );
		        cmsContentImage.setFile( inputStream );
		        cmsContentImage.setFileContentType(FileContentType.PRODUCT);
		        
		        
		        
				BufferedImage bufferedImage = ImageIO.read(inputStream);
				cmsContentImage.setBufferedImage(bufferedImage);
	
		        
	
				addProductImage(product,productImage,cmsContentImage);			
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	@Override
	public void addProductImage(Product product, ProductImage productImage, ImageContentFile inputImage) throws ServiceException {
		
		
		
		
		productImage.setProduct(product);
		
		//ByteArrayOutputStream baos = null;
		try {
			
			Assert.notNull(inputImage.getFile());
			Assert.notNull(inputImage.getBufferedImage());
		
/*			//upload the image in the CMS
			InputContentFile contentImage = new InputContentFile();
			//contentImage.setFile(productImage.getImage());
			contentImage.setFileContentType(FileContentType.PRODUCT);
			contentImage.setFileName(productImage.getProductImage());
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			String contentType = fileNameMap.getContentTypeFor(productImage.getProductImage());
			contentImage.setMimeType(contentType);
			

			String extension = contentType.substring(contentType.indexOf("/")+1,contentType.length());
			BufferedImage image = ImageIO.read(productImage.getImage());
			
			//baos = new ByteArrayOutputStream();
			//ImageIO.write( image, extension, baos );
			
			
			//contentImage.setBufferedImage(image);
			
			ByteArrayInputStream inputStream = productImage.getImage();
			
			
			contentImage.setFile(inputStream);*/

			
			productFileManager.addProductImage(productImage, inputImage);
	
			//insert ProductImage
			this.saveOrUpdate(productImage);
		
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			try {
				//baos.flush();
				//baos.close();
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
	public OutputContentFile getProductImage(ProductImage productImage, ProductImageEnum size) throws ServiceException {

		
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
		
		OutputContentFile outputImage = productFileManager.getProductImage(pi);
		
		return outputImage;
		
	}
	
	@Override
	public OutputContentFile getProductImage(final String storeCode, final String productCode, final String fileName) throws ServiceException {
		OutputContentFile outputImage = productFileManager.getProductImage(storeCode,productCode,fileName);
		return outputImage;
		
	}
	
	@Override
	public List<OutputContentFile> getProductImages(Product product) throws ServiceException {
		return productFileManager.getImages(product);
	}
	
	@Override
	public void removeProductImage(ProductImage productImage) throws ServiceException {

		productFileManager.removeProductImage(productImage);
		
		ProductImage p = this.getById(productImage.getId());
		
		
		this.delete(p);
		
	}
}
