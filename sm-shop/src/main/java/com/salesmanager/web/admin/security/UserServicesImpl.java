package com.salesmanager.web.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.salesmanager.web.admin.controller.categories.CategoryController;
import com.salesmanager.web.constants.Constants;


/**
 * 
 * @author casams1
 *         http://stackoverflow.com/questions/5105776/spring-security-with
 *         -custom-user-details
 */
@Service("userDetailsService")
public class UserServicesImpl implements UserDetailsService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServicesImpl.class);

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
	
	
	
	@SuppressWarnings("deprecation")
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
		
		com.salesmanager.core.business.user.model.User user = null;
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		try {

			user = userService.getByUserName(userName);

			if(user==null) {
				return null;
			}


			
			
	
			GrantedAuthority role = new GrantedAuthorityImpl(Constants.PERMISSION_ADMIN);//required to login
			authorities.add(role);
	
			List<Integer> groupsId = new ArrayList<Integer>();
			List<Group> groups = user.getGroups();
			for(Group group : groups) {
				
				
				groupsId.add(group.getId());
				
			}
			
	
	    	
	    	List<Permission> permissions = permissionService.getPermissions(groupsId);
	    	for(Permission permission : permissions) {
	    		GrantedAuthority auth = new GrantedAuthorityImpl(permission.getPermissionName());
	    		authorities.add(auth);
	    	}
    	
		} catch (Exception e) {
			LOGGER.error("Exception while querrying user",e);
			throw new SecurityDataAccessException("Exception while querrying user",e);
		}
		
		
		
		
/*		GrantedAuthority auth = new GrantedAuthorityImpl("AUTH");
		GrantedAuthority prd = new GrantedAuthorityImpl("PRODUCTS");
		GrantedAuthority ord = new GrantedAuthorityImpl("ORDER");
		GrantedAuthority content = new GrantedAuthorityImpl("CONTENT");
		GrantedAuthority store = new GrantedAuthorityImpl("STORE");
		GrantedAuthority tax = new GrantedAuthorityImpl("TAX");
		GrantedAuthority shp = new GrantedAuthorityImpl("SHIPPING");
		
		authorities.add(auth);
		authorities.add(prd);
		authorities.add(ord);
		authorities.add(content);
		authorities.add(store);
		authorities.add(tax);
		authorities.add(shp);*/
		
		User secUser = new User(userName, user.getAdminPassword(), user.isActive(), true,
				true, true, authorities);
		return secUser;
	}
	
	
	public void createDefaultAdmin() throws Exception {
		
		  //TODO create all groups and permissions
		
		  MerchantStore store = merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);

		  Group gsuperadmin = new Group("SUPERADMIN");
		  Group gadmin = new Group("ADMIN");
		  Group gcatalogue = new Group("ADMIN_CATALOGUE");
		  Group gstore = new Group("ADMIN_STORE");
		  Group gorder = new Group("ADMIN_ORDER");
		  Group gcontent = new Group("ADMIN_CONTENT");

		  groupService.create(gsuperadmin);
		  groupService.create(gadmin);
		  groupService.create(gcatalogue);
		  groupService.create(gstore);
		  groupService.create(gorder);
		  groupService.create(gcontent);
		  
		  Permission storeadmin = new Permission("STORE_ADMIN");//Administrator of the store
		  storeadmin.getGroups().add(gsuperadmin);
		  storeadmin.getGroups().add(gadmin);
		  permissionService.create(storeadmin);
		  

		  
		  Permission auth = new Permission("AUTH");//Authenticated
		  auth.getGroups().add(gsuperadmin);
		  auth.getGroups().add(gadmin);
		  auth.getGroups().add(gcatalogue);
		  auth.getGroups().add(gstore);
		  auth.getGroups().add(gorder);
		  permissionService.create(auth);

		  
		  Permission products = new Permission("PRODUCTS");
		  products.getGroups().add(gsuperadmin);
		  products.getGroups().add(gadmin);
		  products.getGroups().add(gcatalogue);
		  permissionService.create(products);

		  
		  Permission order = new Permission("ORDER");
		  order.getGroups().add(gsuperadmin);
		  order.getGroups().add(gorder);
		  order.getGroups().add(gadmin);
		  permissionService.create(order);
		  
		  Permission content = new Permission("CONTENT");
		  content.getGroups().add(gsuperadmin);
		  content.getGroups().add(gadmin);
		  content.getGroups().add(gcontent);
		  permissionService.create(content);
		  
		  
		  
		  Permission pstore = new Permission("STORE");
		  pstore.getGroups().add(gsuperadmin);
		  pstore.getGroups().add(gstore);
		  pstore.getGroups().add(gadmin);
		  permissionService.create(pstore);
		  
		  Permission tax = new Permission("TAX");
		  tax.getGroups().add(gsuperadmin);
		  tax.getGroups().add(gstore);
		  tax.getGroups().add(gadmin);
		  permissionService.create(tax);
		  
		  
		  Permission payment = new Permission("PAYMENT");
		  payment.getGroups().add(gsuperadmin);
		  payment.getGroups().add(gstore);
		  payment.getGroups().add(gadmin);
		  permissionService.create(payment);
		  
		  
		  Permission shipping = new Permission("SHIPPING");
		  shipping.getGroups().add(gsuperadmin);
		  shipping.getGroups().add(gadmin);
		  shipping.getGroups().add(gstore);
		  
		  permissionService.create(shipping);


		  String password = passwordEncoder.encodePassword("password", null);
		  
		  //creation of the super admin admin:password)
		  com.salesmanager.core.business.user.model.User user = new com.salesmanager.core.business.user.model.User("admin",password,"admin@shopizer.com");
		  user.setFirstName("Administrator");
		  user.setLastName("User");
		  user.getGroups().add(gsuperadmin);
		  user.getGroups().add(gadmin);
		  user.setMerchantStore(store);

		  
		  userService.create(user);
		
		
	}



}
