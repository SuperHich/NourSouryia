package com.noursouryia;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
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
	protected static final String TAG = null;
	
	private RelativeLayout principal_layout;
	private NSManager mManager;
	
	
	private Handler splashHandler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			
			case MESSAGE_START :
				SplashHome.this.startActivity(new Intent(SplashHome.this, MainActivity.class));
				Utils.animateSlide(SplashHome.this);
				SplashHome.this.finish();
				break;
			
			case MESSAGE_FINISH :
				showWarningPopup();
//				Toast.makeText(SplashHome.this, "No DATA Found!", Toast.LENGTH_LONG).show();
//				SplashHome.this.finish();
				break;
			}
			

			super.handleMessage(msg);
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashhome);
		
		mManager = NSManager.getInstance(this);
		new NSFonts().Init(this);
		
		principal_layout = (RelativeLayout) findViewById(R.id.principal_layout);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		initData();
		
		
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

			@Override
			protected void onPreExecute() {
			}

			@Override
			protected ArrayList<Type> doInBackground(Void... params) {
				ArrayList<Type> types = new ArrayList<Type>();
				try{

					if(Utils.isOnline(SplashHome.this) && mManager.isOnlineMode())
					{
						types =	NSManager.getInstance(SplashHome.this).getTypes(); // OK

						for(Type t : types){
							NourSouryiaDB.insertOrUpdateType(t);
						}
					}else
						types = NourSouryiaDB.getAllTypes();
					

				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}
				return types;
			}

			@Override
			protected void onPostExecute(ArrayList<Type> result) {
				Message msg = Message.obtain();
				if(result.size() > 0){
					msg.what = MESSAGE_START;
				}else
					msg.what = MESSAGE_FINISH;
				
				splashHandler.sendMessageDelayed(msg, SPLASHTIME);
				
			}
		}.execute();

	}
	
	private void showWarningPopup(){
		AlertDialog.Builder builder = new AlertDialog.Builder(SplashHome.this);
        builder.setMessage(R.string.error_internet_connexion)
               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   finish();
                   }
               });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
	}

}