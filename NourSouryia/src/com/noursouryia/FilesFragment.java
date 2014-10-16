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
	private ExpandableListView expandableLV;
	private LinearLayout loading;
	private boolean isCanceled = false;
	
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
		
		expandableLV = (ExpandableListView) rootView.findViewById(android.R.id.list);
		expandableLV.setGroupIndicator(null);
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		adapter = new FilesAdapter(getActivity(), files);
		expandableLV.setAdapter(adapter);

		initData();
		
		expandableLV.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				Article selectedArticle = files.get(groupPosition).getArticles().get(childPosition);
				((MainActivity)getActivity()).gotoArticleFragment(selectedArticle);
				
				return false;
			}
		});
		
	}

	private void initData(){
		
		new AsyncTask<Void, Void, ArrayList<File>>() {

			@Override
			protected void onPreExecute() {
				files.clear();
				loading.setVisibility(View.VISIBLE);
				expandableLV.setVisibility(View.GONE);
			}
			
			@Override
			protected ArrayList<File> doInBackground(Void... params) {
				try{
					ArrayList<File> list = ((NSActivity)getActivity()).NourSouryiaDB.getAllFiles();

					if(list.size() > 0)
						files.addAll(list);

					else if(NSManager.getInstance(getActivity()).isOnlineMode() && Utils.isOnline(getActivity())){
						files.addAll(NSManager.getInstance(getActivity()).getFiles());

						for(int i=0; i<files.size(); i++){
							File f = files.get(i);

							if(f.getCount() > 0){
								int nbPage = (int) ((f.getCount() / NSManager.MAX_ARTICLE_PER_PAGE));
								if(f.getCount() % NSManager.MAX_ARTICLE_PER_PAGE != 0)
									nbPage += 1;

								for(int j=0; j<nbPage; j++){
									ArrayList<Article> arts = NSManager.getInstance(getActivity()).getArticlesByUrl(f.getLink()+"&page="+j);
									files.get(i).getArticles().addAll(arts);
								}
							}
						}

						for(File f : files){
							((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateFile(f);
						}
					}
				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}
				
				return files;
			}
			
			@Override
			protected void onPostExecute(ArrayList<File> result) {
				loading.setVisibility(View.GONE);
				expandableLV.setVisibility(View.VISIBLE);
				
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
		if(files.size() == 0)
			txv_empty.setVisibility(View.VISIBLE);
		else
			txv_empty.setVisibility(View.GONE);
	}
}
