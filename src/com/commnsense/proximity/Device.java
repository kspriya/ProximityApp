package com.commnsense.proximity;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Device extends BluetoothGattCallback {
	public String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String connectionState = "Disconnected";
	public String STATE_CHANGED = "STATECHANGED";
	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	private int mConnectionState = STATE_DISCONNECTED;
	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";

	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	private Boolean sensorInRange;

	public Boolean getSensorInRange() {
		return sensorInRange;
	}

	public void setSensorInRange(Boolean sensorInRange) {
		this.sensorInRange = sensorInRange;
	}

	private Boolean enable_alarm;
	private int enable_logging=0;

	public int getEnable_logging() {
		return enable_logging;
	}

	public void setEnable_logging(int enable_logging) {
		this.enable_logging = enable_logging;
	}

	public Boolean getEnable_alarm() {
		return enable_alarm;
	}

	public void setEnable_alarm(Boolean enable_alarm) {
		this.enable_alarm = enable_alarm;
	}

	public List<List<String>> logs;
	private final PropertyChangeSupport changeProperty = new PropertyChangeSupport(
			this);

	public List<LocationAndTime> locationHistory;
	public String serialNo;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public enum Status {
		LOW, NORMAL, HIGH
	}

	public BluetoothGatt mBluetoothGatt;
	public double noOfOpenTimes = 0;

	public double getNoOfOpenTimes() {
		return noOfOpenTimes;
	}

	public void setNoOfOpenTimes(double noOfOpenTimes) {
		this.noOfOpenTimes = noOfOpenTimes;
	}

	public int isOpen=0;

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public Status status = Status.NORMAL;
	public int batteryProgress = 90;

	public int getBatteryProgress() {
		return batteryProgress;
	}

	public void setBatteryProgress(int batteryProgress) {
		this.batteryProgress = batteryProgress;
	}

	private String macAddress;

	public String getMacAddress() {
		return macAddress;
	}

	public BluetoothDevice getBluetoothDevice() {
		return bluetoothDevice;
	}

	private BluetoothDevice bluetoothDevice;

	private WriteQueue writeQueue = new WriteQueue();
	private String PROPERTY_OPENCOUNT = "OPEN_COUNT";
	private String PROPERTY_SENSORRANGE = "SENSOR_RANGE";
	private String PROPERTY_ENABLELOGGING="ENABLE_LOGGING";
	
	private int rssi;
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	private int test;
	public boolean isDevice=false;
	private int mState=0;
	
	public int getRssi() {
		return rssi;
	}

	@Override
	public void onConnectionStateChange(BluetoothGatt gatt, int status,
			int newState) {
		String intentAction;
		if (newState == BluetoothProfile.STATE_CONNECTED) {
			intentAction = ACTION_GATT_CONNECTED;
			mConnectionState = STATE_CONNECTED;
			broadcastUpdate(intentAction);
			Log.d("DeviceRecord", name + " Connected to GATT server.");
			Log.d("DeviceRecord", "Attempting to start service discovery for "
					+ name);
			mBluetoothGatt.discoverServices();
		
			connectionState = "Connected";
			changeProperty.firePropertyChange(STATE_CHANGED, "Disconnected",
					"Connected");

		} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
			intentAction = ACTION_GATT_DISCONNECTED;
			mConnectionState = STATE_DISCONNECTED;
			writeQueue.flush();
			Log.d("TAG", name + " Disconnected from GATT server.");
			broadcastUpdate(intentAction);
			connectionState = "Disconnected";
			changeProperty.firePropertyChange(STATE_CHANGED, "Connected",
					"Disconnected");
		}
	}

	@Override
	public void onServicesDiscovered(BluetoothGatt gatt, int status) {
		Log.d("SERVICES", name + " onServicesDiscovered received: " + status
				+ " no of services : " + gatt.getServices().size());
		if (gatt.getServices().size() < 4) {
			disconnectDevice();
			return;
		}
		if (status == BluetoothGatt.GATT_SUCCESS) {
			broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
			readAllData();
		}
	}

	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);

	}
	public void readDoorStatus() {
		writeQueue.queueRunnable(new Runnable() {
			@Override
			public void run() {
				BluetoothGattService magnetService = mBluetoothGatt.getService(UUID
						.fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
				if (magnetService != null) {
					BluetoothGattCharacteristic data = magnetService.getCharacteristic(UUID
							.fromString("0000fff1-0000-1000-8000-00805f9b34fb"));
					if(data!=null){
						setNotifySensor(mBluetoothGatt);
						
					mBluetoothGatt.readCharacteristic(data);
					int open=(int) readOpen(data.getValue());
					if(open==1){
						setIsOpen(1);
					}else if(open==0){
						setIsOpen(0);
					}
					
					}
				}
			}
		});	
	}
	
	
	public void readAllData() {
		writeQueue.queueRunnable(new Runnable() {
			@Override
			public void run() {
				BluetoothGattService magnetService = mBluetoothGatt.getService(UUID
						.fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
				if (magnetService != null) {
					BluetoothGattCharacteristic data = magnetService.getCharacteristic(UUID
							.fromString("0000fff2-0000-1000-8000-00805f9b34fb"));
					mBluetoothGatt.readCharacteristic(data);
					readOpen(data.getValue());
				}
			}
		});
		writeQueue.queueRunnable(new Runnable() {
			@Override
			public void run() {
				BluetoothGattService magnetService = mBluetoothGatt.getService(UUID
						.fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
				if (magnetService != null) {
					BluetoothGattCharacteristic data = magnetService.getCharacteristic(UUID
							.fromString("00002a2b-0000-1000-8000-00805f9b34fb"));
					if(data!=null){
					mBluetoothGatt.readCharacteristic(data);}
				}
			}
		});
//		writeQueue.queueRunnable(new Runnable() {
//			@Override
//			public void run() {
//				BluetoothGattService magnetService = mBluetoothGatt.getService(UUID
//						.fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
//				if (magnetService != null) {
//					BluetoothGattCharacteristic data = magnetService.getCharacteristic(UUID
//							.fromString("0000fff5-0000-1000-8000-00805f9b34fb"));
//					if(data!=null){
//					mBluetoothGatt.readCharacteristic(data);}
//				}
//			}
//		});
	}
	public void changeName(final String str) {
		if (connectionState.equals("Connected")) {
			writeQueue.queueRunnable(new Runnable() {
				@Override
				public void run() {
					BluetoothGattService magnetService = mBluetoothGatt.getService(UUID
							.fromString("00001800-0000-1000-8000-00805f9b34fb"));
					BluetoothGattCharacteristic config = null;
					if(magnetService!=null){
					 config = magnetService.getCharacteristic(UUID
							.fromString("00002a00-0000-1000-8000-00805f9b34fb"));}
					if(config!=null){
					ByteBuffer b = ByteBuffer.allocate(19);
					if(str.getBytes().length<19){
					b.put (str.getBytes());
						config.setValue(b.array());
						mBluetoothGatt.writeCharacteristic(config);
					}}
				}
			});
		}		
	}

	public void changeLoggingCharacteristic(final int enable) {

		if (connectionState.equals("Connected")) {
			writeQueue.queueRunnable(new Runnable() {
				@Override
				public void run() {
					BluetoothGattService magnetService = mBluetoothGatt.getService(UUID
							.fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
					BluetoothGattCharacteristic config = null;
					if(magnetService!=null){
					 config = magnetService.getCharacteristic(UUID
							.fromString("0000fff5-0000-1000-8000-00805f9b34fb"));}
					if(config!=null){
					ByteBuffer b = ByteBuffer.allocate(2);
					b.order(ByteOrder.LITTLE_ENDIAN);
					b.put((byte) (enable));
						config.setValue(b.array());
						mBluetoothGatt.writeCharacteristic(config);
					
					}
				}
			});
		}
	}

	@Override
	// Result of a characteristic read operation
	public void onCharacteristicRead(BluetoothGatt gatt,
			BluetoothGattCharacteristic characteristic, int status) {
		Log.w("TAG", name + " onServicesDiscovered received: " + status);
		if (status == BluetoothGatt.GATT_SUCCESS) {
			if (characteristic.getUuid().equals(
					UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb"))) {
				Double oldValue = noOfOpenTimes;
				setNoOfOpenTimes(readOpen(characteristic.getValue()));
				changeProperty
						.firePropertyChange(PROPERTY_OPENCOUNT, null,
								new OpenTimesChangeObject(oldValue,
										getNoOfOpenTimes()));

			}
			
		
//		if (characteristic.getUuid().equals(
//				UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb"))) {
//			byte[] val = characteristic.getValue();
//			
//			int open=(int) readOpen(val);
//			changeProperty
//			.firePropertyChange(PROPERTY_OPENCOUNT, null,
//					new OpenTimesChangeObject(0,
//							open));
//
//
//		}
		if (characteristic.getUuid().equals(
				UUID.fromString("00002a2b-0000-1000-8000-00805f9b34fb"))) {
			byte[] val = characteristic.getValue();
			
			int time=(int) readOpen(val);
		}
		if (characteristic.getUuid().equals(
				UUID.fromString("0000fff3-0000-1000-8000-00805f9b34fb"))) {
			byte[] val = characteristic.getValue();
//			int oldlog=enable_logging;
			enable_logging=(int) readOpen(val);
//			changeProperty
//			.firePropertyChange(PROPERTY_ENABLELOGGING, null,
//					new OpenTimesChangeObject(oldlog,
//							enable_logging));

		}
		}
		writeQueue.issue();
	}

	
	private void setNotifySensor(BluetoothGatt gatt) {
		 
	    BluetoothGattCharacteristic characteristic = null;
	    switch (mState) {
	        case 0:
	        	BluetoothGattService magnetService = mBluetoothGatt.getService(UUID
						.fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
				if(magnetService!=null){
					characteristic = magnetService.getCharacteristic(UUID
						.fromString("0000fff1-0000-1000-8000-00805f9b34fb"));}
	            break;
	    
	        default:
	            return;
	    }
	 
	    //Enable local notifications
	    gatt.setCharacteristicNotification(characteristic, true);
	 
	    //Enabled remote notifications
	    BluetoothGattDescriptor desc = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
	    desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
	    gatt.writeDescriptor(desc);
	}
	public static final TimeZone UTC = TimeZone.getTimeZone("UTC") ;
	public static final Calendar Y2K_EPOCH = Calendar.getInstance(UTC);
	static {
		Y2K_EPOCH.clear();
		// Month is 0 based; day is 1 based.  Reset time to be first second of January 1, 2000
		Y2K_EPOCH.set(2000, 0, 1, 0, 0, 0);
	}
	public static final long MS_BETWEEN_ORIGINAL_EPOCH_AND_Y2K_EPOCH = Y2K_EPOCH.getTimeInMillis();

	public static int getSecondsSinceY2K(Date date) {
		long time = date.getTime();
		if (time < MS_BETWEEN_ORIGINAL_EPOCH_AND_Y2K_EPOCH) {
			throw new IllegalArgumentException("Date must occur after January 1, 2000");
		}
		return (int)((time - MS_BETWEEN_ORIGINAL_EPOCH_AND_Y2K_EPOCH)/1000);
	}
	void changeTime(){
		writeQueue.queueRunnable(new Runnable() {
			@Override
			public void run() {
				int timeSince1Jan2000 = getSecondsSinceY2K(new Date()); 
				BluetoothGattCharacteristic config = null;
				BluetoothGattService magnetService = mBluetoothGatt.getService(UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
				if(magnetService!=null){
				 config = magnetService.getCharacteristic(UUID.fromString("00002a2b-0000-1000-8000-00805f9b34fb"));}
				ByteBuffer b = ByteBuffer.allocate(4);
				b.order(ByteOrder.LITTLE_ENDIAN);
				b.putInt(timeSince1Jan2000);
				if(config!= null) {
					config.setValue(b.array());
				 mBluetoothGatt.writeCharacteristic(config);
				} 		
			}
		});

	}
	@Override
	public void onCharacteristicChanged(BluetoothGatt gatt,
			BluetoothGattCharacteristic characteristic) {
		
			if (characteristic.getUuid().equals(
					UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))) {
				Log.d("DeviceRecord", name + " characteristic changed");
					
			}
		
		Log.d("DeviceRecord", name + " characteristic changed");
		readOpen(characteristic.getValue());
		Double oldValue = noOfOpenTimes;
		setNoOfOpenTimes(readOpen(characteristic.getValue()));
		changeProperty.firePropertyChange(PROPERTY_OPENCOUNT, null,
				new OpenTimesChangeObject(oldValue, getNoOfOpenTimes()));
		logs.get(logs.size() - 1).add(
				"Door was opened " + getNoOfOpenTimes() + " times");
	}

	public void connectToDevice(Context context) {
		if (bluetoothDevice != null) {
			mBluetoothGatt = this.bluetoothDevice.connectGatt(context, false,
					this);
		}
	}

	public void disconnectDevice() {
		if (bluetoothDevice != null && mBluetoothGatt != null)
			mBluetoothGatt.close();
		writeQueue.flush();
	}

	public double readOpen(byte[] rawValue) {
		double intValue = 0;
		if (rawValue != null) {
			if (rawValue.length > 0)
				intValue = (int) rawValue[0];
			if (rawValue.length > 1)
				intValue = intValue + ((int) rawValue[1] << 8);
			if (rawValue.length > 2)
				intValue = intValue + ((int) rawValue[1] << 8);
			if (rawValue.length > 3)
				intValue = intValue + ((int) rawValue[1] << 8);
		}
		return intValue;
	}

	public void addDevice(BluetoothDevice device) {
		this.bluetoothDevice = device;
		name = device.getName();
		macAddress = device.getAddress();
		serialNo = device.getAddress().replace(":", "");
		setSensorInRange(false);
		enable_alarm = false;
		rssi=80;
		logs = new ArrayList<List<String>>();
		logs.add(new ArrayList<String>());
		logs.get(0).add("Door was opened " + getNoOfOpenTimes() + " times");
		if (serialNo.endsWith("362B1")) {
			name = "RBCs (Red Blood Cells)";
		} else if (serialNo.endsWith("394D6")) {
			name = "FFP (Fresh Frozen Plasma)";
		} else if (serialNo.endsWith("352B5")) {
			name = "Platelets";
		}
isDevice=true;
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeProperty.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		List<PropertyChangeListener> listeners = Arrays.asList(changeProperty
				.getPropertyChangeListeners());
		if (!listeners.contains(listener))
			changeProperty.addPropertyChangeListener(listener);

	}

	@Override
	public void onCharacteristicWrite(BluetoothGatt gatt,
			BluetoothGattCharacteristic characteristic, int status) {
		Log.d("DeviceRecord", name + " characteristic write");
		writeQueue.issue();
	}

	@Override
	public void onDescriptorRead(BluetoothGatt gatt,
			BluetoothGattDescriptor descriptor, int status) {
		descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
		Log.d("DeviceRecord", name + " descriptor read");
	}

	@Override
	public void onDescriptorWrite(BluetoothGatt gatt,
			BluetoothGattDescriptor descriptor, int status) {
		writeQueue.issue();
	}

	@Override
	public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
		Log.d("DeviceRecord", name + " descriptor write");
		this.rssi = rssi;
	}

	@Override
	public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
		Log.d("DeviceRecord", name + " some write read");
	}


	

	
}
