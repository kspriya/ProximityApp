//package com.commnsense.proximity;
//
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.GraphViewSeries;
//import com.jjoe64.graphview.LineGraphView;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.widget.LinearLayout;
//class GraphViewData{
//	int x;
//	double y;
//	public GraphViewData(int x,double y) {
//	this.x=x;
//	this.y=y;
//	}}
//public class LogGraph extends Activity {
//
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.graph_activity);
//		GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
//			      new GraphViewData(1, 2.0d)
//			      , new GraphViewData(2, 1.5d)
//			      , new GraphViewData(3, 2.5d)
//			      , new GraphViewData(4, 1.0d)
//			});
//			 
//			GraphView graphView = new LineGraphView(
//			      this // context
//			      , "GraphViewDemo" // heading
//			);
//			graphView.addSeries(exampleSeries); // data
//			 
//			LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
//			layout.addView(graphView);
//	}
//
//}
