package com.commnsense.proximity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CalibrateActivity extends BaseActivity implements
		PropertyChangeListener {

	private Device device;
	ImageView calibrate_circle;
	TextView devName;

	// ScheduledExecutorService scheduleTaskExecutor = Executors
	// .newScheduledThreadPool(1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calibrate_activity);
		String deviceId = this.getIntent().getStringExtra("device_id");
		device = DeviceList.getInstance().getDeviceById(deviceId);
		device.addPropertyChangeListener(this);
		devName = (TextView) findViewById(R.id.dev_name);
		calibrate_circle = (ImageView) findViewById(R.id.calibrate_circle);

		devName.setText(device.name);
		if (device == null) {
			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
			dlgAlert.setTitle("Error");
			dlgAlert.setMessage("Device not visible. Connect Again !!");
			dlgAlert.setIcon(R.drawable.ic_launcher);
			dlgAlert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							CalibrateActivity.this.onBackPressed();
						}
					});
			dlgAlert.create().show();
		}

	//	device.readDoorStatus();

		if (device.isOpen == 0) {
			calibrate_circle.setImageResource(R.drawable.status_circle_green);
		} else {
			calibrate_circle.setImageResource(R.drawable.status_circle_red);
		}

		// scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
		// public void run() {
		 device.readDoorStatus();
		// if (device.getIsOpen()==0) {
		// calibrate_circle.setImageResource(R.drawable.status_circle_green);
		// } else {
		// calibrate_circle.setImageResource(R.drawable.status_circle_red);
		// }
		// }
		// }, 0, 5, TimeUnit.SECONDS);

	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				String propertyName = event.getPropertyName();
				if (propertyName.equals("DOOR_STATUS")) {
					if (device.isOpen == 0) {
						calibrate_circle
								.setImageResource(R.drawable.status_circle_green);
					} else {
						calibrate_circle
								.setImageResource(R.drawable.status_circle_red);
					}

				}
			}
		});

	}

}
