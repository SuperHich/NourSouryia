package com.noursouryia;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
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
	private TextView txv_empty, txv_wait;
	private PullToRefreshScrollView pullToRefreshView;
	private ExpandableListView expandableLV;
	private ListView sideList;
	private RelativeLayout section_toast_layout;
	private TextView section_toast_text;
	private LinearLayout loading;
	private boolean isCanceled = false;
	
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
		
		isCanceled = true;
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_indexed_list, container, false);
		
		pullToRefreshView = (PullToRefreshScrollView) rootView.findViewById(R.id.pullToRefreshView);
		sideList = (ListView) rootView.findViewById(R.id.sideIndex);
		section_toast_layout = (RelativeLayout) rootView.findViewById(R.id.section_toast_layout);
		section_toast_text = (TextView) rootView.findViewById(R.id.section_toast_text);
		section_toast_text.setTypeface(NSFonts.getNoorFont());
		
		loading = (LinearLayout) rootView.findViewById(R.id.loading);
		txv_wait = (TextView) rootView.findViewById(R.id.txv_wait);
		txv_empty = (TextView) rootView.findViewById(R.id.txv_emptyList);
		
		txv_wait.setTypeface(NSFonts.getNoorFont());
		txv_empty.setTypeface(NSFonts.getNoorFont());
		
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
				
				Article selectedArticle = authors.get(groupPosition).getArticles().get(childPosition);
				((MainActivity)getActivity()).gotoArticleFragment(selectedArticle);
				
				return false;
			}
		});
		
		pullToRefreshView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if(!NSManager.getInstance(getActivity()).isOnlineMode())
				{	
					((MainActivity)getActivity()).showOnLineModePopup();
					pullToRefreshView.onRefreshComplete();
				}
				else{
					authors.clear();
					initData();
				}
			}
		});
	}

	private void initData(){
		
		new AsyncTask<Void, Void, ArrayList<Author>>() {

			@Override
			protected void onPreExecute() {
				loading.setVisibility(View.VISIBLE);
				sideList.setVisibility(View.GONE);
				expandableLV.setVisibility(View.GONE);
			}
			
			@Override
			protected ArrayList<Author> doInBackground(Void... params) {
				try{
					if(!NSManager.getInstance(getActivity()).isOnlineMode() && !pullToRefreshView.isRefreshing())
						return ((NSActivity)getActivity()).NourSouryiaDB.getAllAuthors();

					else if(Utils.isOnline(getActivity())){
						ArrayList<Author> list = NSManager.getInstance(getActivity()).getAuthors();

						for(int i=0; i<list.size(); i++){
							Author a = list.get(i);

							if(a.getCount() > 0){
								ArrayList<Article> arts = NSManager.getInstance(getActivity()).getArticlesByUrl(a.getLink()+"&NumPager="+a.getCount());
								list.get(i).getArticles().addAll(arts);
							}
						}

						for(Author a : list){
							((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateAuthor(a);
						}

						return list;
					}
				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}

				return null;
			}
			
			@Override
			protected void onPostExecute(ArrayList<Author> result) {
				if(isCanceled)
					return;
				
				loading.setVisibility(View.GONE);
				sideList.setVisibility(View.VISIBLE);
				expandableLV.setVisibility(View.VISIBLE);
				
				if(pullToRefreshView.isRefreshing())
					pullToRefreshView.onRefreshComplete();
				
				if(result != null){
					authors.clear();
					authors.addAll(result);
					adapter.notifyDataSetChanged();
				}
				
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
