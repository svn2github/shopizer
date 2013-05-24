/**
 * 
 */
package com.salesmanager.core.modules.cms.common;

import java.io.Serializable;

import com.salesmanager.core.business.content.model.content.StaticContentType;

/**
 * Abstract class for Static content data containing common attributes
 * for handling static content data.
 * 
 * @author Umesh Awasthi
 *@since 1.2
 */
public abstract class StaticContentData implements Serializable
{

   
    private static final long serialVersionUID = 1L;
    private String fileName;
    private StaticContentType contentType = StaticContentType.STATIC_FILE;
    private String fileContentType;
    
    public String getFileName()
    {
        return fileName;
    }
    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }
    public StaticContentType getContentType()
    {
        return contentType;
    }
    public void setContentType( StaticContentType contentType )
    {
        this.contentType = contentType;
    }
    public String getFileContentType()
    {
        return fileContentType;
    }
    public void setFileContentType( String fileContentType )
    {
        this.fileContentType = fileContentType;
    }
    
    
}
