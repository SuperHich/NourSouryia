package com.noursouryia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.CalendarAdapter;
import com.noursouryia.utils.NSFonts;

public class FragmentThawraDiaries extends BaseFragment{

	public Calendar month;
	public CalendarAdapter adapter;
	public Handler handler;
	public ArrayList<String> items; // container to store some random calendar items

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



}
