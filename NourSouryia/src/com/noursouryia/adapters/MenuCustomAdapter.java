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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noursouryia.R;
import com.noursouryia.entity.Type;
import com.noursouryia.utils.NSFonts;

public class MenuCustomAdapter extends BaseExpandableListAdapter implements OnTouchListener {

	static final String TAG = MenuCustomAdapter.class.getSimpleName();
	IMenuListener listener;
	ArrayList<Type> types = new ArrayList<Type>();
	LayoutInflater inflater;
	static Context mContext ;
	public NSFonts mNSFonts ;

	public MenuCustomAdapter(Context context, ArrayList<Type> types)
	{
		
		mContext = context ;
		this.types.addAll(types);
		inflater= LayoutInflater.from(context);
		listener = (IMenuListener) context;
		mNSFonts = new NSFonts() ;
		
	}

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
		ImageView puce ;
		RelativeLayout row_bg ;
		ImageView expand ;
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
		return types.get(groupPosition);
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return types.get(groupPosition).getCategories().get(childPosition);
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
		return true;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.rowlv_module, null);
			
			holder.tv= (TextView) convertView.findViewById(R.id.trim1);
			holder.row_bg = (RelativeLayout) convertView.findViewById(R.id.row_bg);
			holder.expand = (ImageView) convertView.findViewById(R.id.expand);
			
			holder.tv.setTypeface(NSFonts.getKufah());

			holder.row_bg.setBackgroundResource(R.drawable.drawer_list_selector);
			
			convertView.setTag(holder);
		}
		else {
			
			holder = (ViewHolder)convertView.getTag();
		}
		
		Type type = types.get(groupPosition);
		
		holder.tv.setTag(new Integer[]{groupPosition});
		holder.tv.setText(type.getNameAr());
	
		if (type.getCategories().size() > 0)
		{
			holder.expand.setVisibility(View.VISIBLE);
			
			if(isExpanded)
				holder.expand.setImageResource(R.drawable.minus);
			else
				holder.expand.setImageResource(R.drawable.plus);
			
		}else
			holder.expand.setVisibility(View.GONE);
		
		return convertView;
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.rowlv_child, null);
			
			holder.tv= (TextView) convertView.findViewById(R.id.trim1);
			holder.puce= (ImageView) convertView.findViewById(R.id.puce);
			holder.row_bg = (RelativeLayout) convertView.findViewById(R.id.row_bg);
			holder.expand = (ImageView) convertView.findViewById(R.id.expand);
			
			holder.tv.setTypeface(NSFonts.getKufah());
			
			holder.row_bg.setBackgroundResource(R.drawable.drawer_subitem_selector);
			holder.puce.setImageResource(R.drawable.blue_puce);
			adjustViewDimensions(holder.puce, 50 , 5);
			
			holder.expand.setVisibility(View.GONE);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		
		String sub_title = types.get(groupPosition).getCategories().get(childPosition).getName(); 
		
		holder.tv.setTag(new Integer[]{groupPosition, childPosition});
		holder.tv.setText(sub_title);
		
		return convertView;
	}
	
	
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	
	public static void adjustViewDimensions (View v, int myNewX, int myNewY) {
		
		float d = mContext.getResources().getDisplayMetrics().density;
		
		int dpX = (int)(myNewX * d);
		int dpY = (int)(myNewY * d);
		
		
		RelativeLayout.LayoutParams absParams = 
				(RelativeLayout.LayoutParams)v.getLayoutParams();
		absParams.rightMargin = dpX ;
		absParams.width = dpY ;
		absParams.height = dpY ;
		v.setLayoutParams(absParams);
	}

}