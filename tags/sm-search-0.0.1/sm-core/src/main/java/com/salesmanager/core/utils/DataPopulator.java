/**
 * 
 */
package com.salesmanager.core.utils;

import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

/**
 * @author Umesh A
 *
 */
public interface DataPopulator<Source,Target>
{


    public Target populateFromEntity(Source source,Target target, MerchantStore store, Language language) throws ConversionException;
    public Target populateFromEntity(Source source) throws ConversionException;
   
  
   
}
