package com.commnsense.proximity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CalibrateActivity extends Activity {

	private Device device;
	ImageView calibrate_circle;
	TextView devName;
	ScheduledExecutorService scheduleTaskExecutor = Executors
			.newScheduledThreadPool(1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calibrate_activity);
		String deviceId = this.getIntent().getStringExtra("device_id");
		device = DeviceList.getInstance().getDeviceById(deviceId);
		devName = (TextView) findViewById(R.id.dev_name);
		calibrate_circle = (ImageView) findViewById(R.id.calibrate_circle);
		
//		if(device==null){
//			
//			 AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			 builder.setMessage("Device is not active now!!")
//			  .setCancelable(true)
//			   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//			   public void onClick(DialogInterface dialog, int id) {
//			        CalibrateActivity.this.finish();
//			   }
//			 });
//			 
//			AlertDialog alert = builder.create();
//			alert.show();
//		}else{
		devName.setText(device.name);
		
		if (device.isOpen==0) {
			calibrate_circle.setImageResource(R.drawable.status_circle_green);
		} else {
			calibrate_circle.setImageResource(R.drawable.status_circle_red);
		}
		
		scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
			public void run() {
				device.readDoorStatus();
				if (device.getIsOpen()==0) {
					calibrate_circle.setImageResource(R.drawable.status_circle_green);
				} else {
					calibrate_circle.setImageResource(R.drawable.status_circle_red);
				}
			}
		}, 0, 5, TimeUnit.SECONDS);

	}
		//}

	
}
