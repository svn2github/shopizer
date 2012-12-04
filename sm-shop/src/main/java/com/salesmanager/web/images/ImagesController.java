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
import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
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
	 * @param imageType
	 * @param imageName
	 * @return
	 * @throws IOException
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unused")
	@RequestMapping("/static/{storeId}/{imageType}/{imageName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final Integer storeId, @PathVariable final String imageType, @PathVariable final String imageName, @PathVariable final String extension) throws IOException, ServiceException {

		// example -> /static/1/CONTENT/myImage.png
		
		ImageContentType imgType = null;
		
		if(imageType.equals(ImageContentType.LOGO)) {
			imgType = ImageContentType.LOGO;
		}
		
		if(imageType.equals(ImageContentType.CONTENT)) {
			imgType = ImageContentType.CONTENT;
		}
		
		OutputContentImage image =contentService.getContentImage(storeId, new StringBuilder().append(imageName).append(".").append(extension).toString());
		
		
		if(image!=null) {
			return image.getImage().toByteArray();
		} else {
			//empty image placeholder
			return null;
		}

	}
	
	@SuppressWarnings("unused")
	@RequestMapping("/static/{storeId}/{imageType}/{productId}/{imageName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final Integer storeId, @PathVariable final Long productId, @PathVariable final String imageType, @PathVariable final String imageName, @PathVariable final String extension) throws IOException {

		
		// example -> /static/1/PRODUCT/120/product1.jpg
		
		ImageContentType imgType = null;
		
		if(imageType.equals(ImageContentType.PRODUCT)) {
			imgType = ImageContentType.PRODUCT;
		}
		
		if(imageType.equals(ImageContentType.PROPERTY)) {
			imgType = ImageContentType.PROPERTY;
		}
		
		OutputContentImage image = null;
		try {
			image = productImageService.getProductImage(storeId, productId, new StringBuilder().append(imageName).append(".").append(extension).toString());
		} catch (ServiceException e) {
			LOGGER.error("Cannot retrieve image " + imageName, e);
		}
		if(image!=null) {
			return image.getImage().toByteArray();
		} else {
			//empty image placeholder
			return null;
		}

	}

}
