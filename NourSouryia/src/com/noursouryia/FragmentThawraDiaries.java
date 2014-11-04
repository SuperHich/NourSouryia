package com.noursouryia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView.InternalListView;
import com.noursouryia.entity.Article;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.CalendarAdapter;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;

public class FragmentThawraDiaries extends BaseFragment{

	public Calendar month;
	public CalendarAdapter adapter;
	public Handler handler;
	public ArrayList<String> items; // container to store some random calendar items

	private LinearLayout all_layout ;
	private RelativeLayout calendar_layout ;

	private TextView monthText, yearText, txv_first , txv_title, txv_second;
	private ImageView nextMonth, previousMonth, img_first  ;
	private GridView gridview ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.thawra_diaries, container, false);

		monthText = (TextView) rootView.findViewById(R.id.month_text);
		yearText = (TextView) rootView.findViewById(R.id.year_text);

		txv_first = (TextView) rootView.findViewById(R.id.txv_first);
		img_first = (ImageView) rootView.findViewById(R.id.img_first);
		txv_title = (TextView) rootView.findViewById(R.id.txv_title);
		txv_second = (TextView) rootView.findViewById(R.id.txv_second);


		all_layout = (LinearLayout) rootView.findViewById(R.id.all_layout);
		//		all_layout.bringToFront();
		all_layout.bringChildToFront(calendar_layout);
		all_layout.bringChildToFront(previousMonth);
		all_layout.bringChildToFront(nextMonth);

		nextMonth = (ImageView) rootView.findViewById(R.id.next_month);
		previousMonth = (ImageView) rootView.findViewById(R.id.previous_month);

		nextMonth.bringToFront();
		previousMonth.bringToFront();

		monthText.setTypeface(NSFonts.getNoorFont());
		yearText.setTypeface(NSFonts.getLatin());
		txv_first.setTypeface(NSFonts.getNoorFont());
		txv_title.setTypeface(NSFonts.getNoorFont());
		txv_second.setTypeface(NSFonts.getNoorFont());

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
					// return chosen date as string format 
					String chosenDate =  android.text.format.DateFormat.format("yyyy-MM", month)+"-"+day;

					Toast.makeText(getActivity(), chosenDate, Toast.LENGTH_SHORT).show();

				}

			}
		});


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


//	private void initData(){
//
//		new AsyncTask<Void, Void, ArrayList<Article>>() {
//
//			@Override
//			protected void onPreExecute() {
//				loading.setVisibility(View.VISIBLE);
//				all_layout.setVisibility(View.GONE);
//			}
//
//			@Override
//			protected ArrayList<Article> doInBackground(Void... params) {
//				try{
//					if(!NSManager.getInstance(getActivity()).isOnlineMode() && !listView.isRefreshing())
//					{
//						return ((NSActivity)getActivity()).NourSouryiaDB.getArticlesByStringID(NSManager.TYPE, category);
//					}
//					else if(Utils.isOnline(getActivity())){
//						if(pageNb == 0)
//							articles.clear();
//
//						ArrayList<Article> list = NSManager.getInstance(getActivity()).getArticlesByUrl(link+"&page="+pageNb++);
//
//						if(list.size() > 0)
//							for(Article a : list){
//								((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateArticle(a, NSManager.DEFAULT_VALUE, NSManager.DEFAULT_VALUE);
//							}
//
//						return list;
//					}
//				}catch(Exception e){
//					Log.e(TAG, "Error while initData !");
//				}
//				return null;
//			}
//
//			@Override
//			protected void onPostExecute(ArrayList<Article> result) {
//
//				if(isCanceled)
//					return;
//
//				loading.setVisibility(View.GONE);
//				listView.setVisibility(View.VISIBLE);
//				img_title.setVisibility(View.VISIBLE);
//
//				((InternalListView)listView.getRefreshableView()).onLoadMoreComplete();
//
//				if(listView.isRefreshing())
//					listView.onRefreshComplete();
//
//				if(result != null){
//
//					if(pageNb == 1)
//						listView.getRefreshableView().addFooterView(footer, null, true);
//					else 
//						if(pageNb == 2)
//							listView.getRefreshableView().removeFooterView(footer);
//
//					articles.addAll(result);
//					adapter.notifyDataSetChanged();
//				}else
//					((MainActivity)getActivity()).showConnectionErrorPopup();
//
//				toggleEmptyMessage();
//			}
//		}.execute();
//
//	}

}
