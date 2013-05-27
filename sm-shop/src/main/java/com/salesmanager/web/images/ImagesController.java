package com.salesmanager.web.images;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.content.model.content.FileContentType;
import com.salesmanager.core.business.content.model.content.OutputContentFile;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.generic.exception.ServiceException;

@Controller
public class ImagesController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImagesController.class);
	

	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private ProductImageService productImageService;
	
	/**
	 * Logo, content image
	 * @param storeId
	 * @param imageType (LOGO, CONTENT, IMAGE)
	 * @param imageName
	 * @return
	 * @throws IOException
	 * @throws ServiceException 
	 */
	@RequestMapping("/static/{storeCode}/{imageType}/{imageName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final String storeCode, @PathVariable final String imageType, @PathVariable final String imageName, @PathVariable final String extension) throws IOException, ServiceException {

		// example -> /static/mystore/CONTENT/myImage.png
		
		FileContentType imgType = null;
		
		if(FileContentType.LOGO.name().equals(imageType)) {
			imgType = FileContentType.LOGO;
		}
		
		if(FileContentType.IMAGE.name().equals(imageType)) {
			imgType = FileContentType.IMAGE;
		}
		
		if(FileContentType.PROPERTY.name().equals(imageType)) {
			imgType = FileContentType.PROPERTY;
		}
		
		OutputContentFile image =contentService.getContentImage(storeCode, imgType, new StringBuilder().append(imageName).append(".").append(extension).toString());
		
		
		if(image!=null) {
			return image.getFile().toByteArray();
		} else {
			//empty image placeholder
			return null;
		}

	}
	

	@RequestMapping("/static/{storeCode}/{imageType}/{productCode}/{imageName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final String storeCode, @PathVariable final String productCode, @PathVariable final String imageType, @PathVariable final String imageName, @PathVariable final String extension) throws IOException {

		// product image
		// example -> /static/1/PRODUCT/120/product1.jpg
		
		// 
		
		FileContentType imgType = null;
		
		if(imageType.equals(FileContentType.PRODUCT)) {
			imgType = FileContentType.PRODUCT;
		}
		
		if(imageType.equals(FileContentType.PROPERTY)) {
			imgType = FileContentType.PROPERTY;
		}
		
		OutputContentFile image = null;
		try {
			image = productImageService.getProductImage(storeCode, productCode, new StringBuilder().append(imageName).append(".").append(extension).toString());
		} catch (ServiceException e) {
			LOGGER.error("Cannot retrieve image " + imageName, e);
		}
		if(image!=null) {
			return image.getFile().toByteArray();
		} else {
			//empty image placeholder
			return null;
		}

	}

}
