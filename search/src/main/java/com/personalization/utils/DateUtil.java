package com.personalization.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static Date formatDate(String date) throws Exception {
		
		DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return myDateFormat.parse(date);
	}

}
