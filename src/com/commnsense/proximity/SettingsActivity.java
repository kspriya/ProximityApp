package com.commnsense.proximity;

import java.beans.PropertyChangeSupport;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity {
	TextView devName;
	TextView name;
	private String NAME_CHANGED = "NAME_CHANGED";

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
		
		 if(devName.getText()!=null){
		 String str= devName.getText().toString();
		 name.setText(str);
		 // devName.setText(str);
		 }
		 }
		 });
		String deviceId = this.getIntent().getStringExtra("device_id");

		final Device dev = DeviceList.getInstance().getDeviceById(deviceId);
		Button calibrateButton = (Button) findViewById(R.id.calibrate);
		
		Button saveButton = (Button) findViewById(R.id.saveButton);
		final ToggleButton enableAlarm = (ToggleButton) findViewById(R.id.enableAlarm);

		
//		if(dev==null){
//			
//			 AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
//			 builder.setMessage("Device is not active now!!")
//			 
//			   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//			   public void onClick(DialogInterface dialog, int id) {
//				   SettingsActivity.this.finish();
//						   }
//			 });
//			
//			AlertDialog alert = builder.create();
//			alert.show();
//		}else{
		name.setText(dev.name);
		devName.setText(dev.name);

		enableAlarm.setChecked(dev.getEnable_alarm());

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
	//}

}
