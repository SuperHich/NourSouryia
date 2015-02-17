package com.noursouryia;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.noursouryia.AuthorsFragment.ArticlesAsync;
import com.noursouryia.adapters.FilesAdapter;
import com.noursouryia.entity.Article;
import com.noursouryia.entity.Author;
import com.noursouryia.entity.File;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;


public class FilesFragment extends BaseFragment {

	private FilesAdapter adapter;
	private ArrayList<File> files = new ArrayList<File>();
	private TextView txv_empty, txv_wait;
	private PullToRefreshExpandableListView expandableLV;
	private LinearLayout loading;
	private boolean isCanceled = false;
	private boolean isFirstStart = true;
	private AtomicInteger executedTasksCount;
	
	private TextView txv_showMore;
	private ProgressBar progressBar;
	private View footer;
	private int pageNb = 0;
	
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
		
		//Add footer to items list
		LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footer = vi.inflate(R.layout.list_footer, null);
		txv_showMore = (TextView) footer.findViewById(R.id.txv_showMore);
		progressBar = (ProgressBar) footer.findViewById(R.id.progressBar);
		footer.setBackgroundResource(R.drawable.drawer_subitem_selector);

		txv_showMore.setTypeface(NSFonts.getNoorFont());
		
		txv_wait.setTypeface(NSFonts.getNoorFont());
		txv_empty.setTypeface(NSFonts.getNoorFont());
		
		expandableLV = (PullToRefreshExpandableListView) rootView.findViewById(R.id.listView);
		expandableLV.getRefreshableView().setGroupIndicator(null);
		expandableLV.getRefreshableView().addFooterView(footer, null, true);
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		((MainActivity)getActivity()).showImageTitle();
		((MainActivity)getActivity()).setImageTitle(R.drawable.btn_folders);
		
		adapter = new FilesAdapter(getActivity(), files);
		expandableLV.getRefreshableView().setAdapter(adapter);

		if(isFirstStart)
		{
			isFirstStart = false;
			initData();
		}else
			toggleEmptyMessage();
		
		expandableLV.getRefreshableView().setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				Article selectedArticle = files.get(groupPosition).getArticles().get(childPosition);
				((MainActivity)getActivity()).gotoArticleFragment(selectedArticle);
				
				return false;
			}
		});
		
		expandableLV.setOnRefreshListener(new OnRefreshListener<ExpandableListView>() {

			@Override
			public void onRefresh(
					PullToRefreshBase<ExpandableListView> refreshView) {
				if(!Utils.isOnline(getActivity()))
				{	
//					((MainActivity)getActivity()).showOnLineModePopup();
					expandableLV.onRefreshComplete();
				}
				else{
					pageNb = 0;
					files.clear();
					initData();
				}
			}
		});
		
		footer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Utils.isOnline(getActivity()))
				{
					initData();
				}
			}
		});
		
	}

	private void initData(){
		
		new AsyncTask<Void, Void, ArrayList<File>>() {

			boolean isFromOnline = false;
			
			@Override
			protected void onPreExecute() {
				if(pageNb == 0){
					loading.setVisibility(View.VISIBLE);
					expandableLV.setVisibility(View.GONE);
					txv_empty.setVisibility(View.GONE);
				}else{
					txv_showMore.setVisibility(View.GONE);
					progressBar.setVisibility(View.VISIBLE);
				}

				executedTasksCount = new AtomicInteger();
			}
			
			@Override
			protected ArrayList<File> doInBackground(Void... params) {
				try{
					if(!Utils.isOnline(getActivity()) && !expandableLV.isRefreshing())
						return ((NSActivity)getActivity()).NourSouryiaDB.getAllFiles();

					else if(Utils.isOnline(getActivity())){
						ArrayList<File> list = NSManager.getInstance(getActivity()).getFiles(pageNb++);

						if(list != null){
							isFromOnline = list.size() > 0;
							
							for(File f : list){
								if(f.getCount() > 0){
									executedTasksCount.incrementAndGet();
								}
							}
						}

						return list;
					}
				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<File> result) {
				if(isCanceled)
					return;
				
				if(isFromOnline)
				{
					files.addAll(result);
					
					for(int i=0; i<result.size(); i++){
						File f = result.get(i);

						if(f.getCount() > 0){
							ArticlesAsync articleAsync = new ArticlesAsync((pageNb-1)*10+i, f.getLink()+"&NumPager="+f.getCount());
							articleAsync.executeOnExecutor(THREAD_POOL_EXECUTOR);
						}
					}
				}else
				{
					if(result != null){
						files.addAll(result);
					}
					
					initViewsIfPossible();
				}
			}
		}.execute();

	}
	
	public class ArticlesAsync extends AsyncTask<Void, Void, ArrayList<Article>>
	{
		private final int position;
		private final String link;
		
		public ArticlesAsync(int position, String link) {
			this.position = position;
			this.link = link;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<Article> doInBackground(Void... params) {
			Log.v("", " >>>>>>>>>>>> link: "+link);
			int taskExecutionNumber = executedTasksCount.get();
            Log.i("", "doInBackground: entered, taskExecutionNumber = " + taskExecutionNumber);
            ArrayList<Article> articles = NSManager.getInstance(getActivity()).getArticlesByUrl(link); // some job
            taskExecutionNumber = executedTasksCount.decrementAndGet();
            Log.i("", "doInBackground: is about to finish, taskExecutionNumber = " + taskExecutionNumber);
            
			if(articles != null)
			{
				Log.i(TAG, ">>> link("+position+")" + link + " => " + articles.size());
				files.get(position).getArticles().addAll(articles);
			}

            if(executedTasksCount.get() == 0)
            	for(File f : files){
            		((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateFile(f);
            	}
            
			return articles;
		}

		@Override
		protected void onPostExecute(ArrayList<Article> result) {
			
			synchronized (executedTasksCount) {
				if(executedTasksCount.get() == 0){
					initViewsIfPossible();
				}
			}

		}
	}
	
	private void toggleEmptyMessage() {
		if(files.size() == 0)
			txv_empty.setVisibility(View.VISIBLE);
		else
			txv_empty.setVisibility(View.GONE);
	}
	
	private void initViewsIfPossible(){
		loading.setVisibility(View.GONE);
		expandableLV.setVisibility(View.VISIBLE);

		txv_showMore.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);

		if(expandableLV.isRefreshing())
			expandableLV.onRefreshComplete();

		if(files != null){
			adapter.notifyDataSetChanged();
			if(files.size() % 10 != 0)
				expandableLV.getRefreshableView().removeFooterView(footer);
			
		}else
			((MainActivity)getActivity()).showConnectionErrorPopup();

		toggleEmptyMessage();
	}
}
