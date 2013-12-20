package com.salesmanager.web.shop.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.customer.SecuredCustomer;

/**
 * Custom Spring Security authentication
 * @author Carl Samson
 *
 */
@Controller
public class CustomerLoginController {
	
	@Autowired
    private AuthenticationManager customerAuthenticationManager;
	
	@Autowired
    private CustomerService customerService;
	
	@RequestMapping(value="/customer/logon.html", method=RequestMethod.POST)
	public @ResponseBody String displayLogin(@RequestBody SecuredCustomer customer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		AjaxResponse resp = new AjaxResponse();
		
        Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(customer.getUserName(), customer.getPassword());
        try {
            Authentication authentication = customerAuthenticationManager.authenticate(authenticationToken);
            
            //check if username is from the appropriate store
            Customer customerModel = customerService.getByNick(customer.getUserName());
            if(customerModel==null) {
            	resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
            	return resp.toJSONString();
            }
            if(!customerModel.getMerchantStore().getCode().equals(customer.getStoreCode())){
            	resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
            	return resp.toJSONString();
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession().setAttribute(Constants.CUSTOMER, customerModel);
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
        } catch (AuthenticationException ex) {//TODO is it an application problem or username and password
        	resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
        } catch(Exception e) {
        	resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
        }
		
        
        return resp.toJSONString();
		
		
	}

}
