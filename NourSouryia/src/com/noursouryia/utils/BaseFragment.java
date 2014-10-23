package com.noursouryia.utils;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.noursouryia.HomeFragment;
import com.noursouryia.ListArticlesFragment;
import com.noursouryia.MainActivity;
import com.noursouryia.MediaFragment;
import com.noursouryia.R;
import com.noursouryia.entity.Type;
import com.slidinglayer.SlidingLayer;
import com.slidinglayer.SlidingLayer.ISlidingLayerOpenCloseListener;

public class BaseFragment extends Fragment implements ISlidingLayerOpenCloseListener, OnTouchListener{

	public final String TAG = getClass().getSimpleName();

	protected RelativeLayout rootView;
	private Button opener_bottom, btn_news, btn_folders, btn_researches, btn_writers, btn_articles;
	private int height_halfScreen ;
	private SlidingLayer mSlidingLayer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		btn_news 		= (Button) view.findViewById(R.id.btn_news); 
		btn_folders 	= (Button) view.findViewById(R.id.btn_folders);
		btn_researches 	= (Button) view.findViewById(R.id.btn_researches); 
		btn_writers 	= (Button) view.findViewById(R.id.btn_writers);
		btn_articles 	= (Button) view.findViewById(R.id.btn_articles);
		
		opener_bottom 	= (Button) view.findViewById(R.id.opener_bottom);
		
		mSlidingLayer = (SlidingLayer) view.findViewById(R.id.slidingLayer1);
		
		mSlidingLayer.setOpenCloseListener(this);
		mSlidingLayer.setShadowWidth(0);
		mSlidingLayer.setShadowDrawable(null);
		mSlidingLayer.setOffsetWidth(getResources().getDimensionPixelOffset(R.dimen.offset_width));

		LayoutParams rlp = (LayoutParams) mSlidingLayer.getLayoutParams();
		
		if(this instanceof HomeFragment || this instanceof MediaFragment){
			mSlidingLayer.setStickTo(SlidingLayer.STICK_TO_BOTTOM);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		}else{
			mSlidingLayer.setStickTo(SlidingLayer.STICK_TO_TOP);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		}
		rlp.width = LayoutParams.MATCH_PARENT;
//		rlp.height = getResources().getDimensionPixelSize(R.dimen.layer_width);

		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		height_halfScreen = (displaymetrics.heightPixels)/2;
		
		rlp.height = height_halfScreen;
		
		
		mSlidingLayer.setLayoutParams(rlp);

		opener_bottom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonClicked(v);
			}
		});
		
		btn_news.setOnTouchListener(this); 
		btn_folders.setOnTouchListener(this);
		btn_researches.setOnTouchListener(this);
		btn_writers.setOnTouchListener(this);
		btn_articles.setOnTouchListener(this);
		
		btn_news.setTag(MainActivity.NEWS_FRAGMENT); 
		btn_folders.setTag(MainActivity.FILES_FRAGMENT);
		btn_researches.setTag(MainActivity.LIST_ARTICLE_FRAGMENT);
		btn_writers.setTag(MainActivity.AUTHORS_FRAGMENT);
		btn_articles.setTag(MainActivity.LIST_ARTICLE_FRAGMENT);
		
		if(this instanceof HomeFragment){
			// start with opened sliding layer
			mSlidingLayer.openLayer(true);
		}
	}

	public void buttonClicked(View v) {
		switch (v.getId()) {
		case R.id.opener_bottom:
			if (!mSlidingLayer.isOpened()) {
				mSlidingLayer.openLayer(true);
			}else{
				mSlidingLayer.closeLayer(true);
			}
			break;
		}
	}
	
	private void hideShowViews(boolean status){
		
		int visibility = status ? View.VISIBLE : View.GONE;
		int resBtn = status ? R.drawable.ctrl_open_bottom : R.drawable.ctrl_close_bottom; 

		btn_news.setVisibility(visibility);
		btn_folders.setVisibility(visibility);
		btn_researches.setVisibility(visibility);
		btn_writers.setVisibility(visibility);
		btn_articles.setVisibility(visibility);
		
		opener_bottom.setBackgroundResource(resBtn);
		
	}

	public boolean onBackPressed(){
		return false;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data){

	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			Button view = (Button) v;
			view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
			v.invalidate();
			break;
		}
		case MotionEvent.ACTION_UP: {
			String fragTAG = (String) v.getTag();
			Bundle args = null;
			Type type = null;
			if(v == btn_researches)
				type = ((MainActivity) getActivity()).NourSouryiaDB.getTypeByName("research");
			else if(v == btn_articles)
				type = ((MainActivity) getActivity()).NourSouryiaDB.getTypeByName("article");

			if(type != null){
				args = new Bundle();
				args.putString(ListArticlesFragment.ARG_ARTICLE_LINK, type.getLink());
				args.putString(ListArticlesFragment.ARG_ARTICLE_CATEGORY, type.getNameEn());
				args.putString(ListArticlesFragment.ARG_ARTICLE_TITLE, type.getNameAr());
			}
				
				
			((MainActivity) getActivity()).onTypeItemClicked(fragTAG, args);

		}
		case MotionEvent.ACTION_CANCEL: {
			Button view = (Button) v;
			view.getBackground().clearColorFilter();
			view.invalidate();
			break;
		}
		}
		return true;
	}

	@Override
	public void onSlidingLayerOpened() {
		hideShowViews(true);
		Log.v(TAG, "onSlidingLayerOpened : " + mSlidingLayer.isOpened());
	}

	@Override
	public void onSlidingLayerClosed() {
		hideShowViews(false);
		Log.v(TAG, "onSlidingLayerClosed : " + mSlidingLayer.isOpened());
	}
	
}
