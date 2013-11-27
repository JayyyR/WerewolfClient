package com.example.werewolfclient;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class News extends Fragment{

	String userID;
	boolean isWolf;
	boolean isDead;
	View theRootView;
	public News() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_news, container, false);
		getActivity().setTitle("News");
		theRootView = rootView;
		Bundle bundle = this.getArguments();
		userID = bundle.getString("login");
		isWolf = bundle.getBoolean("wolf");
		isDead = bundle.getBoolean("dead");

	

		return rootView;
	}

	public void setNews(String[] news){
		
		//fix this to grab from DB
		TextView newsText = (TextView) theRootView.findViewById(R.id.newsText);
		LinearLayout myLayout = (LinearLayout)theRootView.findViewById(R.id.newsFrag);
		if (!(news.length==0)){
			myLayout.removeAllViews();
			for (int i = 0; i< news.length; i++){

				final TextView rowTextView = new TextView(getActivity());

				// set some properties of rowTextView or something
				rowTextView.setText(news[i]);

				// add the textview to the linearlayout
				myLayout.addView(rowTextView);
			}
		}




	}




}