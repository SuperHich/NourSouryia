package com.noursouryia;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.noursouryia.adapters.AlphabetAdapter;
import com.noursouryia.adapters.AuthorsAdapter;
import com.noursouryia.entity.Article;
import com.noursouryia.entity.Author;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;


public class AuthorsFragment extends BaseFragment {
	
	static final int SHOW_TOAST = 10;
	static final int HIDE_TOAST = 20;

	final String[] alphabaticalList = { "أ", "ب", "ت", "ث", "ج",
			"ح", "خ", "د", "ذ", "ر", "ز", "س", "ش", "ص", "ض", "ظ", "ط",
			"ق", "ف", "ع", "غ", "ك", "ل", "م", "ه", "و", "ن", "ي"};
	
	private AuthorsAdapter adapter;
	private ArrayList<Author> authors = new ArrayList<Author>();
	private TextView txv_empty;
	private ExpandableListView expandableLV;
	private ListView sideList;
	private RelativeLayout section_toast_layout;
	private TextView section_toast_text;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case SHOW_TOAST:
				section_toast_layout.setVisibility(View.VISIBLE);
				section_toast_text.setText((String)msg.obj);
				
				removeMessages(HIDE_TOAST);
				
				Message toSendMsg = new Message();
				toSendMsg.what = HIDE_TOAST;
				sendMessageDelayed(toSendMsg, 2000);
				break;
			case HIDE_TOAST:
				section_toast_layout.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	};
	
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
		section_toast_layout = (RelativeLayout) rootView.findViewById(R.id.section_toast_layout);
		section_toast_text = (TextView) rootView.findViewById(R.id.section_toast_text);
		section_toast_text.setTypeface(NSFonts.getNoorFont());
		
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
				
				requestToast(item);
				
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
		
		
		expandableLV.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
//				Toast.makeText(getActivity(), "File at position " + childPosition + "("+ groupPosition + ")", Toast.LENGTH_LONG).show();
				
				Article selectedArticle = authors.get(groupPosition).getArticles().get(childPosition);
				((MainActivity)getActivity()).gotoArticleFragment(selectedArticle);
				
				return false;
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
				
				else if(NSManager.getInstance(getActivity()).isOnlineMode() && Utils.isOnline(getActivity())){
					authors.addAll(NSManager.getInstance(getActivity()).getAuthors());

					for(int i=0; i<authors.size(); i++){
						Author a = authors.get(i);
						
						if(a.getCount() > 0){
							int nbPage = (int) ((a.getCount() / NSManager.MAX_ARTICLE_PER_PAGE));
							if(a.getCount() % NSManager.MAX_ARTICLE_PER_PAGE != 0)
								nbPage += 1;
							
							for(int j=0; j<nbPage; j++){
								ArrayList<Article> arts = NSManager.getInstance(getActivity()).getArticlesByUrl(a.getLink()+"&page="+j);
								authors.get(i).getArticles().addAll(arts);
							}
						}
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
				
//				for(Author au : authors){
//					Log.v("", au.getName());
//					for(Article a : au.getArticles())
//						Log.i("", a.getName());
//				}
				
				toggleEmptyMessage();
			}
		}.execute();

	}
	
	private void toggleEmptyMessage() {
		if(authors.size() == 0){
			txv_empty.setVisibility(View.VISIBLE);
			sideList.setVisibility(View.GONE);
		}else{
			txv_empty.setVisibility(View.GONE);
			sideList.setVisibility(View.VISIBLE);
		}
	}
	
	private void requestToast(String text){
		
		Message msg = new Message();
		msg.what = SHOW_TOAST;
		msg.obj = text;
		mHandler.sendMessage(msg);
			
	}
}
