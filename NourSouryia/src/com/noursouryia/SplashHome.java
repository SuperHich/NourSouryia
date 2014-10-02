package com.noursouryia;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.noursouryia.entity.Type;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;


/**
 * Sa7i7 Al Boukhari
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public class SplashHome extends Activity {
	
	private static final int STOPSPLASH = 0;
	private static final long SPLASHTIME = 1500;
	
	public static final int MESSAGE_START = 1;
	public static final int MESSAGE_FINISH = 2;
	
	private RelativeLayout principal_layout;
	public NSFonts mNSFonts ;
	
	private Handler splashHandler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
				
			case MESSAGE_FINISH :
				
				SplashHome.this.startActivity(new Intent(SplashHome.this, MainActivity.class));
				Utils.animateSlide(SplashHome.this);
				SplashHome.this.finish();
				
				break;

			}
			

			super.handleMessage(msg);
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashhome);
		
		principal_layout = (RelativeLayout) findViewById(R.id.principal_layout);
		mNSFonts = new NSFonts() ;
//		Message msg = Message.obtain();
//		msg.what = MESSAGE_FINISH;
//	    splashHandler.sendMessageDelayed(msg, SPLASHTIME);
		
		initData();
		mNSFonts.Init(this);
		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			splashHandler.removeMessages(STOPSPLASH);
		}
		return super.onKeyDown(keyCode, event);

	}	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Utils.cleanViews(principal_layout);
	}
	
	private void initData(){

		new AsyncTask<Void, Void, ArrayList<Type>>() {

//			private ProgressDialog loading;

			@Override
			protected void onPreExecute() {
				//				places.clear();
//				loading = new ProgressDialog(SplashHome.this);
//				loading.setCancelable(false);
//				loading.setMessage(getString(R.string.please_wait));
//				loading.show();
			}

			@Override
			protected ArrayList<Type> doInBackground(Void... params) {
								NSManager.getInstance(SplashHome.this).getTypes(); // OK
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
//				loading.dismiss();

				if(result != null){
				}
				//				toggleEmptyMessage();
				Message msg = Message.obtain();
				msg.what = MESSAGE_FINISH;
				 splashHandler.sendMessageDelayed(msg, SPLASHTIME);
			}
		}.execute();

	}

}