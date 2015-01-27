package com.noursouryia;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.noursouryia.adapters.CustomGridViewAdapter;
import com.noursouryia.adapters.GalleryAdapter;
import com.noursouryia.adapters.VideosListAdapter;
import com.noursouryia.entity.Article;
import com.noursouryia.entity.Category;
import com.noursouryia.entity.Type;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.Airy;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;


public class HomeFragment extends BaseFragment implements IFragmentEnabler{

	public static final String ARG_FOLDER_ID = "folder_id";
	
	private ImageButton btn_folder_sound, btn_folder_photos, btn_folder_video, item_image ;
	private TextView item_text ;
	private ImageView btn_folders_container , slide_shower, logo_sourya, btn_element_share , btn_element_download;;
	private Gallery gallery ;
	private Button paginate_left_slider, paginate_right_slider ;
	private Type type_photo, type_video, type_sound ;
	private GridView gridView ;
	private LinearLayout one_media ;
	private RelativeLayout slider_photos, toggle_folders ;
	private ListView list_videos;
	private ArrayList<Category> photo_categories = new ArrayList<Category>();
	private ArrayList<Category> sound_categories = new ArrayList<Category>();
	private ArrayList<Category> video_categories = new ArrayList<Category>();

	private ArrayList<String> all_photo_URLS = new ArrayList<String>();
	private ArrayList<Article> all_video_titles = new ArrayList<Article>();
	private CustomGridViewAdapter photosGridAdapter, soundsGridAdapter, videosGridAdapter;
	private VideosListAdapter videoListAdpater ;

	public final static int PHOTOS_FOLDER_SELECTED = 1;
	public final static int SOUNDS_FOLDER_SELECTED = 2;
	public final static int VIDEOS_FOLDER_SELECTED = 3;

	private int SELECTED_FOLDER ;

	private NSManager mManager ;
	private Airy mAiry ;

	private TextView news_feed ;
	private Button paginate_left, paginate_right ;
	private int currentFeedPosition = 0;
	private Article currentArticle;
	private View loading_feeds ;
	private RelativeLayout home_layout, media_layout;
//	private ImageView btn_search;
//	private EditText edt_search;

	private ArrayList<Article> mArticles = new ArrayList<Article>();

	private boolean isFirstStart = true;

	private View hidden_view;

	private String actual_image_link ;

	private ThinDownloadManager downloadManager;
	private NotificationManager mNotifyManager;
	private Builder mBuilder;

	public HomeFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_home, container, false);

		hidden_view = (View) rootView.findViewById(R.id.hidden_view);
		hidden_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		home_layout = (RelativeLayout) rootView.findViewById(R.id.home_layout);
		media_layout = (RelativeLayout) rootView.findViewById(R.id.media_layout);
		slider_photos = (RelativeLayout) rootView.findViewById(R.id.slider_photos);
		toggle_folders	= (RelativeLayout) rootView.findViewById(R.id.toggle_folders);

		news_feed = (TextView) rootView.findViewById(R.id.news_feed);
		paginate_left = (Button) rootView.findViewById(R.id.paginate_left_news);
		paginate_right = (Button) rootView.findViewById(R.id.paginate_right_news);

//		btn_search = (ImageView) rootView.findViewById(R.id.btn_search);
//		edt_search = (EditText) rootView.findViewById(R.id.edt_search);
		list_videos = (ListView) rootView.findViewById(R.id.list_videos);

		loading_feeds = (View) rootView.findViewById(R.id.loading_feeds);

		btn_element_share = (ImageView) rootView.findViewById(R.id.btn_element_share);
		btn_element_download = (ImageView) rootView.findViewById(R.id.btn_element_download);


		news_feed.setTypeface(NSFonts.getNoorFont());


		if(!ImageLoader.getInstance().isInited())
		{
			File cacheDir = StorageUtils.getCacheDirectory(getActivity());

			DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.btn_folder_photos) // resource or drawable
			.showImageForEmptyUri(R.drawable.btn_folder_photos) // resource or drawable
			.showImageOnFail(R.drawable.btn_folder_photos) // resource or drawable
			.cacheInMemory(true)
			.cacheOnDisk(true) 
			.build();

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
			.denyCacheImageMultipleSizesInMemory()
			.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
			.memoryCacheSize(2 * 1024 * 1024)
			.diskCache(new UnlimitedDiscCache(cacheDir)) // default
			.diskCacheSize(50 * 1024 * 1024)
			.diskCacheFileCount(100)
			.writeDebugLogs()
			.defaultDisplayImageOptions(options)
			.build();

			ImageLoader.getInstance().init(config);
		}


		btn_folder_sound = (ImageButton) rootView.findViewById(R.id.btn_folder_sound);
		btn_folder_photos = (ImageButton) rootView.findViewById(R.id.btn_folder_photos);
		btn_folder_video = (ImageButton) rootView.findViewById(R.id.btn_folder_video);

		logo_sourya = (ImageView) rootView.findViewById(R.id.logo_sourya);
		btn_folders_container = (ImageView) rootView.findViewById(R.id.btn_folders_container);
		gridView = (GridView) rootView.findViewById(R.id.grid_folders);

		gallery = (Gallery) rootView.findViewById(R.id.gallery);
		paginate_left_slider = (Button) rootView.findViewById(R.id.paginate_left_slider);
		paginate_right_slider = (Button) rootView.findViewById(R.id.paginate_right_slider);

		one_media = (LinearLayout) rootView.findViewById(R.id.one_media);
		item_text = (TextView) rootView.findViewById(R.id.item_text);
		item_image = (ImageButton) rootView.findViewById(R.id.item_image);
		slide_shower = (ImageView) rootView.findViewById(R.id.slide_shower);

		item_text.setTypeface(NSFonts.getNoorFont());
		item_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchView( one_media, gridView);
				all_photo_URLS.clear();
			}
		});

		logo_sourya.setBackgroundDrawable(null);
		btn_folder_sound.setBackgroundDrawable(null);
		btn_folder_photos.setBackgroundDrawable(null);
		btn_folder_video.setBackgroundDrawable(null);
		item_image.setBackgroundDrawable(null);


		downloadManager = new ThinDownloadManager();

		btn_element_share.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					ImageView view = (ImageView) v;
					view.getDrawable().setColorFilter(0x77ffffff,PorterDuff.Mode.SRC_ATOP);
					view.invalidate();
					break;
				}
				case MotionEvent.ACTION_UP: {

					if (actual_image_link != null)
					{
						Bitmap bmp = ImageLoader.getInstance().loadImageSync(actual_image_link);

						if (bmp != null) {

							String pathofBmp = Images.Media.insertImage(getActivity().getContentResolver(), bmp,"title", null);
							Uri bmpUri = Uri.parse(pathofBmp);
							Intent emailIntent1 = new Intent( android.content.Intent.ACTION_SEND);
							emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							emailIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
							emailIntent1.setType("image/png");

							startActivity(emailIntent1);

						} else {
							Toast.makeText(getActivity(),getString(R.string.request_online_mode), Toast.LENGTH_SHORT).show();
						}

					}


				}
				case MotionEvent.ACTION_CANCEL: {
					ImageView view = (ImageView) v;
					view.getDrawable().clearColorFilter();
					view.invalidate();
					break;
				}
				}

				return true;
			}


		});

		btn_element_download.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					ImageView view = (ImageView) v;
					view.getDrawable().setColorFilter(0x77ffffff,PorterDuff.Mode.SRC_ATOP);
					view.invalidate();
					break;
				}
				case MotionEvent.ACTION_UP: {

					if (actual_image_link != null)
					{
						
						Toast.makeText(getActivity(),getString(R.string.isdownloading), Toast.LENGTH_SHORT).show();
						
						Random r = new Random();
						int i = r.nextInt();
						
						final Intent openPicintent = new Intent();
						openPicintent.setAction(Intent.ACTION_VIEW);
						openPicintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						openPicintent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/NoorSouryia_pic_"+i+".jpg")), "image/jpg");
						
						PendingIntent resultPendingIntent =
							    PendingIntent.getActivity(
							    getActivity(),
							    0,
							    openPicintent,
							    PendingIntent.FLAG_UPDATE_CURRENT
							);
						
						
						mNotifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
						mBuilder = new NotificationCompat.Builder(getActivity());
						mBuilder.setContentTitle(getString(R.string.app_name))
						.setContentText(getString(R.string.isdownloading))
						.setContentIntent(resultPendingIntent)
						.setAutoCancel(true)
						.setSmallIcon(R.drawable.logo_app);
						
						
						
						Uri downloadUri = Uri.parse(actual_image_link);
						DownloadRequest downloadRequest = new DownloadRequest(downloadUri);
						
						final int mDownloadId = downloadManager.add(downloadRequest);
						final Uri destinationUri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/NoorSouryia_pic_"+i+".jpg");
						
						
						downloadRequest.setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH);
						downloadRequest.setDownloadListener(new DownloadStatusListener() {
							
							@Override
							public void onDownloadComplete(int id) {

								Toast.makeText(getActivity(),getString(R.string.download_successful), Toast.LENGTH_SHORT).show();
								startActivity(Intent.createChooser(openPicintent, "Open Picture"));
							
							}
							
							
							@Override
							public void onDownloadFailed(int id, int errorCode, String errorMessage) {

								Toast.makeText(getActivity(),getString(R.string.request_online_mode), Toast.LENGTH_SHORT).show();
								mNotifyManager.cancel(0);

							}

							@Override
							public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {
								mBuilder.setProgress(100, progress, false);
								mNotifyManager.notify(mDownloadId, mBuilder.build());
								
							}
						});
						
						

						


					}




				}
				case MotionEvent.ACTION_CANCEL: {
					ImageView view = (ImageView) v;
					view.getDrawable().clearColorFilter();
					view.invalidate();
					break;
				}
				}

				return true;
			}


		});

		/********************************************   FOLDERS TABS   *******************************************************/		


		btn_folder_photos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				SELECTED_FOLDER = PHOTOS_FOLDER_SELECTED ;
				folderPhotosClick();
				gridView.setAdapter(photosGridAdapter);

				one_media.setVisibility(View.GONE);
				gridView.setVisibility(View.VISIBLE);
			}
		});

		btn_folder_sound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				SELECTED_FOLDER = SOUNDS_FOLDER_SELECTED ;
				folderSoundsClick();
				gridView.setAdapter(soundsGridAdapter);

				one_media.setVisibility(View.GONE);
				gridView.setVisibility(View.VISIBLE);
			}
		});

		btn_folder_video.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				SELECTED_FOLDER = VIDEOS_FOLDER_SELECTED ;
				folderVideosClick();
				gridView.setAdapter(videosGridAdapter);


				one_media.setVisibility(View.GONE);
				gridView.setVisibility(View.VISIBLE);
			}
		});

		/********************************************************************************************************************/			


		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				all_photo_URLS.clear() ;

				switch (SELECTED_FOLDER) {

				case PHOTOS_FOLDER_SELECTED:

					item_text.setText(photo_categories.get(position).getName());
					String link = photo_categories.get(position).getLink();

					slider_photos.setVisibility(View.VISIBLE);
					gallery.setVisibility(View.VISIBLE);
					list_videos.setVisibility(View.GONE);

					btn_element_download.setVisibility(View.VISIBLE);
					btn_element_share.setVisibility(View.VISIBLE);

					getPhotosSlider(link);

					//					String url_image = all_photo_URLS.get(2);
					//					ImageLoader.getInstance().displayImage(url_image, slide_shower);



					break;

				case SOUNDS_FOLDER_SELECTED:

					//					switchView(gridView, one_media);
					//					item_text.setText(sound_categories.get(position).getName());


					one_media.setVisibility(View.GONE);
					gridView.setVisibility(View.VISIBLE);

					actual_image_link = null ;

					break;	

				case VIDEOS_FOLDER_SELECTED:

					switchView(gridView, one_media);
					item_text.setText(video_categories.get(position).getName());

					slider_photos.setVisibility(View.GONE);
					gallery.setVisibility(View.GONE);
					list_videos.setVisibility(View.VISIBLE);

					btn_element_download.setVisibility(View.GONE);
					btn_element_share.setVisibility(View.GONE);

					actual_image_link = null ;


					initListVideos(video_categories.get(position));

					list_videos.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {

							Intent i = new Intent(getActivity(), PreviewVideo.class);
							i.putExtra("link", all_video_titles.get(arg2).getYoutubeLink());
							startActivity(i);

						}
					});


					break;
				default:
					break;
				}

			}
		});

//		btn_search.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				String keyword = edt_search.getText().toString();
//				if(!keyword.equals("")){
//					((MainActivity) getActivity()).gotoSearchArticlesFragment(keyword);
//					Utils.hideKeyboard(getActivity(), edt_search);
//				}
//			}
//		});
//
//		edt_search.setOnEditorActionListener(new OnEditorActionListener() {
//			public boolean onEditorAction(TextView v, int aoString();
//					if(!keyword.equals("")){
//						((MainActivity) getActivity()).gotoSearchArticlesFragment(keyword);
//						Utils.hideKeyboard(getActivity(), edt_search);
//					}ctionId, KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_DONE) {
//					String keyword = edt_search.getText().t
//					return true;
//				}
//				return false;
//			}
//		});

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mManager = NSManager.getInstance(getActivity());
		mManager.setFragmentEnabler(this);

		initData();
		initMediaData();

		Airy mAiry = new Airy(getActivity()) {
			@Override
			public void onGesture(View pView, int pGestureId) {
				if (pView == news_feed) {
					switch (pGestureId) {
					case Airy.INVALID_GESTURE:
						break;
					case Airy.TAP:

						if(currentArticle != null)
							((MainActivity) getActivity()).gotoArticleFragment(currentArticle);

						break;
					case Airy.SWIPE_UP:
						break;
					case Airy.SWIPE_DOWN:
						break;
					case Airy.SWIPE_LEFT:
						previousFeed();
						break;
					case Airy.SWIPE_RIGHT:
						nextFeed();
						break;
					case Airy.TWO_FINGER_TAP:
						break;
					case Airy.TWO_FINGER_SWIPE_UP:
						break;
					case Airy.TWO_FINGER_SWIPE_DOWN:
						break;
					case Airy.TWO_FINGER_SWIPE_LEFT:
						break;
					case Airy.TWO_FINGER_SWIPE_RIGHT:
						break;
					case Airy.TWO_FINGER_PINCH_IN:
						break;
					case Airy.TWO_FINGER_PINCH_OUT:
						break;
					}
				}
			}
		};

		news_feed.setOnTouchListener(mAiry);

		paginate_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				nextFeed();
			}
		});

		paginate_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				previousFeed();
			}
		});

		if(isFirstStart)
		{
			Log.e(TAG, ">>> isFirstStart ");
			if(getArguments() == null)
				mSlidingLayer.openLayer(true);
			else
				mSlidingLayer.closeLayer(true);
			
			isFirstStart = false;
		}
		//		else{
		//
		//			if (isHome){
		//
		//				switchView2(media_layout, home_layout);
		////				home_layout.setVisibility(View.VISIBLE);
		////				media_layout.setVisibility(View.GONE);
		//
		//			} else {
		//
		//				switchView2(home_layout, media_layout);
		////				home_layout.setVisibility(View.GONE);
		////				media_layout.setVisibility(View.VISIBLE);
		//
		//			}
		//		}



	}

	private void initData(){

		new AsyncTask<Void, Void, ArrayList<Article>>() {

			@Override
			protected void onPreExecute() {
				news_feed.setEnabled(false);
				paginate_left.setEnabled(false);
				paginate_right.setEnabled(false);
				loading_feeds.setVisibility(View.VISIBLE);
			}

			@Override
			protected ArrayList<Article> doInBackground(Void... params) {

				try{
					if(Utils.isOnline(getActivity())){
						mArticles = NSManager.getInstance(getActivity()).getArticles(null, 
								NSManager.DEFAULT_TIMESTAMP, 
								NSManager.DEFAULT_VALUE, 
								NSManager.DEFAULT_VALUE);

						for(Article a : mArticles){
							((NSActivity) getActivity()).NourSouryiaDB.insertOrUpdateArticle(a, NSManager.DEFAULT_VALUE, NSManager.DEFAULT_VALUE);
						}
					}else
						mArticles = ((MainActivity) getActivity()).NourSouryiaDB.getLastArticles();
				}catch(Exception e){
					Log.e(TAG, "Error while init Data !");
					//					e.printStackTrace();
				}

				return mArticles;
			}

			@Override
			protected void onPostExecute(ArrayList<Article> result) {

				news_feed.setEnabled(true);
				paginate_left.setEnabled(true);
				paginate_right.setEnabled(true);
				loading_feeds.setVisibility(View.GONE);

				if(result.size() > 0){
					showFeed();
				}
			}
		}.execute();

	}

	private void initMediaData(){

		new AsyncTask<Void, Void, Boolean>() {

			//			private ProgressDialog loading;

			@Override
			protected void onPreExecute() {
				//				loading = new ProgressDialog(getActivity());
				//				loading.setCancelable(false);
				//				loading.setMessage(getString(R.string.please_wait));
				//				loading.show();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try{
					type_photo = ((NSActivity)getActivity()).NourSouryiaDB.getTypeByName("photos");
					type_sound = ((NSActivity)getActivity()).NourSouryiaDB.getTypeByName("sounds");
					type_video = ((NSActivity)getActivity()).NourSouryiaDB.getTypeByName("videos");


					photo_categories  = type_photo.getCategories() ;
					sound_categories  = type_sound.getCategories() ;
					video_categories  = type_video.getCategories() ;

					return true;

				}catch(Exception e){
					e.printStackTrace();
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				//				loading.dismiss();

				if(result){

					photosGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.row_grid, photo_categories);
					soundsGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.row_grid, sound_categories);					
					videosGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.row_grid, video_categories);	

					if(getArguments() != null)
						SELECTED_FOLDER = getArguments().getInt(ARG_FOLDER_ID);
					else
						SELECTED_FOLDER = PHOTOS_FOLDER_SELECTED ;
					
					selectFolder();

				}
			}
		}.execute();

	}

	private void initListVideos(final Category category){

		new AsyncTask<Void, Void, ArrayList<Article>>() {

			ProgressDialog loading ;

			@Override
			protected void onPreExecute() {
				loading = new ProgressDialog(getActivity());
				loading.setCancelable(false);
				loading.setMessage(getString(R.string.please_wait));
				loading.show();
			}


			@Override
			protected ArrayList<Article> doInBackground(Void... params) {

				ArrayList<Article> articles = mManager.getArticlesByUrl(category.getLink());
				all_video_titles.clear();

				if(articles.size() > 0 ){

					for (int i = 0; i < articles.size(); i++) {

						all_video_titles.add(articles.get(i));
					}
				}


				return all_video_titles;
			}


			@Override
			protected void onPostExecute(ArrayList<Article> result) {
				loading.dismiss();

				if(result.size() > 0){

					videoListAdpater = new VideosListAdapter(getActivity(),  all_video_titles);

					list_videos.setAdapter(videoListAdpater);
					videoListAdpater.notifyDataSetChanged();
				}
			}
		}.execute();

	}



	public void folderPhotosClick(){

		btn_folder_photos.setImageResource(R.drawable.btn_folder_photos_clicked);
		btn_folder_sound.setImageResource(R.drawable.btn_folder_sound);
		btn_folder_video.setImageResource(R.drawable.btn_folder_video);

		btn_folders_container.setImageResource(R.drawable.bg_btn_folder_photos);
		toggle_folders.setVisibility(View.VISIBLE);
		gridView.setVisibility(View.VISIBLE);

	}

	public void folderSoundsClick(){

		btn_folder_photos.setImageResource(R.drawable.btn_folder_photos);
		btn_folder_sound.setImageResource(R.drawable.btn_folder_sound_clicked);
		btn_folder_video.setImageResource(R.drawable.btn_folder_video);

		btn_folders_container.setImageResource(R.drawable.bg_btn_folder_sounds);
		toggle_folders.setVisibility(View.VISIBLE);
		gridView.setVisibility(View.VISIBLE);

	}

	public void folderVideosClick(){

		btn_folder_photos.setImageResource(R.drawable.btn_folder_photos);
		btn_folder_sound.setImageResource(R.drawable.btn_folder_sound);
		btn_folder_video.setImageResource(R.drawable.btn_folder_video_clicked);

		btn_folders_container.setImageResource(R.drawable.bg_btn_folder_videos);
		toggle_folders.setVisibility(View.VISIBLE);
		gridView.setVisibility(View.VISIBLE);

	}


	private void showFeed(){
		if(currentFeedPosition < mArticles.size()){
			currentArticle = mArticles.get(currentFeedPosition);
			news_feed.setText(currentArticle.getTitle());

			if(currentFeedPosition == 0)
			{
				paginate_left.setBackgroundResource(R.drawable.paginate_left_selector);
				paginate_right.setBackgroundResource(R.drawable.paginate_right_selected);
			}
			else if(currentFeedPosition == mArticles.size() - 1)
			{
				paginate_left.setBackgroundResource(R.drawable.paginate_left_selected);
				paginate_right.setBackgroundResource(R.drawable.paginate_right_selector);
			}
			else
			{
				paginate_left.setBackgroundResource(R.drawable.paginate_left_selector);
				paginate_right.setBackgroundResource(R.drawable.paginate_right_selector);
			}
		}
	}

	private void nextFeed(){

		if(currentFeedPosition + 1 < mArticles.size()){
			currentFeedPosition += 1;
			showFeed();
		}

	}

	private void previousFeed(){

		if(currentFeedPosition - 1 >= 0){
			currentFeedPosition -= 1;
			showFeed();
		}

	}


	public  int getDPI(int size){
		DisplayMetrics metrics;
		metrics = new DisplayMetrics();         
		(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(metrics);

		return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;        
	}

	@Override
	public void onSlidingLayerOpened() {
		super.onSlidingLayerOpened();

		if(!isFirstStart){
			switchView2(media_layout, home_layout);
		}

	}

	@Override
	public void onSlidingLayerClosed() {
		super.onSlidingLayerClosed();

		if(!isFirstStart){
			switchView2(home_layout, media_layout);
		}
	}


	private void getPhotosSlider(final String link){

		new AsyncTask<Void, Void, ArrayList<String>>() {

			private ProgressDialog loading;

			@Override
			protected void onPreExecute() {
				loading = new ProgressDialog(getActivity());
				loading.setCancelable(false);
				loading.setMessage(getString(R.string.please_wait));
				loading.show();
			}

			@Override
			protected ArrayList<String> doInBackground(Void... params) {

				ArrayList<Article> articles = mManager.getArticlesByUrl(link);
				all_photo_URLS.clear();

				if(articles.size() > 0 ){

					for (int i = 0; i < articles.size(); i++) {

						ArrayList<String> images_urls = articles.get(i).getFilePath();

						for (int j = 0; j < images_urls.size(); j++) {

							all_photo_URLS.add(images_urls.get(j));
							Log.w("Image URLS article "+i+" numero "+j, images_urls.get(j));
						}
					}
				}


				return all_photo_URLS;
			}

			@Override
			protected void onPostExecute(ArrayList<String> result) {

				loading.dismiss();

				if (result.size() > 0){

					switchView(gridView, one_media);


					actual_image_link = result.get(0);
					ImageLoader.getInstance().displayImage(actual_image_link, slide_shower);

					gallery.setAdapter(new GalleryAdapter(getActivity(), result ));


					gallery.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							Log.e("SIZE ALL PHOTOS", all_photo_URLS.size()+"");

							actual_image_link = all_photo_URLS.get(position);
							ImageLoader.getInstance().displayImage(actual_image_link, slide_shower);

						}
					});

					gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {

							if( position == all_photo_URLS.size()-1) {
								paginate_right_slider.setBackgroundResource(R.drawable.paginate_right_selected);
							} else {
								paginate_right_slider.setBackgroundResource(R.drawable.paginate_right_selector);
							}

							if( position == 0) {
								paginate_left_slider.setBackgroundResource(R.drawable.paginate_left_selected);
							}
							else {
								paginate_left_slider.setBackgroundResource(R.drawable.paginate_left_selector);
							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							paginate_left_slider.setBackgroundResource(R.drawable.paginate_left_selected);
							paginate_right_slider.setBackgroundResource(R.drawable.paginate_right_selector);
						}
					});

					paginate_right_slider.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							sliderGoNext();

						}


					});

					paginate_left_slider.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							sliderGoBack();

						}


					});


					mAiry = new Airy(getActivity()) {
						@Override
						public void onGesture(View pView, int pGestureId) {
							if (pView == slide_shower) {
								switch (pGestureId) {
								case Airy.INVALID_GESTURE:
									break;
								case Airy.TAP:

									break;
								case Airy.SWIPE_UP:
									break;
								case Airy.SWIPE_DOWN:
									break;
								case Airy.SWIPE_LEFT:

									sliderGoNext();

									break;
								case Airy.SWIPE_RIGHT:

									sliderGoBack();			

									break;
								case Airy.TWO_FINGER_TAP:
									break;
								case Airy.TWO_FINGER_SWIPE_UP:
									break;
								case Airy.TWO_FINGER_SWIPE_DOWN:
									break;
								case Airy.TWO_FINGER_SWIPE_LEFT:
									break;
								case Airy.TWO_FINGER_SWIPE_RIGHT:
									break;
								case Airy.TWO_FINGER_PINCH_IN:
									break;
								case Airy.TWO_FINGER_PINCH_OUT:
									break;
								}
							}
						}
					};

					slide_shower.setOnTouchListener(mAiry);



				} else {

					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.empty_list), Toast.LENGTH_SHORT).show();

				}


			}
		}.execute();


	}


	protected void switchView(final View firstLayout, final View secondeLayout) {
		final Animation in = new AlphaAnimation(0.0f, 1.0f);
		in.setDuration(400);

		final Animation out = new AlphaAnimation(1.0f, 0.0f);
		out.setDuration(300);

		AnimationSet as = new AnimationSet(true);
		as.addAnimation(out);
		in.setStartOffset(400);
		as.addAnimation(in);

		out.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				firstLayout.setVisibility(View.GONE);
				secondeLayout.setVisibility(View.VISIBLE);
				secondeLayout.startAnimation(in);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});

		firstLayout.startAnimation(out);
	}

	protected void switchView2(final View firstLayout, final View secondeLayout) {
		final Animation in = new AlphaAnimation(0.0f, 1.0f);
		in.setDuration(400);

		final Animation out = new AlphaAnimation(1.0f, 0.0f);
		out.setDuration(200);

		AnimationSet as = new AnimationSet(true);
		as.addAnimation(out);
		as.addAnimation(in);

		out.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				firstLayout.setVisibility(View.GONE);
				secondeLayout.setVisibility(View.VISIBLE);
				secondeLayout.startAnimation(in);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});

		firstLayout.startAnimation(out);
	}

	private void sliderGoBack() {

		int gallery_position = gallery.getSelectedItemPosition();

		if(gallery_position > 0 ){
			gallery.setSelection(gallery_position - 1);
			actual_image_link = all_photo_URLS.get(gallery_position - 1);
			ImageLoader.getInstance().displayImage(actual_image_link, slide_shower);
		}
	}

	private void sliderGoNext() {

		int gallery_position = gallery.getSelectedItemPosition();

		if(gallery_position < all_photo_URLS.size()-1){
			gallery.setSelection(gallery_position + 1);
			actual_image_link = all_photo_URLS.get(gallery_position + 1);
			ImageLoader.getInstance().displayImage(actual_image_link, slide_shower);
		}
	}

//	public void showHiddenView(){
//		hidden_view.setVisibility(View.VISIBLE);
//	}
//
//	public void hideHiddenView(){
//		hidden_view.setVisibility(View.GONE);
//	}

	@Override
	public void setEnabled(boolean enable) {
//		if(enable)
//			hideHiddenView();
//		else
//			showHiddenView();
	}

	@Override
	public void onFolderClicked(int folderId) {

		if(mSlidingLayer.isOpened())
			mSlidingLayer.closeLayer(true);

		SELECTED_FOLDER = folderId ;

		selectFolder();
	}

	private void selectFolder(){
		switch (SELECTED_FOLDER) {
		case PHOTOS_FOLDER_SELECTED:
			gridView.setAdapter(photosGridAdapter);
			folderPhotosClick();
			break;
		case SOUNDS_FOLDER_SELECTED:
			gridView.setAdapter(soundsGridAdapter);
			folderSoundsClick();
			break;
		case VIDEOS_FOLDER_SELECTED:
			gridView.setAdapter(videosGridAdapter);
			folderVideosClick();
			break;
		default:
			break;
		}
	}
}
