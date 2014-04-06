package com.commnsense.proximity;

public class DoorLogRecord {
	private int status;	
	private int timeSinceY2K;
	private long time;
	
	public DoorLogRecord( int status,int time) {
		this.timeSinceY2K = time;
		this.status = status;
		this.time = Device.getmilliSecondsSinceStartOfTime(time);
	}
	
	public int getStatus() {
		return status;
	}
	
	public float getTime() {
		return timeSinceY2K;
	}
	
	
//	public int getRelativeTime(int baseTime) {
//		return timeSinceY2K - baseTime;
//	}
	
	public long getTimeSinceY2K() {
		return time;
	}
}
