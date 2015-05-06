package com.noursouryia;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.BaseFragment;
import com.noursouryia.utils.Utils;


public class SearchArticlesWebFragment extends BaseFragment implements IFragmentEnabler{
	
	public static final String ARG_ARTICLE_KEYWORD 	= "article_keyword";
	
	private static final String BASE_URL_SEARCH = "http://syrianoor.net/goosearch?qq=";
	
	private ImageButton btn_back;
	private WebView webview;
	private boolean isFirstStart = true;
	
	private String keyword;
	private String mUrl;
	
	private NSManager mManager;
	
	public SearchArticlesWebFragment() {
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
		
		if(getArguments() != null){
			keyword		= getArguments().getString(ARG_ARTICLE_KEYWORD);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
		
		btn_back = (ImageButton) rootView.findViewById(R.id.btn_back);
		webview = (WebView) rootView.findViewById(R.id.webview);
		
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setDisplayZoomControls(false);
		webview.setWebViewClient(new WebViewClient(){

		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url){
		      view.loadUrl(url);
		      return true;
		    }
		});
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(webview.getUrl() != null)
				{
					if(!webview.getUrl().startsWith(BASE_URL_SEARCH))
					{
						webview.goBack();
					}else 
						getActivity().onBackPressed();
				}else 
					getActivity().onBackPressed();
			}
		});
				
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mManager = NSManager.getInstance(getActivity());
		mManager.setFragmentEnabler(this);

		if(isFirstStart)
		{
			isFirstStart = false;
			initData();
		}

	}
	
	private void initData(){
		
		if(Utils.isOnline(getActivity())){
			mUrl = BASE_URL_SEARCH +keyword.replaceAll(" ", "%20");
			webview.loadUrl(mUrl);
		}
		else
			((MainActivity)getActivity()).showConnectionErrorPopup();
		
	}

	@Override
	public void setEnabled(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFolderClicked(int folderId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetSearch(String keyword) {
		this.keyword = keyword;
		
		initData();
	}
}
