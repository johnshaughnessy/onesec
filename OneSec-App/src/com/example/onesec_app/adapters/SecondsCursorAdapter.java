package com.example.onesec_app.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.onesec.impl.database.KitchenContract;
import com.example.onesec.impl.second.Second;
import com.example.onesec.impl.util.Utilities;
import com.example.onesec_app.R;

public class SecondsCursorAdapter extends SimpleCursorAdapter {
	Context context; 
    int layoutResourceId;    
    List<Second> secondsList;
    
    @SuppressLint("NewApi")
	public SecondsCursorAdapter(Context context, int layoutResourceId, Cursor c, String[] from, int[] to, int flags) {
        super(context, layoutResourceId, c, from, to, 0);
    }

//    public void bindView(View view, Context context, Cursor c) {
//    	//String id = c.getString(KitchenContract.SECOND_ID_COL_NUM);
//		String date = c.getString(KitchenContract.DATE_COL_NUM);
//		//Uri videoUri = Uri.fromFile(new File(c.getString(KitchenContract.VIDEO_PATH_COL_NUM)));
//		Uri thumbnailUri = Uri.fromFile(new File(c.getString(KitchenContract.THUMBNAIL_PATH_COL_NUM)));
//		
//		TextView dateView = (TextView)view.findViewById(R.id.secondDate);
//		ImageView thumbnailView = (ImageView)view.findViewById(R.id.secondThumbnail);
//		dateView.setText(date);
//		
//		Bitmap thumbnail = null;
//		try {
//			thumbnail = MediaStore.Images.Media.getBitmap(context.getContentResolver(), thumbnailUri);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//        thumbnailView.setImageBitmap(thumbnail);
//    }
//    
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//    	LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.listview_seconds_row, parent, false);
//		bindView(v, context, cursor);
//		return v;
//    }
    
//    @Override
//    public void setViewText(TextView textView, String dateStr)
//    {
//    	textView.setText(Utilities.getNiceTimeWithSecs(dateStr) + " on " + Utilities.getNiceDate(dateStr));
//    }
//    
//    public void setViewImage(ImageView imageView, String thumbnailUri)
//    {
//    	imageView.setImageURI(Uri.parse(thumbnailUri));
//    }
    
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.dateView = (TextView) view.findViewById(R.id.secondDate);
            holder.tagsView = (TextView) view.findViewById(R.id.secondTags);
            view.setTag(holder);
        }

        String dateStr = cursor.getString(KitchenContract.SECOND_DATE_COL_NUM);
        String niceDate = Utilities.getNiceTime(dateStr) + " on " + Utilities.getNiceDate(dateStr);
        holder.dateView.setText(niceDate);
        
        Second second = new Second(cursor);
        String uid = second.getId();
        String tags = second.getTagsString(context, uid);
        holder.tagsView.setText(tags);
    }

    static class ViewHolder {
        TextView dateView;
        TextView tagsView;
    }
}
