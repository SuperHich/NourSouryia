package com.noursouryia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.noursouryia.utils.BaseFragment;

public class NewsFragment extends BaseFragment  {


	private ImageView diaries, jawla, takarir, news_comment ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.news_fragment, container, false);

		diaries = (ImageView) rootView.findViewById(R.id.thawra_diaries);
		jawla = (ImageView) rootView.findViewById(R.id.jawla_sahafa);
		takarir = (ImageView) rootView.findViewById(R.id.takarir_news);
		news_comment = (ImageView) rootView.findViewById(R.id.comment_news);

		news_comment.setTag(MainActivity.COMMENTS_FRAGMENT);
		diaries.setTag(MainActivity.THAWRA_DIARIES);
		
		
		news_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String fragTAG = (String) v.getTag();
				Bundle args = null;
				((MainActivity) getActivity()).onTypeItemClicked(fragTAG, args);
				
			}
		});

		diaries.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String fragTAG = (String) v.getTag();
				Bundle args = null;
				((MainActivity) getActivity()).onTypeItemClicked(fragTAG, args);
				
			}
		});
		
		
		return rootView;
	}

}
