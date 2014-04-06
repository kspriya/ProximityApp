package com.commnsense.proximity.propertyObjects;

import java.util.List;

import com.commnsense.proximity.OpenCountRecord;

public class LogOpenCountObject {
	List<OpenCountRecord> oldvalue;
	List<OpenCountRecord> newvalue;

	public LogOpenCountObject(List<OpenCountRecord> oldValue, List<OpenCountRecord> newValue) {
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
