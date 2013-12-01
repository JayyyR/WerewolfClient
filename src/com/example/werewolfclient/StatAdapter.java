package com.example.werewolfclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StatAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] values;
	private final Long[] scores;

	public StatAdapter(Context context, String[] values, Long[] scores) {
		super(context, R.layout.player_list, values);
		this.context = context;
		this.values = values;
		this.scores = scores;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.stat_list, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		TextView score = (TextView) rowView.findViewById(R.id.score);
		textView.setText(values[position]);
		score.setText(Long.toString(scores[position]));

		return rowView;
	}
}