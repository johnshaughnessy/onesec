package com.example.onesec.impl.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utilities {
	
	public static String dateToString(Date dateObj)
	{
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
		String dateString = df.format(dateObj);
		return dateString;
	}
}
