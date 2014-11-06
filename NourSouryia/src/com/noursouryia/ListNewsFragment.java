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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView.InternalListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView.OnLoadMoreListener;
import com.noursouryia.adapters.NewsAdapter;
import com.noursouryia.entity.Article;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;


public class ListNewsFragment extends BaseFragment {
	
	public static final String ARG_ARTICLE_LINK 	= "article_link";
	public static final String ARG_ARTICLE_CATEGORY = "article_category";
	public static final String ARG_ARTICLE_TITLE 	= "article_title";
	public static final String ARG_ARTICLE_WITH_COMMENTS 	= "article_with_comments";
	
	private NewsAdapter adapter;
	private ArrayList<Article> articles = new ArrayList<Article>();
	private ImageView img_title;
	private TextView txv_empty, txv_wait;
	private PullToRefreshListView listView;
	private TextView txv_showMore;
	private ProgressBar progressBar;
	private LinearLayout loading;
	private View footer;
	private boolean isCanceled = false;
	private boolean isFirstStart = true;
	private int pageNb = 0;
	
	private String link, category; 
	private int imageId;
	private boolean withComments = false;
	
	public ListNewsFragment() {
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
			imageId 	= getArguments().getInt(ARG_ARTICLE_TITLE);
			withComments 	= getArguments().getBoolean(ARG_ARTICLE_WITH_COMMENTS);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_list_news, container, false);
		
		img_title = (ImageView) rootView.findViewById(R.id.img_title);
		txv_empty = (TextView) rootView.findViewById(R.id.txv_emptyList);
		txv_wait = (TextView) rootView.findViewById(R.id.txv_wait);
		listView = (PullToRefreshListView) rootView.findViewById(R.id.listView);
		loading = (LinearLayout) rootView.findViewById(R.id.loading);
		
		//Add footer to items list
		LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footer = vi.inflate(R.layout.list_footer, null);
		txv_showMore = (TextView) footer.findViewById(R.id.txv_showMore);
		progressBar = (ProgressBar) footer.findViewById(R.id.progressBar);
		footer.setBackgroundResource(R.drawable.drawer_subitem_selector);
		
		txv_wait.setTypeface(NSFonts.getNoorFont());
		txv_empty.setTypeface(NSFonts.getNoorFont());
		
		img_title.setImageResource(imageId);
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		adapter = new NewsAdapter(getActivity(), articles);
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
				((MainActivity)getActivity()).gotoArticleFragment(selectedArticle, withComments);
			}
		});
		
		((InternalListView)listView.getRefreshableView()).setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				// Do the work to load more items at the end of list
				// here
				if(!NSManager.getInstance(getActivity()).isOnlineMode())
				{	
					((MainActivity)getActivity()).showOnLineModePopup();
					((InternalListView)listView.getRefreshableView()).onLoadMoreComplete();
				}
				else{
					initData();
				}
			}
		});
		
		footer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				txv_showMore.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				initData();
			}
		});
		
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if(!NSManager.getInstance(getActivity()).isOnlineMode())
				{	
					((MainActivity)getActivity()).showOnLineModePopup();
					listView.onRefreshComplete();
				}
				else{
					
					if(pageNb==1)
						listView.getRefreshableView().removeFooterView(footer);
					
					txv_showMore.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
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
					img_title.setVisibility(View.GONE);
				}
			}
			
			@Override
			protected ArrayList<Article> doInBackground(Void... params) {
				try{
					if(!NSManager.getInstance(getActivity()).isOnlineMode() && !listView.isRefreshing())
					{
						if(articles.size() == 0)
							return ((NSActivity)getActivity()).NourSouryiaDB.getArticlesByStringID(NSManager.TYPE, category);
					}
					else if(Utils.isOnline(getActivity())){
						if(pageNb == 0)
							articles.clear();
						
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
				img_title.setVisibility(View.VISIBLE);

				((InternalListView)listView.getRefreshableView()).onLoadMoreComplete();
				
				if(listView.isRefreshing())
					listView.onRefreshComplete();
				
				if(result != null){
					
					if(pageNb == 1)
						listView.getRefreshableView().addFooterView(footer, null, true);
					else 
					if(pageNb == 2)
						listView.getRefreshableView().removeFooterView(footer);
					
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
