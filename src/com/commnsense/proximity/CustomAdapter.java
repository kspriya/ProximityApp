package com.commnsense.proximity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	private Context context;

	public CustomAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return DeviceList.getInstance().getLocalDevices().size();
	}

	@Override
	public Object getItem(int position) {
		return DeviceList.getInstance().getLocalDevices().toArray()[position];
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

		setBatteryStatus(
				(TextView) convertView.findViewById(R.id.battery_status),
				device.getBatteryProgress(), convertView.getContext());

		TextView t4 = (TextView) convertView.findViewById(R.id.sensor_status);
		// t4.setTag(device.status);
		t4.setText(Integer.toString(device.getRssi()));

		return convertView;
	}

	private void setBatteryStatus(TextView battery, int batteryProgress,
			Context context) {

		if (batteryProgress < 50) {
			battery.setBackground(context.getResources().getDrawable(
					R.drawable.status_circle_red));
		} else {
			battery.setBackground(context.getResources().getDrawable(
					R.drawable.status_circle_green));
		}
	}
}
