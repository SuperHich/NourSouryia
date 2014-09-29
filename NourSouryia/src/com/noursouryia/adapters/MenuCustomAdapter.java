package com.noursouryia.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.noursouryia.R;
import com.noursouryia.entity.Type;

public class MenuCustomAdapter extends BaseExpandableListAdapter implements OnTouchListener {

	IMenuListener listener;
	ArrayList<Type> types = new ArrayList<Type>();
	LayoutInflater inflater;
	public MenuCustomAdapter(Context context, ArrayList<Type> types)
	{
		this.types.addAll(types);
		inflater= LayoutInflater.from(context);
		listener = (IMenuListener) context;

	}
//	@Override
//	public int getCount() {
//		return images.length();
//	}
//	@Override
//	public Object getItem(int arg0) {
//		return arg0;
//	}
//	@Override
//	public long getItemId(int position) {
//		return 0;
//	}
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder holder;
//		if(convertView==null)
//		{
//			holder = new ViewHolder();
//			convertView = inflater.inflate(R.layout.rowlv_module, null);
//			
//			holder.iv= (ImageView) convertView.findViewById(R.id.trim1);
//			holder.iv.setOnTouchListener(this);
//			convertView.setTag(holder);
//		}
//		else {
//			holder = (ViewHolder)convertView.getTag();
//		}
//		
//		holder.iv.setTag(position);
//		holder.iv.setBackgroundDrawable(images.getDrawable(position));
//		return convertView;
//	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			ImageView view = (ImageView) v;
			view.getBackground().setColorFilter(0x77ffffff, PorterDuff.Mode.SRC_ATOP);
			v.invalidate();
			break;
		}
		case MotionEvent.ACTION_UP: {
			int position = (Integer) v.getTag();
			listener.onMenuItemClicked(position);

		}
		case MotionEvent.ACTION_CANCEL: {
			ImageView view = (ImageView) v;
			view.getBackground().clearColorFilter();
			view.invalidate();
			break;
		}
		}
		return true;
	}

	class ViewHolder
	{
		TextView tv;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return types.size();
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return types.get(groupPosition).getCategories().size();
	}
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.rowlv_module, null);
			
			holder.tv= (TextView) convertView.findViewById(R.id.trim1);
//			holder.tv.setOnTouchListener(this);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		String sub_title = types.get(groupPosition).getNameAr(); 
		
		holder.tv.setTag(new Integer[]{groupPosition});
		holder.tv.setText(sub_title);
//		holder.tv.setBackgroundDrawable(images.getDrawable(childPosition));
		return convertView;
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.rowlv_module, null);
			
			holder.tv= (TextView) convertView.findViewById(R.id.trim1);
//			holder.tv.setOnTouchListener(this);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		String sub_title = types.get(groupPosition).getCategories().get(childPosition).getName(); 
		
		holder.tv.setTag(new Integer[]{groupPosition, childPosition});
		holder.tv.setText(sub_title);
//		holder.tv.setBackgroundDrawable(images.getDrawable(childPosition));
		return convertView;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}


}