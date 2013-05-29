package com.salesmanager.core.modules.cms.product;

import java.awt.image.BufferedImage;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.content.model.content.FileContentType;
import com.salesmanager.core.business.content.model.content.ImageContentFile;
import com.salesmanager.core.business.content.model.content.InputContentFile;
import com.salesmanager.core.business.content.model.content.OutputContentFile;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.core.utils.ProductImageCropUtils;


public class ProductFileManagerImpl extends ProductFileManager {
	

	private ProductImagePut uploadImage;
	private ProductImageGet getImage;
	private ProductImageRemove removeImage;
	
	private CoreConfiguration configuration;


	public CoreConfiguration getConfiguration() {
		return configuration;
	}


	public void setConfiguration(CoreConfiguration configuration) {
		this.configuration = configuration;
	}


	public ProductImageRemove getRemoveImage() {
		return removeImage;
	}


	public void setRemoveImage(ProductImageRemove removeImage) {
		this.removeImage = removeImage;
	}


	public void addProductImage(ProductImage productImage, ImageContentFile contentImage)
	throws ServiceException {
	
	
	try {
		
/*				FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentType = fileNameMap.getContentTypeFor(productImage.getProductImage());
		String extension = contentType.substring(contentType.indexOf("/")+1,contentType.length());
		
		BufferedImage image = ImageIO.read(productImage.getImage());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( image, extension, baos );*/



		//upload original
		uploadImage.addProductImage(productImage, contentImage);

/*				//default large
		InputContentImage largeContentImage = new InputContentImage(ImageContentType.PRODUCT);
		largeContentImage.setFile(contentImage.getFile());
		largeContentImage.setDefaultImage(productImage.isDefaultImage());
		largeContentImage.setImageName(new StringBuilder().append("L-").append(productImage.getProductImage()).toString());

		
		uploadImage.uploadProductImage(configuration, productImage, largeContentImage);*/
		
/*				//default small
		InputContentImage smallContentImage = new InputContentImage(ImageContentType.PRODUCT);
		smallContentImage.setFile(contentImage.getFile());
		smallContentImage.setDefaultImage(productImage.isDefaultImage());
		smallContentImage.setImageName(new StringBuilder().append("S-").append(productImage.getProductImage()).toString());
		
		uploadImage.uploadProductImage(configuration, productImage, smallContentImage);*/
		
		
		//get template properties file
			
		String slargeImageHeight = configuration.getProperty("PRODUCT_IMAGE_HEIGHT_SIZE");
		String slargeImageWidth = configuration.getProperty("PRODUCT_IMAGE_WIDTH_SIZE");
		
		//String ssmallImageHeight = configuration.getProperty("SMALL_IMAGE_HEIGHT_SIZE");
		//String ssmallImageWidth = configuration.getProperty("SMALL_IMAGE_WIDTH_SIZE");

			
		if(!StringUtils.isBlank(slargeImageHeight) && !StringUtils.isBlank(slargeImageWidth)) { //&& !StringUtils.isBlank(ssmallImageHeight) && !StringUtils.isBlank(ssmallImageWidth)) {
			
			
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			
			String contentType = fileNameMap.getContentTypeFor(contentImage.getFileName());
			
			BufferedImage cropped = ImageIO.read(contentImage.getFile());
			//BufferedImage cropped = contentImage.getBufferedImage();
			
			String extension = contentType.substring(contentType.indexOf("/")+1,contentType.length());
			
			if(extension==null){
				extension="jpeg";
			}
			
			
			int largeImageHeight = Integer.parseInt(slargeImageHeight);
			int largeImageWidth = Integer.parseInt(slargeImageWidth);
			
			if(largeImageHeight>0 && largeImageWidth>0) {
			
				//int smallImageHeight = Integer.parseInt(ssmallImageHeight);
				//int smallImageWidth = Integer.parseInt(ssmallImageWidth);
				
				
				
				if(!StringUtils.isBlank(configuration.getProperty("CROP_UPLOADED_IMAGES")) && configuration.getProperty("CROP_UPLOADED_IMAGES").equals("true")) {
				
					//crop image
					ProductImageCropUtils utils = new ProductImageCropUtils(cropped,largeImageWidth, largeImageHeight);

					if(utils.isCropeable()) {
						cropped = utils.getCroppedImage();
					} 
				
				} 
				
				//ByteArrayOutputStream output = new ByteArrayOutputStream();
				
				//do not keep a large image for now, just take care of the regular image and a small image
				
/*						//resize large
				BufferedImage largeResizedImage = ProductImageSizeUtils.resize(cropped, largeImageWidth, largeImageHeight);
				File tempLarge = File.createTempFile(new StringBuilder().append(productImage.getProduct().getId()).append("tmpLarge").toString(), "." + extension );
				ImageIO.write(largeResizedImage, extension, tempLarge);

				FileInputStream isLarge = new FileInputStream(tempLarge);
				
				 
	            IOUtils.copy(isLarge, output);
				

				largeContentImage = new InputContentImage(ImageContentType.PRODUCT);
				largeContentImage.setFile(output);
				largeContentImage.setDefaultImage(false);
				largeContentImage.setImageName(new StringBuilder().append("L-").append(productImage.getProductImage()).toString());

				uploadImage.uploadProductImage(configuration, productImage, largeContentImage);
				
				output.flush();
				output.close();
				
				tempLarge.delete();*/
				
				
				
/*						//resize small
				BufferedImage smallResizedImage = ProductImageSizeUtils.resize(cropped, smallImageWidth, smallImageHeight);
				File tempSmall = File.createTempFile(new StringBuilder().append(productImage.getProduct().getId()).append("tmpSmall").toString(), "." + extension );
				ImageIO.write(smallResizedImage, extension, tempSmall);
				
				//byte[] is = IOUtils.toByteArray(new FileInputStream(tempSmall));
				
				FileInputStream isSmall = new FileInputStream(tempSmall);
				
				output = new ByteArrayOutputStream(); 
	            IOUtils.copy(isSmall, output);
				

				smallContentImage = new InputContentImage(ImageContentType.PRODUCT);
				smallContentImage.setFile(output);
				smallContentImage.setDefaultImage(false);
				smallContentImage.setImageName(new StringBuilder().append("S-").append(productImage.getProductImage()).toString());
				
				uploadImage.uploadProductImage(configuration, productImage, smallContentImage);
				
				output.flush();
				output.close();
				
				tempSmall.delete();*/
			
			}

		}

	
		
		
	} catch (Exception e) {
		throw new ServiceException(e);
	} finally {
		try {
			productImage.getImage().close();
		} catch(Exception ignore) {}
	}

}

	
	public OutputContentFile getProductImage(ProductImage productImage) throws ServiceException {
		//will return original
		return getImage.getProductImage(productImage);
	}

	
	@Override
	public List<OutputContentFile> getImages(final String merchantStoreCode, FileContentType imageContentType)
			throws ServiceException {
		//will return original
		return getImage.getImages(merchantStoreCode,FileContentType.PRODUCT);
	}
	
	@Override
	public List<OutputContentFile> getImages(Product product)
			throws ServiceException {
		return getImage.getImages(product);
	}






	@Override
	public void removeProductImage(ProductImage productImage)
			throws ServiceException {

		this.removeImage.removeProductImage(productImage);
		
/*		ProductImage large = new ProductImage();
		large.setProduct(productImage.getProduct());
		large.setProductImage("L" + productImage.getProductImage());
		
		this.removeImage.removeProductImage(large);
		
		ProductImage small = new ProductImage();
		small.setProduct(productImage.getProduct());
		small.setProductImage("S" + productImage.getProductImage());
		
		this.removeImage.removeProductImage(small);*/
		
	}


	@Override
	public void removeProductImages(Product product) throws ServiceException {

		this.removeImage.removeProductImages(product);
		
	}


	@Override
	public void removeImages(final String merchantStoreCode) throws ServiceException {
		
		this.removeImage.removeImages(merchantStoreCode);
		
	}


	public ProductImagePut getUploadImage() {
		return uploadImage;
	}


	public void setUploadImage(ProductImagePut uploadImage) {
		this.uploadImage = uploadImage;
	}


	

	public ProductImageGet getGetImage() {
		return getImage;
	}


	public void setGetImage(ProductImageGet getImage) {
		this.getImage = getImage;
	}


	@Override
	public OutputContentFile getProductImage(String merchantStoreCode,
			String productCode, String imageName) throws ServiceException {
		return getImage.getProductImage(merchantStoreCode, productCode, imageName);
	}





}
