package com.salesmanager.web.admin.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.PermissionService;
import com.salesmanager.core.business.user.service.UserService;


/**
 * 
 * @author casams1
 *         http://stackoverflow.com/questions/5105776/spring-security-with
 *         -custom-user-details
 */
@Service("customerDetailsService")
public class CustomerServicesImpl implements UserDetailsService{

	@Autowired
	private UserService userService;
	

	
	@Autowired
	protected PermissionService  permissionService;
	
	@Autowired
	protected GroupService   groupService;
	
	
	
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {

		com.salesmanager.core.business.user.model.User myUser = null;
		
		try {

			myUser = userService.getByUserName(userName);
		
			if(myUser==null) {
				return null;
			}

		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		@SuppressWarnings("deprecation")
		GrantedAuthority role = new GrantedAuthorityImpl("ADMIN");
		authorities.add(role);
		
		User user = new User(userName, myUser.getAdminPassword(), true, true,
				true, true, authorities);
		return user;
	}
	




}
