package com.noursouryia;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
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
	private PullToRefreshExpandableListView expandableLV;
	private ListView sideList;
	private RelativeLayout section_toast_layout;
	private TextView section_toast_text;
	private LinearLayout loading;
	private boolean isCanceled = false;
	private boolean isFirstStart = true;
	private AtomicInteger executedTasksCount;
	
	private TextView txv_showMore;
	private ProgressBar progressBar;
	private View footer;
	private int pageNb = 0;
	
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
		expandableLV.getRefreshableView().setDivider(null);
		expandableLV.getRefreshableView().addFooterView(footer, null, true);

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

		adapter = new AuthorsAdapter(getActivity(), authors);
		expandableLV.getRefreshableView().setAdapter(adapter);

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
					expandableLV.getRefreshableView().setSelection(posToGo);
//					Toast.makeText(getActivity(), "Going to item at position " + posToGo, Toast.LENGTH_LONG).show();
				}

			}

		});


		expandableLV.getRefreshableView().setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				Article selectedArticle = authors.get(groupPosition).getArticles().get(childPosition);
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
					authors.clear();
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

		new AsyncTask<Void, Void, ArrayList<Author>>() {

			boolean isFromOnline = false;
			
			@Override
			protected void onPreExecute() {
				if(pageNb == 0){
					loading.setVisibility(View.VISIBLE);
					sideList.setVisibility(View.GONE);
					expandableLV.setVisibility(View.GONE);
					txv_empty.setVisibility(View.GONE);
				}else{
					txv_showMore.setVisibility(View.GONE);
					progressBar.setVisibility(View.VISIBLE);
				}

				executedTasksCount = new AtomicInteger();
			}

			@Override
			protected ArrayList<Author> doInBackground(Void... params) {
				try{
					if(!Utils.isOnline(getActivity()) && !expandableLV.isRefreshing())
						return ((NSActivity)getActivity()).NourSouryiaDB.getAllAuthors();

					else if(Utils.isOnline(getActivity())){
						ArrayList<Author> list = NSManager.getInstance(getActivity()).getAuthors(pageNb++);

						if(list != null){
							isFromOnline = list.size() > 0;
							
							for(Author a : list){
								if(a.getCount() > 0){
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
			protected void onPostExecute(ArrayList<Author> result) {
				if(isCanceled)
					return;
				
				if(isFromOnline)
				{
					authors.addAll(result);
					
					for(int i=0; i<result.size(); i++){
						Author a = result.get(i);

						if(a.getCount() > 0){
							ArticlesAsync articleAsync = new ArticlesAsync((pageNb-1)*10+i, a.getLink()+"&NumPager="+a.getCount());
							articleAsync.executeOnExecutor(THREAD_POOL_EXECUTOR);
						}
					}
				}else
				{
					if(result != null){
						authors.addAll(result);
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
				authors.get(position).getArticles().addAll(articles);
			}

            if(executedTasksCount.get() == 0)
            	for(Author a : authors){
            		((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateAuthor(a);
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
		expandableLV.setVisibility(View.VISIBLE);

		txv_showMore.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);

		if(expandableLV.isRefreshing())
			expandableLV.onRefreshComplete();

		if(authors != null){
			adapter.notifyDataSetChanged();
			if(authors.size() % 10 != 0)
				expandableLV.getRefreshableView().removeFooterView(footer);
		}else
			((MainActivity)getActivity()).showConnectionErrorPopup();

		toggleEmptyMessage();
	}

}
