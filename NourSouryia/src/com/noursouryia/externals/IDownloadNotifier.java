package com.noursouryia.externals;

/**
 * AlMoufasserAlSaghir
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public interface IDownloadNotifier {

	void onProgressDownload(int progress);
	void onDownloadComplete(String path);
	void onErrorDownload();
	
}
