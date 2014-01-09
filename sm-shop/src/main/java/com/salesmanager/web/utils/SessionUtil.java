/**
 *
 */
package com.salesmanager.web.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Umesh Awasthi
 *
 */
public class SessionUtil
{

    /**
     * Method which will help to retrieving values from Session
     * based on the key being passed to the method.
     * @param key
     * @return value stored in session corresponding to the key
     */
    @SuppressWarnings( "unchecked" )
    public static <T> T getSessionValue(final String key) {
          ServletRequestAttributes reqAttr = (ServletRequestAttributes) RequestContextHolder
                              .currentRequestAttributes();
              return (T) reqAttr.getRequest().getSession().getAttribute( key );
      }


}
