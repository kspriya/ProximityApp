package com.commnsense.proximity.utils;
/**
 * @author kpriya
 * custom adapter to manage the list based device collection 
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.commnsense.proximity.Device;
import com.commnsense.proximity.R;

public class CustomAdapter extends BaseAdapter {
	private Context context;
	private List<Device> deviceRecordList = new ArrayList<Device>();
	

	public CustomAdapter(Context context,Collection<Device> deviceList) {
		deviceRecordList.addAll(deviceList);
		
		this.context = context;
	}

	@Override
	public int getCount() {
		return deviceRecordList.size();
	}

	@Override
	public Object getItem(int position) {
		return deviceRecordList.toArray()[position];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (ViewGroup) inflater.inflate(
					R.layout.device_listview, null, false);
		}

		Device device = (Device) getItem(pos);

		TextView t1 = (TextView) convertView.findViewById(R.id.item_name);
		t1.setText(device.name);

		TextView t2 = (TextView) convertView.findViewById(R.id.item_serial);
		if (device.getMacAddress() != null) {
			t2.setText('#' + device.getMacAddress().replace(":", ""));
		}
		
		((TextView) convertView.findViewById(R.id.battery_status)).setText(" 100");
		TextView t4 = (TextView) convertView.findViewById(R.id.sensor_status);
		t4.setText(Integer.toString(device.getRssi()));

		return convertView;
	}
}
