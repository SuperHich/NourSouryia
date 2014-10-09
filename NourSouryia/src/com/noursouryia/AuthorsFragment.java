package com.noursouryia;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.noursouryia.adapters.AlphabetAdapter;
import com.noursouryia.adapters.AuthorsAdapter;
import com.noursouryia.entity.Article;
import com.noursouryia.entity.Author;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;


public class AuthorsFragment extends BaseFragment {

	final String[] alphabaticalList = { "أ", "ب", "ت", "ث", "ج",
			"ح", "خ", "د", "ذ", "ر", "ز", "س", "ش", "ص", "ض", "ظ", "ط",
			"ق", "ف", "ع", "غ", "ك", "ل", "م", "ه", "و", "ن", "ي"};
	
	private AuthorsAdapter adapter;
	private ArrayList<Author> authors = new ArrayList<Author>();
	private TextView txv_empty;
	private ExpandableListView expandableLV;
	private ListView sideList;
	
	public AuthorsFragment() {
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
		
		View rootView = inflater.inflate(R.layout.fragment_indexed_list, container, false);
		
		sideList = (ListView) rootView.findViewById(R.id.sideIndex);
		
		txv_empty = (TextView) rootView.findViewById(R.id.txv_emptyList);
		expandableLV = (ExpandableListView) rootView.findViewById(android.R.id.list);
		expandableLV.setGroupIndicator(null);
		expandableLV.setDivider(null);
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// side list
		AlphabetAdapter alphabaticalListAdapter = new AlphabetAdapter(getActivity(), R.layout.alphabet_item, alphabaticalList);
		sideList.setAdapter(alphabaticalListAdapter);
		
		adapter = new AuthorsAdapter(getActivity(), authors);
		expandableLV.setAdapter(adapter);

		initData();
		
		sideList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String item = (String) sideList.getAdapter().getItem(position);
				int posToGo = -1;
				for (int i = 0; i < authors.size(); i++) {
					String name = authors.get(i).getName();
					String subName = name.substring(0, 1);
					if (subName.startsWith(item)) {
						posToGo = i;
						break;
					}
				}
				
				if(posToGo != -1){
					expandableLV.setSelection(posToGo);
					Toast.makeText(getActivity(), "Going to item at position " + posToGo, Toast.LENGTH_LONG).show();
				}
				
			}

		});
		
	}

	private void initData(){
		
		new AsyncTask<Void, Void, ArrayList<Author>>() {

			private ProgressDialog loading;

			@Override
			protected void onPreExecute() {
				authors.clear();
				loading = new ProgressDialog(getActivity());
				loading.setCancelable(false);
				loading.setMessage(getString(R.string.please_wait));
				loading.show();
			}
			
			@Override
			protected ArrayList<Author> doInBackground(Void... params) {
				
				ArrayList<Author> list = ((NSActivity)getActivity()).NourSouryiaDB.getAllAuthors();
				
				if(list.size() > 0)
					authors.addAll(list);
				
				else if(NSManager.getInstance(getActivity()).isOnlineMode()){
					authors.addAll(NSManager.getInstance(getActivity()).getAuthors());

					for(int i=0; i<authors.size(); i++){
						Author a = authors.get(i);
						ArrayList<Article> arts = NSManager.getInstance(getActivity()).getArticlesByUrl(a.getLink());
						authors.get(i).getArticles().addAll(arts);
					}

					for(Author a : authors){
						((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateAuthor(a);
					}

				}

				return authors;
			}
			
			@Override
			protected void onPostExecute(ArrayList<Author> result) {
				loading.dismiss();
				
				if(result != null){
					adapter.notifyDataSetChanged();
				}
				
				for(Author au : authors){
					Log.v("", au.getName());
					for(Article a : au.getArticles())
						Log.i("", a.getName());
				}
				
				toggleEmptyMessage();
			}
		}.execute();

	}
	
	private void toggleEmptyMessage() {
		if(authors.size() == 0)
			txv_empty.setVisibility(View.VISIBLE);
		else
			txv_empty.setVisibility(View.GONE);
	}
}
