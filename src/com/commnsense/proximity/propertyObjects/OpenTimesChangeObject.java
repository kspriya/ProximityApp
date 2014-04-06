package com.commnsense.proximity.propertyObjects;

public class OpenTimesChangeObject extends ChangeNotificationObject {
	double oldvalue;
	double newvalue;

	public OpenTimesChangeObject(double oldValue, double newValue) {
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
