package com.salesmanager.web.shop.controller.customer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.review.ProductReviewService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.product.PersistableProductReview;
import com.salesmanager.web.entity.catalog.product.ReadableProduct;
import com.salesmanager.web.entity.catalog.product.ReadableProductReview;
import com.salesmanager.web.populator.catalog.PersistableProductReviewPopulator;
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
@RequestMapping(Constants.SHOP_URI + "/customer")
public class CustomerProductReviewController extends AbstractController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private ProductReviewService productReviewService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerFacade customerFacade;

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/review.html", method=RequestMethod.GET)
	public String displayProductReview(@RequestParam Long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
	    Language language = super.getLanguage(request);

        
        
        //get product
        Product product = productService.getById(productId);
        if(product==null) {
        	return "redirect:" + Constants.SHOP_URI;
        }
        
        if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
        	return "redirect:" + Constants.SHOP_URI;
        }
        
        
        //create readable product
        ReadableProduct readableProduct = new ReadableProduct();
        ReadableProductPopulator readableProductPopulator = new ReadableProductPopulator();
        readableProductPopulator.setPricingService(pricingService);
        readableProductPopulator.populate(product, readableProduct,  store, language);
        model.addAttribute("product", readableProduct);
        

        Customer customer =  customerFacade.getCustomerByUserName(request.getRemoteUser(), store);
        
        List<ProductReview> reviews = productReviewService.getByProduct(product, language);
	    for(ProductReview r : reviews) {
	    	if(r.getCustomer().getId().longValue()==customer.getId().longValue()) {
	    		
				ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
				ReadableProductReview rev = new ReadableProductReview();
				reviewPopulator.populate(r, rev, store, language);
	    		
	    		model.addAttribute("customerReview", rev);
	    		break;
	    	}
	    }
        
        
        ProductReview review = new ProductReview();
        review.setCustomer(customer);
        review.setProduct(product);
        
        ReadableProductReview productReview = new ReadableProductReview();
        ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
        reviewPopulator.populate(review,  productReview, store, language);
        
        model.addAttribute("review", productReview);
        model.addAttribute("reviews", reviews);
		
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.review).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}
	
	
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/review/submit.html", method=RequestMethod.POST)
	public String submitProductReview(@Valid @ModelAttribute("review") PersistableProductReview review, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
	    Language language = getLanguage(request);
	    
	    Product product = productService.getById(review.getProductId());
	    if(product==null) {
	    	return "redirect:" + Constants.SHOP_URI;
	    }
	    
        ReadableProduct readableProduct = new ReadableProduct();
        ReadableProductPopulator readableProductPopulator = new ReadableProductPopulator();
        readableProductPopulator.setPricingService(pricingService);
        readableProductPopulator.populate(product, readableProduct,  store, language);
        model.addAttribute("product", readableProduct);
	    
	    Customer customer =  customerFacade.getCustomerByUserName(request.getRemoteUser(), store);

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.review).append(".").append(store.getStoreTemplate());

        
        //check if customer has already evaluated the product
	    List<ProductReview> reviews = productReviewService.getByProduct(product);
	    
	    for(ProductReview r : reviews) {
	    	if(r.getCustomer().getId().longValue()==customer.getId().longValue()) {
				ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
				ReadableProductReview rev = new ReadableProductReview();
				reviewPopulator.populate(r, rev, store, language);
	    		
	    		model.addAttribute("customerReview", rev);
	    		return template.toString();
	    	}
	    }

	    
	    PersistableProductReviewPopulator populator = new PersistableProductReviewPopulator();
	    populator.setCustomerService(customerService);
	    populator.setLanguageService(languageService);
	    populator.setProductService(productService);
	    
	    ProductReview productReview = populator.populate(review, store, language);
	    productReviewService.create(productReview);
        
        model.addAttribute("review", review);
        model.addAttribute("success", "success");
		
		

		return template.toString();
		
	}
	
	
	
}
