package com.commnsense.proximity;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.commnsense.proximity.Device.Status;

public class DeviceList 
{
	private static DeviceList instance;
	private static Set<String>keySet = new HashSet<String>();
	public static DeviceList getInstance()
	{
		if(instance == null)
			instance = new DeviceList();

		return instance;
	}

	private Set<Device> tags = new HashSet<Device>();
	public Set<Device> getTags() {
		return tags;
	}


	private HashMap<String,Device> deviceMap = new HashMap<String,Device>();
	{
		deviceMap = new HashMap<String,Device>();

		Device device1 = new Device();
		device1.name = "RBCs (Red Blood Cells)";
		device1.batteryProgress = 90;
		device1.status=Status.NORMAL;
		device1.serialNo="8";
		deviceMap.put(device1.serialNo,device1);

		Device device2 = new Device();
		device2.name = "Test2";
		device2.batteryProgress = 20;
		device2.status=Status.NORMAL;
		device2.serialNo="9";
		deviceMap.put(device2.serialNo,device2);

		Device device3 = new Device();
		device3.name = "Test3";
		device3.batteryProgress = 80;
		device3.status=Status.LOW;
		device3.serialNo="10";
		deviceMap.put(device3.serialNo,device3);
	}

	private DeviceList() { }

	public boolean addDevice(Context context, BluetoothDevice device,PropertyChangeListener listener, int rssi) {

		String id = device.getAddress().replace(":", "");
		boolean newdevice = false; 

		if(!deviceMap.containsKey(id)) {
			Device deviceRecord = new Device();	
			deviceRecord.addDevice(device);
			deviceRecord.setRssi(rssi);
			deviceRecord.logs=new ArrayList<List<String>>();
			deviceRecord.logs.add(new ArrayList<String>());
			deviceRecord.logs.get(deviceRecord.logs.size()-1).add("Door was opened "+deviceRecord.getNoOfOpenTimes()+" times");
			deviceRecord.addPropertyChangeListener(listener);
			deviceRecord.setSensorInRange(false);
			deviceRecord.setEnable_alarm(false);
			deviceMap.put(deviceRecord.serialNo, deviceRecord);
			Log.d("TAG",device.getAddress());
			newdevice = true;
		} else {
			keySet.remove(id);
		}
		Device deviceRecord  = deviceMap.get(id);
		//deviceRecord.batteryProgress = batteryPercentage;
		
			tags.add(deviceRecord);
		

		return newdevice;	
	}

	public Collection<Device> getLocalDevices()
	{
		return deviceMap.values();
	}

	public Device getDeviceById(String id)
	{
		return deviceMap.get(id);
	}

	public void clearBeforeScan() {
		tags.clear();
			}

	public void backupList() {
		keySet = new HashSet<String>();
		keySet.addAll(deviceMap.keySet());
	}

	public void removeLostBLEList() {

		for (String key:keySet) {
			deviceMap.remove(key);

		}
		keySet.clear();	
	}

}
