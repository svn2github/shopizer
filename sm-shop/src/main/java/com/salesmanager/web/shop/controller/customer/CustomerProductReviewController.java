package com.salesmanager.web.shop.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.product.ReadableProduct;
import com.salesmanager.web.entity.catalog.product.ReadableProductReview;
import com.salesmanager.web.populator.catalog.ReadableProductPopulator;
import com.salesmanager.web.populator.catalog.ReadableProductReviewPopulator;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;

/**
 * Entry point for logged in customers
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping("/shop/customer")
public class CustomerProductReviewController extends AbstractController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private CustomerFacade customerFacade;

	@Secured("AUTH_CUSTOMER")
	@RequestMapping(value="/review.html", method=RequestMethod.GET)
	public String displayCustomerAccount(@RequestParam Long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
	    Language language = (Language)request.getAttribute(Constants.LANGUAGE);

        
        
        //get product
        Product product = productService.getById(productId);
        if(product==null) {
        	return "redirect:/shop";
        }
        
        if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
        	return "redirect:/shop";
        }
        
        
        //create readable product
        ReadableProduct readableProduct = new ReadableProduct();
        ReadableProductPopulator readableProductPopulator = new ReadableProductPopulator();
        readableProductPopulator.setPricingService(pricingService);
        readableProductPopulator.populate(product, readableProduct,  store, language);
        model.addAttribute("product", readableProduct);
        

        Customer customer =  customerFacade.getCustomerByUserName(request.getRemoteUser(), store);
        
        ProductReview review = new ProductReview();
        review.setCustomer(customer);
        review.setProduct(product);
        
        ReadableProductReview productReview = new ReadableProductReview();
        ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
        reviewPopulator.populate(review,  productReview, store, language);
        
        model.addAttribute("review", productReview);
		
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.review).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}
	
	
	
}
