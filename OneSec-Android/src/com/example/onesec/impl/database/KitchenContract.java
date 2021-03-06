package com.example.onesec.impl.database;

import android.provider.BaseColumns;

public final class KitchenContract {
	// Define Second column numbers
	public static final int SECOND_ID_COL_NUM = 1;
	public static final int SECOND_DATE_COL_NUM = 2;
	public static final int SECOND_VIDEO_PATH_COL_NUM = 3;
	public static final int SECOND_THUMBNAIL_PATH_COL_NUM = 4;
	
	// Define Cake column numbers
	public static final int CAKE_ID_COL_NUM = 1;
	public static final int CAKE_TITLE_COL_NUM = 2;
	public static final int CAKE_DATE_COL_NUM = 3;
	public static final int CAKE_VIDEO_PATH_COL_NUM = 4;
	public static final int CAKE_THUMBNAIL_PATH_COL_NUM = 5;
	public static final int CAKE_BATTER_PATH_COL_NUM = 6;
	
	// Define Sprinkle column numbers
	public static final int SPRINKLE_VIDEO_ID_COL_NUM = 1;
	public static final int SPRINKLE_TAG_COL_NUM = 2;

	// Empty constructor to prevent misuse
	public KitchenContract() {}
	
	// Define Second table contents
	public static abstract class SecondEntry implements BaseColumns {
		public static final String TABLE_NAME = "second";
		public static final String COLUMN_NAME_SECOND_ID = "id";
		public static final String COLUMN_NAME_DATE = "date";
		public static final String COLUMN_NAME_VIDEO_PATH = "videoPath";
		public static final String COLUMN_NAME_THUMBNAIL_PATH = "thumbnailPath";
		public static final String COLUMN_NAME_NULLABLE = "null";
	}
	
	public static final String[] FULL_SECOND_PROJECTION = new String[] {
		SecondEntry._ID,
	    SecondEntry.COLUMN_NAME_SECOND_ID,
	    SecondEntry.COLUMN_NAME_DATE,
	    SecondEntry.COLUMN_NAME_VIDEO_PATH,
	    SecondEntry.COLUMN_NAME_THUMBNAIL_PATH
	    };
	
	// Define Cake table contents
	public static abstract class CakeEntry implements BaseColumns {
		public static final String TABLE_NAME = "cake";
		public static final String COLUMN_NAME_CAKE_ID = "id";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_DATE = "date";
		public static final String COLUMN_NAME_VIDEO_PATH = "videoPath";
		public static final String COLUMN_NAME_THUMBNAIL_PATH = "thumbnailPath";
		public static final String COLUMN_NAME_BATTER_PATH = "batterPath";
		public static final String COLUMN_NAME_NULLABLE = "null";
	}
	
	public static final String[] FULL_CAKE_PROJECTION = new String [] {
		CakeEntry._ID,
	    CakeEntry.COLUMN_NAME_CAKE_ID,
	    CakeEntry.COLUMN_NAME_TITLE,
	    CakeEntry.COLUMN_NAME_DATE,
	    CakeEntry.COLUMN_NAME_VIDEO_PATH,
	    CakeEntry.COLUMN_NAME_THUMBNAIL_PATH
	    };
	
	// Define Sprinkle table contents
	public static abstract class SprinkleEntry implements BaseColumns {
		public static final String TABLE_NAME = "sprinkle";
		public static final String COLUMN_NAME_VIDEO_ID = "id";
		public static final String COLUMN_NAME_TAG = "tag";
		public static final String COLUMN_NAME_NULLABLE = "null";
	}
	
	public static final String[] FULL_SPRINKLE_PROJECTION = new String[] {
		SprinkleEntry._ID,
		SprinkleEntry.COLUMN_NAME_VIDEO_ID,
		SprinkleEntry.COLUMN_NAME_TAG
	    };
	
}
