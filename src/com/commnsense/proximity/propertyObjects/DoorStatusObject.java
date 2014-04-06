package com.commnsense.proximity.propertyObjects;

public class DoorStatusObject extends ChangeNotificationObject {
	int oldvalue;
	int newvalue;

	public DoorStatusObject(int oldValue, int newValue) {
		oldvalue = oldValue;
		newvalue = newValue;
	}

	public Object getOldValue() {
		return oldvalue;
	}

	@Override
	public Object getNewValue() {
		return newvalue;
	}

}
