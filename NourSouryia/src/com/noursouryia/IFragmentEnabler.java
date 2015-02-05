package com.noursouryia;

public interface IFragmentEnabler {
	
	void setEnabled(boolean enable);
	void onFolderClicked(int folderId);
	void resetSearch(String keyword);
}
