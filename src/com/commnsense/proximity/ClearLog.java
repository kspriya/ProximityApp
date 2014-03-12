package com.commnsense.proximity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class ClearLog extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clearlog);
		// init example series data
		
		GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
		      new GraphViewData(1, 10)
		      , new GraphViewData(0, 5)
		      , new GraphViewData(1, 7)
		      , new GraphViewData(0, 8)
		});
		 
		GraphView graphView = new LineGraphView(
		      this // context
		      , "GraphViewDemo" // heading
		);
		graphView.addSeries(exampleSeries); // data
	//	graphView.setBackground(true);
		graphView.setViewPort(1, 50);
		graphView.setScalable(true);
		graphView.setScrollable(true);
		graphView.setShowLegend(true);  
		graphView.setLegendAlign(LegendAlign.MIDDLE);  
		graphView.setLegendWidth(600); 
		graphView.setVerticalLabels(new String[]{"Close","Open"});
		graphView.setHorizontalLabels(new String[]{"Time"});
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.YELLOW);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.RED);
		graphView.getGraphViewStyle().setNumHorizontalLabels(5);
		graphView.getGraphViewStyle().setNumVerticalLabels(2);
		graphView.getGraphViewStyle().setVerticalLabelsWidth(300);
//		graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
//			
//			@Override
//			public String formatLabel(double value, boolean isValueX) {
//				 if (isValueX) {
//			         if (value ==0 ) {
//			            return "close";
//			         } else if (value ==1) {
//			            return "open";
//			         } 
//			      }	return null;
//			}
//		});
//		
		graphView.setBackgroundColor(Color.rgb(80, 30, 30)); 
		LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
		layout.addView(graphView);
	}

}
