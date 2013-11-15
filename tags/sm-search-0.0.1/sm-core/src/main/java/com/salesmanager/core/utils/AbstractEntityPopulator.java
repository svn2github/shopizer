/**
 * 
 */
package com.salesmanager.core.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.service.LanguageService;

/**
 * @author Umesh A
 *
 */
public abstract class AbstractEntityPopulator<Source,Target> implements EntityPopulator<Source, Target>
{

    @Autowired
    protected MerchantStoreService merchantService;
    
    @Autowired
    protected LanguageService languageService;
    
    
   
    private Locale locale;

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    public Locale getLocale() {
        return locale;
    }
    
    public Target populateToEntity(final Source source) throws ConversionException{
      return  populateToEntity(source,createTarget(),null);   // not sure about store and language 
    }
    
    protected abstract Target createTarget();
    
   


}
