/**
 * 
 */
package com.salesmanager.core.modules.cms.common;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Data class responsible for carrying static content data.Static content data will be stored in the
 * Infinispan cache,contentType will be used to distinguish between digital and non-digital
 * data.
 * 
 * @author Umesh Awasthi
 * @since 1.2
 *
 */
public class InputStaticContentData extends StaticContentData implements Serializable 
{

    private static final long serialVersionUID = 1L;
   
    private InputStream file;
   
    
    public InputStream getFile()
    {
        return file;
    }
    public void setFile( InputStream file )
    {
        this.file = file;
    }
   
    
    
}
