package com.example.onesec.impl.database;

import android.provider.BaseColumns;

public final class KitchenContract {

	// Empty constructor to prevent misuse
	public KitchenContract() {}
	
	// Define table contents
	public static abstract class SecondEntry implements BaseColumns {
		public static final String TABLE_NAME = "second";
		public static final String COLUMN_NAME_SECOND_ID = "id";
		public static final String COLUMN_NAME_DATE = "date";
		public static final String COLUMN_NAME_VIDEO_PATH = "videoPath";
		public static final String COLUMN_NAME_THUMBNAIL_PATH = "thumbnailPath";
		public static final String COLUMN_NAME_NULLABLE = "null";
	}
	
}
