package com.noursouryia;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.noursouryia.externals.NSManager;
import com.noursouryia.utils.NSFonts;


public class Settings extends Activity {


	private Switch notif_switch, notif_sound_switch ;
	private NSManager mManager;
	private TextView notif_sound_text, notif_text;
	private ImageView about;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);



		notif_text	= (TextView) findViewById(R.id.notif_text);
		notif_sound_text	= (TextView) findViewById(R.id.notif_sound_text);
		notif_switch 	= (Switch) findViewById(R.id.notif_switch);
		notif_sound_switch 	= (Switch) findViewById(R.id.notif_sound_switch);
		about 	= (ImageView) findViewById(R.id.about);
		
		notif_text.setTypeface(NSFonts.getKufah());
		notif_switch.setTypeface(NSFonts.getKufah());
		notif_sound_switch.setTypeface(NSFonts.getKufah());
		notif_sound_text.setTypeface(NSFonts.getKufah());


		notif_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if(isChecked){
					//Switch is currently ON


				}else{
					//Switch is currently OFF"

				}


			}
		});


		about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				finish();
				
			}
		});



	}





}
