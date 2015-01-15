package com.noursouryia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noursouryia.utils.BaseFragment;

public class TahdhibFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.tahdhib_fragment, container, false);
		
		return rootView;
	}
	
}
