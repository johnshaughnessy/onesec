package com.example.onesec_app.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onesec.impl.second.Second;
import com.example.onesec.impl.util.Utilities;
import com.example.onesec_app.R;

public class SecondsAdapter extends ArrayAdapter<Second> {

	Context context; 
    int layoutResourceId;    
    List<Second> secondsList;
    
    public SecondsAdapter(Context context, int layoutResourceId, List<Second> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.secondsList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SecondsHolder holder = null;
        
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new SecondsHolder();
            holder.thumbnailView = (ImageView)row.findViewById(R.id.secondThumbnail);
            holder.dateView = (TextView)row.findViewById(R.id.secondDate);
           
            row.setTag(holder);
        }
        else {
            holder = (SecondsHolder)row.getTag();
        }
        
        Second second = secondsList.get(position);
        holder.dateView.setText(Utilities.dateToString(second.getDate()));
        holder.thumbnailView.setImageBitmap(second.getThumbnail(context));
        
        return row;
    }
    
    static class SecondsHolder
    {
        ImageView thumbnailView;
        TextView dateView;
    }
	
}
