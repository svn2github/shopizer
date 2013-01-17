package com.salesmanager.web.files;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.generic.exception.ServiceException;

@Controller
public class FilesController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FilesController.class);
	

	
	@Autowired
	private ContentService contentService;
	

	/**
	 * Serves static files (css, js ...) the repository is a single node by merchant
	 * @param storeCode
	 * @param imageName
	 * @param extension
	 * @return
	 * @throws IOException
	 * @throws ServiceException
	 */
	@RequestMapping("/files/{storeCode}/{fileName}.{extension}")
	public @ResponseBody byte[] downloadFile(@PathVariable final String storeCode, @PathVariable final String fileName, @PathVariable final String extension) throws IOException, ServiceException {

		// example -> /static/mystore/CONTENT/myImage.png
		
		ImageContentType imgType = null;
		
		// needs to query the new API
		OutputContentImage image =contentService.getContentImage(storeCode, imgType, new StringBuilder().append(fileName).append(".").append(extension).toString());
		
		// handle content type etc...
		if(image!=null) {
			return image.getImage().toByteArray();
		} else {
			//empty image placeholder
			return null;
		}

	}
	
	/**
	 * Serves product download files
	 * @param storeCode
	 * @param productId
	 * @param fileName
	 * @param extension
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/files/{storeCode}/{orderId}/{productId}/{fileName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final String storeCode, @PathVariable final Long orderId, @PathVariable final Long productId, @PathVariable final String fileName, @PathVariable final String extension) throws IOException {

		// product image
		// example -> /files/mystore/12345/120/product1.zip
		
		//TODO role customer, verify the order has the requested product to download
		
		//Need to query the files CMS for merchant and product ( not the order )
		
		ImageContentType imgType = null;
		

		
		OutputContentImage image = null;
		//try {
			//image = productImageService.getProductImage(storeCode, productId, new StringBuilder().append(imageName).append(".").append(extension).toString());
		//} catch (ServiceException e) {
			//LOGGER.error("Cannot retrieve image " + imageName, e);
		//}
		if(image!=null) {
			return image.getImage().toByteArray();
		} else {
			//empty image placeholder
			return null;
		}

	}

}
