/**
 *
 */
package com.salesmanager.web.populator.customer;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.customer.Address;
import com.salesmanager.web.entity.customer.CustomerEntity;

/**
 * <p>
 * CustomerEntityPopulator will help to populate {@link CustomerEntity} from {@link Customer} CustomerEntity will be
 * used to show data on the UI side.
 * </p>
 *
 * @author Umesh Awasthi
 * @version 1.2
 */
public class CustomerEntityPopulator
    extends AbstractDataPopulator<Customer, CustomerEntity>
{

    @Override
    public CustomerEntity populate( final Customer source, final CustomerEntity target,
                                    final MerchantStore merchantStore, final Language language )
        throws ConversionException
    {
        try
        {
            if(source.getFirstname() !=null){
                target.setFirstName( source.getFirstname() );
            }

            if(source.getLastname() !=null){
                target.setLastName( source.getLastname() );
            }

            if ( source.getBilling() != null )
            {
                Address address = new Address();
                address.setCity( source.getBilling().getCity() );
                address.setCompany( source.getBilling().getCompany() );
                address.setName( source.getBilling().getName() );
                address.setPostalCode( source.getBilling().getPostalCode() );
                address.setPhone( source.getBilling().getTelephone() );
                if ( source.getBilling().getCountry() != null )
                {
                    address.setCountry( source.getBilling().getCountry().getIsoCode() );
                }
                if ( source.getBilling().getZone() != null )
                {
                    address.setZone( source.getBilling().getZone().getCode() );
                }

                target.setBilling( address );
            }

            if ( source.getDelivery() != null )
            {
                Address address = new Address();
                address.setCity( source.getDelivery().getCity() );
                address.setCompany( source.getDelivery().getCompany() );
                address.setName( source.getDelivery().getName() );
                address.setPostalCode( source.getDelivery().getPostalCode() );
                address.setPhone( source.getDelivery().getTelephone() );
                if ( source.getDelivery().getCountry() != null )
                {
                    address.setCountry( source.getDelivery().getCountry().getIsoCode() );
                }
                if ( source.getDelivery().getZone() != null )
                {
                    address.setZone( source.getDelivery().getZone().getCode() );
                }

                target.setDelivery( address );
            }

        }
        catch ( Exception e )
        {
            throw new ConversionException( e );
        }

        return target;
    }

    @Override
    protected CustomerEntity createTarget()
    {
        return new CustomerEntity();
    }

}
