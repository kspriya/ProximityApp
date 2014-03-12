package com.commnsense.proximity;

import java.util.Date;

public class LocationAndTime 
{
	public String location;
	public Date timestamp;
	public float latitiude;
	public float longitude;
	public int time;
	public LocationAndTime(){
		
	}
	public LocationAndTime(float lat, float lng, int time) {
		// TODO Auto-generated constructor stub
		latitiude = lat;
		longitude = lng;
		this.time = time;
	}
}
