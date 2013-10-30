/**
 * 
 */
package com.salesmanager.core.business.populator;

import java.util.List;

import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author Umesh A
 *
 */
public abstract class AbstractDataPopulator<Source,Target> implements DataPopulator<Source, Target>
{

    
    @Autowired
    private Provider<Target> targetBean;
    
    private List<DataPopulator<Source,Target>> dataPopulators;
    
    
    
    /**
     * @return the dataPopulators
     */
    public List<DataPopulator<Source, Target>> getDataPopulators()
    {
        return this.dataPopulators;
    }



    /**
     * @param dataPopulators the dataPopulators to set
     */
    public void setDataPopulators( List<DataPopulator<Source, Target>> dataPopulators )
    {
        this.dataPopulators = dataPopulators;
    }



    @Override
    public Target populate( Source source, Target target )
    {
       if(getDataPopulators() !=null){
           for(final DataPopulator<Source, Target> dataPopulator:getDataPopulators()){
               dataPopulator.populate( source, target ); 
           }
       }
       
       return target;
    }
    
    
    public Provider<Target> getTargetBean()
    {
        return this.targetBean;
    }



    protected abstract Target createTarget();



    @Override
    public Target populate( Source source )
    {
       
        return populate(source,createTarget());
    }

}
