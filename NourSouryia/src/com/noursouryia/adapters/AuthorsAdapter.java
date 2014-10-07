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
import com.noursouryia.entity.Article;
import com.noursouryia.entity.Author;
import com.noursouryia.utils.NSFonts;

public class AuthorsAdapter extends BaseExpandableListAdapter implements OnTouchListener {

	IMenuListener listener;
	ArrayList<Author> authors = new ArrayList<Author>();
	LayoutInflater inflater;
	public AuthorsAdapter(Context context, ArrayList<Author> authors)
	{
		this.authors = authors;
		inflater= LayoutInflater.from(context);
		listener = (IMenuListener) context;

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
		TextView txv_count;
		ImageView puce ;
		RelativeLayout row_bg ;
		ImageView expand ;
	}
	
	class ChildViewHolder
	{
		TextView txv_title;
		TextView txv_date;
		ImageView puce;
		RelativeLayout row_bg ;
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return authors.size();
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return authors.get(groupPosition).getArticles().size();
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
			convertView = inflater.inflate(R.layout.list_group_item, null);
			
			holder.tv			= (TextView) convertView.findViewById(R.id.trim1);
			holder.txv_count 	= (TextView) convertView.findViewById(R.id.txv_count);
			holder.row_bg 		= (RelativeLayout) convertView.findViewById(R.id.row_bg);
			holder.expand 		= (ImageView) convertView.findViewById(R.id.expand);
			
			holder.tv.setTypeface(NSFonts.getNoorFont());
//			holder.txv_count.setTypeface(NSFonts.getNoorFont());
			holder.txv_count.setVisibility(View.VISIBLE);
			
			holder.row_bg.setBackgroundResource(R.drawable.drawer_list_selector);

//			holder.tv.setOnTouchListener(this);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Author a = authors.get(groupPosition);
		
		holder.tv.setTag(new Integer[]{groupPosition});
		holder.tv.setText(a.getName());
		
		holder.txv_count.setText(""+a.getCount());

		holder.expand.setVisibility(View.GONE);
		
//		if (files.get(groupPosition).getArticles().size() > 0)
//		{
//			holder.expand.setVisibility(View.VISIBLE);
//		}
		
		
		return convertView;
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder holder;
		if(convertView==null)
		{
			holder = new ChildViewHolder();
			convertView = inflater.inflate(R.layout.list_child_layout, null);
			
			holder.txv_title 	= (TextView) convertView.findViewById(R.id.txv_title);
			holder.txv_date 	= (TextView) convertView.findViewById(R.id.txv_date);
			holder.row_bg 		= (RelativeLayout) convertView.findViewById(R.id.row_bg);

			holder.txv_title.setTypeface(NSFonts.getNoorFont());
//			holder.txv_date.setTypeface(NSFonts.getNoorFont());
			
			holder.row_bg.setBackgroundResource(R.drawable.drawer_subitem_selector);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ChildViewHolder)convertView.getTag();
		}
		
		Article art = authors.get(groupPosition).getArticles().get(childPosition);
//		String authName = ((NSActivity) mContext).NourSouryiaDB.getAuthorNameByID(art.getTid());
		
//		holder.txv_title.setTag(new Integer[]{groupPosition, childPosition});
		holder.txv_title.setText(art.getTitle());
		holder.txv_date.setText(art.getCreated());
//		Log.i("","Child " + art.getName());
//		holder.tv.setBackgroundDrawable(images.getDrawable(childPosition));
		return convertView;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}


}