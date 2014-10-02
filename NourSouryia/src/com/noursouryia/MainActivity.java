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
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	private LinearLayout mDrawerLinear ;
	
	private Button btn_menu_outside, btn_menu_inside;

	private ActionBarDrawerToggle mDrawerToggle;
	private RelativeLayout mainView , moving_layout;

	public static final int MESSAGE_START = 1;
	private int lastPosition = 0;
	private String lastText = "";
	private boolean isFirstStart = true;
	
	private Fragment fragment, fragment1;
	private String currentFragment;
	
	private boolean isBackEnabled = false;
	
	private int width_halfScreen ;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.right_drawer);
		mDrawerLinear = (LinearLayout) findViewById(R.id.drawer_linear);
		
		moving_layout = (RelativeLayout) findViewById(R.id.moving_layout);
		mainView = (RelativeLayout) findViewById(R.id.content_frame);
		
		btn_menu_outside = (Button) findViewById(R.id.btn_menu_outside);
		btn_menu_inside = (Button) findViewById(R.id.btn_menu_inside);
		
		mDrawerList.setGroupIndicator(null);
		mDrawerList.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {

				RelativeLayout ll = (RelativeLayout) mDrawerList.getChildAt(groupPosition);
				ImageView expand = (ImageView) ll.findViewById(R.id.expand);
				
				expand.setImageResource(R.drawable.minus);
				
			}
		});
		
		mDrawerList.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override
			public void onGroupCollapse(int groupPosition) {

				RelativeLayout ll = (RelativeLayout) mDrawerList.getChildAt(groupPosition);
				ImageView expand = (ImageView) ll.findViewById(R.id.expand);
				
				expand.setImageResource(R.drawable.plus);
			}
		});
		
		
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		width_halfScreen = (displaymetrics.widthPixels)/2;
		
	    mDrawerLinear.getLayoutParams().width = width_halfScreen;
	    
	    
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		MenuCustomAdapter adapter = new MenuCustomAdapter(this, NSManager.getInstance(this).getCurrent_types());


		mDrawerList.setAdapter(adapter);
		mDrawerList.setDivider(null);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		

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
		//		moving_layout.setTranslationX(- slideOffset * drawerView.getWidth());
				mDrawerLayout.bringChildToFront(drawerView);
				mDrawerLayout.requestLayout();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		//		if (savedInstanceState == null) {
		//			selectItem(1);
		//		}

		
		btn_menu_outside.setOnTouchListener(this);
		btn_menu_inside.setOnTouchListener(this);
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
		

		lastPosition = position;
		boolean shouldSwitch = true;
		
//		Bundle args = null;
		// update the main content by replacing fragments
		
		switch (position) {
		case 0:
//			fragment = new HomeFragment();
//			fragment = new FilesFragment();
//			fragment = new AuthorsFragment();
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

//		if(shouldSwitch)
//			switchTab(fragment, false);

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerLinear);

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
				case R.id.btn_menu_outside:
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
				case R.id.btn_menu_inside:
///					if(isBackEnabled)
//					{		
//					onBackPressed();
//				}
//				else{
					if(!mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
						mDrawerLayout.openDrawer(Gravity.RIGHT);
					else
						mDrawerLayout.closeDrawer(Gravity.RIGHT);		
//				}
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