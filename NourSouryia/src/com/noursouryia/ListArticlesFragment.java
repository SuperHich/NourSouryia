package com.noursouryia;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.noursouryia.adapters.ArticlesAdapter;
import com.noursouryia.entity.Article;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;


public class ListArticlesFragment extends BaseFragment {
	
	public static final String ARG_ARTICLE_LINK 	= "article_link";
	public static final String ARG_ARTICLE_CATEGORY = "article_category";
	public static final String ARG_ARTICLE_TITLE = "article_title";
	
	private ArticlesAdapter adapter;
	private ArrayList<Article> articles = new ArrayList<Article>();
	private TextView txv_title, txv_empty, txv_wait;
	private ListView listView;
	private LinearLayout loading;
	private boolean isCanceled = false;
	
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
		
		txv_title = (TextView) rootView.findViewById(R.id.txv_title);
		txv_empty = (TextView) rootView.findViewById(R.id.txv_emptyList);
		txv_wait = (TextView) rootView.findViewById(R.id.txv_wait);
		listView = (ListView) rootView.findViewById(android.R.id.list);
		loading = (LinearLayout) rootView.findViewById(R.id.loading);
		
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

		initData();
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Article selectedArticle = articles.get(arg2);
				((MainActivity)getActivity()).gotoArticleFragment(selectedArticle);
			}
		});
		
	}

	private void initData(){
		
		new AsyncTask<Void, Void, ArrayList<Article>>() {

			@Override
			protected void onPreExecute() {
				articles.clear();
				loading.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
			
			@Override
			protected ArrayList<Article> doInBackground(Void... params) {
				try{
					ArrayList<Article> list = ((NSActivity)getActivity()).NourSouryiaDB.getArticlesByStringID(NSManager.TYPE, category);

					if(list.size() > 0)
						articles.addAll(list);

					else if(NSManager.getInstance(getActivity()).isOnlineMode()){
						articles.addAll(NSManager.getInstance(getActivity()).getArticlesByUrl(link));

						for(Article a : articles){
							((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateArticle(a, NSManager.DEFAULT_VALUE, NSManager.DEFAULT_VALUE);
						}
					}
				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}
				return articles;
			}
			
			@Override
			protected void onPostExecute(ArrayList<Article> result) {
				loading.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				
				if(isCanceled)
					return;
				
				if(result != null){
					adapter.notifyDataSetChanged();
				}
				
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
