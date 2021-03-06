package com.noursouryia.utils;


import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
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

import com.noursouryia.AuthorsFragment;
import com.noursouryia.FilesFragment;
import com.noursouryia.FragmentThawraDiaries;
import com.noursouryia.HomeFragment;
import com.noursouryia.ListArticlesFragment;
import com.noursouryia.ListNewsFragment;
import com.noursouryia.MainActivity;
import com.noursouryia.NewsFragment;
import com.noursouryia.PollsFragment;
import com.noursouryia.R;
import com.noursouryia.SearchArticlesFragment;
import com.noursouryia.entity.Category;
import com.noursouryia.entity.Type;
import com.noursouryia.externals.NSManager;
import com.slidinglayer.SlidingLayer;
import com.slidinglayer.SlidingLayer.ISlidingLayerOpenCloseListener;

public class BaseFragment extends Fragment implements ISlidingLayerOpenCloseListener, OnTouchListener, IMenuOpener{

	public final String TAG = getClass().getSimpleName();

	protected RelativeLayout rootView, mlayout_search;
	private Button opener_bottom, btn_news, btn_tahdhib, btn_researches, btn_letters, btn_opinions;
	private int height_halfScreen ;
	protected SlidingLayer mSlidingLayer;
	
	private Handler mHandler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			if(BaseFragment.this instanceof HomeFragment){
				((MainActivity) getActivity()).isTopOpener = false;
				((MainActivity) getActivity()).hideOpenerTop();
			}else{
				((MainActivity) getActivity()).isTopOpener = false;

				if(BaseFragment.this instanceof FilesFragment
						|| BaseFragment.this instanceof AuthorsFragment 
						|| BaseFragment.this instanceof NewsFragment
						|| BaseFragment.this instanceof FragmentThawraDiaries
						|| BaseFragment.this instanceof ListArticlesFragment
						|| BaseFragment.this instanceof ListNewsFragment
						|| BaseFragment.this instanceof SearchArticlesFragment
						|| BaseFragment.this instanceof PollsFragment){
					((MainActivity) getActivity()).showOpenerTop();
					((MainActivity) getActivity()).isTopOpener = true;
				}else
					((MainActivity) getActivity()).hideOpenerTop();

				if(BaseFragment.this instanceof FilesFragment || BaseFragment.this instanceof AuthorsFragment)
					((MainActivity) getActivity()).isImgTitle = true;
			}
			return false;
		}
	});

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if(!(this instanceof HomeFragment))
			if(NSManager.getInstance(getActivity()).getFragmentEnabler() != null)
				NSManager.getInstance(getActivity()).getFragmentEnabler().setEnabled(false);

	}

	@Override
	public void onDetach() {
		super.onDetach();

		if(!(this instanceof HomeFragment))
			NSManager.getInstance(getActivity()).getFragmentEnabler().setEnabled(true);

		if(this instanceof FilesFragment
				|| this instanceof AuthorsFragment 
				|| this instanceof NewsFragment
				|| this instanceof FragmentThawraDiaries
				|| this instanceof ListArticlesFragment
				|| this instanceof ListNewsFragment
				|| this instanceof PollsFragment
				|| this instanceof SearchArticlesFragment){
			((MainActivity) getActivity()).hideOpenerTop();
			((MainActivity) getActivity()).isTopOpener = false;
			((MainActivity) getActivity()).hideImageTitle();
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mSlidingLayer = (SlidingLayer) view.findViewById(R.id.slidingLayer1);
		mlayout_search = (RelativeLayout) getActivity().findViewById(R.id.layout_search);

		if(mSlidingLayer != null){

			NSManager.getInstance(getActivity()).setMenuOpener(this);

			btn_news 		= (Button) view.findViewById(R.id.btn_news); 
			btn_tahdhib 	= (Button) view.findViewById(R.id.btn_tahdhib);
			btn_researches 	= (Button) view.findViewById(R.id.btn_researches); 
			btn_letters 	= (Button) view.findViewById(R.id.btn_letters);
			btn_opinions 	= (Button) view.findViewById(R.id.btn_opinions);

			opener_bottom 	= (Button) view.findViewById(R.id.opener_bottom);

			mSlidingLayer.setOpenCloseListener(this);
			mSlidingLayer.setShadowWidth(0);
			mSlidingLayer.setShadowDrawable(null);
			mSlidingLayer.setOffsetWidth(getResources().getDimensionPixelOffset(R.dimen.offset_width));

			LayoutParams rlp = (LayoutParams) mSlidingLayer.getLayoutParams();

			((MainActivity) getActivity()).hideImageTitle();
			((MainActivity) getActivity()).isImgTitle = false;
			
			mHandler.sendMessageDelayed(new Message(), 1000);

			if(this instanceof HomeFragment){
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
			btn_tahdhib.setOnTouchListener(this);
			btn_researches.setOnTouchListener(this);
			btn_letters.setOnTouchListener(this);
			btn_opinions.setOnTouchListener(this);

			btn_news.setTag(MainActivity.NEWS_FRAGMENT); 
			btn_tahdhib.setTag(MainActivity.LIST_ARTICLE_FRAGMENT);
			btn_researches.setTag(MainActivity.LIST_ARTICLE_FRAGMENT);
			btn_letters.setTag(MainActivity.LIST_ARTICLE_FRAGMENT);
			btn_opinions.setTag(MainActivity.LIST_ARTICLE_FRAGMENT);

			//		if(this instanceof HomeFragment){
			//			// start with opened sliding layer
			//			mSlidingLayer.openLayer(true);
			//		}

		}


		mlayout_search.bringToFront();
	}

	public void buttonClicked(View v) {
		switch (v.getId()) {
		case R.id.opener_bottom:
			if (!mSlidingLayer.isOpened()) {
				mSlidingLayer.openLayer(true);
				Log.i(TAG, ">>> buttonClicked openLayer ");
				if(((MainActivity) getActivity()).isTopOpener)
					((MainActivity) getActivity()).hideOpenerTop();
				if(((MainActivity) getActivity()).isImgTitle)
					((MainActivity) getActivity()).hideImageTitle();

			}else{
				mSlidingLayer.closeLayer(true);
				Log.i(TAG, ">>> buttonClicked closeLayer ");
				if(((MainActivity) getActivity()).isTopOpener)
					((MainActivity) getActivity()).showOpenerTop();
				if(((MainActivity) getActivity()).isImgTitle)
					((MainActivity) getActivity()).showImageTitle();

			}
			break;
		}
	}

	private void hideShowViews(boolean status){

		int visibility = status ? View.VISIBLE : View.GONE;
		int resBtn = status ? R.drawable.ctrl_open_bottom : R.drawable.ctrl_close_bottom; 

		if(((MainActivity) getActivity()).isTopOpener)
			resBtn = status ? R.drawable.ctrl_close_bottom : R.drawable.ctrl_open_bottom;

		btn_news.setVisibility(visibility);
		btn_tahdhib.setVisibility(visibility);
		btn_researches.setVisibility(visibility);
		btn_letters.setVisibility(visibility);
		btn_opinions.setVisibility(visibility);

		opener_bottom.setBackgroundResource(resBtn);

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
			Category category = null;
			if(v == btn_researches)
				type = ((MainActivity) getActivity()).NourSouryiaDB.getTypeByName("research");
			else if(v == btn_news)
				type = ((MainActivity) getActivity()).NourSouryiaDB.getTypeByName("article");
			else if(v == btn_tahdhib)
				category = ((MainActivity) getActivity()).NourSouryiaDB.getCategoryByID(17);
			else if(v == btn_letters)
				category = ((MainActivity) getActivity()).NourSouryiaDB.getCategoryByID(16);
			else if(v == btn_opinions)
				category = ((MainActivity) getActivity()).NourSouryiaDB.getCategoryByID(40);

			if(type != null){
				args = new Bundle();
				args.putString(ListArticlesFragment.ARG_ARTICLE_LINK, type.getLink());
				args.putString(ListArticlesFragment.ARG_ARTICLE_CATEGORY, type.getNameEn());
				args.putString(ListArticlesFragment.ARG_ARTICLE_TITLE, type.getNameAr());
				((MainActivity) getActivity()).onTypeItemClicked(fragTAG, args, true);
			}
			else
				if(category != null){
					((MainActivity) getActivity()).gotoListArticlesFragment(category.getLink(), category.getName(), category.getName());
				}
			
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
		if(!(this instanceof HomeFragment)){
			if(((MainActivity) getActivity()).isTopOpener)
				((MainActivity) getActivity()).hideOpenerTop();
			if(((MainActivity) getActivity()).isImgTitle)
				((MainActivity) getActivity()).hideImageTitle();
		}
	}

	@Override
	public void onSlidingLayerClosed() {
		hideShowViews(false);
		if(!(this instanceof HomeFragment)){
			if(((MainActivity) getActivity()).isTopOpener)
				((MainActivity) getActivity()).showOpenerTop();
			if(((MainActivity) getActivity()).isImgTitle)
				((MainActivity) getActivity()).showImageTitle();
		}
	}

	@Override
	public void openMenu() {
		mSlidingLayer.openLayer(true);
		if(((MainActivity) getActivity()).isTopOpener)
			((MainActivity) getActivity()).hideOpenerTop();
		if(((MainActivity) getActivity()).isImgTitle)
			((MainActivity) getActivity()).hideImageTitle();

	}

	@Override
	public void closeMenu() {
		Log.i(TAG, ">>> closeMenu ");
		mSlidingLayer.closeLayer(true);
		if(((MainActivity) getActivity()).isTopOpener)
			((MainActivity) getActivity()).showOpenerTop();
		if(((MainActivity) getActivity()).isImgTitle)
			((MainActivity) getActivity()).showImageTitle();

	}

}
