/**
 * 
 */
package com.salesmanager.web.admin.entity.digital;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import com.salesmanager.core.business.catalog.product.model.Product;

/**
 * A bean class responsible for getting form data from shop Admin for uploading
 * product files for a given product and validating the provided data.
 * 
 * This will work as a wrapper for underlying cache where these content images will be stored
 * and retrieved in future.
 * 
 * @author Carl Samson
 * @since 1.2
 *
 */
public class ProductFiles implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    
    private List<MultipartFile> files;
    private String fileName;
    private Product product;

    @NotEmpty(message="{product.files.invalid}")
    @Valid
    public List<MultipartFile> getFiles()
    {
        return files;
    }

    public void Files( final List<MultipartFile> files )
    {
        this.files = files;
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}


    

}
