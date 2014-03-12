package com.commnsense.proximity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.commnsense.proximity.Device.Status;

public class DeviceDetailsActivity extends Activity implements
		PropertyChangeListener {
	TextView openCount;
	private Device device;
	boolean isStop = true;
	TextView name;
	TextView status_sensor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_details);
		String deviceId = this.getIntent().getStringExtra("device_id");
		device = DeviceList.getInstance().getDeviceById(deviceId);
		device.addPropertyChangeListener(this);
		device.connectToDevice(this);
		if(!device.connectionState.equalsIgnoreCase("Connected")){
			 AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 builder.setMessage("Device is not active now!!")
			  .setCancelable(true)
			   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int id) {
			        DeviceDetailsActivity.this.finish();
			   }
			 });
			 
			AlertDialog alert = builder.create();
			alert.show();
		}
		
		name = (TextView) findViewById(R.id.name);
		name.setText(device.name);

		TextView battery = (TextView) findViewById(R.id.battery);
		battery.setText(Integer.toString(device.batteryProgress));

		TextView serialNo = (TextView) findViewById(R.id.serialNo);
		serialNo.setText(device.serialNo);

		status_sensor = (TextView) findViewById(R.id.status_sensor);
		if (device.getIsOpen()== 1) {
			status_sensor.setBackground(getResources().getDrawable(
					R.drawable.status_circle_red));
		} else {
			status_sensor.setBackground(getResources().getDrawable(
					R.drawable.status_circle_green));
		}

		openCount = (TextView) findViewById(R.id.doorOpenCount);
		openCount.setText("Door was opened " + device.getNoOfOpenTimes()
				+ " times");

		Button clearLogButton = (Button) findViewById(R.id.clearLog);
		clearLogButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				device.logs.clear();
				Intent intent = new Intent(DeviceDetailsActivity.this,
						ClearLog.class);
				startActivity(intent);
			}
		});

		Button startStopButton = (Button) findViewById(R.id.start_stop);
		startStopButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isStop) {
					device.changeLoggingCharacteristic(1);
					isStop = false;
					// System.out.println(n);

				} else {
					device.changeLoggingCharacteristic(0);
					isStop = true;
				}

			}
		});

		Button viewLogButton = (Button) findViewById(R.id.view_log);
		viewLogButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DeviceDetailsActivity.this,
						LogActivity.class);
				intent.putExtra("device_id", device.serialNo);
				startActivity(intent);

			}
		});

		Button settingsButton = (Button) findViewById(R.id.settings);
		settingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(DeviceDetailsActivity.this,
						SettingsActivity.class);

				intent.putExtra("device_id", device.serialNo);
				startActivity(intent);
			}
		});
	//	device.changeTime();

	}

	@Override
	protected void onResume() {
		super.onResume();
		name.setText(device.getName());
		if (device.getIsOpen()== 1) {
			status_sensor.setBackground(getResources().getDrawable(
					R.drawable.status_circle_red));
		} else {
			status_sensor.setBackground(getResources().getDrawable(
					R.drawable.status_circle_green));
		}
		
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				String prop = event.getPropertyName();
				if (prop.equalsIgnoreCase("OPEN_COUNT")) {

					openCount.setText("Door was opened "
							+ device.getNoOfOpenTimes() + " times");
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		device.removePropertyChangeListener(this);
		device.disconnectDevice();
		Intent intent=new Intent(DeviceDetailsActivity.this, StartActivity.class);
		startActivity(intent);
		finish();
		
	}

}
