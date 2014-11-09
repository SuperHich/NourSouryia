package com.noursouryia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

public class PreviewVideo extends Activity {

	private WebView video_shower ;
	private ImageView back ;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_preview);

		back = (ImageView) findViewById(R.id.back);
		video_shower = (WebView) findViewById(R.id.video_shower);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if(bundle !=null){
			
			String url = bundle.getString("link");
			video_shower.loadUrl(url);
			
		}
		

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				finish();

			}
		});

	}


}
