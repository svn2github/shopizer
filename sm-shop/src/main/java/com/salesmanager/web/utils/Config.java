/**
 *
 */
package com.salesmanager.web.utils;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Umesh Awasthi
 *
 */
public class Config
{
   protected static final Logger LOG=LoggerFactory.getLogger( Config.class );
    private static Configuration config=null;

    public static String getParameter(final String key){
        if(config == null){
            config=getConfiguration();
        }
        if(StringUtils.isNotBlank( key )){
            LOG.info( "Preparing to fetch value for key {} ",key );
            return config.getString( key );
        }
        else{
            LOG.error( "Provided key is null..retunring null value" );
            return null;
        }

    }

    private static CompositeConfiguration getConfiguration(){
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());
        try
        {
            config.addConfiguration(new PropertiesConfiguration("project.properties"));
            return config;

        }
        catch ( ConfigurationException e )
        {
            LOG.error( "Error while reading project.properties file ",e );
        }


        return null;
    }
}

