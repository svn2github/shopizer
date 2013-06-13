package com.salesmanager.web.admin.controller.products;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.file.DigitalProduct;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.file.DigitalProductService;
import com.salesmanager.core.business.content.model.content.FileContentType;
import com.salesmanager.core.business.content.model.content.InputContentFile;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.content.ContentFiles;
import com.salesmanager.web.admin.entity.digital.ProductFiles;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;

@Controller
public class DigitalProductController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DigitalProductController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private DigitalProductService digitalProductService;
	
	@Secured("PRODUCTS")
	@RequestMapping(value={"/admin/products/digitalProduct.html"}, method=RequestMethod.GET)
	public String getDigitalProduct(@RequestParam("id") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		Product product = productService.getById(productId);
		
		if(product==null || product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			return "redirect:/admin/products/products.html";
		}
		
		DigitalProduct digitalProduct = digitalProductService.getByProduct(store, product);
		if(digitalProduct!=null) {
			
			ProductFiles productFiles = new ProductFiles();
			productFiles.setProduct(product);
			productFiles.setDigitalProduct(digitalProduct);
			model.addAttribute("file", null);
			
		}

		return ControllerConstants.Tiles.Product.digitalProduct;
		
	}
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/product/saveDigitalProduct.html", method=RequestMethod.POST)
	public String saveFile(@ModelAttribute(value="productFiles") @Valid final ContentFiles contentFiles, final BindingResult bindingResult,final Model model, final HttpServletRequest request) throws Exception{
	    
		this.setMenu(model, request);
	    if (bindingResult.hasErrors()) {
	        LOGGER.info( "Found {} Validation errors", bindingResult.getErrorCount());
	        return ControllerConstants.Tiles.Product.digitalProduct;
	       
        }
	    final List<InputContentFile> contentFilesList=new ArrayList<InputContentFile>();
        final MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
        if(CollectionUtils.isNotEmpty( contentFiles.getFile() )){
            LOGGER.info("Saving {} product files for merchant {}",contentFiles.getFile().size(),store.getId());
            for(final MultipartFile multipartFile:contentFiles.getFile()){
                if(!multipartFile.isEmpty()){
                    ByteArrayInputStream inputStream = new ByteArrayInputStream( multipartFile.getBytes() );
                    InputContentFile cmsContentImage = new InputContentFile();
                    cmsContentImage.setFileName(multipartFile.getOriginalFilename() );
                    cmsContentImage.setFileContentType( FileContentType.STATIC_FILE );
                    cmsContentImage.setFile( inputStream );
                    contentFilesList.add( cmsContentImage);
                }
            }
            
            if(CollectionUtils.isNotEmpty( contentFilesList )){
            	//contentService.addContentFiles( store.getCode(), contentFilesList );
            }
            else{
                // show error message on UI
            }
        }
        
        return ControllerConstants.Tiles.Product.digitalProduct;
	}
	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-products", "catalogue-products");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
