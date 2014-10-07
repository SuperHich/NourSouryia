package com.noursouryia.adapters;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;


public class GalleryAdapter extends BaseAdapter {

	 private Context mContext;      
	 private ArrayList<String> all_photo_URLS  ;
	 
	 
	 
	        public GalleryAdapter(Context c, ArrayList<String> photos_urls) {
	            mContext = c;
	            all_photo_URLS = photos_urls;
	        }

	        public int getCount() {
	            return all_photo_URLS.size();
	        }

	        public Object getItem(int position) {
	            return position;
	        }

	        public long getItemId(int position) {
	            return position;
	        }

	        public View getView(int position, View convertView, ViewGroup parent) {
	      	
	        	ImageView i = new ImageView(mContext);

	        	
	        	String url_image = all_photo_URLS.get(position);
				ImageLoader.getInstance().displayImage(url_image, i );
	        	
	            i.setScaleType(ImageView.ScaleType.FIT_CENTER);
	            i.setLayoutParams(new Gallery.LayoutParams(74, 65));
	            return i;
	        }

	       

	      
	
	
}
