package com.noursouryia.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.noursouryia.R;

public class ImageAdapter extends ArrayAdapter<Integer> {

	Context mContext;
	int layoutResourceId;
	Integer data[] = null;
	public ImageAdapter(Context mContext, int layoutResourceId, Integer[] data) {

		super(mContext, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View listItem = convertView;

		// inflate the listview_item_row.xml parent
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		listItem = inflater.inflate(layoutResourceId, parent, false);

		// get the elements in the layout
		ImageView imageViewFolderIcon = (ImageView) listItem.findViewById(R.id.trim1);

		/*
		 * Set the data for the list item. You can also set tags here if you
		 * want.
		 */
		Integer folder = data[position];

		imageViewFolderIcon.setImageResource(folder);

		return listItem;
	}

}
