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
import com.noursouryia.entity.File;
import com.noursouryia.utils.NSFonts;

public class FilesListAdapter extends ArrayAdapter<File> {

	ArrayList<File> items = new ArrayList<File>();
	LayoutInflater inflater;
	Context mContext;

	public FilesListAdapter(Context context, ArrayList<File> items)
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

		File f = items.get(position);

		holder.tv.setText(f.getName());
		holder.txv_count.setText(""+f.getCount());

		return convertView;
	}


}