package com.salesmanager.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.modules.cms.content.ContentFileManager;

/**
 * Servlet implementation class ImageServlet
 */
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	private MerchantStoreService merchantService;
	
	private ContentFileManager contentFileManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        merchantService = applicationContext.getBean("merchantService", MerchantStoreService.class);
        contentFileManager = applicationContext.getBean("contentFileManager", ContentFileManager.class);
        super.init(config);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		StringBuffer requestUrl = request.getRequestURL();
		String requestUri = request.getRequestURI();
		
		System.out.println(requestUrl);
		System.out.println(requestUri);
		
		//Should have /storecode/<LOGO>|<CONTENT>/imageName.extension
		try {
		
			MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
		
		
			OutputContentImage img = contentFileManager.getImage(store, store.getStoreLogo(),ImageContentType.LOGO);
			
			
			if(img!=null) {
				String mimeType = img.getImageContentType();
			    if (mimeType == null) {
			        //sc.log("Could not get MIME type of "+filename);
			        //resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			        return;
			    }
	
			    // Set content type
			    response.setContentType(mimeType);
	
			    // Set content size
			    //File file = new File(filename);
			   // resp.setContentLength((int)file.length());
	
			    // Open the file and output streams
			    
			    ByteArrayOutputStream out = img.getImage();
			    response.getOutputStream().write(out.toByteArray());
			    

			    out.close();
			}
			
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
