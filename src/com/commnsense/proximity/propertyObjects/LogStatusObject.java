package com.commnsense.proximity.propertyObjects;

import java.util.List;

import com.commnsense.proximity.DoorLogRecord;

public class LogStatusObject {
	List<DoorLogRecord> oldvalue;
	List<DoorLogRecord> newvalue;

	public LogStatusObject(List<DoorLogRecord> oldValue, List<DoorLogRecord> newValue) {
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
