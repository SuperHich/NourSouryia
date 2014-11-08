package com.noursouryia;

import java.io.File;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.noursouryia.entity.Article;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSFonts;


public class ArticleFragment extends BaseFragment {

	public static final String ARG_WITH_COMMENTS = "with_comments";
	private TextView txv_article_title, txv_author_name, txv_article_content1, txv_article_content2;
	private Button btn_share, btn_comments;
	private ImageView img_article;
	private Article currentArticle;
	private boolean isFirstStart = true;
	private boolean withComments = false;
	
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
		btn_comments = (Button) rootView.findViewById(R.id.btn_comments);
		img_article = (ImageView) rootView.findViewById(R.id.img_article);
		
		txv_article_title.setTypeface(NSFonts.getNoorFont());
		txv_author_name.setTypeface(NSFonts.getNoorFont());
		txv_article_content1.setTypeface(NSFonts.getNoorFont());
		txv_article_content2.setTypeface(NSFonts.getNoorFont());
		
		currentArticle = NSManager.getInstance(getActivity()).getCurrentArticle();
		
		if(withComments)
			btn_comments.setVisibility(View.VISIBLE);
		
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

//		if(isFirstStart){
//			isFirstStart = false;
			initData();
//		}
		
		btn_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Share Article
			}
		});
		
		btn_comments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// go to comments
				//				((MainActivity)getActivity()).gotoCommentsFragment(6687);	
				((MainActivity)getActivity()).gotoCommentsFragment(currentArticle.getNid());
				//				((MainActivity)getActivity()).gotoAddCommentFragment(currentArticle.getNid());
			}
		});
		
	}

	private void initData(){
		
		String[] contentParts = splitContent(currentArticle.getBody());
		
		txv_article_title.setText(currentArticle.getTitle());
		txv_author_name.setText(currentArticle.getName());
		txv_article_content1.setText(formatText(contentParts[0]));
		txv_article_content2.setText(formatText(contentParts[1]));
		
		if(currentArticle.getFilePath().size() > 0)
			ImageLoader.getInstance().displayImage(currentArticle.getFilePath().get(0), img_article);

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
	
}
