package com.commnsense.proximity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DeviceDetailsActivity extends Activity implements
		PropertyChangeListener {
	TextView openCount;

	// This progress bar will be used to display the progress of loading.
	ProgressBar pBar;
	// This is a loading spinner to block the user until the device is connected
	// properly and
	// all the necessary data is read from the device.

	Dialog pd;

	// This is variabel that holds a dialog object that gets thrown when ever we
	// have an issue
	// connecting to the tag
	Dialog dialog;

	private Device device;

	public Device getDevice() {
		return device;
	}

	boolean isStop = true;
	TextView name;
	TextView status_sensor;
	TextView enableLog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_details);
		String deviceId = this.getIntent().getStringExtra("device_id");
		device = DeviceList.getInstance().getDeviceById(deviceId);

		// pd = new Dialog(DeviceDetailsActivity.this);
		// pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// pd.setCancelable(false);

		if (device != null) {
			if (!device.isConnected()) {
				pd = new Dialog(this);
				pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
				pd.setCancelable(false);

				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				lp.copyFrom(pd.getWindow().getAttributes());
				lp.setTitle("Connecting..");
				lp.width = WindowManager.LayoutParams.MATCH_PARENT;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

				pd.setContentView(R.layout.layout_loadingspinner);
				pBar = ((ProgressBar) pd.findViewById(R.id.loading));
				((ProgressBar) pd.findViewById(R.id.loadingSpinner))
						.setVisibility(0);
				pBar.setMax(100);
				pd.show();
				pd.getWindow().setAttributes(lp);
				pd.getWindow().setBackgroundDrawable(
						new ColorDrawable(0x80333333));

				device.addPropertyChangeListener(this);

				device.connectToDevice(this);
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						try {

							// 10 second time out for the device to get
							// connected and all the
							// data read from it.
							Thread.sleep(10000);
							if (pd != null && pd.isShowing()) {

								// This should be there so that we dont see the
								// loading spinner for ever
								// laoding spinner get dismissed.
								pd.dismiss();

								// we have to show a dialog on the error we
								// faced while connecting
								// so the user doesnt get frustrated which might
								// may be lead to
								// breaking his phone.
								callErrorDialogOnConnecting();
							}

						} catch (InterruptedException e) {

							e.printStackTrace();
						}
					}

				});
				thread.start();
			}

			name = (TextView) findViewById(R.id.name);
			name.setText(device.name);

			TextView battery = (TextView) findViewById(R.id.battery);
			battery.setText(Integer.toString(device.batteryProgress));

			TextView serialNo = (TextView) findViewById(R.id.serialNo);
			serialNo.setText(device.serialNo);

			status_sensor = (TextView) findViewById(R.id.status_sensor);
			if (device.getIsOpen() == 1) {
				status_sensor.setBackground(getResources().getDrawable(
						R.drawable.status_circle_red));
			} else {
				status_sensor.setBackground(getResources().getDrawable(
						R.drawable.status_circle_green));
			}

			enableLog = (TextView) findViewById(R.id.enable_log);
			enableLog.setText("0");

			openCount = (TextView) findViewById(R.id.doorOpenCount);
			openCount.setText("Door was opened " + device.getNoOfOpenTimes()
					+ " times");

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

			final Button startStopButton = (Button) findViewById(R.id.start_stop);
			startStopButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (isStop) {
						device.changeLoggingCharacteristic(1);
						isStop = false;
					} else {
						device.changeLoggingCharacteristic(0);
						isStop = true;
					}
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
		}

	}

	// Thread thread = new Thread(new Runnable(){
	// AlertDialog alert;
	// @Override
	// public void run() {
	// try {
	// Thread.sleep(5000);
	// runOnUiThread(new Runnable() {
	// public void run() {
	// if(!device.isConnected()){
	// AlertDialog.Builder builder = new
	// AlertDialog.Builder(getApplicationContext());
	// builder.setIcon(R.drawable.ic_launcher);
	// builder.setTitle("Error");
	// builder.setMessage("Device is not active now!!")
	// .setCancelable(true)
	// .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int id) {
	// // Intent intent=new Intent(DeviceDetailsActivity.this,
	// StartActivity.class);
	// // startActivity(intent);
	// // DeviceDetailsActivity.this.finish();
	// DeviceDetailsActivity.this.onBackPressed();
	//
	// }
	// });
	// builder.create().show();
	// }
	// // else{
	// // device.changeTime();
	// // }
	// }
	// });
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// thread.start();

	/**
	 * We show the error dialog using this.
	 */
	public void callErrorDialogOnConnecting() {

		runOnUiThread(new Runnable() {
			// Since we are calling UI from a non Ui thread this has to run on
			// seperate thread
			// and also android doesnt allow UI to run on a normal thread so we
			// have to run
			// this on a runUIThread.
			@Override
			public void run() {
				AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
						DeviceDetailsActivity.this);
				dlgAlert.setTitle("Error");
				dlgAlert.setMessage("Connecting to device taking longer than expected");
				dlgAlert.setIcon(R.drawable.ic_launcher);
				dlgAlert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								DeviceDetailsActivity.this.onBackPressed();
							}
						});
				dlgAlert.create().show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		name.setText(device.getName());
		if (device.getIsOpen() == 1) {
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
				if (prop.equals("OPEN_COUNT")) {

					openCount.setText("Door was opened "
							+ device.getNoOfOpenTimes() + " times");
				} else if (prop.equals("DOOR_STATUS")) {
					openCount.setText("Door was opened "
							+ device.getNoOfOpenTimes() + " times");

					if (device.getIsOpen() == 1) {
						status_sensor.setBackground(getResources().getDrawable(
								R.drawable.status_circle_red));
					} else {
						status_sensor.setBackground(getResources().getDrawable(
								R.drawable.status_circle_green));
					}
				} else if (prop.equals("ENABLE_LOGGING")) {
					enableLog
							.setText(Integer.toString(device.getIsLogEnabled()));
				}else 	if(event.getPropertyName().equals("CONNECTED_AND_READ")) {
					// Once all the necessary data is read we dismiss the laoding spinner or 
					// progress dialog for the user.
					if(pd != null){
						if(pd.isShowing()){
							pBar.setProgress(100);
							pd.dismiss();
						}
					}
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		if (device != null) {
			device.removePropertyChangeListener(this);
			device.disconnectDevice();
		}

		Intent intent = new Intent(DeviceDetailsActivity.this,
				StartActivity.class);
		startActivity(intent);
		finish();

	}

}
