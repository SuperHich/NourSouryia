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
import com.noursouryia.entity.Author;
import com.noursouryia.utils.NSFonts;

public class AuthorsListAdapter extends ArrayAdapter<Author> {

	ArrayList<Author> items = new ArrayList<Author>();
	LayoutInflater inflater;
	Context mContext;

	public AuthorsListAdapter(Context context, ArrayList<Author> items)
	{
		super(context, 0, items);
		this.items = items;
		inflater= LayoutInflater.from(context);
		mContext = context;

	}

	class ViewHolder
	{
		TextView tv;
		TextView txv_count;
		ImageView puce ;
		RelativeLayout row_bg ;
		ImageView expand ;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_group_item, null);

			holder.tv			= (TextView) convertView.findViewById(R.id.trim1);
			holder.txv_count 	= (TextView) convertView.findViewById(R.id.txv_count);
			holder.row_bg 		= (RelativeLayout) convertView.findViewById(R.id.row_bg);
			holder.expand 		= (ImageView) convertView.findViewById(R.id.expand);

			holder.tv.setTypeface(NSFonts.getNoorFont());
			//			holder.txv_count.setTypeface(NSFonts.getNoorFont());
			holder.txv_count.setVisibility(View.VISIBLE);

			holder.expand.setVisibility(View.GONE);
			holder.row_bg.setBackgroundResource(R.drawable.drawer_list_selector);

			//			holder.tv.setOnTouchListener(this);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}

		Author a = items.get(position);

		holder.tv.setText(a.getName());
		holder.txv_count.setText(""+a.getCount());

		return convertView;
	}


}