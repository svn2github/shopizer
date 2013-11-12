/**
 * 
 */
package com.salesmanager.web.shop.controller;

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
        interface ShoppingCart{
            final String shoppingCart="maincart";
        }
        
        interface Category{
            final String category="category";
        }
        
        interface Product{
            final String product="product";
        }
        
        interface Customer{
            final String customer="customer";
            final String register="register";
        }
        
        interface Content{
            final String content="content";
        }
        
        interface Pages{
            final String notFound="404";
        }
        
        interface Merchant{
            final String contactUs="contactus";
        }
        
        interface Checkout{
            final String checkout="checkout";
        }
        
        interface Search{
            final String search="search";
        }
        

        
    }
}
