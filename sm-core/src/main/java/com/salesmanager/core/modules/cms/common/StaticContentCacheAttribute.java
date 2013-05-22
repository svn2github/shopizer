/**
 * 
 */
package com.salesmanager.core.modules.cms.common;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Cache attribute class which encapsulate details of the object being stored in the
 * Infinispan cache.
 * This is an extension of {@link CacheAttribute} with information about type of object 
 * being stored in the cache.
 * </p>
 * @author Umesh Awasthi
 * @version 1.2
 *
 */
public class StaticContentCacheAttribute extends CacheAttribute implements Serializable
{

   
    private static final long serialVersionUID = -1482978134467061295L;
    private String dataType;

    public String getDataType()
    {
        return dataType;
    }
    public void setDataType( String dataType )
    {
        this.dataType = dataType;
    }

    
    
    
}
