package com.noursouryia.viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.noursouryia.R;

/**
 * @author H.L - admin
 *
 */
public class ArticlePagerFragment extends Fragment {

	public ArticlePagerAdapter mAdapter_Community;
	public ViewPager mPager_Community;
	public RelativeLayout relative_comunityyy;
	public PageIndicator mIndicator;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		//Log.v(TAG, "onCreateView............ autoLinQContinentalApplication.isChanged()");
//		mAdapter_Community = new ArticlePagerAdapter(getActivity().getSupportFragmentManager(), );

		relative_comunityyy = (RelativeLayout) inflater.inflate(R.layout.article_pager_layout, container, false);
		mPager_Community = (ViewPager)relative_comunityyy.findViewById(R.id.view_pager);
		mIndicator = (CirclePageIndicator)relative_comunityyy.findViewById(R.id.indicator);

		mPager_Community.setAdapter(mAdapter_Community);
		mPager_Community.setSaveEnabled(false);
		mIndicator.setViewPager(mPager_Community);

		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
//				autoLinQContinentalApplication.setCommunityFragment(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});

//		mIndicator.setCurrentItem(autoLinQContinentalApplication.getCommunityFragment());

		return relative_comunityyy;
	} 

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void  cleanViewOnDestroy(){

		mPager_Community.setAdapter(null);
		mPager_Community = null;
		mIndicator.setOnPageChangeListener(null);
		mIndicator = null;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
//		ContiViewManager.cleanViews(relative_comunityyy);

	}

}
