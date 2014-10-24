package com.noursouryia;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.noursouryia.adapters.FilesAdapter;
import com.noursouryia.entity.Article;
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
		
		expandableLV = (PullToRefreshExpandableListView) rootView.findViewById(R.id.listView);
		expandableLV.getRefreshableView().setGroupIndicator(null);
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
		}
		
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
				if(!NSManager.getInstance(getActivity()).isOnlineMode())
				{	
					((MainActivity)getActivity()).showOnLineModePopup();
					expandableLV.onRefreshComplete();
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
				expandableLV.setVisibility(View.GONE);
			}
			
			@Override
			protected ArrayList<File> doInBackground(Void... params) {
				try{
					if(!NSManager.getInstance(getActivity()).isOnlineMode() && !expandableLV.isRefreshing())
					{
						return ((NSActivity)getActivity()).NourSouryiaDB.getAllFiles();
					}
					else if(Utils.isOnline(getActivity())){
						ArrayList<File> list = NSManager.getInstance(getActivity()).getFiles();

						for(int i=0; i<list.size(); i++){
							File f = list.get(i);

							if(f.getCount() > 0){
								ArrayList<Article> arts = NSManager.getInstance(getActivity()).getArticlesByUrl(f.getLink()+"&NumPager="+f.getCount());
								list.get(i).getArticles().addAll(arts);
							}
						}

						for(File f : list){
							((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateFile(f);
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
				
				loading.setVisibility(View.GONE);
				expandableLV.setVisibility(View.VISIBLE);
				
				if(expandableLV.isRefreshing())
					expandableLV.onRefreshComplete();
				
				if(result != null){
					files.clear();
					files.addAll(result);
					adapter.notifyDataSetChanged();
				}else
					((MainActivity)getActivity()).showConnectionErrorPopup();
				
				toggleEmptyMessage();
			}
		}.execute();

	}
	
	private void toggleEmptyMessage() {
		if(files.size() == 0)
			txv_empty.setVisibility(View.VISIBLE);
		else
			txv_empty.setVisibility(View.GONE);
	}
}
