package com.commnsense.proximity;

public class OpenCountRecord {

	private int openCount;
	private int timeSinceY2K;
	private long time;

	public OpenCountRecord(int openCount, int time) {
		this.timeSinceY2K = time;
		this.openCount = openCount;
		this.time = Device.getmilliSecondsSinceStartOfTime(time);
	}

	public int getOpenCount() {
		return openCount;
	}

	public float getTime() {
		return timeSinceY2K;
	}

	// public int getRelativeTime(int baseTime) {
	// return timeSinceY2K - baseTime;
	// }

	public long getTimeSinceY2K() {
		return time;
	}

}
