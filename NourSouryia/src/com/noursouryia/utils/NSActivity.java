package com.noursouryia.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.noursouryia.externals.NSDatabaseManager;

public class NSActivity extends FragmentActivity{

	public NSDatabaseManager NourSouryiaDB;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		NourSouryiaDB = new NSDatabaseManager(this);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(NourSouryiaDB == null)
			NourSouryiaDB = new NSDatabaseManager(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if(NourSouryiaDB != null){
			NourSouryiaDB.close();
			NourSouryiaDB = null;
		}
	}
	
}
