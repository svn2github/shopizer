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
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
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
	private MerchantStoreService merchantStoreService;
	
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
		
		  //TODO create all groups and permissions
		
		  MerchantStore store = merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);
		
		  Permission auth = new Permission("AUTH");//Authenticated
		  permissionService.create(auth);
		  
		  Permission categories = new Permission("CATEGORIES");
		  permissionService.create(categories);
		  Permission products = new Permission("PRODUCTS");
		  permissionService.create(products);
		  Permission attributes = new Permission("ATTRIBUTES");
		  permissionService.create(attributes);
		  Permission featured = new Permission("FEATURED");
		  permissionService.create(featured);
		  
		  Permission content = new Permission("CONTENT");
		  permissionService.create(content);
		  Permission pstore = new Permission("STORE");
		  permissionService.create(pstore);
		  Permission tax = new Permission("TAX");
		  permissionService.create(tax);
		  Permission payment = new Permission("PAYMENT");
		  permissionService.create(payment);
		  Permission shipping = new Permission("SHIPPING");
		  permissionService.create(shipping);
		  
		  Permission superadmin = new Permission("SUPERADMIN");
		  permissionService.create(superadmin);
		  Permission admin = new Permission("ADMIN");
		  permissionService.create(admin);
		  
		  //TODO to be continued
		  
		  

		  
		  Group gsuperadmin = new Group("SUPERADMIN");
		  Group gadmin = new Group("ADMIN");
		  Group gcatalogue = new Group("GROUP_CATALOGUE");
		  Group gstore = new Group("GROUP_STORE");
		  Group gorder = new Group("GROUP_ORDER");
		  
		  gsuperadmin.getPermissions().add(superadmin);
		  gsuperadmin.getPermissions().add(admin);
		  gsuperadmin.getPermissions().add(auth);
		  gsuperadmin.getPermissions().add(categories);
		  gsuperadmin.getPermissions().add(products);
		  gsuperadmin.getPermissions().add(attributes);
		  gsuperadmin.getPermissions().add(featured);
		  gsuperadmin.getPermissions().add(content);
		  gsuperadmin.getPermissions().add(pstore);
		  gsuperadmin.getPermissions().add(tax);
		  gsuperadmin.getPermissions().add(payment);
		  gsuperadmin.getPermissions().add(shipping);
		  
		  groupService.create(gsuperadmin);

		  String password = passwordEncoder.encodePassword("password", null);
		  
		  //creation of the super admin
		  com.salesmanager.core.business.user.model.User user = new com.salesmanager.core.business.user.model.User("admin",password,"admin@shopizer.com");
		  user.setFirstName("Administrator");
		  user.setLastName("User");
		  user.getGroups().add(gsuperadmin);

		  
		  userService.create(user);
		
		
	}



}
