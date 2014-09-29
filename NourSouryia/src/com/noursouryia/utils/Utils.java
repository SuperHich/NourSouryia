package com.noursouryia.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Utils {

	
	public static void animateFad(Activity pActivity){
		pActivity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
	}
	
	public static void animateSlide(Activity pActivity){
		pActivity.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	}
	
	@SuppressLint("NewApi")
	public static void animateFrags(android.support.v4.app.FragmentTransaction ft2){
		ft2.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	}
	
	public static void stopMediaPlayer(MediaPlayer mp){
			if (mp != null) {
				mp.stop();
				mp.release();
			}
	}
	public void startMediaPlayer(MediaPlayer mp){
		if (mp != null) {
			mp.stop();
			mp.release();
		}
	}
	public static boolean isOnline(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Removes the reference to the activity from every view in a view hierarchy (listeners, images etc.).
	 * This method should be called in the onDestroy() method of each activity
	 * @param viewGroup is the root layout
	 * 
	 */
	public static void cleanViews(ViewGroup viewGroup) {
		try{
			int nrOfChildren = viewGroup.getChildCount();
			for (int i=0; i<nrOfChildren; i++){
				View view = viewGroup.getChildAt(i);
				unbindViewReferences(view);
				if (view instanceof ViewGroup)
					cleanViews((ViewGroup) view);
				
//				cleanStatus(view, i);
			}
			try {
				clearBitmap(viewGroup.getDrawingCache());
				viewGroup.removeAllViews();
			}
			catch (Throwable mayHappen) {
				// AdapterViews, ListViews and potentially other ViewGroups don't support the removeAllViews operation
			}
			
			System.gc();

		}catch (Exception e) {
			Log.e("", "Unable to clean views ! ");
		}
	}

	public static void cleanStatus(View view, int position){
		if(view instanceof LinearLayout)
			Log.i("Utils : cleanStatus " + position, "LinearLayout");
		else if(view instanceof RelativeLayout)
			Log.i("Utils : cleanStatus " + position, "RelativeLayout");
	}

	public static void clearImageView(ImageView imgView){

		Drawable d = imgView.getDrawable();
		if (d!=null) d.setCallback(null);

		Bitmap bm= imgView.getDrawingCache();
		if (bm != null) {
			bm.recycle();
			bm = null;
		}

		imgView.setImageDrawable(null);
		imgView.setBackgroundDrawable(null);
	}

	private static void unbindViewReferences(View view) {
		// set all listeners to null (not every view and not every API level supports the methods)
		try {view.setOnClickListener(null);} catch (Throwable mayHappen) {};
		try {view.setOnCreateContextMenuListener(null);} catch (Throwable mayHappen) {};
		try {view.setOnFocusChangeListener(null);} catch (Throwable mayHappen) {};
		try {view.setOnKeyListener(null);} catch (Throwable mayHappen) {};
		try {view.setOnLongClickListener(null);} catch (Throwable mayHappen) {};
		try {view.setOnClickListener(null);} catch (Throwable mayHappen) {};

		// clear bkg drawable
		clearBitmap(view.getDrawingCache());

		// set background to null
		Drawable d = view.getBackground(); 
		if (d!=null) d.setCallback(null);

		if (view instanceof ImageView) {

			ImageView imageView = (ImageView) view;
			clearImageView(imageView);
		}

		// destroy webview
		if (view instanceof WebView) {
			((WebView) view).destroyDrawingCache();
			((WebView) view).destroy();
		}
	}

	public static void clearBitmap(Bitmap bm) {
		if(bm != null){
			bm.recycle();
			bm = null;
		}
	}
	
}
