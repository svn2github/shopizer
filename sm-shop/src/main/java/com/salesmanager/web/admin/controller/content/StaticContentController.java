package com.salesmanager.web.admin.controller.content;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.salesmanager.core.business.content.service.StaticContentService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.modules.cms.common.CMSContentImage;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.content.ContentImages;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;


@Controller
public class StaticContentController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StaticContentController.class);
	
	@Autowired
	private StaticContentService staticContentService;
	
	/**
	 * Entry point for the file browser used from the javascript
	 * content editor
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	//@Secured("CONTENT")
	//@RequestMapping(value={"/admin/staticcontent/fileBrowser.html"}, method=RequestMethod.GET)
	//public String displayFileBrowser(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		//return ControllerConstants.Tiles.ContentImages.fileBrowser;
		
	//}
	
	
	

	
	
	@SuppressWarnings({ "unchecked"})
	@Secured("CONTENT")
	@RequestMapping(value="/admin/content/static/page.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageStaticContent(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();

		try {
			

			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			
/*			List<String> imageNames = staticContentService.
			
			if(imageNames!=null) {

				for(String name : imageNames) {

					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					entry.put("picture", ImageFilePathUtils.buildStaticImageFilePath(store, name));
					entry.put("name", name);
					entry.put("id", name);
					resp.addDataEntry(entry);

				}
			
			}*/
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("Error while paging content images", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	/**
	 * Controller methods which allow Admin to add static content files to underlying
	 * Infinispan cache.
	 * @param model model object
	 * @param request http request object
	 * @param response http response object
	 * @return view allowing user to add content images
	 * @throws Exception
	 */
	@Secured("CONTENT")
	@RequestMapping(value="/admin/content/static/createContentFiles.html", method=RequestMethod.GET)
    public String displayContentFilesCreate(final Model model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
      
	    return ControllerConstants.Tiles.ContentFiles.addContentFiles;

    }
	
	/**
	 * Method responsible for adding content images to underlying Infinispan cache.
	 * It will add given content image(s) for given merchant store in the cache.
	 * Following steps will be performed in order to add images
	 * <pre>
	 * 1. Validate form data
	 * 2. Get Merchant Store based on merchant Id.
	 * 3. Call {@link CMSContentImage} to add image(s).
	 * </pre>
	 * 
	 * @param contentImages
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Secured("CONTENT")
	@RequestMapping(value="/admin/content/saveContentImages.html", method=RequestMethod.POST)
	public String saveContentImages(@ModelAttribute(value="contentImages") @Valid final ContentImages contentImages, final BindingResult bindingResult,final Model model, final HttpServletRequest request) throws Exception{
	    
	    if (bindingResult.hasErrors()) {
	        LOGGER.info( "Found {} Validation errors", bindingResult.getErrorCount());
	       return ControllerConstants.Tiles.ContentImages.addContentImages;
	       
        }
	    final List<CMSContentImage> contentImagesList=new ArrayList<CMSContentImage>();
        final MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
        if(CollectionUtils.isNotEmpty( contentImages.getImage() )){
            LOGGER.info("Saving {} content images for merchant {}",contentImages.getImage().size(),store.getId());
            for(final MultipartFile multipartFile:contentImages.getImage()){
                if(!multipartFile.isEmpty()){
                    final ByteArrayInputStream inputStream = new ByteArrayInputStream( multipartFile.getBytes() );
                    final CMSContentImage cmsContentImage = new CMSContentImage();
                    cmsContentImage.setImageName(multipartFile.getOriginalFilename() );
                    cmsContentImage.setContentType( multipartFile.getContentType() );
                    cmsContentImage.setFile( inputStream );
                    contentImagesList.add( cmsContentImage);
                }
            }
            
            if(CollectionUtils.isNotEmpty( contentImagesList )){
                //contentService.addContentImages( store.getCode(), contentImagesList );
            }
            else{
                // show error message on UI
            }
        }
        this.setMenu(model, request);
        return ControllerConstants.Tiles.ContentImages.contentImages;
	}
	
	

	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("content", "content");
		activeMenus.put("content-images", "content-images");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("content");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
