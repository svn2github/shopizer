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

import com.salesmanager.web.entity.customer.Customer;

/**
 * Overrides Spring Security authentication
 * @author Carl Samson
 *
 */
@Controller
public class CustomerLoginController {
	
	@Autowired
    private AuthenticationManager customerAuthenticationManager;
	
	@RequestMapping(value="/customer/logon.html", method=RequestMethod.POST)
	public String displayLogin(@RequestBody Customer customer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//TODO encrypt password
		
        Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(customer.getUserName(), customer.getPassword());
        try {
            Authentication authentication = customerAuthenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //return new LoginDetail().success().principal(authentication.getName());
        } catch (AuthenticationException ex) {
            //return new LoginDetail().failed();
        }
		
		return "success";
		
		
	}

}
