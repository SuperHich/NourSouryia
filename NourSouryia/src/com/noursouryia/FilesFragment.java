package com.noursouryia;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.noursouryia.adapters.FilesAdapter;
import com.noursouryia.entity.Article;
import com.noursouryia.entity.File;
import com.noursouryia.externals.NSManager;


public class FilesFragment extends Fragment {

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
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		adapter = new FilesAdapter(getActivity(), files);
		expandableLV.setAdapter(adapter);
//		getListView().setCacheColorHint(Color.TRANSPARENT);

		initData();
		
//		expandableLV.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				
//				((IndexActivity) getActivity()).onPlaceSelected(places.get(position));
//			}
//		});
		
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
				files.addAll(NSManager.getInstance(getActivity()).getFiles());
				
				for(int i=0; i<files.size(); i++){
					File f = files.get(i);
					ArrayList<Article> arts = NSManager.getInstance(getActivity()).getArticlesByUrl(f.getLink());
					files.get(i).getArticles().addAll(arts);
				}
				
				return files;
			}
			
			@Override
			protected void onPostExecute(ArrayList<File> result) {
				loading.dismiss();
				
				if(result != null){
					adapter.notifyDataSetChanged();
				}
				
				for(File f : files){
					Log.v("", f.getName());
					for(Article a : f.getArticles())
						Log.i("", a.getName());
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
