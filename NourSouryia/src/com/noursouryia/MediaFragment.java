package com.noursouryia;

import java.io.File;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.noursouryia.entity.Article;
import com.noursouryia.entity.Category;
import com.noursouryia.entity.Type;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.Airy;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;


public class MediaFragment extends BaseFragment {

	private ImageButton btn_folder_sound, btn_folder_photos, btn_folder_video, item_image ;
	private TextView item_text ;
	private ImageView btn_folders_container , slide_shower, logo_sourya;
	private Gallery gallery ;
	private Button paginate_left_slider, paginate_right_slider ;
	private Type type_photo, type_video, type_sound ;
	private GridView gridView ;
	private LinearLayout one_media ;
	private ArrayList<Category> photo_categories = new ArrayList<Category>();
	private ArrayList<Category> sound_categories = new ArrayList<Category>();
	private ArrayList<Category> video_categories = new ArrayList<Category>();

	private ArrayList<String> all_photo_URLS = new ArrayList<String>();
	private CustomGridViewAdapter photosGridAdapter, soundsGridAdapter, videosGridAdapter;

	private final static int PHOTOS_FOLDER_SELECTED = 1;
	private final static int SOUNDS_FOLDER_SELECTED = 2;
	private final static int VIDEOS_FOLDER_SELECTED = 3;

	private int SELECTED_FOLDER ;

	private NSFonts mNSFonts ;
	private NSManager mManager ;
	private Airy mAiry ;

	public MediaFragment() {
		// Empty constructor required for fragment subclasses
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_medias, container, false);

		mNSFonts = new NSFonts() ;
		mManager = new NSManager(getActivity());

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

		item_text.setTypeface(mNSFonts.getNoorFont());
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

		initData();


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

					getPhotosSlider(link);

					//					String url_image = all_photo_URLS.get(2);
					//					ImageLoader.getInstance().displayImage(url_image, slide_shower);



					break;

				case SOUNDS_FOLDER_SELECTED:

					//					switchView(gridView, one_media);
					//					item_text.setText(sound_categories.get(position).getName());


					one_media.setVisibility(View.GONE);
					gridView.setVisibility(View.VISIBLE);

					break;	

				case VIDEOS_FOLDER_SELECTED:

					//					switchView(gridView, one_media);
					//					item_text.setText(video_categories.get(position).getName());

					one_media.setVisibility(View.GONE);
					gridView.setVisibility(View.VISIBLE);

					break;
				default:
					break;
				}

			}
		});




		return rootView;
	}



	private void initData(){

		new AsyncTask<Void, Void, ArrayList<Type>>() {

			private ProgressDialog loading;

			@Override
			protected void onPreExecute() {
				loading = new ProgressDialog(getActivity());
				loading.setCancelable(false);
				loading.setMessage(getString(R.string.please_wait));
				loading.show();
			}

			@Override
			protected ArrayList<Type> doInBackground(Void... params) {
				type_photo = ((NSActivity)getActivity()).NourSouryiaDB.getTypeByName("photos");
				type_sound = ((NSActivity)getActivity()).NourSouryiaDB.getTypeByName("sounds");
				type_video = ((NSActivity)getActivity()).NourSouryiaDB.getTypeByName("videos");


				photo_categories  = type_photo.getCategories() ;
				sound_categories  = type_sound.getCategories() ;
				video_categories  = type_video.getCategories() ;

				//				NSManager.getInstance(getActivity()).getTypes(); // OK
				//				NSManager.getInstance(getActivity()).getCommentsByID(6687); // OK
				//				NSManager.getInstance(getActivity()).getFiles(); // OK
				//				NSManager.getInstance(getActivity()).getAuthors(); // OK
				//				NSManager.getInstance(getActivity()).getPolls(); // OK
				//				NSManager.getInstance(getActivity()).getQuestionByID("http://syrianoor.net/get/poll?qid=5"); // OK
				//				NSManager.getInstance(getActivity()).getArticles(Calendar.getInstance().getTimeInMillis(), 10, 1); // OK
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<Type> result) {
				loading.dismiss();

				//	if(result != null){

				photosGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.row_grid, photo_categories);
				soundsGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.row_grid, sound_categories);					
				videosGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.row_grid, video_categories);	

				gridView.setAdapter(photosGridAdapter);
				SELECTED_FOLDER = PHOTOS_FOLDER_SELECTED ;
				folderPhotosClick();
				//		}
				//				toggleEmptyMessage();
			}
		}.execute();

	}

	//	private void toggleEmptyMessage() {
	//		if(places.size() == 0)
	//			txv_empty.setVisibility(View.VISIBLE);
	//		else
	//			txv_empty.setVisibility(View.GONE);
	//	}

	public void folderPhotosClick(){

		btn_folder_photos.setImageResource(R.drawable.btn_folder_photos_clicked);
		btn_folder_sound.setImageResource(R.drawable.btn_folder_sound);
		btn_folder_video.setImageResource(R.drawable.btn_folder_video);

		btn_folders_container.setImageResource(R.drawable.bg_btn_folder_photos);

	}

	public void folderSoundsClick(){

		btn_folder_photos.setImageResource(R.drawable.btn_folder_photos);
		btn_folder_sound.setImageResource(R.drawable.btn_folder_sound_clicked);
		btn_folder_video.setImageResource(R.drawable.btn_folder_video);

		btn_folders_container.setImageResource(R.drawable.bg_btn_folder_sounds);

	}

	public void folderVideosClick(){

		btn_folder_photos.setImageResource(R.drawable.btn_folder_photos);
		btn_folder_sound.setImageResource(R.drawable.btn_folder_sound);
		btn_folder_video.setImageResource(R.drawable.btn_folder_video_clicked);

		btn_folders_container.setImageResource(R.drawable.bg_btn_folder_videos);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onDetach() {
		super.onDetach();

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


					String url_image = result.get(0);
					ImageLoader.getInstance().displayImage(url_image, slide_shower);

					gallery.setAdapter(new GalleryAdapter(getActivity(), result ));


					gallery.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							Log.e("SIZE ALL PHOTOS", all_photo_URLS.size()+"");

							String url_image = all_photo_URLS.get(position);
							ImageLoader.getInstance().displayImage(url_image, slide_shower);

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

	
	private void sliderGoBack() {

		int gallery_position = gallery.getSelectedItemPosition();

		if(gallery_position > 0 ){
			gallery.setSelection(gallery_position - 1);
			String url_image = all_photo_URLS.get(gallery_position - 1);
			ImageLoader.getInstance().displayImage(url_image, slide_shower);
		}
	}
	
	private void sliderGoNext() {

		int gallery_position = gallery.getSelectedItemPosition();

		if(gallery_position < all_photo_URLS.size()-1){
			gallery.setSelection(gallery_position + 1);
			String url_image = all_photo_URLS.get(gallery_position + 1);
			ImageLoader.getInstance().displayImage(url_image, slide_shower);
		}
	}

}
