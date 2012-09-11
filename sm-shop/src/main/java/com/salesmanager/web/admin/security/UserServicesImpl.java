package com.salesmanager.web.admin.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.PermissionService;
import com.salesmanager.core.business.user.service.UserService;


/**
 * 
 * @author casams1
 *         http://stackoverflow.com/questions/5105776/spring-security-with
 *         -custom-user-details
 */
@Service("userDetailsService")
public class UserServicesImpl implements UserDetailsService{

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	
	@Autowired
	protected PermissionService  permissionService;
	
	@Autowired
	protected GroupService   groupService;
	
	
	
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
		//UserDetails userDetails = new UserDetails();
		// UserEntity userEntity = dao.findByName(username);
		// if (userEntity == null)
		// throw new UsernameNotFoundException("user not found");
		// return assembler.buildUserFromUserEntity(userEntity);

		/*		
  		String username = userEntity.getUsername();
		String password = userEntity.getPassword();
		boolean enabled = userEntity.isActive();
		boolean accountNonExpired = userEntity.isActive();
		boolean credentialsNonExpired = userEntity.isActive();
		boolean accountNonLocked = userEntity.isActive();
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (SecurityRoleEntity role : userEntity.getUserSecurityRoleEntity()) {
			authorities.add(new GrantedAuthorityImpl(role.getName()));
		}
		User user = new User(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);
		*/
		
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
	
	
	public void createDefaultAdmin() throws Exception {
		
		
		
		  Permission userperm = new Permission("GRANT_USER");
		  permissionService.create(userperm);
		  Permission storeperm = new Permission("GRANT_STORE");
		  permissionService.create(storeperm);
		  Permission catalogperm = new Permission("GRANT_CATALOG");
		  permissionService.create(catalogperm);
		  Permission orderperm = new Permission("GRANT_ORDER");
		  permissionService.create(orderperm);
		  Permission configperm = new Permission("GRANT_CONFIG");
		  permissionService.create(configperm);
		  
		  Group admin = new Group("ADMIN");
		  
		  admin.getPermissions().add(userperm);
		  admin.getPermissions().add(storeperm);
		  admin.getPermissions().add(catalogperm);
		  admin.getPermissions().add(orderperm);
		  admin.getPermissions().add(configperm);
		  
		  groupService.create(admin);

		  String password = passwordEncoder.encodePassword("password", null);
		  
		  
		  com.salesmanager.core.business.user.model.User user = new com.salesmanager.core.business.user.model.User("admin",password,"admin@shopizer.com");
		  user.setFirstName("Administrator");
		  user.setLastName("User");
		  user.getGroups().add(admin);
		  
		  userService.create(user);
		
		
	}



}
