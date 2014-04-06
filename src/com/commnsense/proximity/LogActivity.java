package com.commnsense.proximity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.WindowManager;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip.IconTabProvider;
import com.commnsense.proximity.fragments.DoorFragment;
import com.commnsense.proximity.fragments.HistoFragment;
import com.commnsense.proximity.fragments.SuperAwesomeCardFragment;

public class LogActivity extends BaseActivity  {
	Device dev;
	// private XYPlot plot;
	// DoorSeries series;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	private int currentColor = 0xffb74d3f;
	private DoorFragment doorFrag = new DoorFragment();
	private HistoFragment histoFrag = new HistoFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_data);
		String deviceId = this.getIntent().getStringExtra("device_id");
		dev = DeviceList.getInstance().getDeviceById(deviceId);

		if (dev == null) {
			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
			dlgAlert.setTitle("Error");
			dlgAlert.setMessage("Device not visible. Connect Again !!");
			dlgAlert.setIcon(R.drawable.ic_launcher);
			dlgAlert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							LogActivity.this.onBackPressed();
						}
					});
			dlgAlert.create().show();
		}
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);

		changeColor(currentColor);

		dev.readLogData();
	//	dev.addPropertyChangeListener(this);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
				WindowManager.LayoutParams.FLAG_SECURE);
		// plot = (XYPlot) findViewById(R.id.plot1);
		// if (dev.getDoorStatuses().size() > 0) {
		//
		// plotGraph(dev);
		//
		// }
		// else{
		// try {
		// Thread.sleep(2000);
		// plotGraph(dev);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
	}

	// private void plotGraph(final Device dev) {
	// Log.d("plot graph", "plot graph started");
	// if (series != null) {
	// plot.removeSeries(series);
	// }
	// series = new DoorSeries(dev.getDoorStatuses(), "Door Status");
	// LineAndPointFormatter series1Format = new LineAndPointFormatter(
	// Color.RED, Color.GREEN, Color.TRANSPARENT, null);
	// // add a new series' to the xyplot:
	// series1Format.getLinePaint().setStrokeWidth(4);
	//
	// plot.addSeries(series, series1Format);
	// // reduce the number of range labels
	// plot.getGraphWidget().setDomainLabelOrientation(-45);
	// plot.setDomainStepMode(XYStepMode.SUBDIVIDE);
	// // thin out domain/range tick labels so they don't overlap each other:
	// plot.setTicksPerDomainLabel(2);
	// plot.setTicksPerRangeLabel(5);
	// plot.setDomainBoundaries(series.getFirstTime(), series.getLastTime(),
	// BoundaryMode.FIXED);
	// Log.d("plot graph", "plot graph completed");
	//
	// plot.getTitleWidget().getLabelPaint().setTextSize(45);
	// plot.getTitleWidget().setSize(
	// new SizeMetrics(50, SizeLayoutType.ABSOLUTE, 600,
	// SizeLayoutType.ABSOLUTE));
	//
	// // customize the appearance of the graph.
	// plot.getGraphWidget().getGridBackgroundPaint()
	// .setColor(Color.parseColor("#2d353d"));
	// plot.getGraphWidget().getDomainOriginLinePaint()
	// .setColor(Color.parseColor("#b74d3f"));
	// plot.getGraphWidget().getDomainOriginLinePaint().setStrokeWidth(7);
	// plot.getGraphWidget().getRangeOriginLinePaint()
	// .setColor(Color.parseColor("#b74d3f"));
	// plot.getGraphWidget().getRangeOriginLinePaint().setStrokeWidth(7);
	//
	// // customize our domain/range labels
	// plot.setDomainLabel("Time");
	// plot.getGraphWidget().getDomainOriginLabelPaint().setTextSize(30);
	// plot.getGraphWidget().getDomainLabelPaint().setTextSize(30);
	// plot.getDomainLabelWidget().getLabelPaint().setTextSize(30);
	// plot.getDomainLabelWidget().setSize(
	// new SizeMetrics(40, SizeLayoutType.ABSOLUTE, 500,
	// SizeLayoutType.ABSOLUTE));
	//
	// plot.setRangeLabel("Open/Close");
	// plot.getGraphWidget().getRangeLabelPaint().setTextSize(30);
	// plot.getGraphWidget().getRangeOriginLabelPaint().setTextSize(30);
	// plot.getRangeLabelWidget().getLabelPaint().setTextSize(30);
	// plot.getRangeLabelWidget().setSize(
	// new SizeMetrics(400, SizeLayoutType.ABSOLUTE, 40,
	// SizeLayoutType.ABSOLUTE));
	//
	// plot.getLegendWidget().setTableModel(new DynamicTableModel(1, 2));
	// plot.getLegendWidget().setSize(
	// new SizeMetrics(200, SizeLayoutType.ABSOLUTE, 300,
	// SizeLayoutType.ABSOLUTE));
	// plot.getLegendWidget().setPositionMetrics(
	// new PositionMetrics(60, XLayoutStyle.ABSOLUTE_FROM_RIGHT, 100,
	// YLayoutStyle.ABSOLUTE_FROM_TOP,
	// AnchorPosition.RIGHT_TOP));
	//
	// plot.getLegendWidget().getTextPaint().setTextSize(40);
	// plot.getLegendWidget().setIconSizeMetrics(
	// new SizeMetrics(40, SizeLayoutType.ABSOLUTE, 40,
	// SizeLayoutType.ABSOLUTE));
	//
	// Paint bgPaint = new Paint();
	// bgPaint.setColor(0x809a8e7e);
	// bgPaint.setStyle(Paint.Style.FILL);
	// bgPaint.setAlpha(140);
	// plot.getLegendWidget().setBackgroundPaint(bgPaint);
	//
	// plot.redraw();
	// // format of how the values on x axis should look.
	// plot.setDomainValueFormat(new Format() {
	// private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:MM:SS");
	//
	// @Override
	// public StringBuffer format(Object obj, StringBuffer toAppendTo,
	// FieldPosition pos) {
	//
	// // because our timestamps are in seconds we convet it to mins when
	// //displaying
	// // them as labels on the axis.
	// // Double timestamp = Math.ceil(((Number) obj).intValue() / 60);
	// // return new StringBuffer(""+timestamp);
	// //
	// long timestamp = ((Number) obj).longValue() * 1000;
	// Date date = new Date(timestamp);
	// return dateFormat.format(date, toAppendTo, pos);
	// }
	//
	// @Override
	// public Object parseObject(String source, ParsePosition pos) {
	// return null;
	//
	// }
	// });
	//
	// // Set the boundaries of the x axis and y axis.
	// // if(dev.getMinTemp() <= dev.getMaxTemp()) {
	// // plot.setRangeBoundaries(dev.getMinTemp()-2, dev.getMaxTemp()+2,
	// // BoundaryMode.FIXED);
	// // } else {
	// // plot.setRangeBoundaries(0, dev.getMaxTemp()+2,
	// // BoundaryMode.FIXED);
	// // }
	//
	// }

//	@Override
//	public void propertyChange(final PropertyChangeEvent event) {
//		runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
//				String property = event.getPropertyName();
//				if (property.equalsIgnoreCase("LOG_STATUS")) {
//					Log.d("property change", "property changed");
//					// plotGraph(dev);
//				}
//
//			}
//		});
//	}

	private void changeColor(int newColor) {

		tabs.setIndicatorColor(newColor);
		currentColor = newColor;

	}

	public class MyPagerAdapter extends FragmentPagerAdapter implements
			IconTabProvider {

		private final String[] TITLES = { "Door Status ",
				"No. of times door opened" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return doorFrag;
			case 1:
				return histoFrag;

			}
			return SuperAwesomeCardFragment.newInstance(position);
		}

		@Override
		public int getPageIconResId(int position) {

			// switch(position) {
			// case 0:
			// return R.drawable.temperature_48;
			// case 1:
			// return R.drawable.location_48;
			// }
			return R.drawable.ic_launcher;
		}

	}

	@Override
	public void onBackPressed() {
		    	super.onBackPressed();
		    			}

	public Device getDevice() {
		return dev;
	}

}
