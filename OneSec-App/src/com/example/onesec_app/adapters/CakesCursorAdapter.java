package com.example.onesec_app.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.onesec.impl.cake.Cake;
import com.example.onesec.impl.database.KitchenContract;
import com.example.onesec.impl.util.Utilities;
import com.example.onesec_app.R;

public class CakesCursorAdapter extends SimpleCursorAdapter {
	Context context; 
    int layoutResourceId;    
    List<Cake> cakesList;
    
    @SuppressLint("NewApi")
	public CakesCursorAdapter(Context context, int layoutResourceId, Cursor c, String[] from, int[] to, int flags) {
        super(context, layoutResourceId, c, from, to, 0);
    }
    
//    @Override
//    public void setViewText(TextView textView, String string)
//    {
//    	textView.setText(string);
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
            holder.titleView = (TextView) view.findViewById(R.id.cakeTitle);
            holder.dateView = (TextView) view.findViewById(R.id.cakeDate);
            holder.tagsView = (TextView) view.findViewById(R.id.cakeTags);
            view.setTag(holder);
        }

        String title = cursor.getString(KitchenContract.CAKE_TITLE_COL_NUM);
        if(title.equals(""))
        	title = "[untitled]";
        holder.titleView.setText(title);
        String dateStr = cursor.getString(KitchenContract.CAKE_DATE_COL_NUM);
        String niceDate = Utilities.getNiceDate(dateStr) + ", " + Utilities.getNiceTimeWithSecs(dateStr);
        holder.dateView.setText(niceDate);
        
        Cake cake = new Cake(cursor);
        String uid = cake.getId();
        String tags = cake.getTagsString(context, uid);
        holder.tagsView.setText(tags);
    }

    static class ViewHolder {
        TextView titleView;
        TextView dateView;
        TextView tagsView;
    }
}
