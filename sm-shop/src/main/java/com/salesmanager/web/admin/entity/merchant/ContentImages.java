/**
 * 
 */
package com.salesmanager.web.admin.entity.merchant;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

/**
 * A bean class responsible for getting form data from shop Admin for uploading
 * content images for a given merchant and validating the provided data.
 * 
 * This will work as a wrapper for underlying cache where these content images will be stored
 * and retrieved in future.
 * 
 * @author Umesh Awasthi
 * @since 1.2
 *
 */
public class ContentImages implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    
    private List<MultipartFile> image;
    private String imageFileName;

    @NotEmpty(message="{merchant.images.invalid}")
    @Valid
    public List<MultipartFile> getImage()
    {
        return image;
    }

    public void setImage( final List<MultipartFile> image )
    {
        this.image = image;
    }

    public String getImageFileName()
    {
        return imageFileName;
    }

    public void setImageFileName( final String imageFileName )
    {
        this.imageFileName = imageFileName;
    }
    
    
    

}
