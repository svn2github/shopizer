package com.salesmanager.web.utils;

import com.salesmanager.web.constants.Constants;

public class FilePathUtils {
	
	
	
	/**
	 * Builds a static content content file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param imageName
	 * @return
	 */
	public static String buildStaticFilePath(String imageName) {
		return new StringBuilder().append(Constants.FILES_URI).append("/").append(imageName).toString();
	}
	

}
