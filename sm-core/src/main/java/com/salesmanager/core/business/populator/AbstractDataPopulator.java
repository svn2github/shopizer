/**
 * 
 */
package com.salesmanager.core.business.populator;

import java.util.Locale;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;


/**
 * @author Umesh A
 *
 */
public abstract class AbstractDataPopulator<Source,Target> implements DataPopulator<Source, Target>
{

    private MerchantStore store;
    private Locale locale;
    private Language language;
	public void setStore(MerchantStore store) {
		this.store = store;
	}
	public MerchantStore getStore() {
		return store;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public Language getLanguage() {
		return language;
	}


}
