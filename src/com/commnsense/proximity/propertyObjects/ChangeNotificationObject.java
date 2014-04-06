package com.commnsense.proximity.propertyObjects;

public abstract class ChangeNotificationObject {
	String deviceId;

	public String getDeviceId() {
		return deviceId;
	}

	public abstract Object getOldValue();

	public abstract Object getNewValue();
}
