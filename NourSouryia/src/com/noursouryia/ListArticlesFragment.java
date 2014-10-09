package com.noursouryia;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.noursouryia.adapters.ArticlesAdapter;
import com.noursouryia.entity.Article;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;


public class ListArticlesFragment extends BaseFragment {
	
	public static final String ARG_ARTICLE_LINK 	= "article_link";
	public static final String ARG_ARTICLE_CATEGORY = "article_category";
	
	private ArticlesAdapter adapter;
	private ArrayList<Article> articles = new ArrayList<Article>();
	private TextView txv_empty;
	private ListView listView;
	
	private String link, category;
	
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
		
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getArguments() != null){
			link 		= getArguments().getString(ARG_ARTICLE_LINK);
			category 	= getArguments().getString(ARG_ARTICLE_CATEGORY);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		
		txv_empty = (TextView) rootView.findViewById(R.id.txv_emptyList);
		listView = (ListView) rootView.findViewById(android.R.id.list);
//		listView.setDivider(null);
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
//				Toast.makeText(getActivity(), "Article at position " + arg2 , Toast.LENGTH_LONG).show();
				Article selectedArticle = articles.get(arg2);
				((MainActivity)getActivity()).gotoArticleFragment(selectedArticle);
				
			}
		});
		
	}

	private void initData(){
		
		new AsyncTask<Void, Void, ArrayList<Article>>() {

			private ProgressDialog loading;

			@Override
			protected void onPreExecute() {
				articles.clear();
				loading = new ProgressDialog(getActivity());
				loading.setCancelable(false);
				loading.setMessage(getString(R.string.please_wait));
				loading.show();
			}
			
			@Override
			protected ArrayList<Article> doInBackground(Void... params) {
				
				ArrayList<Article> list = ((NSActivity)getActivity()).NourSouryiaDB.getArticlesByStringID(NSManager.TYPE, category);
				
				if(list.size() > 0)
					articles.addAll(list);
				
				else if(NSManager.getInstance(getActivity()).isOnlineMode()){
					articles.addAll(NSManager.getInstance(getActivity()).getArticlesByUrl(link));

					for(Article a : articles){
						((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateArticle(a, NSManager.DEFAULT_VALUE, NSManager.DEFAULT_VALUE);
					}
				}
				
				return articles;
			}
			
			@Override
			protected void onPostExecute(ArrayList<Article> result) {
				loading.dismiss();
				
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
