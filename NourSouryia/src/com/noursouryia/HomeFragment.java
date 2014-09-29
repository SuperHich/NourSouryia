package com.noursouryia;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noursouryia.entity.Type;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.NSFonts;


public class HomeFragment extends Fragment {

	private TextView txv_empty, top_header ;
	
	public HomeFragment() {
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
		
		View rootView = inflater.inflate(R.layout.fragment_places, container, false);
		
		txv_empty = (TextView) rootView.findViewById(R.id.txv_emptyList);
		top_header =  (TextView) rootView.findViewById(R.id.top_header);
		
		top_header.setTypeface(NSFonts.getBDRFont());
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initData();
		
	}

	private void initData(){
		
		new AsyncTask<Void, Void, ArrayList<Type>>() {

			private ProgressDialog loading;

			@Override
			protected void onPreExecute() {
//				places.clear();
				loading = new ProgressDialog(getActivity());
				loading.setCancelable(false);
				loading.setMessage(getString(R.string.please_wait));
				loading.show();
			}
			
			@Override
			protected ArrayList<Type> doInBackground(Void... params) {
//				NSManager.getInstance(getActivity()).getTypes(); // OK
//				NSManager.getInstance(getActivity()).getCommentsByID(6687); // OK
//				NSManager.getInstance(getActivity()).getFiles(); // OK
//				NSManager.getInstance(getActivity()).getAuthors(); // OK
//				NSManager.getInstance(getActivity()).getPolls(); // OK
//				NSManager.getInstance(getActivity()).getQuestionByID("http://syrianoor.net/get/poll?qid=5"); // OK
//				NSManager.getInstance(getActivity()).getArticles(Calendar.getInstance().getTimeInMillis(), 10, 1); // OK
				return null;
			}
			
			@Override
			protected void onPostExecute(ArrayList<Type> result) {
				loading.dismiss();
				
				if(result != null){
				}
//				toggleEmptyMessage();
			}
		}.execute();

	}
	
//	private void toggleEmptyMessage() {
//		if(places.size() == 0)
//			txv_empty.setVisibility(View.VISIBLE);
//		else
//			txv_empty.setVisibility(View.GONE);
//	}
}
