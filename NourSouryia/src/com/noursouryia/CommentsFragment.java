package com.noursouryia;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.noursouryia.adapters.CommentsAdapter;
import com.noursouryia.entity.Comment;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;


public class CommentsFragment extends BaseFragment {

	private CommentsAdapter adapter;
	private ArrayList<Comment> comments = new ArrayList<Comment>();
	private TextView txv_title, txv_empty, txv_wait;
	private Button btn_add_comment;
	private PullToRefreshScrollView pullToRefreshView;
	private ListView listView;
	private LinearLayout loading;
	private boolean isCanceled = false;
	private boolean isFirstStart = true;

	private int articleNID;

	public CommentsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onDetach() {
		super.onDetach();

		isCanceled = true;

	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(getArguments() != null){
			articleNID 		= getArguments().getInt(NSManager.NID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_list_comments, container, false);

		pullToRefreshView = (PullToRefreshScrollView) rootView.findViewById(R.id.pullToRefreshView);
		txv_title = (TextView) rootView.findViewById(R.id.txv_title);
		txv_empty = (TextView) rootView.findViewById(R.id.txv_emptyList);
		txv_wait = (TextView) rootView.findViewById(R.id.txv_wait);
		listView = (ListView) rootView.findViewById(android.R.id.list);
		loading = (LinearLayout) rootView.findViewById(R.id.loading);
		
		btn_add_comment = (Button) rootView.findViewById(R.id.btn_add_comment);

		txv_wait.setTypeface(NSFonts.getNoorFont());
		txv_empty.setTypeface(NSFonts.getNoorFont());
		txv_title.setTypeface(NSFonts.getNoorFont());
		txv_title.setText(R.string.comments_title);

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		adapter = new CommentsAdapter(getActivity(), comments);
		listView.setAdapter(adapter);

		if(isFirstStart)
		{
			isFirstStart = false;
			initData();
		}

		pullToRefreshView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if(!NSManager.getInstance(getActivity()).isOnlineMode())
				{	
					((MainActivity)getActivity()).showOnLineModePopup();
					pullToRefreshView.onRefreshComplete();
				}
				else{
					initData();
				}
			}
		});

		btn_add_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Add comment
				((MainActivity)getActivity()).gotoAddCommentFragment(articleNID);
			}
		});
	}

	private void initData(){

		new AsyncTask<Void, Void, ArrayList<Comment>>() {

			@Override
			protected void onPreExecute() {
				comments.clear();
				loading.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				txv_title.setVisibility(View.GONE);
			}

			@Override
			protected ArrayList<Comment> doInBackground(Void... params) {
				try{
					if(!NSManager.getInstance(getActivity()).isOnlineMode() && !pullToRefreshView.isRefreshing())
					{
						if(comments.size() == 0)
							return ((NSActivity)getActivity()).NourSouryiaDB.getCommentsById(articleNID);
					}
					else if(Utils.isOnline(getActivity())){
						ArrayList<Comment> list = NSManager.getInstance(getActivity()).getCommentsByID(articleNID);

						if(list.size() > 0)
							for(Comment c : list){
								((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateComment(c, articleNID);
							}

						return list;
					}
				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<Comment> result) {

				if(isCanceled)
					return;

				loading.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				txv_title.setVisibility(View.VISIBLE);

				if(pullToRefreshView.isRefreshing())
					pullToRefreshView.onRefreshComplete();

				if(result != null){
					comments.addAll(result);
					adapter.notifyDataSetChanged();
				}else
					((MainActivity)getActivity()).showConnectionErrorPopup();

				toggleEmptyMessage();
			}
		}.execute();

	}

	private void toggleEmptyMessage() {
		if(comments.size() == 0)
			txv_empty.setVisibility(View.VISIBLE);
		else
			txv_empty.setVisibility(View.GONE);
	}
}
