package com.noursouryia;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.noursouryia.entity.Category;
import com.noursouryia.utils.BaseFragment;

public class NewsFragment extends BaseFragment  {


	private Button diaries, jawla, takarir, news_comment ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = (RelativeLayout) inflater.inflate(R.layout.news_fragment, container, false);

		diaries = (Button) rootView.findViewById(R.id.thawra_diaries);
		jawla = (Button) rootView.findViewById(R.id.jawla_sahafa);
		takarir = (Button) rootView.findViewById(R.id.takarir_news);
		news_comment = (Button) rootView.findViewById(R.id.comment_news);

		news_comment.setOnTouchListener(this);
		jawla.setOnTouchListener(this);
		takarir.setOnTouchListener(this);
		diaries.setOnTouchListener(this);
		
		return rootView;
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

			Button view = (Button) v;
			view.getBackground().clearColorFilter();
			view.invalidate();

			switch (v.getId()) {
			case R.id.comment_news:
				Category cat1 = ((MainActivity) getActivity()).NourSouryiaDB.getCategoryByID(12);
				((MainActivity) getActivity()).gotoListNewsFragment(cat1.getLink(), cat1.getParent(), R.drawable.comment_news, true);
				break;
				
			case R.id.jawla_sahafa:
				Category cat2 = ((MainActivity) getActivity()).NourSouryiaDB.getCategoryByID(13);
				((MainActivity) getActivity()).gotoListNewsFragment(cat2.getLink(), cat2.getParent(), R.drawable.jawla_sahafa, false);
				break;
				
			case R.id.takarir_news:
				Category cat3 = ((MainActivity) getActivity()).NourSouryiaDB.getCategoryByID(14);
				((MainActivity) getActivity()).gotoListNewsFragment(cat3.getLink(), cat3.getParent(), R.drawable.takarir_news, false);
				break;

			case R.id.thawra_diaries:
				Category cat = ((MainActivity) getActivity()).NourSouryiaDB.getCategoryByID(15);

				Bundle args = new Bundle();
				args.putString(ListNewsFragment.ARG_ARTICLE_LINK, cat.getLink());
				args.putString(ListNewsFragment.ARG_ARTICLE_CATEGORY, cat.getParent());

				((MainActivity) getActivity()).gotoFragmentByTag(MainActivity.THAWRA_DIARIES, args, true);
				break;
			default:
				break;
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

}
