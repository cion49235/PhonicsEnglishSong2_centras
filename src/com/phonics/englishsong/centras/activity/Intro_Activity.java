package com.phonics.englishsong.centras.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.phonics.englishsong.centras.R;

public class Intro_Activity extends Activity {
	public static LinearLayout intro_layout;
	public Handler handler;
	public Context context;
	public static Activity activity;
	public static LinearLayout bg_intro;
    public static int background_type = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		context = this;
		activity = this;
        bg_intro = (LinearLayout)findViewById(R.id.bg_intro);
        if(getIntent().getIntExtra("backgournd_type", background_type) == 0){
        	bg_intro.setBackgroundColor(Color.TRANSPARENT);
        }else{
            bg_intro.setBackgroundColor(Color.TRANSPARENT);
        }
		handler = new Handler();
		handler.postDelayed(runnable, 2000);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(handler != null) handler.removeCallbacks(runnable);
	}
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			Intent intent = new Intent(context, SubActivity.class);
			intent.putExtra("num", "408");
			intent.putExtra("subject", context.getString(R.string.app_name));
			intent.putExtra("thumb", "http://i.ytimg.com/vi/oSRAU7bSD3k/hqdefault.jpg");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			
//			Intent intent = new Intent(context, TabContent.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
			finish();
			//fade_animation
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
	};
		@Override
		public void onBackPressed() {
			super.onBackPressed();
			if(handler != null) handler.removeCallbacks(runnable);
			finish();
		}
	}