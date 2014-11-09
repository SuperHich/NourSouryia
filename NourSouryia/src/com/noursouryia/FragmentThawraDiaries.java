package com.noursouryia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.noursouryia.entity.Article;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.CalendarAdapter;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;
import com.noursouryia.viewpager.ArticlePagerAdapter;
import com.noursouryia.viewpager.CirclePageIndicator;
import com.noursouryia.viewpager.PageIndicator;

public class FragmentThawraDiaries extends BaseFragment{

	public static final String ARG_ARTICLE_LINK 	= "article_link";
	public static final String ARG_ARTICLE_CATEGORY = "article_category";
	public static final String ARG_ARTICLE_TITLE 	= "article_title";
	
	public Calendar month;
	public CalendarAdapter adapter;
	public Handler handler;
	public ArrayList<String> items; // container to store some random calendar items

	private LinearLayout all_layout ,month_calendar_layout;
	private RelativeLayout calendar_layout ;

	private TextView monthText, yearText, txv_first , txv_title, txv_second;
	private ImageView nextMonth, previousMonth, img_first;
	private GridView gridview ;
	
	private ArrayList<Article> articles = new ArrayList<Article>();
	private Article currentArticle;
	private LinearLayout loading;
	private boolean isFirstStart = true;
	private boolean isCanceled = false;
	private int pageNb = 0;
	
	private String chosenDate;
	
	private String link, category; 
	
	private ArticlePagerAdapter mAdapter;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	
	private RelativeLayout pager_layout;
	
	@Override
	public void onDetach() {
		super.onDetach();
		
		isCanceled = true;
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getArguments() != null){
			link 		= getArguments().getString(ARG_ARTICLE_LINK);
			category 	= getArguments().getString(ARG_ARTICLE_CATEGORY);
//			imageId 	= getArguments().getInt(ARG_ARTICLE_TITLE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.thawra_diaries2, container, false);

		mPager = (ViewPager) rootView.findViewById(R.id.view_pager);
		mIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
		pager_layout = (RelativeLayout) rootView.findViewById(R.id.pager_layout);
		
		mAdapter = new ArticlePagerAdapter(getFragmentManager(), articles);
		mPager.setAdapter(mAdapter);
		mPager.setSaveEnabled(false);
		mIndicator.setViewPager(mPager);
		
		monthText = (TextView) rootView.findViewById(R.id.month_text);
		yearText = (TextView) rootView.findViewById(R.id.year_text);

		txv_first = (TextView) rootView.findViewById(R.id.txv_first);
		img_first = (ImageView) rootView.findViewById(R.id.img_first);
		txv_title = (TextView) rootView.findViewById(R.id.txv_title);
		txv_second = (TextView) rootView.findViewById(R.id.txv_second);
		
		loading = (LinearLayout) rootView.findViewById(R.id.loading);

		all_layout = (LinearLayout) rootView.findViewById(R.id.all_layout);
		month_calendar_layout =  (LinearLayout) rootView.findViewById(R.id.month_calendar_layout);
		calendar_layout  =  (RelativeLayout) rootView.findViewById(R.id.calendar_layout);

		nextMonth = (ImageView) rootView.findViewById(R.id.next_month);
		previousMonth = (ImageView) rootView.findViewById(R.id.previous_month);

		nextMonth.bringToFront();
		previousMonth.bringToFront();
		
//		calendar_layout.bringToFront();
//		previousMonth.bringToFront();
//		mSlidingLayer.invalidate();
//		all_layout.invalidate();
//		month_calendar_layout.invalidate();

		monthText.setTypeface(NSFonts.getNoorFont());
		yearText.setTypeface(NSFonts.getLatin());

		month = Calendar.getInstance();

		items = new ArrayList<String>();
		adapter = new CalendarAdapter(getActivity(), month);

		gridview = (GridView) rootView.findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		handler = new Handler();
		handler.post(calendarUpdater);


		String monthName = String.format(Locale.US,"%tB",month);
		monthText.setText(monthName+"");
		yearText.setText(month.get(Calendar.YEAR)+"");

		nextMonth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				month.add(Calendar.MONTH, 1);
				refreshCalendar();

				String monthName = String.format(Locale.US,"%tB",month);
				monthText.setText(monthName+"");
				yearText.setText(month.get(Calendar.YEAR)+"");
			}
		});

		previousMonth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				month.add(Calendar.MONTH, -1);
				refreshCalendar();

				String monthName = String.format(Locale.US,"%tB",month);
				monthText.setText(monthName+"");
				yearText.setText(month.get(Calendar.YEAR)+"");

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				TextView date = (TextView)v.findViewById(R.id.date);
				if(date instanceof TextView && !date.getText().equals("")) {

					String day = date.getText().toString();
					if(day.length()==1) {
						day = "0"+day;
					}
					
					month.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
					
					Calendar today = Calendar.getInstance();
					
					if(month.compareTo(today) < 1){

						// return chosen date as string format 
						chosenDate =  android.text.format.DateFormat.format("yyyy-MM", month)+"-"+day;

						initData();

						Toast.makeText(getActivity(), chosenDate, Toast.LENGTH_SHORT).show();
					}else
						Toast.makeText(getActivity(), R.string.no_diaries, Toast.LENGTH_SHORT).show();
				}

			}
		});
		
		if(chosenDate == null)
			chosenDate =  android.text.format.DateFormat.format("yyyy-MM-dd", month).toString();


		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mSlidingLayer.setCloseOnTapEnabled(false);
		mSlidingLayer.setOpenOnTapEnabled(false);

		//		Button btn_opener_top		= (Button) getActivity().findViewById(R.id.btn_opener_top);
		//		
		//		btn_opener_top.setOnClickListener(new OnClickListener() {
		//			@Override
		//			public void onClick(View arg0) {
		//				mSlidingLayer.bringToFront();
		//				all_layout.setVisibility(View.GONE);
		//			}
		//		});

		if(isFirstStart)
		{
			isFirstStart = false;
			initData();
		}

	}

	public void refreshCalendar()
	{

		adapter.refreshDays();
		adapter.notifyDataSetChanged();				
		handler.post(calendarUpdater); // generate some random calendar items				

	}


	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items.clear();
			// format random values. You can implement a dedicated class to provide real values
			for(int i=0;i<31;i++) {
				Random r = new Random();

				if(r.nextInt(10)>6)
				{
					items.add(Integer.toString(i));
				}
			}

			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};


	private void initData(){

		new AsyncTask<Void, Void, ArrayList<Article>>() {

			@Override
			protected void onPreExecute() {
				loading.setVisibility(View.VISIBLE);
//				all_layout.setVisibility(View.GONE);
				pager_layout.setVisibility(View.GONE);
				
				setEnabled(false);
			}

			@Override
			protected ArrayList<Article> doInBackground(Void... params) {
				try{
					if(!NSManager.getInstance(getActivity()).isOnlineMode())
					{
						return ((NSActivity)getActivity()).NourSouryiaDB.getArticlesByStringID(NSManager.TYPE, category);
					}
					else if(Utils.isOnline(getActivity())){

						ArrayList<Article> list = NSManager.getInstance(getActivity()).getArticles(link, NSManager.getTimeStamp(month), NSManager.DEFAULT_VALUE, NSManager.DEFAULT_VALUE);

						if(list.size() > 0)
							for(Article a : list){
								((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateArticle(a, NSManager.DEFAULT_VALUE, NSManager.DEFAULT_VALUE);
							}

						return list;
					}
				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<Article> result) {

				if(isCanceled)
					return;

				loading.setVisibility(View.GONE);
//				all_layout.setVisibility(View.VISIBLE);
				pager_layout.setVisibility(View.VISIBLE);
				setEnabled(true);

				if(result != null){
					articles.clear();
					articles.addAll(result);
					mAdapter.notifyDataSetChanged();
					mIndicator.setCurrentItem(articles.size()-1);
//					pickCurrentArticleByDate(chosenDate);
				}else
					((MainActivity)getActivity()).showConnectionErrorPopup();

//				toggleEmptyMessage();
			}
		}.execute();

	}
	
	private void pickCurrentArticleByDate(String date){
//		for(Article article : articles){
//			if(article.getCreated().equals(date))
//			{
//				Log.i(TAG, "article.getCreated() " + article.getCreated() + " ... date " + date);
//				currentArticle = article;
//				break;
//			}
//		}
		
		if(articles.size() > 0){
			currentArticle = articles.get(0);
			String[] contentParts = ArticleFragment.splitContent(currentArticle.getBody());

			txv_title.setText(currentArticle.getTitle());
			txv_first.setText(ArticleFragment.formatText(contentParts[0]));
			txv_second.setText(ArticleFragment.formatText(contentParts[1]));

			if(currentArticle.getFilePath().size() > 0)
				ImageLoader.getInstance().displayImage(currentArticle.getFilePath().get(0), img_first);
		}
	}
	
	private void setEnabled(boolean enabled){
		nextMonth.setEnabled(enabled);
		previousMonth.setEnabled(enabled);
		gridview.setEnabled(enabled);
	}

}
