package com.noursouryia.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Janaez wa Da3wa
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public class NSFonts {

	private static Typeface noorFont, kufah;
	private static Typeface arabic;
	private static Typeface latin;

	public void Init(Context context){

		setArabicFont(Typeface.createFromAsset(context.getAssets(), "fonts/Arabic.TTF"));
		setNoorFont(Typeface.createFromAsset(context.getAssets(), "fonts/GE_Dinar_One_Medium.otf"));
		setKufah(Typeface.createFromAsset(context.getAssets(), "fonts/mohammad bold art 1.ttf"));
		setLatin(Typeface.createFromAsset(context.getAssets(), "fonts/DroidKufi-Regular.ttf"));
	}



	public static Typeface getArabicFont() {
		return arabic;
	}

	public static void setArabicFont(Typeface arabic) {
		NSFonts.arabic = arabic;
	}


	public static Typeface getNoorFont() {
		return noorFont;
	}


	public void setNoorFont(Typeface noorFont) {
		NSFonts.noorFont = noorFont;
	}


	public static Typeface getLatin() {
		return latin;
	}


	public static void setLatin(Typeface latin) {
		NSFonts.latin = latin;
	}


	public static Typeface getKufah() {
		return kufah;
	}


	public static void setKufah(Typeface kufah) {
		NSFonts.kufah = kufah;
	}



}
