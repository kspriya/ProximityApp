package com.commnsense.proximity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LogActivity extends Activity implements PropertyChangeListener {
	Device device;
	private TextView logArea;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_log_activity);
		String deviceId = this.getIntent().getStringExtra("device_id");
		device = DeviceList.getInstance().getDeviceById(deviceId);
		//device.addPropertyChangeListener(this);
		TextView devName = (TextView) findViewById(R.id.dev_name);
		devName.setText(device.name);

		this.logArea = (TextView) findViewById(R.id.logArea);
		logArea.setText("Door was opened "+device.noOfOpenTimes+" times");
//		List<List<String>> logs = device.logs;
//		if(logs!=null){
//		for (List<String> list : logs) {
//			for (String string : list) {
//				logArea.append(string);
//			}
//		}}

		Button clearLog = (Button) findViewById(R.id.clearLogButton);
		clearLog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				logArea.setText("");

			}
		});

	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				String prop = event.getPropertyName();
//				if (prop.equalsIgnoreCase("OPEN_COUNT")) {
//					if(device.logs!=null){
//					int size = device.logs.size();
//					device.logs.add(new ArrayList<String>());
//					device.logs.clear();
//					device.logs.get(0).add("Door was opened " + device.getNoOfOpenTimes()
//						+ " times");
//					device.logs.get(size--).add(
//							"Door was opened " + device.getNoOfOpenTimes()
//									+ " times");
//					List<List<String>> logs = device.logs;
//					for (List<String> list : logs) {
//						for (String string : list) {
//							logArea.append(string);
//						}
//					}
//					}
//				}
			}
		});

	}

}
