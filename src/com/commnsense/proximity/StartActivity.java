package com.commnsense.proximity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


public class StartActivity extends Activity  implements PropertyChangeListener{
	private final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothManager bluetoothManager;
	private boolean mScanning;
	private ArrayList<BluetoothDevice> devices;
	private PropertyChangeListener listener = this;

	private Handler mHandler = new Handler();

	HashMap<String, BluetoothDevice> allDevices;
	private BluetoothAdapter.LeScanCallback mLeScanCallback;
	ScheduledExecutorService scheduleTaskExecutor = Executors
			.newScheduledThreadPool(1);

	private CustomAdapter listAdapter;
	private long SCAN_DURATION=2500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startactivity_main);
		
		devices = new ArrayList<BluetoothDevice>();
		allDevices = new HashMap<String, BluetoothDevice>();
	
		listAdapter=new CustomAdapter(this);
		
		ListView list = (ListView) findViewById(R.id.device_list);
		list.setAdapter(listAdapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View arg1, int pos,
					long arg3) {
				Device device = (Device) listView.getAdapter().getItem(pos);

				Intent intent = new Intent(StartActivity.this,
						DeviceDetailsActivity.class);
				intent.putExtra("device_id", device.serialNo);
				startActivity(intent);
			}
		});

		ImageButton refreshButton = (ImageButton) findViewById(R.id.refresh);
		refreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				scanLeDevice();
			}
		});
		bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
		bluetoothAdapter = bluetoothManager.getAdapter();
		if (bluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth Not Supported", Toast.LENGTH_LONG)
					.show();
		} else {
			bluetoothAdapter.startDiscovery();
		}

		if (!bluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}

		mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
			@Override
			public void onLeScan(final BluetoothDevice device, final int rssi,
					final byte[] scanRecord) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						final int mID = scanRecord[1] << 8;
						
						if (mID == 256) {
							devices.add(device);
							allDevices.put(device.getAddress(), device);
							boolean changed = DeviceList.getInstance()
									.addDevice(getApplicationContext(), device,
											listener, rssi);
							if (changed) {

								listAdapter.notifyDataSetChanged();
							}
							System.out.println("Got the device " + allDevices);

							Log.d("TAG", new String(scanRecord));
						      }
					}
				});
			}
		};
	
		scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
			public void run() {
				scanLeDevice();
			}
		}, 0, 5, TimeUnit.SECONDS);

	}


	private void scanLeDevice() {
		if (!devices.isEmpty()) {
			System.out.println("scanned devices");
		}
		if (bluetoothAdapter.isDiscovering()) {
			mScanning = false;
			bluetoothAdapter.stopLeScan(mLeScanCallback);
			DeviceList.getInstance().removeLostBLEList();
			DeviceList.getInstance().backupList();
		
		}
		DeviceList.getInstance().clearBeforeScan();
		DeviceList.getInstance().backupList();

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mScanning = false;
				bluetoothAdapter.stopLeScan(mLeScanCallback);
				DeviceList.getInstance().removeLostBLEList();
				DeviceList.getInstance().backupList();
				listAdapter.notifyDataSetChanged();
			}
		}, SCAN_DURATION);

		mScanning = true;
		bluetoothAdapter.startLeScan(mLeScanCallback);
	}
	@Override
	public void onPause() {
		super.onPause();
		//scheduleTaskExecutor.shutdown();
		finish();
	}

@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
}
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
