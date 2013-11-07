/**
 * 
 */
package com.salesmanager.core.business.populator;

/**
 * @author Umesh A
 *
 */
public interface DataPopulator<Source,Target>
{


    public Target populate(Source source,Target target);
}
