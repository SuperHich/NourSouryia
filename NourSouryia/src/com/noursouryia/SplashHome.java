package com.noursouryia;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.noursouryia.entity.Type;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;


/**
 * Nour Souryia
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public class SplashHome extends NSActivity {
	
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
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
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
				ArrayList<Type> types =	NSManager.getInstance(SplashHome.this).getTypes(); // OK
				//				NSManager.getInstance(getActivity()).getCommentsByID(6687); // OK
				//				NSManager.getInstance(getActivity()).getFiles(); // OK
//				ArrayList<Author> auths = NSManager.getInstance(SplashHome.this).getAuthors(); // OK
				//				NSManager.getInstance(getActivity()).getPolls(); // OK
				//				NSManager.getInstance(getActivity()).getQuestionByID("http://syrianoor.net/get/poll?qid=5"); // OK
//				ArrayList<Article> articles = NSManager.getInstance(SplashHome.this).getArticles(NSManager.DEFAULT_TIMESTAMP, 
//										NSManager.DEFAULT_VALUE, 
//										NSManager.DEFAULT_VALUE); // OK
				
								
				for(Type t : types){
					NourSouryiaDB.insertOrUpdateType(t);
				}
				
//				for(Author a : auths){
//					NourSouryiaDB.insertOrUpdateAuthor(a);
//				}
				
//				for(Article a : articles){
//					NourSouryiaDB.insertOrUpdateArticle(a, NSManager.DEFAULT_VALUE, NSManager.DEFAULT_VALUE);
//				}
				
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