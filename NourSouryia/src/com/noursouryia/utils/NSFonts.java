package com.noursouryia.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Janaez wa Da3wa
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public class NSFonts {
	
	private static Typeface bdr_gr_2;
	private static Typeface arabic;
	
	public static void Init(Context context){
		bdr_gr_2  				= Typeface.createFromAsset(context.getAssets(), "fonts/bdr_gr_2.ttf");
		arabic  				= Typeface.createFromAsset(context.getAssets(), "fonts/Arabic.TTF");
	}

	public static Typeface getBDRFont() {
		return bdr_gr_2;
	}
	
	public static Typeface getArabicFont() {
		return arabic;
	}
}
