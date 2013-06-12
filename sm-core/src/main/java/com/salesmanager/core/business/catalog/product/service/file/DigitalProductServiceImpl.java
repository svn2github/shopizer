package com.salesmanager.core.business.catalog.product.service.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.salesmanager.core.business.catalog.product.dao.file.DigitalProductDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.file.DigitalProduct;
import com.salesmanager.core.business.content.model.content.InputContentFile;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.modules.cms.content.StaticContentFileManager;

@Service("digitalProductService")
public class DigitalProductServiceImpl extends SalesManagerEntityServiceImpl<Long, DigitalProduct> 
	implements DigitalProductService {
	
	private DigitalProductDao digitalProductDao;
	
    @Autowired
    StaticContentFileManager productDownloadsFileManager;

	@Autowired
	public DigitalProductServiceImpl(DigitalProductDao digitalProductDao) {
		super(digitalProductDao);
		this.digitalProductDao = digitalProductDao;
	}
	
	
	public void addProductFile(Product product, DigitalProduct digitalProduct, InputContentFile inputFile) throws ServiceException {
	
		digitalProduct.setProduct(product);

		try {
			
			Assert.notNull(inputFile.getFile(),"InputContentFile.file cannot be null");


			
			//contentFileManager.
	
			//insert ProductImage
			//this.saveOrUpdate(productImage);
			

		
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			try {
				
/*				if(inputImage.getBufferedImage()!=null){
					inputImage.getBufferedImage().flush();
				}
				
				if(inputImage.getFile()!=null) {
					inputImage.getFile().close();
				}*/

			} catch(Exception ignore) {
				
			}
		}
		
		
	}
	

}
