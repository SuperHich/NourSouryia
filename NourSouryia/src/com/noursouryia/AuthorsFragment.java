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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.noursouryia.adapters.AlphabetAdapter;
import com.noursouryia.adapters.AuthorsListAdapter;
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

	private AuthorsListAdapter adapter;
	private ArrayList<Author> authors = new ArrayList<Author>();
	private TextView txv_empty, txv_wait;
	private PullToRefreshListView listView;
	private ListView sideList;
	private RelativeLayout section_toast_layout;
	private TextView section_toast_text;
	private LinearLayout loading;
	private boolean isCanceled = false;
	private boolean isFirstStart = true;
	
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
		((MainActivity)getActivity()).hideImageTitle();
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
		((MainActivity)getActivity()).setImageTitle(R.drawable.btn_writers);
		
		// side list
		AlphabetAdapter alphabaticalListAdapter = new AlphabetAdapter(getActivity(), R.layout.alphabet_item, alphabaticalList);
		sideList.setAdapter(alphabaticalListAdapter);

		adapter = new AuthorsListAdapter(getActivity(), authors);
		listView.getRefreshableView().setAdapter(adapter);

		if(isFirstStart)
		{
			isFirstStart = false;
			initData();
		}else
			toggleEmptyMessage();

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
					listView.getRefreshableView().setSelection(posToGo);
//					Toast.makeText(getActivity(), "Going to item at position " + posToGo, Toast.LENGTH_LONG).show();
				}

			}

		});


		listView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2-1 >= 0){
					Author a = authors.get(arg2-1);
					String link = a.getLink()+"&NumPager="+a.getCount();
					((MainActivity)getActivity()).gotoListArticlesFragment(link, a.getName(), a.getName());
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
				listView.setVisibility(View.GONE);
				txv_empty.setVisibility(View.GONE);
			}

			@Override
			protected ArrayList<Author> doInBackground(Void... params) {
				try{
					if(!Utils.isOnline(getActivity()) && !listView.isRefreshing())
						return ((NSActivity)getActivity()).NourSouryiaDB.getAllAuthors();

					else if(Utils.isOnline(getActivity())){
						return NSManager.getInstance(getActivity()).getAuthors();

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

				if(result != null){
					authors.addAll(result);
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
//				authors.get(position).getArticles().addAll(articles);
//			}
//
//            if(executedTasksCount.get() == 0)
//            	for(Author a : authors){
//            		((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateAuthor(a);
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
	
	private void initViewsIfPossible(){
		loading.setVisibility(View.GONE);
		sideList.setVisibility(View.VISIBLE);
		listView.setVisibility(View.VISIBLE);

		if(listView.isRefreshing())
			listView.onRefreshComplete();

		if(authors != null){
			adapter.notifyDataSetChanged();
		}else
			((MainActivity)getActivity()).showConnectionErrorPopup();

		toggleEmptyMessage();
	}

}
