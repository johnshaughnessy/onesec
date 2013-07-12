package com.example.onesec.impl.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utilities {
	
	// Turns Date object to date string
	public static String dateToString(Date dateObj){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
		String dateString = df.format(dateObj);
		return dateString;
	}
	
	// Turns Date object to nice date string
	public static String dateToNiceString(Date dateObj){
		SimpleDateFormat df = new SimpleDateFormat("HH:mm MM/dd/yyyy", Locale.US);
		String dateString = df.format(dateObj);
		return dateString;
	}
	
	// Turns date string to Date object
	public static Date stringToDate(String dateStr){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
		Date date = new Date();
    	try {
    		date = formatter.parse(dateStr);
    	} catch (java.text.ParseException e) {
			e.printStackTrace();
			return new Date();		// TODO hackathon. this is bad
		}
    	return date;
	}
	
	// HACKATHON METHODS!!! are you proud of me now?
	
	// Get date in July 11, 2013 format from yyyyMMdd_HHmmss date string
	public static String getNiceDate(String fullDate) {
		String year = fullDate.substring(0, 4);
		int monthNum = Integer.valueOf(fullDate.substring(4, 6));
		Calendar cal = Calendar.getInstance();		// actually tho, this is so ghetto
		cal.set(Calendar.MONTH, monthNum);
		String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
		String day = fullDate.substring(6, 8);
		return month + " " + day + ", " + year;
	}
	
	// Get time in 12:15am format from yyyyMMdd_HHMMss date string
	public static String getNiceTime(String fullDate) {
		String ampm = "am";
		int hour = Integer.valueOf(fullDate.substring(9, 11));
		String minute = fullDate.substring(11, 13);
		if(hour > 12) {
			hour -= 12;
			ampm = "pm";
		}
		return String.valueOf(hour) + ":" + minute + ampm;
	}
	
	// Get time in 12:15:22am format from yyyyMMdd_HHMMss date string
	public static String getNiceTimeWithSecs(String fullDate) {
		String ampm = "am";
		int hour = Integer.valueOf(fullDate.substring(9, 11));
		String minute = fullDate.substring(11, 13);
		String second = fullDate.substring(13, 15);
		if(hour > 12) {
			hour -= 12;
			ampm = "pm";
		}
		return String.valueOf(hour) + ":" + minute + ":" + second + ampm;
	}
	
}
