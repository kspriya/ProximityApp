package com.commnsense.proximity;

import java.beans.PropertyChangeSupport;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsActivity extends BaseActivity {
	TextView devName;
	TextView name;
	final static private long ONE_SECOND = 1000;
	final static private long TWENTY_SECONDS = ONE_SECOND * 20;
	PendingIntent pi;
	BroadcastReceiver br;
	AlarmManager am;
	private final PropertyChangeSupport changeProperty = new PropertyChangeSupport(
			this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);

		name = (TextView) findViewById(R.id.name);

		devName = (TextView) findViewById(R.id.dev_name);
		devName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				if (devName.getText() != null) {
					String str = devName.getText().toString();
					name.setText(str);
					// devName.setText(str);
				}
			}
		});

		String deviceId = this.getIntent().getStringExtra("device_id");

		final Device dev = DeviceList.getInstance().getDeviceById(deviceId);

		if (dev == null) {
			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
			dlgAlert.setTitle("Error");
			dlgAlert.setMessage("Device not visible. Connect Again !!");
			dlgAlert.setIcon(R.drawable.ic_launcher);
			dlgAlert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							SettingsActivity.this.onBackPressed();
						}
					});
			dlgAlert.create().show();
		}
		Button calibrateButton = (Button) findViewById(R.id.calibrate);

		Button clearLogButton = (Button) findViewById(R.id.clearLog);
		clearLogButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dev.writeClearLog();
				// dev.getDoorStatuses().clear();
				// Log.d("log size",""+dev.getDoorStatuses().size());
				// Intent intent = new Intent(DeviceDetailsActivity.this,
				// ClearLog.class);
				// intent.putExtra("device_id", device.serialNo);
				//
				// startActivity(intent);
			}
		});

		Button saveButton = (Button) findViewById(R.id.saveButton);
		final ToggleButton enableAlarm = (ToggleButton) findViewById(R.id.enableAlarm);
		enableAlarm.setChecked(dev.getEnable_alarm());

		enableAlarm.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
						SystemClock.elapsedRealtime() + TWENTY_SECONDS, pi);

			}
		});

		setup();

		// if(dev==null){
		//
		// AlertDialog.Builder builder = new
		// AlertDialog.Builder(SettingsActivity.this);
		// builder.setMessage("Device is not active now!!")
		//
		// .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// SettingsActivity.this.finish();
		// }
		// });
		//
		// AlertDialog alert = builder.create();
		// alert.show();
		// }else{
		name.setText(dev.name);
		devName.setText(dev.name);

		calibrateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,
						CalibrateActivity.class);
				intent.putExtra("device_id", dev.serialNo);
				startActivity(intent);

			}
		});

		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean on = ((ToggleButton) enableAlarm).isChecked();
				dev.setEnable_alarm(on);
				String str = devName.getText().toString();
				dev.setName(str);
				dev.changeName(str);

				Toast.makeText(SettingsActivity.this, "Settings changes saved",
						Toast.LENGTH_SHORT).show();

			}
		});
	}

	private void setup() {
		br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i) {
				Toast.makeText(c, "Rise and Shine!", Toast.LENGTH_LONG).show();
			}

		};
		registerReceiver(br, new IntentFilter(
				"com.commnsense.proximity.DeviceDetailsActivity"));
		pi = PendingIntent.getBroadcast(this, 0, new Intent(
				"com.authorwjf.DeviceDetailsActivity"), 0);
		am = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
	}

	// }
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		am.cancel(pi);
		unregisterReceiver(br);
		super.onDestroy();
	}

}

class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Alarm worked.", Toast.LENGTH_LONG).show();

	}
}