/**
 * 
 */
package com.salesmanager.core.modules.cms.common;

import java.io.InputStream;

/**
 * ContentImage class responsible for carrying information about image being added to CMS for given store. It will
 * contain following information for the image being stored in the CMS
 * 
 * <pre>
 * 1. Image Name
 * 2. Image Content Type
 * 3. Input Stream containing image data.
 * </pre>
 * 
 * @author Umesh Awasthi
 * @since 07-11-2012
 * @version 1.2
 */
public class CMSContentImage
{

    private String imageName;

    private InputStream file;

    private String contentType;

    public String getImageName()
    {
        return imageName;
    }

    public void setImageName( String imageName )
    {
        this.imageName = imageName;
    }

    public InputStream getFile()
    {
        return file;
    }

    public void setFile( InputStream file )
    {
        this.file = file;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType( String contentType )
    {
        this.contentType = contentType;
    }

}
