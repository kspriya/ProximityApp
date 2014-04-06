package com.commnsense.proximity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.commnsense.proximity.utils.CustomAdapter;

public class StartActivity extends Activity implements PropertyChangeListener {
	// ArrayList<String> listItems=new ArrayList<String>();
	// ArrayAdapter<String> adapter;

	private static final String TAG = StartActivity.class.getSimpleName();
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;
	private Handler mHandler = new Handler();
	private int scanCount;
	private PropertyChangeListener listener = this;
	private static final int REQUEST_ENABLE_BT = 123456;
	ListView list;
	private CustomAdapter listAdapter;
	private boolean isRefresh;
	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 4000;
	ScheduledExecutorService scheduleTaskExecutor = Executors
			.newScheduledThreadPool(1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startactivity_main);

		listAdapter = new CustomAdapter(this, DeviceList.getInstance()
				.getLocalDevices());

		list = (ListView) findViewById(R.id.device_list);
		list.setAdapter(listAdapter);
		isRefresh = false;
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View arg1,
					int pos, long arg3) {
				Device device = (Device) listView.getAdapter().getItem(pos);

				Intent intent = new Intent(StartActivity.this,
						DeviceDetailsActivity.class);
				intent.putExtra("device_id", device.serialNo);
				startActivity(intent);
				finish();
			}
		});
		ImageButton refreshButton = (ImageButton) findViewById(R.id.refresh);
		refreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isRefresh = true;
				scanLeDevice(true);
			}
		});
		// adapter=new
		// ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
		// setListAdapter(adapter);
		checkBLE();
		init();
		boolean ret = enableBLE();
		if (ret) {
			startScan(false);
		} else {
			Log.d(TAG, getCtx() + " onCreate Waiting for on onActivityResult");
		}
		scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
			public void run() {
				scanLeDevice(true);
			}
		}, 0, 8, TimeUnit.SECONDS);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void init() {
		// Initializes Bluetooth adapter.
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
	}

	private void startScan(boolean success) {
		if (mBluetoothAdapter == null) {
			init();
		}
		if (success) {
			mScanning = true;
			scanLeDevice(mScanning);
			return;
		}
		if (enableBLE()) {
			mScanning = true;
			scanLeDevice(mScanning);
		} else {
			Log.d(TAG, getCtx()
					+ " startScan Waiting for on onActivityResult success:"
					+ success);
		}
	}

	private void scanLeDevice(final boolean enable) {
		if (enable) {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			DeviceList.getInstance().removeLostBLEList();
			DeviceList.getInstance().backupList();
		//	listAdapter.notifyDataSetChanged();
			if (isRefresh) {
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
				isRefresh = false;
			}
			// Stops scanning after a pre-defined scan period.
			DeviceList.getInstance().backupList();

			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					Log.d(TAG, getCtx() + "run stopLeScan");
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					listAdapter = new CustomAdapter(StartActivity.this,
							DeviceList.getInstance().getLocalDevices());
					list.setAdapter(listAdapter);

				}
			}, SCAN_PERIOD);
			Log.d("devicelist", DeviceList.getInstance().getLocalDevices()
					.toString());

			Log.d(TAG, getCtx() + " scanLeDevice startLeScan:" + enable);
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			Log.d(TAG, getCtx() + " scanLeDevice stopLeScan:" + enable);
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}

	private static String getCtx() {
		Date dt = new Date();
		return dt + " thread:" + Thread.currentThread().getName();
	}

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
				final byte[] scanRecord) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Log.d("Scan record", new String(scanRecord.toString()));
					
					final int mID = scanRecord[1] << 8;
					Log.d("Device scanned ", device.getName() + "  " + mID);
					ByteBuffer b=ByteBuffer.wrap(scanRecord);
					b.order(ByteOrder.LITTLE_ENDIAN);
					int a=b.getShort();
					int f=b.getShort();
					int c=b.getShort();
					int d=b.getShort();
					Log.d("1", ""+(int)(scanRecord[3]<<8));
					
					int manufactureData = (scanRecord[3] & 0xff )<< 8;
					manufactureData = manufactureData + (scanRecord[2] & 0xff );
					
					if (mID == 256) {
						// devices.add(device);
						// allDevices.put(device.getAddress(), device);
						boolean changed = DeviceList.getInstance()
								.addDevice(getApplicationContext(), device,
										listener, rssi);

						if (changed) {
							Log.d("devicelist", DeviceList.getInstance()
									.getLocalDevices().toString());
							listAdapter = new CustomAdapter(StartActivity.this,
									DeviceList.getInstance().getLocalDevices());
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									list.setAdapter(listAdapter);
									listAdapter.notifyDataSetChanged();
								}
							});

							 listAdapter.notifyDataSetChanged();
						}
						System.out.println("Got the device "
								+ DeviceList.getInstance().getLocalDevices());

						Log.d("TAG", new String(scanRecord));
					}

					scanCount++;

					String msg = getCtx()
							+ "\nLeScanCallback.onLeScan stopLeScan run "
							+ scanCount + "\nDevice:" + device + "\nRssi:"
							+ rssi + "\nScanRecord:" + scanRecord.toString();
					Log.d(TAG, msg);

					// addItems(msg);
				}
			});
		}
	};

	// private void addItems(String msg) {
	// synchronized(listItems){
	// listItems.add(msg);
	// adapter.notifyDataSetChanged();
	// }
	// }
	// public void startScan(View v) {
	// startScan(false);
	// }
	//
	// public void stopScan(View v) {
	// mScanning = false;
	// scanLeDevice(mScanning);
	// }

	// public void clear(View v) {
	// Log.d(TAG,getCtx()+" called clear");
	// synchronized(listItems){
	// listItems.clear();
	// adapter.notifyDataSetChanged();
	// }
	// }
	private void checkBLE() {
		// Use this check to determine whether BLE is supported on the device.
		// Then
		// you can selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "Bluetooth Not Supported", Toast.LENGTH_SHORT)
					.show();
			finish();
		}
	}

	private boolean enableBLE() {
		boolean ret = true;
		// Ensures Bluetooth is available on the device and it is enabled. If
		// not,
		// displays a dialog requesting user permission to enable Bluetooth.
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Log.d(TAG,
					getCtx()
							+ " enableBLE either mBluetoothAdapter == null or disabled:"
							+ mBluetoothAdapter);
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			ret = false;
		}
		return ret;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, getCtx() + " onActivityResult requestCode=" + requestCode
				+ ", resultCode=" + resultCode + ", Intent:" + data);
		if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
			startScan(true);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
	}
	
	@Override
	public void onPause() {
		super.onPause();
		// bluetoothAdapter.stopLeScan(this);
		// scheduleTaskExecutor.shutdown();
		// finish();
	
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			finish();
			return;
		}
		DeviceList.getInstance().getLocalDevices().clear();
		listAdapter = new CustomAdapter(this, DeviceList.getInstance().getLocalDevices());
		list.setAdapter(listAdapter);
	}
	/**
	 * Destroys the activity.
	 */
	@Override
	protected void onDestroy() {

		super.onDestroy();
		if (mLeScanCallback != null && mBluetoothAdapter != null) {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		scheduleTaskExecutor.shutdown();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}