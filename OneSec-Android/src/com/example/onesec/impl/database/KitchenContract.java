package com.example.onesec.impl.database;

import android.provider.BaseColumns;

public final class KitchenContract {
	public static final int SECOND_ID_COL_NUM = 0;
	public static final int DATE_COL_NUM = 1;
	public static final int VIDEO_PATH_COL_NUM = 2;
	public static final int THUMBNAIL_PATH_COL_NUM = 3;

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
