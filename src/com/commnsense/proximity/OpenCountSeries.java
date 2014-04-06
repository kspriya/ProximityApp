package com.commnsense.proximity;

import java.util.List;

import android.util.Log;

import com.androidplot.xy.XYSeries;

public class OpenCountSeries implements XYSeries {
    private List<OpenCountRecord> datasource;
    private String title;
 
    public OpenCountSeries(List<OpenCountRecord> datasource, String title) {
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
    	Log.d("x"," "+ (datasource.get(index).getTimeSinceY2K()));
      return (datasource.get(index).getTimeSinceY2K());
    }
 
    @Override
    public Number getY(int index) {
    	Log.d("Y", ""+(datasource.get(index).getOpenCount()));
    	return datasource.get(index).getOpenCount();
    }
    
    public long getFirstTime() {
    	if(datasource.size() <1) {
    		Log.d("FIRST TIME","0");
        	return 0;
    	}
    	else{	
    	return	(datasource.get(0).getTimeSinceY2K()-1000);
    	
    	}
    }
    
    public int getLastCount(){
    	if(datasource.size() <1) {
    		return 10;
    	}
    
    	Log.d("LAST COUNT",""+(datasource.get(datasource.size()-1).getOpenCount()));
    	return  (int)(datasource.get(datasource.size()-1).getOpenCount());
    	
    }
    public long getLastTime() {
    	if(datasource.size() <1) {
    		return 10;
    	}
    	Log.d("LAST TIME",""+(datasource.get(datasource.size()-1).getTimeSinceY2K() - datasource.get(0).getTimeSinceY2K()) );
    	return (datasource.get(datasource.size()-1).getTimeSinceY2K() );
    }
}
