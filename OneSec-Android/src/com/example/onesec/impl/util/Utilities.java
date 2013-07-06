package com.example.onesec.impl.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utilities {
	
	public static String dateToString(Date dateObj){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
		String dateString = df.format(dateObj);
		return dateString;
	}
	
	public static Date stringToDate(String dateStr){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
		Date date = new Date();
    	try {
    		date = formatter.parse(dateStr);
    	} catch (java.text.ParseException e) {
			
			e.printStackTrace();
		}
    	return date;
	}
}
