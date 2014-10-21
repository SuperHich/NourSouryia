package com.noursouryia;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.noursouryia.adapters.ArticlesAdapter;
import com.noursouryia.entity.Article;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.LoadMoreListView;
import com.noursouryia.utils.LoadMoreListView.OnLoadMoreListener;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;


public class ListArticlesFragment extends BaseFragment {
	
	public static final String ARG_ARTICLE_LINK 	= "article_link";
	public static final String ARG_ARTICLE_CATEGORY = "article_category";
	public static final String ARG_ARTICLE_TITLE = "article_title";
	
	private ArticlesAdapter adapter;
	private ArrayList<Article> articles = new ArrayList<Article>();
	private TextView txv_title, txv_empty, txv_wait;
	private PullToRefreshScrollView pullToRefreshView;
	private LoadMoreListView listView;
	private LinearLayout loading;
	private View footer;
	private boolean isCanceled = false;
	private boolean isFirstStart = true;
	private int pageNb = 0;
	
	private String link, category, title;
	
	public ListArticlesFragment() {
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
			link 		= getArguments().getString(ARG_ARTICLE_LINK);
			category 	= getArguments().getString(ARG_ARTICLE_CATEGORY);
			title 		= getArguments().getString(ARG_ARTICLE_TITLE);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		
		pullToRefreshView = (PullToRefreshScrollView) rootView.findViewById(R.id.pullToRefreshView);
		txv_title = (TextView) rootView.findViewById(R.id.txv_title);
		txv_empty = (TextView) rootView.findViewById(R.id.txv_emptyList);
		txv_wait = (TextView) rootView.findViewById(R.id.txv_wait);
		listView = (LoadMoreListView) rootView.findViewById(android.R.id.list);
		loading = (LinearLayout) rootView.findViewById(R.id.loading);
		
		//Add footer to items list
		LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footer = vi.inflate(R.layout.list_footer, null);
		listView.addFooterView(footer, null, true);
		
		txv_wait.setTypeface(NSFonts.getNoorFont());
		txv_empty.setTypeface(NSFonts.getNoorFont());
		txv_title.setTypeface(NSFonts.getNoorFont());
		txv_title.setText(title);
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		adapter = new ArticlesAdapter(getActivity(), articles);
		listView.setAdapter(adapter);

		if(isFirstStart)
		{
			isFirstStart = false;
			initData();
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Article selectedArticle = articles.get(arg2);
				((MainActivity)getActivity()).gotoArticleFragment(selectedArticle);
			}
		});
		
		listView.setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				// Do the work to load more items at the end of list
				// here
				initData();
			}
		});
		
		footer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initData();
			}
		});
		
		pullToRefreshView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if(!NSManager.getInstance(getActivity()).isOnlineMode())
				{	
					((MainActivity)getActivity()).showOnLineModePopup();
					pullToRefreshView.onRefreshComplete();
				}
				else{
					articles.clear();
					pageNb = 0;
					initData();
				}
			}
		});
		
	}

	private void initData(){
		
		new AsyncTask<Void, Void, ArrayList<Article>>() {

			@Override
			protected void onPreExecute() {
				if(articles.size() == 0)
				{
					loading.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
					txv_title.setVisibility(View.GONE);
				}
			}
			
			@Override
			protected ArrayList<Article> doInBackground(Void... params) {
				try{
					if(!NSManager.getInstance(getActivity()).isOnlineMode() && !pullToRefreshView.isRefreshing())
					{
						if(articles.size() == 0)
							return ((NSActivity)getActivity()).NourSouryiaDB.getArticlesByStringID(NSManager.TYPE, category);
					}
					else if(Utils.isOnline(getActivity())){
						ArrayList<Article> list = NSManager.getInstance(getActivity()).getArticlesByUrl(link+"&page="+pageNb++);
						
						if(list.size() > 0)
							for(Article a : list){
								((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateArticle(a, NSManager.DEFAULT_VALUE, NSManager.DEFAULT_VALUE);
							}
						
						return list;
					}
				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(ArrayList<Article> result) {
				
				if(isCanceled)
					return;
				
				loading.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				txv_title.setVisibility(View.VISIBLE);

				listView.onLoadMoreComplete();
				
				if(pullToRefreshView.isRefreshing())
					pullToRefreshView.onRefreshComplete();
				
				if(result != null){
					
					if(pageNb == 2)
						listView.removeFooterView(footer);
					
					articles.addAll(result);
					adapter.notifyDataSetChanged();
				}else
					((MainActivity)getActivity()).showConnectionErrorPopup();
				
				toggleEmptyMessage();
			}
		}.execute();

	}
	
	private void toggleEmptyMessage() {
		if(articles.size() == 0)
			txv_empty.setVisibility(View.VISIBLE);
		else
			txv_empty.setVisibility(View.GONE);
	}
}
