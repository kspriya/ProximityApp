package com.commnsense.proximity;

import java.util.List;

import android.util.Log;

import com.androidplot.xy.XYSeries;

public class DoorSeries implements XYSeries {
	private List<DoorLogRecord> datasource;
	private String title;

	public DoorSeries(List<DoorLogRecord> datasource, String title) {
		this.datasource = datasource;
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public int size() {
		return datasource.size();
	}

	@Override
	public Number getX(int index) {
		Log.d("x", " " + (datasource.get(index).getTimeSinceY2K()));
		return (datasource.get(index).getTimeSinceY2K());
	}

	@Override
	public Number getY(int index) {
		Log.d("Y", "" + (datasource.get(index).getStatus()));
		return datasource.get(index).getStatus();
	}

	public long getFirstTime() {
		if (datasource.size() > 0) {
			Log.d("FIRST TIME", "" + datasource.get(0).getTimeSinceY2K());
			return (datasource.get(0).getTimeSinceY2K());
		} else
			return 0;
	}

	public long getLastTime() {
		if (datasource.size() <= 1) {
			return 10;
		}
		Log.d("LAST TIME",
				"" + (datasource.get(datasource.size() - 1).getTimeSinceY2K()));
		return (datasource.get(datasource.size() - 1).getTimeSinceY2K());
	}
}
