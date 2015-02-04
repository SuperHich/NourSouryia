package com.noursouryia;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.noursouryia.adapters.IMenuListener;
import com.noursouryia.adapters.MenuCustomAdapter;
import com.noursouryia.entity.Article;
import com.noursouryia.entity.Category;
import com.noursouryia.entity.Type;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;

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
	public static final String LIST_NEWS_FRAGMENT 		= "list_news_fragment";
	public static final String POLLS_FRAGMENT 			= "polls_fragment";



	public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "saved_state_action_bar_hidden";

	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	private LinearLayout mDrawerLinear ;

	private Button btn_menu_outside, btn_menu_inside, btn_share, btn_lamp,  btn_opener_top, btn_search_top, btn_rss, btn_settings;
	private ImageView img_title, btn_search;
	private View headerSeparator;

	private ActionBarDrawerToggle mDrawerToggle;
	private RelativeLayout mainView , moving_layout, layout_search;
	private EditText edt_search;

	public static final int MESSAGE_START = 1;

	private int lastPosition = 0;
	private boolean isFirstStart = true;
	public boolean isTopOpener = false;
	public boolean isImgTitle = false;
	public boolean searchBtnEnable = false;

	private Fragment fragment, fragment1;
	private String currentFragment;

	private int width_halfScreen ;

	private ArrayList<Type> mTypes = new ArrayList<Type>();
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
//		mainView = (RelativeLayout) findViewById(R.id.content_frame);
		layout_search = (RelativeLayout) findViewById(R.id.layout_search);

		btn_menu_outside 	= (Button) findViewById(R.id.btn_menu_outside);
		btn_menu_inside 	= (Button) findViewById(R.id.btn_menu_inside);
		btn_share 			= (Button) findViewById(R.id.btn_share); 
		btn_lamp 			= (Button) findViewById(R.id.btn_lamp); 
		btn_settings 		= (Button) findViewById(R.id.btn_settings); 
		btn_rss 			= (Button) findViewById(R.id.btn_rss);
		btn_search_top			= (Button) findViewById(R.id.btn_search_top);

		btn_opener_top		= (Button) findViewById(R.id.btn_opener_top);
		img_title			= (ImageView) findViewById(R.id.img_title);
		headerSeparator		= (View) findViewById(R.id.headerSeparator);
		btn_search = (ImageView) findViewById(R.id.btn_search);
		edt_search = (EditText) findViewById(R.id.edt_search);

		edt_search.setTypeface(NSFonts.getNoorFont());

		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String keyword = edt_search.getText().toString();
				if(!keyword.equals("")){
					gotoSearchArticlesFragment(keyword);
					Utils.hideKeyboard(MainActivity.this, edt_search);
					layout_search.bringToFront();
					edt_search.bringToFront();
					btn_search.bringToFront();
				}
			}
		});


		edt_search.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					String keyword = edt_search.getText().toString();
					if(!keyword.equals("")){
						gotoSearchArticlesFragment(keyword);
						Utils.hideKeyboard(MainActivity.this, edt_search);
						layout_search.bringToFront();
						edt_search.bringToFront();
						btn_search.bringToFront();
					}
					return true;
				}
				return false;
			}
		});


		btn_share.setOnTouchListener(this); 
		btn_lamp.setOnTouchListener(this);
		btn_settings.setOnTouchListener(this); 
		btn_rss.setOnTouchListener(this);
		btn_opener_top.setOnTouchListener(this);
		btn_search_top.setOnTouchListener(this);

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
		//		mTypes = NourSouryiaDB.getTypesExcept("photos", "sounds", "videos", "revnews", "research", "article");
		
		//HomeType
		Type homeType = new Type();
		homeType.setNameEn("homeType");
		homeType.setNameAr(getString(R.string.menu_home));

		mTypes.add(homeType);
//		mTypes.addAll(NourSouryiaDB.getTypesExcept("photos", "sounds", "videos"));
		mTypes.addAll(getOrderedTypes());
		mTypes.addAll(getAnnexeTypes());
		mTypes.add(getMediaType());

		adapter = new MenuCustomAdapter(this, mTypes);

		mDrawerList.setAdapter(adapter);
		mDrawerList.setDivider(null);

		mDrawerList.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
					long arg3) {

				Type selectedType = mTypes.get(arg2);

				if(selectedType.getCategories().size() == 0){
					int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
					if(backStackCount > 1)
						popBackStackTillEntry(1);
					
					if(selectedType.getNameEn().equals("homeType")){
						onTypeItemClicked(HOME_FRAGMENT, null, true);
					}
					else if(selectedType.getLink().equals(NSManager.URL_AUTHORS))
						onTypeItemClicked(AUTHORS_FRAGMENT, null, true);
					else if(selectedType.getLink().equals(NSManager.URL_FILES))
						onTypeItemClicked(FILES_FRAGMENT, null, true);
					else if(selectedType.getLink().equals(NSManager.URL_POLL))
						onTypeItemClicked(POLLS_FRAGMENT, null, true);
					else
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

				int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
				if(backStackCount > 1)
					popBackStackTillEntry(1);
				
				Category selectedCategory = mTypes.get(groupPosition).getCategories().get(childPosition);
				if(groupPosition < mTypes.size() - 1){

					switch (selectedCategory.getTid()) {
					case 12:
						gotoListNewsFragment(selectedCategory.getLink(), selectedCategory.getParent(), R.drawable.comment_news, true);
						break;
					case 13:
						gotoListNewsFragment(selectedCategory.getLink(), selectedCategory.getParent(), R.drawable.jawla_sahafa, false);
						break;
					case 14:
						gotoListNewsFragment(selectedCategory.getLink(), selectedCategory.getParent(), R.drawable.takarir_news, false);
						break;
					case 15:
						Bundle args = new Bundle();
						args.putString(ListNewsFragment.ARG_ARTICLE_LINK, selectedCategory.getLink());
						args.putString(ListNewsFragment.ARG_ARTICLE_CATEGORY, selectedCategory.getParent());

						gotoFragmentByTag(MainActivity.THAWRA_DIARIES, args, true);
						break;

					default:
						gotoListArticlesFragment(selectedCategory.getLink(), selectedCategory.getName(), selectedCategory.getName());
						break;
					}

				}else{
					// Media group clicked

					if(fragment1 instanceof HomeFragment)
						mManager.getFragmentEnabler().onFolderClicked(selectedCategory.getTid());
					else{
						Bundle args = new Bundle();
						args.putInt(HomeFragment.ARG_FOLDER_ID, selectedCategory.getTid());
						gotoFragmentByTag(MainActivity.HOME_FRAGMENT, args, false);
					}
					
//					if(currentFragment != null){
//						emptyBackStack();
//					}
//
//					mManager.getFragmentEnabler().onFolderClicked(selectedCategory.getTid());

				}

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
//			selectItem(lastPosition);
			gotoFragmentByTag(HOME_FRAGMENT, null, true);

			isFirstStart = false;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	public void onMenuItemClicked(int position) {
//		selectItem(position);
	}

	public void onTypeItemClicked(String fragmentTAG, Bundle args, boolean addToBack){
		gotoFragmentByTag(fragmentTAG, args, addToBack);
	}

	private Fragment getFragmentByTag(String fragTAG){
		if(fragTAG.equals(HOME_FRAGMENT))
			return new HomeFragment();
		else if(fragTAG.equals(NEWS_FRAGMENT))
			return new NewsFragment();
		else if(fragTAG.equals(FILES_FRAGMENT))
			return new FilesFragment();
		else if(fragTAG.equals(AUTHORS_FRAGMENT))
			return new AuthorsFragment();
		else if(fragTAG.equals(MEDIA_FRAGMENT))
			return new MediaFragment();
		else if(fragTAG.equals(ARTICLE_FRAGMENT))
			return new ArticleFragment();
		else if(fragTAG.equals(LIST_ARTICLE_FRAGMENT))
			return new ListArticlesFragment();
		else if(fragTAG.equals(SEARCH_FRAGMENT))
			return new SearchArticlesFragment();
		else if(fragTAG.equals(THAWRA_DIARIES))
			return new FragmentThawraDiaries();
		else if(fragTAG.equals(LIST_NEWS_FRAGMENT))
			return new ListNewsFragment();
		else if(fragTAG.equals(POLLS_FRAGMENT))
			return new PollsFragment();

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
				//				if(mManager.isOnlineMode())
				//					mManager.setOnLineMode(false);
				//				else
				//					mManager.setOnLineMode(true);
				//
				//				Toast.makeText(MainActivity.this, mManager.isOnlineMode() ? getString(R.string.online_on) : getString(R.string.online_off), Toast.LENGTH_LONG).show();

				startActivity(new Intent(MainActivity.this, AboutNS.class));
				overridePendingTransition(R.anim.up_in, R.anim.up_out);


				break;


			case R.id.btn_rss :
				// Share App
				break;

			case R.id.btn_settings :
				// Share App
				break;

			case R.id.btn_search_top :

				if (!searchBtnEnable){
					layout_search.setVisibility(View.VISIBLE);
					layout_search.bringToFront();
					searchBtnEnable = true ;
				}

				else
				{
					layout_search.setVisibility(View.INVISIBLE);
					searchBtnEnable = false ;
				}

				break;
			case R.id.btn_share :
				// Share App
				shareApp();
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

	public void gotoFragmentByTag(String fragmentTAG, Bundle arguments, boolean addToBack){
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);

		fragment1 = (Fragment) getSupportFragmentManager().findFragmentByTag(fragmentTAG);

		if(fragment1 == null || arguments != null){
			fragment1 = getFragmentByTag(fragmentTAG);

			if(fragment1 != null){
				if(arguments != null)
					fragment1.setArguments(arguments);

				transaction.replace(R.id.content_frame_second, fragment1, fragmentTAG);
				if(addToBack)
					transaction.addToBackStack(fragmentTAG);
			}
		}else{
			transaction.attach(fragment1);
		}

		transaction.commit();

		currentFragment = fragmentTAG;
	}


	public void gotoArticleFragment(Article article, boolean withComments){

		NSManager.getInstance(this).setCurrentArticle(article);
		Bundle args = new Bundle();
		args.putBoolean(ArticleFragment.ARG_WITH_COMMENTS, withComments);
		gotoFragmentByTag(ARTICLE_FRAGMENT, args, true);

	}

	public void gotoArticleFragment(Article article){

		NSManager.getInstance(this).setCurrentArticle(article);
		gotoFragmentByTag(ARTICLE_FRAGMENT, null, true);

	}

//	public void gotoCommentsFragment(int articleNID){
//
//		Bundle args = new Bundle();
//		args.putInt(NSManager.NID, articleNID);
//		gotoFragmentByTag(COMMENTS_FRAGMENT, args);
//
//	}
//
//	public void gotoAddCommentFragment(int articleNID){
//
//		Bundle args = new Bundle();
//		args.putInt(NSManager.NID, articleNID);
//		gotoFragmentByTag(ADD_COMMENT_FRAGMENT, args);
//
//	}

	public void gotoSearchArticlesFragment(String keyword){

		Bundle args = new Bundle();
		args.putString(SearchArticlesFragment.ARG_ARTICLE_KEYWORD, keyword);
		gotoFragmentByTag(SEARCH_FRAGMENT, args, true);

	}

	public void gotoListArticlesFragment(String link, String categoryName, String title){

		if(currentFragment.equals(LIST_ARTICLE_FRAGMENT+link))
			return;
		
		Bundle args = new Bundle();
		args.putString(ListArticlesFragment.ARG_ARTICLE_LINK, link);
		args.putString(ListArticlesFragment.ARG_ARTICLE_CATEGORY, categoryName);
		args.putString(ListArticlesFragment.ARG_ARTICLE_TITLE, title);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);

//		fragment1 = (Fragment) getSupportFragmentManager().findFragmentByTag(LIST_ARTICLE_FRAGMENT+link);
//
//		if(fragment1 == null){
			fragment1 = getFragmentByTag(LIST_ARTICLE_FRAGMENT);
			fragment1.setArguments(args);

			transaction.replace(R.id.content_frame_second, fragment1, LIST_ARTICLE_FRAGMENT+link);
			transaction.addToBackStack(LIST_ARTICLE_FRAGMENT+link);
//		}else{
//			transaction.attach(fragment1);
//		}

		transaction.commit();

		currentFragment = LIST_ARTICLE_FRAGMENT;

	}

	public void gotoListNewsFragment(String link, String categoryName, int imageId, boolean withComments){

		Bundle args = new Bundle();
		args.putString(ListNewsFragment.ARG_ARTICLE_LINK, link);
		args.putString(ListNewsFragment.ARG_ARTICLE_CATEGORY, categoryName);
		args.putInt(ListNewsFragment.ARG_ARTICLE_TITLE, imageId);
		args.putBoolean(ListNewsFragment.ARG_ARTICLE_WITH_COMMENTS, withComments);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);

		fragment1 = getFragmentByTag(LIST_NEWS_FRAGMENT);

		if(args != null)
			fragment1.setArguments(args);

		if(fragment1 != null){
			transaction.replace(R.id.content_frame_second, fragment1, LIST_NEWS_FRAGMENT);
			transaction.addToBackStack(LIST_NEWS_FRAGMENT);
		}

		transaction.commit();

		currentFragment = LIST_NEWS_FRAGMENT;

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
		Log.d(TAG, ">>> NUM FRAGMENT(s) " + backStackCount);
		if(backStackCount > 1)
			popBackStackTillEntry(backStackCount);
		else if(backStackCount == 1)
			gotoFragmentByTag(HOME_FRAGMENT, null, false);
		else
			super.onBackPressed();
		
	}


	public void showOpenerTop(){
		Log.v(TAG, ">>> showOpenerTop");
		btn_opener_top.setVisibility(View.VISIBLE);
		headerSeparator.setVisibility(View.VISIBLE);
	}

	public void hideOpenerTop(){
		Log.v(TAG, ">>> hideOpenerTop");
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

	private void shareApp(){

		String shareBody = "http://syrianoor.net/";
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));

	}

	private ArrayList<Type> getOrderedTypes(){
		ArrayList<Type> types = new ArrayList<Type>();
		
		types.add(NourSouryiaDB.getTypeByName("revnews"));
		types.add(NourSouryiaDB.getTypeByName("revmarsad"));
		types.add(NourSouryiaDB.getTypeByName("revto"));
		types.add(NourSouryiaDB.getTypeByName("revsucc"));
		types.add(NourSouryiaDB.getTypeByName("sham"));
		types.add(NourSouryiaDB.getTypeByName("todaysyria"));
		types.add(NourSouryiaDB.getTypeByName("biographies"));
		
		Type tArticle = NourSouryiaDB.getTypeByName("article");
		tArticle.setNameAr(getString(R.string.articles));
		types.add(tArticle);
		
		types.add(NourSouryiaDB.getTypeByName("research"));
		
		return types;
	}
	
	private ArrayList<Type> getAnnexeTypes(){
		ArrayList<Type> types = new ArrayList<Type>();

		Type t1 = new Type();
		t1.setLink(NSManager.URL_AUTHORS);
		t1.setNameEn("authors");
		t1.setNameAr(getString(R.string.menu_writers));

		Type t2 = new Type();
		t2.setLink(NSManager.URL_FILES);
		t2.setNameEn("files");
		t2.setNameAr(getString(R.string.menu_files));

		Type t3 = new Type();
		t3.setLink(NSManager.URL_POLL);
		t3.setNameEn("polls");
		t3.setNameAr(getString(R.string.menu_polls));

		types.add(t1);
		types.add(t2);
		types.add(t3);

		return types;
	}

	private Type getMediaType(){
		Type mediaType = null;
		try{
			mediaType = new Type();
			mediaType.setLink("#");
			mediaType.setNameEn("mediaType");
			mediaType.setNameAr(getString(R.string.medias_library));

			Type t1 = NourSouryiaDB.getTypeByName("photos");
			Type t2 = NourSouryiaDB.getTypeByName("sounds");
			Type t3 = NourSouryiaDB.getTypeByName("videos");

			ArrayList<Category> categories = new ArrayList<Category>();
			
			Category cat3 = new Category();
			cat3.setLink(t3.getLink());
			cat3.setName(t3.getNameAr());
			cat3.setTid(HomeFragment.VIDEOS_FOLDER_SELECTED);
			categories.add(cat3);

			Category cat2 = new Category();
			cat2.setLink(t2.getLink());
			cat2.setName(t2.getNameAr());
			cat2.setTid(HomeFragment.SOUNDS_FOLDER_SELECTED);
			categories.add(cat2);
			
			Category cat1 = new Category();
			cat1.setLink(t1.getLink());
			cat1.setName(t1.getNameAr());
			cat1.setTid(HomeFragment.PHOTOS_FOLDER_SELECTED);
			categories.add(cat1);
			
			mediaType.setCategories(categories);
		}catch(Exception e){
			e.printStackTrace();
		}

		return mediaType;
	}

	public void emptyBackStack() {
		popBackStackTillEntry( 0 );
	}

	/**
	 * 
	 * @param entryIndex
	 *            is the index of fragment to be popped to, for example the
	 *            first fragment will have a index 0;
	 */
	public void popBackStackTillEntry( int entryIndex ) {
		if ( getSupportFragmentManager() == null )
			return;
		if ( getSupportFragmentManager().getBackStackEntryCount() <= entryIndex )
			return;
		BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(
				entryIndex );
		if ( entry != null ) {
			getSupportFragmentManager().popBackStack( entry.getId(),
					FragmentManager.POP_BACK_STACK_INCLUSIVE );
		}
	}

}