package com.commnsense.proximity.fragments;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.PositionMetrics;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.commnsense.proximity.Device;
import com.commnsense.proximity.LogActivity;
import com.commnsense.proximity.OpenCountSeries;
import com.commnsense.proximity.R;

public class HistoFragment extends Fragment implements PropertyChangeListener {
	Device device;
	private XYPlot plot;
	OpenCountSeries series;
	int count = 0;
	Date prevDate = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		device = ((LogActivity) getActivity()).getDevice();
		device.addPropertyChangeListener(this);

		FrameLayout fl = new FrameLayout(getActivity());

		final int margin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
						.getDisplayMetrics());
		params.setMargins(margin, margin, margin, margin);
		fl.setLayoutParams(params);
		fl.setBackgroundColor(0xffa9a9a9);
		inflater.inflate(R.layout.device_graph, fl);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
		// WindowManager.LayoutParams.FLAG_SECURE);
		plot = (XYPlot) fl.findViewById(R.id.plot1);
		if (device.getOpenCountStatuses().size() > 0) {

			plotGraph(device);

		} else {
			try {
				Thread.sleep(2000);
				plotGraph(device);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		return fl;
	}

	private void plotGraph(final Device dev) {
		Log.d("plot graph", "plot graph started");
		if (series != null) {
			plot.removeSeries(series);
		}
		series = new OpenCountSeries(dev.getOpenCountStatuses(), "Open Count");
		BarFormatter series1Format = new BarFormatter(Color.rgb(0, 200, 0),
				Color.rgb(100, 0, 0));// add a new series' to the xyplot:
		series1Format.getLinePaint().setStrokeWidth(6);
		plot.addSeries(series, series1Format);
		BarRenderer renderer = (BarRenderer) plot
				.getRenderer(BarRenderer.class);
		renderer.setBarWidth(120);

		// plot.addSeries(series, series1Format);
		// reduce the number of range labels
		plot.getGraphWidget().setDomainLabelOrientation(-2);
		plot.setDomainStepMode(XYStepMode.SUBDIVIDE);
		//plot.setDomainStep(XYStepMode.SUBDIVIDE, 5);
		plot.setRangeStep(XYStepMode.SUBDIVIDE, 5);
		// thin out domain/range tick labels so they don't overlap each other:
		plot.setTicksPerDomainLabel(4);
//		plot.setTicksPerRangeLabel(2);
		plot.setDomainBoundaries(series.getFirstTime(), series.getLastTime(),
				BoundaryMode.FIXED);
		plot.setRangeBoundaries(0, series.getLastCount(), BoundaryMode.FIXED);
		Log.d("plot graph", "plot graph completed");

		plot.getTitleWidget().getLabelPaint().setTextSize(45);
		plot.getTitleWidget().setSize(
				new SizeMetrics(50, SizeLayoutType.ABSOLUTE, 600,
						SizeLayoutType.ABSOLUTE));

		// customize the appearance of the graph.
		plot.getGraphWidget().getGridBackgroundPaint()
				.setColor(Color.parseColor("#2d353d"));
		plot.getGraphWidget().getDomainOriginLinePaint()
				.setColor(Color.parseColor("#b74d3f"));
		plot.getGraphWidget().getDomainOriginLinePaint().setStrokeWidth(7);
		plot.getGraphWidget().getRangeOriginLinePaint()
				.setColor(Color.parseColor("#b74d3f"));
		plot.getGraphWidget().getRangeOriginLinePaint().setStrokeWidth(7);

		// customize our domain/range labels
		plot.setDomainLabel("  Time (MM/DD/YY HH)  ");
		
		plot.getGraphWidget().getDomainOriginLabelPaint().setTextSize(30);
		plot.getGraphWidget().getDomainLabelPaint().setTextSize(30);
		plot.getDomainLabelWidget().getLabelPaint().setTextSize(30);
		plot.getDomainLabelWidget().setSize(
				new SizeMetrics(40, SizeLayoutType.ABSOLUTE, 500,
						SizeLayoutType.ABSOLUTE));

		plot.setRangeLabel("Open Count");
		plot.getGraphWidget().getRangeLabelPaint().setTextSize(30);
		plot.getGraphWidget().getRangeOriginLabelPaint().setTextSize(30);
		plot.getRangeLabelWidget().getLabelPaint().setTextSize(30);
		plot.getRangeLabelWidget().setSize(
				new SizeMetrics(400, SizeLayoutType.ABSOLUTE, 40,
						SizeLayoutType.ABSOLUTE));
		plot.setRangeValueFormat(new DecimalFormat("#"));

		plot.getLegendWidget().setTableModel(new DynamicTableModel(1, 2));
		plot.getLegendWidget().setSize(
				new SizeMetrics(150, SizeLayoutType.ABSOLUTE, 300,
						SizeLayoutType.ABSOLUTE));
		plot.getLegendWidget().setPositionMetrics(
				new PositionMetrics(60, XLayoutStyle.ABSOLUTE_FROM_RIGHT, 100,
						YLayoutStyle.ABSOLUTE_FROM_TOP,
						AnchorPosition.RIGHT_TOP));

		plot.getLegendWidget().getTextPaint().setTextSize(30);
		plot.getLegendWidget().setIconSizeMetrics(
				new SizeMetrics(30, SizeLayoutType.ABSOLUTE, 30,
						SizeLayoutType.ABSOLUTE));

		Paint bgPaint = new Paint();
		bgPaint.setColor(0x809a8e7e);
		bgPaint.setStyle(Paint.Style.FILL);
		bgPaint.setAlpha(140);
		plot.getLegendWidget().setBackgroundPaint(bgPaint);
 		plot.redraw();
		// format of how the values on x axis should look.
		plot.setDomainValueFormat(new Format() {
			Calendar cal = Calendar.getInstance();
			// TimeZone zone=TimeZone.getDefault();

			private SimpleDateFormat dateFormat = new SimpleDateFormat(
					"MM/dd/yy");
			private SimpleDateFormat hrFormat = new SimpleDateFormat("HH");

			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo,
					FieldPosition pos) {

				long timestamp = ((Number) obj).longValue();

				cal.setTimeInMillis(timestamp);
				Log.d("obj int", ("" + ((Number) obj).intValue()));
				Log.d("obj long", ("" + ((Number) obj).longValue()));
				Log.d("obj doubl", ("" + ((Number) obj).doubleValue()));
				Log.d("obj class", ("" + obj.getClass().getName()));

				Date date = (Date) cal.getTime();
				if(prevDate==null){
					prevDate=new Date();
					prevDate=date;
				}
				
				Log.d("date ", date.toString());
				if (count == 0 || prevDate.getDate()!=date.getDate()) {
					prevDate=date;
					count++;
					return new StringBuffer(dateFormat.format(date)+" "+hrFormat.format(date)+":00");
				} else {
					count++;
					return new StringBuffer(hrFormat.format(date)+":00");
				}
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;

			}
		});
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					String property = event.getPropertyName();
					if (property.equalsIgnoreCase("LOG_OPENCOUNT")) {
						Log.d("property change", "property changed");
						plotGraph(device);
					}

				}
			});
		}
	}

}
