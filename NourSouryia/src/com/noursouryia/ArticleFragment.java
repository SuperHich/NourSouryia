package com.noursouryia;

import java.io.File;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.noursouryia.adapters.CommentsAdapter;
import com.noursouryia.entity.Article;
import com.noursouryia.entity.Comment;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;
import com.noursouryia.utils.Utils;
import com.squareup.picasso.Picasso;


public class ArticleFragment extends BaseFragment {

	public static final String ARG_WITH_COMMENTS = "with_comments";
	private TextView txv_article_title, txv_author_name, txv_article_content1, txv_article_content2;
	private Button btn_share;
	private ImageView img_article;
	private Article currentArticle;
	private boolean isFirstStart = true;
	private boolean withComments = false;

	private RelativeLayout comments_layout, comments_listview;
	private Button btn_old_comments, btn_new_comment, btn_send_comment;
	private ListView listView;
	private LinearLayout loading;
	private ScrollView add_comment_layout;
	private EditText edt_name, edt_email, edt_comment;
	private TextView txv_name, txv_email, txv_comment ;
	private TextView txv_empty, txv_wait;

	private CommentsAdapter adapter;
	private ArrayList<Comment> comments = new ArrayList<Comment>();

	private boolean isCanceled = false;

	public ArticleFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(getArguments() != null){
			withComments 		= getArguments().getBoolean(ARG_WITH_COMMENTS);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_article, container, false);

		txv_article_title = (TextView) rootView.findViewById(R.id.txv_article_title);
		txv_author_name = (TextView) rootView.findViewById(R.id.txv_author_name);
		txv_article_content1 = (TextView) rootView.findViewById(R.id.txv_article_content1);
		txv_article_content2 = (TextView) rootView.findViewById(R.id.txv_article_content2);

		btn_share = (Button) rootView.findViewById(R.id.btn_share);
		img_article = (ImageView) rootView.findViewById(R.id.img_article);

		comments_listview = (RelativeLayout) rootView.findViewById(R.id.comments_listview);
		comments_layout = (RelativeLayout) rootView.findViewById(R.id.comments_layout);
		btn_old_comments = (Button) rootView.findViewById(R.id.btn_old_comments);
		btn_new_comment = (Button) rootView.findViewById(R.id.btn_new_comment);
		btn_send_comment = (Button) rootView.findViewById(R.id.btn_add);
		add_comment_layout = (ScrollView) rootView.findViewById(R.id.add_comment_layout);

		txv_name 	= (TextView) rootView.findViewById(R.id.txv_name);
		txv_email 	= (TextView) rootView.findViewById(R.id.txv_email);
		txv_comment = (TextView) rootView.findViewById(R.id.txv_comment);
		edt_name 	= (EditText) rootView.findViewById(R.id.edt_name);
		edt_email 	= (EditText) rootView.findViewById(R.id.edt_email);
		edt_comment = (EditText) rootView.findViewById(R.id.edt_comment);

		listView = (ListView) rootView.findViewById(android.R.id.list);
		loading = (LinearLayout) rootView.findViewById(R.id.loading);
		txv_empty = (TextView) rootView.findViewById(R.id.txv_emptyList);
		txv_wait = (TextView) rootView.findViewById(R.id.txv_wait);

		txv_wait.setTypeface(NSFonts.getNoorFont());
		txv_empty.setTypeface(NSFonts.getNoorFont());

		txv_name.setTypeface(NSFonts.getNoorFont());
		txv_email.setTypeface(NSFonts.getNoorFont());
		txv_comment.setTypeface(NSFonts.getNoorFont());
		edt_name.setTypeface(NSFonts.getNoorFont());
		edt_email.setTypeface(NSFonts.getNoorFont());
		edt_comment.setTypeface(NSFonts.getNoorFont());
		
		btn_old_comments.setTypeface(NSFonts.getNoorFont());
		btn_new_comment.setTypeface(NSFonts.getNoorFont());
		btn_send_comment.setTypeface(NSFonts.getNoorFont());

		btn_send_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Utils.hideKeyboard(getActivity(), edt_comment);

				if(!isDataReady())
				{
					((MainActivity) getActivity()).showInfoPopup(getString(R.string.please_fill));
				}else{
					addComment();
				}
			}
		});

		btn_old_comments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				comments_listview.setVisibility(View.VISIBLE);
				add_comment_layout.setVisibility(View.GONE);

				btn_old_comments.setBackgroundResource(R.drawable.btn_selected_selector);
				btn_new_comment.setBackgroundResource(R.drawable.btn_selector);
			}
		});

		btn_new_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				comments_listview.setVisibility(View.GONE);
				add_comment_layout.setVisibility(View.VISIBLE);

				btn_old_comments.setBackgroundResource(R.drawable.btn_selector);
				btn_new_comment.setBackgroundResource(R.drawable.btn_selected_selector);
			}
		});


		txv_article_title.setTypeface(NSFonts.getNoorFont());
		txv_author_name.setTypeface(NSFonts.getNoorFont());
		txv_article_content1.setTypeface(NSFonts.getKufah());
		txv_article_content2.setTypeface(NSFonts.getKufah());

		currentArticle = NSManager.getInstance(getActivity()).getCurrentArticle();

		if(withComments)
			comments_layout.setVisibility(View.VISIBLE);

		//		txv_article_content1.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener()
		//	    {           
		//	        boolean isJustified = false;
		//
		//	        @Override
		//	        public boolean onPreDraw() 
		//	        {
		//	            if(!isJustified)
		//	            {
		//	                TextJustifyUtils.run(txv_article_content1, 300);
		//	                isJustified = true;
		//	            }
		//
		//	            return true;
		//	        }
		//
		//	    });


		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

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

		if(isFirstStart){
			isFirstStart = false;
			initData();

			adapter = new CommentsAdapter(getActivity(), comments);
			listView.setAdapter(adapter);

			if(withComments)
				if(isFirstStart)
				{
					isFirstStart = false;
					refreshOldComments();
				}else
					toggleEmptyMessage();
		}

		btn_share.setOnTouchListener(this);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			Button view = (Button) v;
			view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
			v.invalidate();
			break;
		}
		case MotionEvent.ACTION_UP: {

			Button view = (Button) v;
			view.getBackground().clearColorFilter();
			view.invalidate();

			switch (v.getId()) {
			case R.id.btn_share:
				shareArticle();
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

	private void initData(){

		String[] contentParts = splitContent(currentArticle.getBody());

		txv_article_title.setText(currentArticle.getTitle());
		txv_author_name.setText(currentArticle.getName());
		txv_article_content1.setText(formatText(contentParts[0]));
		txv_article_content2.setText(formatText(contentParts[1]));

		
		Log.e("File Path SIZE ", "File Path SIZE "+currentArticle.getFilePath().size());
		
		if(currentArticle.getFilePath().size() > 0){
			
			
			
			String url = currentArticle.getFilePath().get(0) ;
			
			if (url.contains(" ")) {
				url = url.replace(" ", "%20");
			}
			
			Log.e("URL PHOTO ARTICLE ", "URL : "+url);
			
//			Picasso.with(getActivity()).load(url).placeholder(R.drawable.btn_folder_photos).into(img_article);
			ImageLoader.getInstance().displayImage(currentArticle.getFilePath().get(0), img_article);
		}

		
		
		

	}

	public static String[] splitContent(String body){
		String[] parts = null;
		int indexOfPart1 = body.indexOf("&nbsp;");

		if(indexOfPart1 != -1){
			String part1 = body.substring(0, indexOfPart1);
			String part2 = body.substring(indexOfPart1, body.length());

			parts = new String[]{part1, part2};
		}

		if(parts == null)
		{
			parts = new String[]{body, ""};
		}

		return parts;
	}


	public static String formatText (String body){


		body = body.replaceAll("\\r\\n|\\r|\\n", "LINE_RETURN");

		body = Html.fromHtml(body).toString() ;

		body = body.replaceAll("LINE_RETURN", System.getProperty("line.separator"));


		return body;


	}

	private void shareArticle(){

		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		
		String shareBody = NSManager.BASE_URL + currentArticle.getType() + "/" + currentArticle.getNid();  
		
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, currentArticle.getTitle());
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, formatText(currentArticle.getBody()));
		

		//		if(currentArticle.getFilePath().size() > 0){
		//			String imageUri = ImageLoader.getInstance().getLoadingUriForView(img_article);
		//			Log.v(TAG, "imageUri " + imageUri);
		//			sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
		//		}

		startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));

	}

	private void addComment(){

		new AsyncTask<Comment, Void, Integer>() {

			ProgressDialog loading;

			@Override
			protected void onPreExecute() {
				loading = new ProgressDialog(getActivity());
				loading.setMessage(getString(R.string.please_wait));
				loading.setCancelable(false);
				loading.show();
			}

			@Override
			protected Integer doInBackground(Comment... params) {
				try{
					if(Utils.isOnline(getActivity())){

						Comment comment = new Comment();
						comment.setName(edt_name.getText().toString());
						comment.setEmail(edt_email.getText().toString());
						comment.setBody(edt_comment.getText().toString());

						return NSManager.getInstance(getActivity()).addComment(comment, currentArticle.getNid());
					}
				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}
				return null;
			}

			@Override
			protected void onPostExecute(Integer result) {
				loading.dismiss();

				if(isCanceled)
					return;

				if(result != null){
					if(result != NSManager.DEFAULT_VALUE){
						Toast.makeText(getActivity(), getString(R.string.comment_added), Toast.LENGTH_LONG).show();
						edt_name.setText("");
						edt_email.setText("");
						edt_comment.setText("");
					}else
						Toast.makeText(getActivity(), getString(R.string.comment_not_added), Toast.LENGTH_LONG).show();
				}else{
					((MainActivity)getActivity()).showConnectionErrorPopup();
				}
			}
		}.execute();

	}

	private boolean isDataReady(){

		if(edt_name.getText().toString().equals("") || 
				edt_email.getText().toString().equals("") ||
				edt_comment.getText().toString().equals(""))
			return false;

		return true;
	}

	private void refreshOldComments(){

		new AsyncTask<Void, Void, ArrayList<Comment>>() {

			@Override
			protected void onPreExecute() {
				comments.clear();
				loading.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}

			@Override
			protected ArrayList<Comment> doInBackground(Void... params) {
				try{
					if(!Utils.isOnline(getActivity()))
					{
						if(comments.size() == 0)
							return ((NSActivity)getActivity()).NourSouryiaDB.getCommentsById(currentArticle.getNid());
					}
					else if(Utils.isOnline(getActivity())){
						ArrayList<Comment> list = NSManager.getInstance(getActivity()).getCommentsByID(currentArticle.getNid());

						if(list.size() > 0)
							for(Comment c : list){
								((NSActivity)getActivity()).NourSouryiaDB.insertOrUpdateComment(c, currentArticle.getNid());
							}

						return list;
					}
				}catch(Exception e){
					Log.e(TAG, "Error while initData !");
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<Comment> result) {

				if(isCanceled)
					return;

				loading.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);

				if(result != null){
					comments.addAll(result);
					adapter.notifyDataSetChanged();
				}else
					((MainActivity)getActivity()).showConnectionErrorPopup();

				toggleEmptyMessage();
			}
		}.execute();

	}

	private void toggleEmptyMessage() {
		if(comments.size() == 0)
			txv_empty.setVisibility(View.VISIBLE);
		else
			txv_empty.setVisibility(View.GONE);
	}


	@Override
	public void onDetach() {
		super.onDetach();

		isCanceled  = true;
	}
}
