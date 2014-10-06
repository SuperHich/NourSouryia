package com.noursouryia.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.noursouryia.R;
import com.noursouryia.entity.Category;
import com.noursouryia.utils.NSFonts;




public class CustomGridViewAdapter extends ArrayAdapter<Category> {
	
	Context context;
	int layoutResourceId;
	public NSFonts mNSFonts ;
	ArrayList<Category> data = new ArrayList<Category>();

	public CustomGridViewAdapter(Context context, int layoutResourceId,
			ArrayList<Category> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		mNSFonts = new NSFonts() ;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;

		if (row == null) {
			
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new RecordHolder();
			holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
			holder.imageCategory = (ImageView) row.findViewById(R.id.item_image);
			row.setTag(holder);
			
		} else {
			
			holder = (RecordHolder) row.getTag();
		}
		
		holder.txtTitle.setTypeface(mNSFonts.getNoorFont());
		
		
		Category item = data.get(position);
		holder.txtTitle.setText(item.getName());
		holder.imageCategory.setImageResource(R.drawable.icon_folder);
		return row;
	}

	static class RecordHolder {
		TextView txtTitle;
		ImageView imageCategory;
	}
}
