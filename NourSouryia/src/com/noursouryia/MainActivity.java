package com.noursouryia;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.noursouryia.adapters.IMenuListener;
import com.noursouryia.adapters.MenuCustomAdapter;
import com.noursouryia.entity.Article;
import com.noursouryia.entity.Category;
import com.noursouryia.entity.Type;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.NSActivity;

public class MainActivity extends NSActivity implements IMenuListener, OnTouchListener{

	protected static final String TAG = MainActivity.class.getSimpleName();

	public static final String HOME_FRAGMENT 			= "home_fragment";
	public static final String NEWS_FRAGMENT 			= "news_fragment";
	public static final String RESEARCH_FRAGMENT 		= "research_fragment";
	public static final String FILES_FRAGMENT 			= "files_fragment";
	public static final String AUTHORS_FRAGMENT 		= "authors_fragment";
	public static final String MEDIA_FRAGMENT 			= "media_fragment";
	public static final String ARTICLE_FRAGMENT 		= "article_fragment";
	public static final String LIST_ARTICLE_FRAGMENT 	= "list_article_fragment";
	public static final String COMMENTS_FRAGMENT 		= "comments_fragment";
	public static final String ADD_COMMENT_FRAGMENT 	= "add_comment_fragment";
	public static final String SEARCH_FRAGMENT 			= "search_fragment";
	public static final String THAWRA_DIARIES 			= "thawra_diaries";

	public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "saved_state_action_bar_hidden";

	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	private LinearLayout mDrawerLinear ;

	private Button btn_menu_outside, btn_menu_inside, btn_share, btn_lamp, btn_settings, btn_rss, btn_opener_top;
	private ImageView img_title;
	private View headerSeparator;

	private ActionBarDrawerToggle mDrawerToggle;
	private RelativeLayout mainView , moving_layout;

	public static final int MESSAGE_START = 1;

	private int lastPosition = 0;
	private boolean isFirstStart = true;
	public boolean isTopOpener = false;
	public boolean isImgTitle = false;

	private Fragment fragment, fragment1;
	private String currentFragment;

	private int width_halfScreen ;

	private ArrayList<Type> mTypes;
	private MenuCustomAdapter adapter;

	private NSManager mManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mManager = NSManager.getInstance(this);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.right_drawer);
		mDrawerLinear = (LinearLayout) findViewById(R.id.drawer_linear);

		moving_layout = (RelativeLayout) findViewById(R.id.moving_layout);
		mainView = (RelativeLayout) findViewById(R.id.content_frame);

		btn_menu_outside 	= (Button) findViewById(R.id.btn_menu_outside);
		btn_menu_inside 	= (Button) findViewById(R.id.btn_menu_inside);
		btn_share 			= (Button) findViewById(R.id.btn_share); 
		btn_lamp 			= (Button) findViewById(R.id.btn_lamp); 
		btn_settings 		= (Button) findViewById(R.id.btn_settings); 
		btn_rss 			= (Button) findViewById(R.id.btn_rss);

		btn_opener_top		= (Button) findViewById(R.id.btn_opener_top);
		img_title			= (ImageView) findViewById(R.id.img_title);
		headerSeparator		= (View) findViewById(R.id.headerSeparator);

		btn_share.setOnTouchListener(this); 
		btn_lamp.setOnTouchListener(this);
		btn_settings.setOnTouchListener(this); 
		btn_rss.setOnTouchListener(this);
		btn_opener_top.setOnTouchListener(this);

		mDrawerList.setGroupIndicator(null);
		mDrawerList.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				adapter.notifyDataSetChanged();
			}
		});

		mDrawerList.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				adapter.notifyDataSetChanged();
			}
		});



		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		width_halfScreen = (displaymetrics.widthPixels)/2;

		mDrawerLinear.getLayoutParams().width = width_halfScreen;


		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mTypes = NourSouryiaDB.getTypesExcept("photos", "sounds", "videos", "revnews", "research", "article");
		adapter = new MenuCustomAdapter(this, mTypes);

		mDrawerList.setAdapter(adapter);
		mDrawerList.setDivider(null);

		mDrawerList.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
					long arg3) {

				Type selectedType = mTypes.get(arg2);
				if(selectedType.getCategories().size() == 0){
					gotoListArticlesFragment(selectedType.getLink(), selectedType.getNameEn(), selectedType.getNameAr());
					mDrawerLayout.closeDrawer(mDrawerLinear);
				}
				return false;
			}
		});

		mDrawerList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				Category selectedCategory = mTypes.get(groupPosition).getCategories().get(childPosition);
				gotoListArticlesFragment(selectedCategory.getLink(), selectedCategory.getName(), selectedCategory.getName());

				mDrawerLayout.closeDrawer(mDrawerLinear);

				return false;
			}
		});


		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 

				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				supportInvalidateOptionsMenu();
				if(isImgTitle)
					showImageTitle();
			}

			public void onDrawerOpened(View drawerView) {
				supportInvalidateOptionsMenu();
				if(isImgTitle)
					hideImageTitle();
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


	private void selectItem(int position) {

		lastPosition = position;
		boolean shouldSwitch = true;

		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		default:
			shouldSwitch = false;
			break;
		}

		if(shouldSwitch)
			switchTab(fragment, false);

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

	public void onTypeItemClicked(String fragmentTAG, Bundle args){
		gotoFragmentByTag(fragmentTAG, args);
	}

	private Fragment getFragmentByTag(String fragTAG){
		if(fragTAG.equals(NEWS_FRAGMENT))
			return new NewsFragment();
		if(fragTAG.equals(FILES_FRAGMENT))
			return new FilesFragment();
		else if(fragTAG.equals(AUTHORS_FRAGMENT))
			return new AuthorsFragment();
		else if(fragTAG.equals(MEDIA_FRAGMENT))
			return new MediaFragment();
		else if(fragTAG.equals(ARTICLE_FRAGMENT))
			return new ArticleFragment();
		else if(fragTAG.equals(LIST_ARTICLE_FRAGMENT))
			return new ListArticlesFragment();
		else if(fragTAG.equals(COMMENTS_FRAGMENT))
			return new CommentsFragment();
		else if(fragTAG.equals(ADD_COMMENT_FRAGMENT))
			return new AddCommentFragment();
		else if(fragTAG.equals(SEARCH_FRAGMENT))
			return new SearchArticlesFragment();
		else if(fragTAG.equals(THAWRA_DIARIES))
			return new FragmentThawraDiaries();

		return null;
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
				if(!mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
					mDrawerLayout.openDrawer(Gravity.RIGHT);
				else
					mDrawerLayout.closeDrawer(Gravity.RIGHT);		
				break;
			case R.id.btn_menu_inside:
				if(!mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
					mDrawerLayout.openDrawer(Gravity.RIGHT);
				else
					mDrawerLayout.closeDrawer(Gravity.RIGHT);		
				break;
			case R.id.btn_lamp :
				if(mManager.isOnlineMode())
					mManager.setOnLineMode(false);
				else
					mManager.setOnLineMode(true);

				Toast.makeText(MainActivity.this, mManager.isOnlineMode() ? getString(R.string.online_on) : getString(R.string.online_off), Toast.LENGTH_LONG).show();
				break;

			case R.id.btn_share :
				// Share App
				break;
			case R.id.btn_settings :
				// Go to settings
				break;
			case R.id.btn_rss :
				// RSS
				gotoSearchArticlesFragment("سوريا");
				break;
			case R.id.btn_opener_top :
				if(mManager.getMenuOpener() != null)
					mManager.getMenuOpener().openMenu();
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

	public void gotoFragmentByTag(String fragmentTAG, Bundle arguments){
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);

		fragment1 = (Fragment) getSupportFragmentManager().findFragmentByTag(fragmentTAG);

		if(fragment1 == null || arguments != null){
			fragment1 = getFragmentByTag(fragmentTAG);

			if(fragment1 != null){
				if(arguments != null)
					fragment1.setArguments(arguments);

				transaction.replace(R.id.content_frame, fragment1, fragmentTAG);
				transaction.addToBackStack(fragmentTAG);
			}
		}else{
			transaction.attach(fragment1);
		}

		transaction.commit();

		currentFragment = fragmentTAG;
	}

	public void gotoArticleFragment(Article article){

		NSManager.getInstance(this).setCurrentArticle(article);
		gotoFragmentByTag(ARTICLE_FRAGMENT, null);

	}

	public void gotoCommentsFragment(int articleNID){

		Bundle args = new Bundle();
		args.putInt(NSManager.NID, articleNID);
		gotoFragmentByTag(COMMENTS_FRAGMENT, args);

	}

	public void gotoAddCommentFragment(int articleNID){

		Bundle args = new Bundle();
		args.putInt(NSManager.NID, articleNID);
		gotoFragmentByTag(ADD_COMMENT_FRAGMENT, args);

	}

	public void gotoSearchArticlesFragment(String keyword){

		Bundle args = new Bundle();
		args.putString(SearchArticlesFragment.ARG_ARTICLE_KEYWORD, keyword);
		gotoFragmentByTag(SEARCH_FRAGMENT, args);

	}

	public void gotoListArticlesFragment(String link, String categoryName, String title){

		Bundle args = new Bundle();
		args.putString(ListArticlesFragment.ARG_ARTICLE_LINK, link);
		args.putString(ListArticlesFragment.ARG_ARTICLE_CATEGORY, categoryName);
		args.putString(ListArticlesFragment.ARG_ARTICLE_TITLE, title);
		//			gotoFragmentByTag(LIST_ARTICLE_FRAGMENT, args);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);

		fragment1 = getFragmentByTag(LIST_ARTICLE_FRAGMENT);

		if(args != null)
			fragment1.setArguments(args);

		if(fragment1 != null){
			transaction.replace(R.id.content_frame, fragment1, LIST_ARTICLE_FRAGMENT);
			transaction.addToBackStack(LIST_ARTICLE_FRAGMENT);
		}

		transaction.commit();

		currentFragment = LIST_ARTICLE_FRAGMENT;

	}

	//	public void gotoMediaFragment(){
	//
	//		FragmentManager fragmentManager = getSupportFragmentManager();
	//		FragmentTransaction transaction = fragmentManager.beginTransaction();
	//		transaction.setCustomAnimations(R.anim.down_in, R.anim.down_out, R.anim.up_in, R.anim.up_out);
	//
	//		fragment1 = getFragmentByTag(MEDIA_FRAGMENT);
	//
	//		if(fragment1 != null){
	//			transaction.replace(R.id.media_frame, fragment1, MEDIA_FRAGMENT);
	//			transaction.addToBackStack(MEDIA_FRAGMENT);
	//		}
	//
	//		transaction.commit();
	//
	//		currentFragment = MEDIA_FRAGMENT;
	//
	//	}
	//	
	//	public void gotoHomeFragment(){
	//
	//		FragmentManager fragmentManager = getSupportFragmentManager();
	//		FragmentTransaction transaction = fragmentManager.beginTransaction();
	//		transaction.setCustomAnimations(R.anim.down_in, R.anim.down_out, R.anim.up_in, R.anim.up_out);
	//
	//		fragment1 = getFragmentByTag(HOME_FRAGMENT);
	//
	//		if(fragment1 != null){
	//			transaction.replace(R.id.content_frame, fragment1, HOME_FRAGMENT);
	//			transaction.addToBackStack(HOME_FRAGMENT);
	//		}
	//
	//		transaction.commit();
	//
	//		currentFragment = HOME_FRAGMENT;
	//
	//	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();

		hideOpenerTop();
		hideImageTitle();
	}


	public void showOpenerTop(){
		btn_opener_top.setVisibility(View.VISIBLE);
		headerSeparator.setVisibility(View.VISIBLE);
	}

	public void hideOpenerTop(){
		btn_opener_top.setVisibility(View.GONE);
		headerSeparator.setVisibility(View.GONE);
	}

	public void showImageTitle(){
		img_title.setVisibility(View.VISIBLE);
	}

	public void hideImageTitle(){
		img_title.setVisibility(View.GONE);
	}

	protected void setImageTitle(int imgResource){
		img_title.setImageResource(imgResource);
	}

	private boolean isOnLineModePopup = false;
	protected void showOnLineModePopup(){
		if(!isOnLineModePopup)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setMessage(R.string.request_online_mode)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					mManager.setOnLineMode(true);
					Toast.makeText(MainActivity.this, getString(R.string.online_on), Toast.LENGTH_LONG).show();
					isOnLineModePopup = false;
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					isOnLineModePopup = false;
					return;
				}
			})
			;
			// Create the AlertDialog object and return it
			builder.create();
			builder.show();
			isOnLineModePopup = true;
		}
	}

	protected void showConnectionErrorPopup(){
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage(R.string.error_internet_connexion)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				return;
			}
		});
		// Create the AlertDialog object and return it
		builder.create();
		builder.show();
	}

	protected void showInfoPopup(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage(message)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				return;
			}
		});
		// Create the AlertDialog object and return it
		builder.create();
		builder.show();
	}

}