package com.noursouryia;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.noursouryia.adapters.FilesAdapter;
import com.noursouryia.entity.File;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;


public class FilesFragment extends BaseFragment {

	private FilesAdapter adapter;
	private ArrayList<File> files = new ArrayList<File>();
	private TextView txv_empty;
	private ExpandableListView expandableLV;
	
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
		
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_expandable, container, false);
		
		txv_empty = (TextView) rootView.findViewById(R.id.txv_emptyList);
		expandableLV = (ExpandableListView) rootView.findViewById(android.R.id.list);
		expandableLV.setGroupIndicator(null);
		expandableLV.setDivider(null);
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
				
				Toast.makeText(getActivity(), "File at position " + childPosition + "("+ groupPosition + ")", Toast.LENGTH_LONG).show();
				return false;
			}
		});
		
	}

	private void initData(){
		
		new AsyncTask<Void, Void, ArrayList<File>>() {

			private ProgressDialog loading;

			@Override
			protected void onPreExecute() {
				files.clear();
				loading = new ProgressDialog(getActivity());
				loading.setCancelable(false);
				loading.setMessage(getString(R.string.please_wait));
				loading.show();
			}
			
			@Override
			protected ArrayList<File> doInBackground(Void... params) {
//				if(NSManager.getInstance(getActivity()).isOnlineMode()){
//					files.addAll(NSManager.getInstance(getActivity()).getFiles());
//
//					for(int i=0; i<files.size(); i++){
//						File f = files.get(i);
//						ArrayList<Article> arts = NSManager.getInstance(getActivity()).getArticlesByUrl(f.getLink());
//						files.get(i).getArticles().addAll(arts);
//					}
//
//					for(File f : files){
//						((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateFile(f);
//					}
//				}else
					files.addAll(((NSActivity)getActivity()).NourSouryiaDB.getAllFiles());
				
				return files;
			}
			
			@Override
			protected void onPostExecute(ArrayList<File> result) {
				loading.dismiss();
				
				if(result != null){
					adapter.notifyDataSetChanged();
				}
				
//				for(File f : files){
//					Log.v("", f.getName());
//					for(Article a : f.getArticles())
//						Log.i("", a.getName());
//				}
				
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
