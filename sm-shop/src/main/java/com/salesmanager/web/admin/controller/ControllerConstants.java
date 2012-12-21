/**
 * 
 */
package com.salesmanager.web.admin.controller;

/**
 * Interface contain constant for Controller.These constant will be used throughout
 * sm-shop to  providing constant values to various Controllers being used in the
 * application.
 * @author Umesh A
 *
 */
public interface ControllerConstants
{

    interface Tiles{
        interface ContentImages{
            final String addContentImages="admin-contentImages-add";
            final String contentImages="admin-content-images";
            final String fileBrowser="admin-content-filebrowser";
        }
        
        interface Product{
            final String productReviews="catalogue-product-reviews";
            final String productPrices="admin-products-prices";
        }
        
    }
}
