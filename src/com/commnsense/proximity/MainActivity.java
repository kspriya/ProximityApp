package com.commnsense.proximity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends BaseActivity {
	private static final int STOPSPLASH = 0;
	//time duration in millisecond for which your splash screen should visible to
      //user. here i have taken half second
    private static final long SPLASHTIME = 2000;
	  //handler for splash screen
    private Handler splashHandler = new Handler() {
         @Override
         public void handleMessage(Message msg) {
              switch (msg.what) {
              	case STOPSPLASH:
              		//Generating and Starting new intent on splash time out	
              		Intent intent = new Intent(MainActivity.this, 
                                             StartActivity.class);
              		startActivity(intent);
                        MainActivity.this.finish(); 
              		break;
              }
              super.handleMessage(msg);
         }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 //Generating message and sending it to splash handle 
        Message msg = new Message();
        msg.what = STOPSPLASH;
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
        
//		ImageView img = (ImageView) findViewById(R.id.startpic);
//		
//		img.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(MainActivity.this,
//						StartActivity.class);
//				startActivity(intent);
//
//			}
//		});

	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
