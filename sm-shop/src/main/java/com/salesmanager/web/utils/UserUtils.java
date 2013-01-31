package com.salesmanager.web.utils;

import java.util.List;

import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.web.constants.Constants;

public class UserUtils {
	
	public static boolean userInGroup(User user,String groupName) {
		
		
		
		List<Group> logedInUserGroups = user.getGroups();
		for(Group group : logedInUserGroups) {
			if(group.getGroupName().equals(Constants.GROUP_SUPERADMIN)) {
				return true;
			}
		}
		
		return false;
		
	}

}
