package com.salesmanager.core.business.user.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.user.dao.GroupDao;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;

@Service("groupService")
public class GroupServiceImpl extends
		SalesManagerEntityServiceImpl<Integer, Group> implements GroupService {

	GroupDao groupDao;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	public GroupServiceImpl(GroupDao groupDao) {
		super(groupDao);
		this.groupDao = groupDao;

	}


	@Override
	public void removeGroup(Group group) throws ServiceException {
		group = this.getById(group.getId());//Prevents detached entity error
//		group.setPermissions(null);
		
		this.delete(group);
	}
	
	@Override
	public void deleteGroup(Group group) throws ServiceException {
		group=this.getById(group.getId());
		
		List<Group> groups = new ArrayList<Group>();
		
		if(groups.size()==0) {
			groups.add(group);
		}
		
		Collections.reverse(groups);
		
		List<Integer> groupIds = new ArrayList<Integer>();

			
		for(Group g : groups) {
			groupIds.add(g.getId());
		}
			
			
		
		
		List<Permission> permissions = permissionService.getPermissions(groupIds);
		
		for(Permission permission : permissions) {
			
			permission.getGroups().remove(group);
			
//			permissionService.removePermission(permission);

			//need to delete a few things
			
			//delete attributes
			
			//delete availabilities
		}
		
		for(Group g : groups) {
			groupDao.delete(g);
		}

	}

	@Override
	public void saveOrUpdate(Group group) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Group> getByName() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}


//	@Override
//	public List<Group> getGroups(List<Integer> permissionIds) throws ServiceException {
//		@SuppressWarnings({ "unchecked", "rawtypes" })
//		Set ids = new HashSet(permissionIds);
//		return groupDao.getGroupsListBypermissions(ids);
//	}

}
