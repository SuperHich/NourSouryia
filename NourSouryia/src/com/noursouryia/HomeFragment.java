package com.noursouryia;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noursouryia.entity.Article;
import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.Airy;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.NSActivity;
import com.noursouryia.utils.NSFonts;


public class HomeFragment extends BaseFragment {

	private TextView news_feed ;
	private Button paginate_left, paginate_right ;
	private int currentFeedPosition = 0;
	private Article currentArticle;
	private View loading_feeds ;
	
	private ArrayList<Article> mArticles = new ArrayList<Article>();
	
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
		
		news_feed = (TextView) rootView.findViewById(R.id.news_feed);
		paginate_left = (Button) rootView.findViewById(R.id.paginate_left_news);
		paginate_right = (Button) rootView.findViewById(R.id.paginate_right_news);
		
		loading_feeds = (View) rootView.findViewById(R.id.loading_feeds);
	//	loading_feeds.addView(new GIFView(getActivity(), 1000,1000));
		
		
		news_feed.setTypeface(NSFonts.getNoorFont());
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initData();
		
		
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
		
//		news_feed.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(currentArticle != null)
//					((MainActivity) getActivity()).gotoArticleFragment(currentArticle);
//			}
//		});
		
	}

	private void initData(){
		
		new AsyncTask<Void, Void, ArrayList<Article>>() {

			private ProgressDialog loading;

			@Override
			protected void onPreExecute() {
//				loading = new ProgressDialog(getActivity());
//				loading.setCancelable(false);
//				loading.setMessage(getString(R.string.please_wait));
//				loading.show();
				
				loading_feeds.setVisibility(View.VISIBLE);
				
				
			}
			
			@Override
			protected ArrayList<Article> doInBackground(Void... params) {
				
				try{
					mArticles = NSManager.getInstance(getActivity()).getArticles(NSManager.DEFAULT_TIMESTAMP, 
							NSManager.DEFAULT_VALUE, 
							NSManager.DEFAULT_VALUE);

					for(Article a : mArticles){
						((NSActivity) getActivity()).NourSouryiaDB.insertOrUpdateArticle(a, NSManager.DEFAULT_VALUE, NSManager.DEFAULT_VALUE);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
//				NSManager.getInstance(getActivity()).getTypes(); // OK
//				NSManager.getInstance(getActivity()).getCommentsByID(6687); // OK
//				NSManager.getInstance(getActivity()).getFiles(); // OK
//				NSManager.getInstance(getActivity()).getAuthors(); // OK
//				NSManager.getInstance(getActivity()).getPolls(); // OK
//				NSManager.getInstance(getActivity()).getQuestionByID("http://syrianoor.net/get/poll?qid=5"); // OK
//				NSManager.getInstance(getActivity()).getArticles(Calendar.getInstance().getTimeInMillis(), 10, 1); // OK
				return mArticles;
			}
			
			@Override
			protected void onPostExecute(ArrayList<Article> result) {
			//	loading.dismiss();
				
				loading_feeds.setVisibility(View.GONE);
				news_feed.bringToFront();
				
				if(result.size() > 0){
					showFeed();
				}
			}
		}.execute();

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
		
		if(currentFeedPosition - 1 >= 0 ){
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
	
}
