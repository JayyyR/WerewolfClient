package com.example.werewolfclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] values;
	private final Boolean[] wolves;
	private final boolean isWolf;

	public PlayerAdapter(Context context, String[] values, boolean isWolf, Boolean[] wolves) {
		super(context, R.layout.player_list, values);
		this.context = context;
		this.values = values;
		this.isWolf=isWolf;
		this.wolves=wolves;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.player_list, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		textView.setText(values[position]);
		
		if (isWolf){
			
			if (wolves[position])
				imageView.setImageResource(R.drawable.ic_launcher);
			else 
				imageView.setImageResource(R.drawable.villager);
			
		}

		// Change icon based on name
		String s = values[position];

		return rowView;
	}
}