package com.commnsense.proximity.propertyObjects;


public class LogEnableChangeObject {
	int oldvalue;
	int newvalue;

	public LogEnableChangeObject(int oldValue, int newValue) {
		oldvalue = oldValue;
		newvalue = newValue;
	}

	public Object getOldValue() {
		return oldvalue;
	}

	public Object getNewValue() {
		return newvalue;
	}
}
