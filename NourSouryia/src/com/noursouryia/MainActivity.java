package com.noursouryia;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.noursouryia.SearchDialog.EditNameDialogListener;
import com.noursouryia.adapters.IMenuListener;
import com.noursouryia.adapters.MenuCustomAdapter;
import com.noursouryia.externals.NSManager;

public class MainActivity extends FragmentActivity implements IMenuListener, OnTouchListener, EditNameDialogListener{

//	public static final String MOSQUES_FRAGMENT = "mosques_fragment";
//	public static final String JANAEZ_FRAGMENT = "janaez_fragment";
//	public static final String DA3AWI_FRAGMENT = "da3awi_fragment";
	
	public static final String 	DEFAULT_FRAG_POSITION 	= "default_frag_position";
	public static final String 	SELECTED_PLACE 			= "selected_place";
	
	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	private Button btn_menu, btn_search;
	private ImageView header ;

	private ActionBarDrawerToggle mDrawerToggle;
	RelativeLayout mainView ;

	public static final int MESSAGE_START = 1;
	private int lastPosition = 0;
	private String lastText = "";
	private boolean isFirstStart = true;
	
	private Fragment fragment, fragment1;
	private String currentFragment;
	
	private boolean isBackEnabled = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		header = (ImageView) findViewById(R.id.header);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.right_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		MenuCustomAdapter adapter = new MenuCustomAdapter(this, NSManager.getInstance(this).getCurrent_types());


		mDrawerList.setAdapter(adapter);
		mDrawerList.setDivider(null);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mainView = (RelativeLayout) findViewById(R.id.content_frame);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 

				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				supportInvalidateOptionsMenu();
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				mainView.setTranslationX(- slideOffset * drawerView.getWidth());
				mDrawerLayout.bringChildToFront(drawerView);
				mDrawerLayout.requestLayout();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		//		if (savedInstanceState == null) {
		//			selectItem(1);
		//		}

		btn_menu = (Button) findViewById(R.id.menu);
		btn_menu.setOnTouchListener(this);
		btn_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
					mDrawerLayout.openDrawer(Gravity.RIGHT);
				else
					mDrawerLayout.closeDrawer(Gravity.RIGHT);		
			}
		});

		btn_search = (Button) findViewById(R.id.search);
		btn_search.setOnTouchListener(this);

		
//		lastPosition = getIntent().getExtras().getInt(DEFAULT_FRAG_POSITION);
//		selected_placeID = getIntent().getExtras().getInt(SELECTED_PLACE);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}


	@Override
	protected void onStart() {
		super.onStart();

		if(isFirstStart){
			selectItem(lastPosition);
			
			isFirstStart = false;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		{
			selectItem(position);
		}
	}


	private void selectItem(int position) {
		
		btn_menu.setBackgroundResource(R.drawable.menu);

		lastPosition = position;
		boolean shouldSwitch = true;
		
//		Bundle args = null;
		// update the main content by replacing fragments
		
		switch (position) {
		case 0:
			fragment = new HomeFragment();
//			btn_search.setVisibility(View.VISIBLE);
//			currentFragment = MOSQUES_FRAGMENT;
//			header.setBackgroundResource(R.drawable.jana2ez);
			break;
//		case 1:
//			fragment = new Da3waFragment();
//			btn_search.setVisibility(View.VISIBLE);
//			currentFragment = DA3AWI_FRAGMENT;
//			header.setBackgroundResource(R.drawable.dawrat_header);
//			
//			break;
		default:
			shouldSwitch = false;
			break;

		}

//		if(args != null)
//			fragment.setArguments(args);

		if(shouldSwitch)
			switchTab(fragment, false);

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);

	}


	private void switchTab(Fragment tab, boolean withAnimation) {
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.content_frame);

		final FragmentTransaction ft = fm.beginTransaction();
		if(withAnimation)
			ft.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
		
		if (fragment == null) {
			ft.add(R.id.content_frame, tab);

		} else {
			ft.replace(R.id.content_frame, tab);
		}
		
		ft.commit();
		
	}

	@Override
	public void onMenuItemClicked(int position) {
		selectItem(position);
	}


		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				Button view = (Button) v;
				view.getBackground().setColorFilter(0x77000000, 

						PorterDuff.Mode.SRC_ATOP);
				v.invalidate();
				break;
			}
			case MotionEvent.ACTION_UP: {
				
				Button view = (Button) v;
				view.getBackground().clearColorFilter();
				view.invalidate();

				switch (v.getId()) {
				case R.id.menu:
//					if(isBackEnabled)
//					{		
//						onBackPressed();
//					}
//					else{
						if(!mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
							mDrawerLayout.openDrawer(Gravity.RIGHT);
						else
							mDrawerLayout.closeDrawer(Gravity.RIGHT);		
//					}
					break;
				case R.id.search:
					//show search dialog;
					showSearchDialog();
					break;
				default:
					break;
				}

			}
			case MotionEvent.ACTION_CANCEL: {
				Button view = (Button) v;
				view.getBackground().clearColorFilter();
				view.invalidate();
				break;
			}
			}
			return true;
		}

		private void showSearchDialog() {
			FragmentManager fm = getSupportFragmentManager();
			SearchDialog searchDialog = new SearchDialog(lastText);
			searchDialog.show(fm, "fragment_search_keyword");
		}

		@Override
		public void onFinishEditDialog(String inputText) {

//			lastText = inputText;
//
//			// update the main content by replacing fragments
//			fragment = new AhadithFragment();
//			Bundle args = new Bundle();
//			args.putInt(AhadithFragment.ARG_AHADITH, AhadithFragment.TYPE_AHADITH_KEYWORD_ID);
//			args.putInt(AhadithFragment.ARG_AHADITH_SEARCH, lastPosition);
//			args.putString(AhadithFragment.ARG_AHADITH_KEYWORD_TEXT, inputText);
//			args.putInt(AhadithFragment.ARG_BAB_ID, lastBabId);
//			fragment.setArguments(args);
//
//			FragmentManager fragmentManager = getSupportFragmentManager();
//			FragmentTransaction ft = fragmentManager.beginTransaction();
//
//			ft.replace(R.id.content_frame, fragment);
//			scaled = false ;
//			ft.commit();

		}

		
//		public void goToJanaezFragment(){
//			
//			FragmentManager fragmentManager = getSupportFragmentManager();
//			FragmentTransaction transaction = fragmentManager.beginTransaction();
//			transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
//
//			fragment1 = (ListFragment) getSupportFragmentManager().findFragmentByTag(JANAEZ_FRAGMENT);
//
//			if(fragment1 == null){
//				fragment1 = new JanaezFragment();
//
//				transaction.replace(R.id.fragment_view, fragment1, JANAEZ_FRAGMENT);
//				transaction.addToBackStack(JANAEZ_FRAGMENT);
//			}else{
//				transaction.attach(fragment1);
//			}
//
//			transaction.commit();
//			
//			header.setBackgroundResource(R.drawable.jana2ez);
//			btn_menu.setBackgroundResource(R.drawable.back_list);
//			currentFragment = JANAEZ_FRAGMENT;
//			
//			isBackEnabled = true;
//
//		}
		
		@Override
		public void onBackPressed() {
			super.onBackPressed();
//			if(currentFragment.equals(JANAEZ_FRAGMENT)){
//				currentFragment = MOSQUES_FRAGMENT;
//				btn_menu.setBackgroundResource(R.drawable.menu);
//				
//				isBackEnabled = false;
//				super.onBackPressed();
//			}else{
//				startActivity(new Intent(MainActivity.this, IndexActivity.class));
//				overridePendingTransition(R.anim.right_in, R.anim.right_out);
//				finish();
//			}
			
		}
		
}