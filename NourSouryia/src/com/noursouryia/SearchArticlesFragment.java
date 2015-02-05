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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView.InternalListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView.OnLoadMoreListener;
import com.noursouryia.adapters.ArticlesAdapter;
import com.noursouryia.entity.Article;
import com.noursouryia.entity.SearchResult;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;


public class SearchArticlesFragment extends BaseFragment implements IFragmentEnabler{
	
	public static final String ARG_ARTICLE_KEYWORD 	= "article_keyword";
	
	private ArticlesAdapter adapter;
	private ArrayList<Article> articles = new ArrayList<Article>();
	private TextView txv_title, txv_empty, txv_wait;
	private PullToRefreshListView listView;
	private TextView txv_showMore;
	private ProgressBar progressBar;
	private LinearLayout loading;
	private View footer;
	private boolean isCanceled = false;
	private boolean isFirstStart = true;
	private int pageNb = 0;
	
	private String keyword;

	private NSManager mManager;
	
	public SearchArticlesFragment() {
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
			keyword		= getArguments().getString(ARG_ARTICLE_KEYWORD);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		
		txv_title = (TextView) rootView.findViewById(R.id.txv_title);
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
		txv_title.setTypeface(NSFonts.getNoorFont());
		refreshTitle(NSManager.DEFAULT_VALUE);
		
		return rootView;
	}
	
	private void refreshTitle(int total_items){
		String title = "\"" + keyword + "\"";
		if(total_items != NSManager.DEFAULT_VALUE)
			title += " : " + total_items + " " + getString(R.string.result);
		
		txv_title.setText(title);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mManager = NSManager.getInstance(getActivity());
		mManager.setFragmentEnabler(this);
		
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
		
		((InternalListView)listView.getRefreshableView()).setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				// Do the work to load more items at the end of list
				// here
				if(!NSManager.getInstance(getActivity()).isOnlineMode())
				{	
//					((MainActivity)getActivity()).showOnLineModePopup();
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
				if(!Utils.isOnline(getActivity()))
				{	
//					((MainActivity)getActivity()).showOnLineModePopup();
					listView.onRefreshComplete();
				}
				else{
					if(pageNb==1)
						listView.getRefreshableView().removeFooterView(footer);
					
					resetSearch();
				}
			}
		});
		
	}
	
	private void resetSearch(){
		txv_showMore.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		articles.clear();
		pageNb = 0;
		initData();
	}

	private void initData(){
		
		new AsyncTask<Void, Void, ArrayList<Article>>() {

			SearchResult searchResult;
			
			@Override
			protected void onPreExecute() {
				if(articles.size() == 0)
				{
					loading.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
					txv_title.setVisibility(View.GONE);
					txv_empty.setVisibility(View.GONE);
				}
			}
			
			@Override
			protected ArrayList<Article> doInBackground(Void... params) {
				try{
					if(!Utils.isOnline(getActivity()) && !listView.isRefreshing())
					{
						if(articles.size() == 0)
							return ((NSActivity)getActivity()).NourSouryiaDB.searchArticlesByKeyword(keyword);
					}
					else if(Utils.isOnline(getActivity())){
						if(pageNb == 0)
							articles.clear();
						
						searchResult = NSManager.getInstance(getActivity()).searchArticlesByKeyword(keyword, pageNb++);
						ArrayList<Article> list = searchResult.getArticles();
						
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

				((InternalListView)listView.getRefreshableView()).onLoadMoreComplete();
				
				if(listView.isRefreshing())
					listView.onRefreshComplete();
				
				if(result != null){
					
					if(pageNb == 1)
						listView.getRefreshableView().addFooterView(footer, null, true);
					else 
					if(pageNb == 2)
						listView.getRefreshableView().removeFooterView(footer);
					
					refreshTitle(searchResult.getTotal_items());
					
					articles.addAll(result);
					adapter.notifyDataSetChanged();
					
					if(articles.isEmpty())
						listView.getRefreshableView().removeFooterView(footer);
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

	@Override
	public void setEnabled(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFolderClicked(int folderId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetSearch(String keyword) {
		this.keyword = keyword;
		
		if(listView.getRefreshableView().getFooterViewsCount() > 0)
			listView.getRefreshableView().removeFooterView(footer);
		
		resetSearch();
	}
}
