package com.salesmanager.web.shop.controller.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderproduct.OrderProductDownload;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.order.service.orderproduct.OrderProductDownloadService;
import com.salesmanager.core.business.payments.service.PaymentService;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.shipping.service.ShippingService;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.product.ReadableProduct;
import com.salesmanager.web.entity.customer.ReadableCustomer;
import com.salesmanager.web.entity.order.ReadableOrder;
import com.salesmanager.web.entity.order.ReadableOrderProduct;
import com.salesmanager.web.entity.order.ReadableOrderProductDownload;
import com.salesmanager.web.populator.catalog.ReadableProductPopulator;
import com.salesmanager.web.populator.order.ReadableOrderPopulator;
import com.salesmanager.web.populator.order.ReadableOrderProductDownloadPopulator;
import com.salesmanager.web.populator.order.ReadableOrderProductPopulator;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.shop.controller.shoppingCart.facade.ShoppingCartFacade;
import com.salesmanager.web.utils.LabelUtils;

@Controller
@RequestMapping(Constants.SHOP_URI+"/order")
public class ShoppingOrderConfirmationController extends AbstractController {
	
	private static final Logger LOGGER = LoggerFactory
	.getLogger(ShoppingOrderConfirmationController.class);
	
	@Autowired
	private ShoppingCartFacade shoppingCartFacade;
	
    @Autowired
    private ShoppingCartService shoppingCartService;
	

	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private ShippingService shippingService;
	

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private ZoneService zoneService;
	
	@Autowired
	private OrderFacade orderFacade;
	
	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private PricingService pricingService;

    @Autowired
    private  CustomerFacade customerFacade;
	
	@Autowired
    private AuthenticationManager customerAuthenticationManager;
	
	@Autowired
	private OrderProductDownloadService orderProdctDownloadService;

	/**
	 * Invoked once the payment is complete and order is fulfilled
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/confirmation.html")
	public String displayConfirmation(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		Long orderId = super.getSessionAttribute(Constants.ORDER_ID, request);
		if(orderId==null) {
			return new StringBuilder().append("redirect:").append(Constants.SHOP_URI).toString();
		}

		//get the order
		Order order = orderService.getById(orderId);
		if(order == null) {
			LOGGER.warn("Order id [" + orderId + "] does not exist");
			throw new Exception("Order id [" + orderId + "] does not exist");
		}
		
		if(order.getMerchant().getId().intValue()!=store.getId().intValue()) {
			LOGGER.warn("Store id [" + store.getId() + "] differs from order.store.id [" + order.getMerchant().getId() + "]");
			return new StringBuilder().append("redirect:").append(Constants.SHOP_URI).toString();
		}
		
		ReadableCustomer customer = customerFacade.getCustomerById(order.getCustomerId(), store, language);
		
		//model.addAttribute("order", order);
		
        String[] orderMessageParams = {store.getStorename(), String.valueOf(order.getId())};
        String orderMessage = messages.getMessage("label.checkout.text", orderMessageParams, locale);
		model.addAttribute("ordermessage", orderMessage);
		
        String[] orderEmailParams = {order.getCustomerEmailAddress()};
        String orderEmailMessage = messages.getMessage("label.checkout.email", orderEmailParams, locale);
		model.addAttribute("orderemail", orderEmailMessage);
		
		ReadableOrderPopulator orderPopulator = new ReadableOrderPopulator();
		ReadableOrder readableOrder = new ReadableOrder();
		orderPopulator.populate(order, readableOrder,  store, language);
		
		List<ReadableOrderProduct> orderProducts = new ArrayList<ReadableOrderProduct>();
		for(OrderProduct p : order.getOrderProducts()) {
			ReadableOrderProductPopulator orderProductPopulator = new ReadableOrderProductPopulator();
			orderProductPopulator.setLocale(locale);
			ReadableOrderProduct orderProduct = new ReadableOrderProduct();
			orderProductPopulator.populate(p, orderProduct, store, language);
			
			Product product = productService.getById(p.getId());
			
			if(product!=null) {
				
				ReadableProductPopulator productPopulator = new ReadableProductPopulator();
				productPopulator.setPricingService(pricingService);
				
				ReadableProduct productProxy = productPopulator.populate(product, new ReadableProduct(), store, language);
				orderProduct.setProduct(productProxy);
				
			}

			orderProducts.add(orderProduct);
		}
		
		readableOrder.setProducts(orderProducts);
		
		readableOrder.setCustomer(customer);
		
		
		model.addAttribute("order",readableOrder);
		
		//check if any downloads exist for this order
		List<OrderProductDownload> orderProductDownloads = orderProdctDownloadService.getByOrderId(order.getId());
		if(CollectionUtils.isNotEmpty(orderProductDownloads)) {
			ReadableOrderProductDownloadPopulator populator = new ReadableOrderProductDownloadPopulator();
			List<ReadableOrderProductDownload> downloads = new ArrayList<ReadableOrderProductDownload>();
			for(OrderProductDownload download : orderProductDownloads) {
				ReadableOrderProductDownload view = new ReadableOrderProductDownload();
				populator.populate(download, view,  store, language);
				downloads.add(view);
			}
			model.addAttribute("downloads", downloads);
		}
		
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Checkout.confirmation).append(".").append(store.getStoreTemplate());
		return template.toString();

		
	}
	
	


}
