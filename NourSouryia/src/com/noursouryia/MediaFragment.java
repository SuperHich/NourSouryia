package com.noursouryia;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.noursouryia.adapters.CustomGridViewAdapter;
import com.noursouryia.entity.Category;
import com.noursouryia.entity.Type;
import com.noursouryia.utils.NSActivity;


@SuppressLint("NewApi")
public class MediaFragment extends Fragment {

	private ImageButton btn_folder_sound, btn_folder_photos, btn_folder_video ;
	private ImageView btn_folders_container ;
	private Type type_photo, type_video, type_sound ;
	private GridView gridView ;
	private ArrayList<Category> photo_categories = new ArrayList<Category>();
	private ArrayList<Category> sound_categories = new ArrayList<Category>();
	private ArrayList<Category> video_categories = new ArrayList<Category>();
	
	
	CustomGridViewAdapter photosGridAdapter, soundsGridAdapter, videosGridAdapter;

	
	public MediaFragment() {
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
		
		View rootView = inflater.inflate(R.layout.fragment_medias, container, false);
		
		btn_folder_sound = (ImageButton) rootView.findViewById(R.id.btn_folder_sound);
		btn_folder_photos = (ImageButton) rootView.findViewById(R.id.btn_folder_photos);
		btn_folder_video = (ImageButton) rootView.findViewById(R.id.btn_folder_video);
		
		btn_folders_container = (ImageView) rootView.findViewById(R.id.btn_folders_container);
		gridView = (GridView) rootView.findViewById(R.id.grid_folders);

		
		btn_folder_sound.setBackground(null);
		btn_folder_photos.setBackground(null);
		btn_folder_video.setBackground(null);
		
		
		initData();
		
		
		
		
		btn_folder_photos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				folderPhotosClick();
				
				gridView.setAdapter(photosGridAdapter);
				
				
			}
		});
		
		btn_folder_sound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				folderSoundsClick();
				
				gridView.setAdapter(soundsGridAdapter);
				
			}
		});
		
		btn_folder_video.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				folderVideosClick();
				
				gridView.setAdapter(videosGridAdapter);
			}
		});
		
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initData();
		
	}

	private void initData(){
		
		new AsyncTask<Void, Void, ArrayList<Type>>() {

			private ProgressDialog loading;

			@Override
			protected void onPreExecute() {
//				places.clear();
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
	
	
	
}
