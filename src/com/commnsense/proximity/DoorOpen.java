package com.commnsense.proximity;

import android.bluetooth.BluetoothGattCharacteristic;

public class DoorOpen {
	private static DoorOpen singleTemp = new DoorOpen();

	private DoorOpen() {
	}

	public static DoorOpen getInstance() {
		return singleTemp;
	}

	public double extractTemperature(BluetoothGattCharacteristic c) {
		byte[] rawValue = c.getValue();
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
}
