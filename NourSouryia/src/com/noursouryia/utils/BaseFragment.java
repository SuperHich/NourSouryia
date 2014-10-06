package com.noursouryia.utils;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.noursouryia.R;
import com.slidinglayer.SlidingLayer;
import com.slidinglayer.SlidingLayer.ISlidingLayerOpenCloseListener;

public class BaseFragment extends Fragment implements ISlidingLayerOpenCloseListener{

	final String TAG = getClass().getSimpleName();

	protected RelativeLayout rootView;
	private Button opener_bottom, btn_news, btn_folders, btn_researches, btn_writers, btn_articles;

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
		mSlidingLayer.setStickTo(SlidingLayer.STICK_TO_BOTTOM);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlp.width = LayoutParams.MATCH_PARENT;
		rlp.height = getResources().getDimensionPixelSize(R.dimen.layer_width);

		mSlidingLayer.setLayoutParams(rlp);

		opener_bottom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonClicked(v);
			}
		});
		
		// start with opened sliding layer
		mSlidingLayer.openLayer(true);
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
