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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.noursouryia.adapters.FilesListAdapter;
import com.noursouryia.entity.File;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;


public class FilesFragment extends BaseFragment {

	private FilesListAdapter adapter;
	private ArrayList<File> files = new ArrayList<File>();
	private TextView txv_empty, txv_wait;
	private PullToRefreshListView listView;
	private LinearLayout loading;
	private boolean isCanceled = false;
	private boolean isFirstStart = true;
	
	public FilesFragment() {
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
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_expandable, container, false);
		
		loading = (LinearLayout) rootView.findViewById(R.id.loading);
		txv_wait = (TextView) rootView.findViewById(R.id.txv_wait);
		txv_empty = (TextView) rootView.findViewById(R.id.txv_emptyList);
		
		txv_wait.setTypeface(NSFonts.getNoorFont());
		txv_empty.setTypeface(NSFonts.getNoorFont());
		
		listView = (PullToRefreshListView) rootView.findViewById(R.id.listView);
		listView.getRefreshableView().setDivider(null);
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		((MainActivity)getActivity()).showImageTitle();
		((MainActivity)getActivity()).setImageTitle(R.drawable.btn_folders);
		
		adapter = new FilesListAdapter(getActivity(), files);
		listView.getRefreshableView().setAdapter(adapter);

		if(isFirstStart)
		{
			isFirstStart = false;
			initData();
		}else
			toggleEmptyMessage();
		
		listView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2-1 >= 0){
					File f = files.get(arg2-1);
					String link = f.getLink()+"&NumPager="+f.getCount();
					((MainActivity)getActivity()).gotoListArticlesFragment(link, f.getName(), f.getName());
				}
			}
		});

		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if(!Utils.isOnline(getActivity()))
				{	
					listView.onRefreshComplete();
				}
				else{
					files.clear();
					initData();
				}
			}
		});
		
	}

	private void initData(){
		
		new AsyncTask<Void, Void, ArrayList<File>>() {

			@Override
			protected void onPreExecute() {
				loading.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				txv_empty.setVisibility(View.GONE);
			}
			
			@Override
			protected ArrayList<File> doInBackground(Void... params) {
				try{
					if(!Utils.isOnline(getActivity()) && !listView.isRefreshing())
						return ((NSActivity)getActivity()).NourSouryiaDB.getAllFiles();

					else if(Utils.isOnline(getActivity()))
						return NSManager.getInstance(getActivity()).getFiles();
					
				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<File> result) {
				if(isCanceled)
					return;

				if(result != null){
					files.addAll(result);
				}

				initViewsIfPossible();

			}
		}.execute();

	}
	
//	public class ArticlesAsync extends AsyncTask<Void, Void, ArrayList<Article>>
//	{
//		private final int position;
//		private final String link;
//		
//		public ArticlesAsync(int position, String link) {
//			this.position = position;
//			this.link = link;
//		}
//		
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected ArrayList<Article> doInBackground(Void... params) {
//			Log.v("", " >>>>>>>>>>>> link: "+link);
//			int taskExecutionNumber = executedTasksCount.get();
//            Log.i("", "doInBackground: entered, taskExecutionNumber = " + taskExecutionNumber);
//            ArrayList<Article> articles = NSManager.getInstance(getActivity()).getArticlesByUrl(link); // some job
//            taskExecutionNumber = executedTasksCount.decrementAndGet();
//            Log.i("", "doInBackground: is about to finish, taskExecutionNumber = " + taskExecutionNumber);
//            
//			if(articles != null)
//			{
//				Log.i(TAG, ">>> link("+position+")" + link + " => " + articles.size());
//				files.get(position).getArticles().addAll(articles);
//			}
//
//            if(executedTasksCount.get() == 0)
//            	for(File f : files){
//            		((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateFile(f);
//            	}
//            
//			return articles;
//		}
//
//		@Override
//		protected void onPostExecute(ArrayList<Article> result) {
//			
//			synchronized (executedTasksCount) {
//				if(executedTasksCount.get() == 0){
//					initViewsIfPossible();
//				}
//			}
//
//		}
//	}
	
	private void toggleEmptyMessage() {
		if(files.size() == 0)
			txv_empty.setVisibility(View.VISIBLE);
		else
			txv_empty.setVisibility(View.GONE);
	}
	
	private void initViewsIfPossible(){
		loading.setVisibility(View.GONE);
		listView.setVisibility(View.VISIBLE);

		if(listView.isRefreshing())
			listView.onRefreshComplete();

		if(files != null){
			adapter.notifyDataSetChanged();
		}else
			((MainActivity)getActivity()).showConnectionErrorPopup();

		toggleEmptyMessage();
	}
}
