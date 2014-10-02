package com.noursouryia.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Janaez wa Da3wa
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public class NSFonts {
	
	private static Typeface noorFont;
	private static Typeface arabic;
	
	public void Init(Context context){
		arabic  				= Typeface.createFromAsset(context.getAssets(), "fonts/Arabic.TTF");
		noorFont  				= Typeface.createFromAsset(context.getAssets(), "fonts/GE_Dinar_One_Medium.otf");
	}

	
	public static Typeface getArabicFont() {
		return arabic;
	}


	public Typeface getNoorFont() {
		return noorFont;
	}


	public void setNoorFont(Typeface noorFont) {
		NSFonts.noorFont = noorFont;
	}
}
