package com.noursouryia.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noursouryia.R;
import com.noursouryia.entity.Article;
import com.noursouryia.utils.NSFonts;

public class ArticlesAdapter extends ArrayAdapter<Article> {

	IMenuListener listener;
	ArrayList<Article> articles = new ArrayList<Article>();
	LayoutInflater inflater;
	Context mContext;
	
	public ArticlesAdapter(Context context, ArrayList<Article> articles)
	{
		super(context, 0, articles);
		this.articles = articles;
		inflater= LayoutInflater.from(context);
		listener = (IMenuListener) context;
		mContext = context;

	}

	class ChildViewHolder
	{
		TextView txv_title;
		TextView txv_author;
		TextView txv_date;
		ImageView puce;
		RelativeLayout row_bg ;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChildViewHolder holder;
		if(convertView==null)
		{
			holder = new ChildViewHolder();
			convertView = inflater.inflate(R.layout.list_child_layout, null);
			
			holder.txv_title 	= (TextView) convertView.findViewById(R.id.txv_title);
			holder.txv_author 	= (TextView) convertView.findViewById(R.id.txv_author);
			holder.txv_date 	= (TextView) convertView.findViewById(R.id.txv_date);
			holder.row_bg 		= (RelativeLayout) convertView.findViewById(R.id.row_bg);

			holder.txv_title.setTypeface(NSFonts.getNoorFont());
			holder.txv_author.setTypeface(NSFonts.getNoorFont());
//			holder.txv_date.setTypeface(NSFonts.getNoorFont());
			
			holder.row_bg.setBackgroundResource(R.drawable.drawer_subitem_selector);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ChildViewHolder)convertView.getTag();
		}
		
		Article art = articles.get(position);
		
		holder.txv_title.setText(art.getTitle());
		holder.txv_author.setText(art.getName());
		holder.txv_date.setText(art.getCreated());
		return convertView;
	}


}