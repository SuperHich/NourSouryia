package com.noursouryia.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.noursouryia.R;
import com.noursouryia.entity.Comment;
import com.noursouryia.utils.NSFonts;

public class CommentsAdapter extends ArrayAdapter<Comment> {

	IMenuListener listener;
	ArrayList<Comment> comments = new ArrayList<Comment>();
	LayoutInflater inflater;
	Context mContext;
	
	public CommentsAdapter(Context context, ArrayList<Comment> comments)
	{
		super(context, 0, comments);
		this.comments = comments;
		inflater= LayoutInflater.from(context);
		listener = (IMenuListener) context;
		mContext = context;

	}

	class ChildViewHolder
	{
		TextView txv_name;
		TextView txv_country;
		TextView txv_date;
		TextView txv_body;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChildViewHolder holder;
		if(convertView==null)
		{
			holder = new ChildViewHolder();
			convertView = inflater.inflate(R.layout.list_comment_item, null);
			
			holder.txv_name 	= (TextView) convertView.findViewById(R.id.txv_name);
			holder.txv_country 	= (TextView) convertView.findViewById(R.id.txv_country);
			holder.txv_date 	= (TextView) convertView.findViewById(R.id.txv_date);
			holder.txv_body 	= (TextView) convertView.findViewById(R.id.txv_body);

			holder.txv_name.setTypeface(NSFonts.getNoorFont());
			holder.txv_country.setTypeface(NSFonts.getNoorFont());
//			holder.txv_date.setTypeface(NSFonts.getNoorFont());
			holder.txv_body.setTypeface(NSFonts.getNoorFont());
			
			convertView.setTag(holder);
		}
		else {
			holder = (ChildViewHolder)convertView.getTag();
		}
		
		Comment comment = comments.get(position);
		
		holder.txv_name.setText(comment.getName());
		holder.txv_country.setText(comment.getCountry());
		holder.txv_date.setText(comment.getDate());
		holder.txv_body.setText(comment.getBody());
		
		return convertView;
	}


}