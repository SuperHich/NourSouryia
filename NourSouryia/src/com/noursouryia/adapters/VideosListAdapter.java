package com.noursouryia.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.noursouryia.R;
import com.noursouryia.entity.Article;
import com.noursouryia.utils.NSFonts;



public class VideosListAdapter extends ArrayAdapter<Article>  

{

	Context mContext;
	ArrayList<Article> data = null;
	LayoutInflater inflater;

	public VideosListAdapter(Context mContext, ArrayList<Article> list) {

		super(mContext, 0, list);
		this.mContext = mContext;
		this.data = list;
		inflater = ((Activity) mContext).getLayoutInflater();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.videos_list_item, parent, false);

			// get the elements in the layout
			holder.txv_title 	= (TextView) convertView.findViewById(R.id.txv_title); 

			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}

		holder.txv_title.setText(data.get(position).getTitle());

		
		holder.txv_title.setTypeface(NSFonts.getNoorFont());
		

		return convertView;
	}

	class ViewHolder
	{
		TextView txv_title;
	}


}
