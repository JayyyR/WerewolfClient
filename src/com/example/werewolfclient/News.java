package com.example.werewolfclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.R.color;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
	String theNews;
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

		getNews task = new getNews();
		task.execute();
		

		return rootView;
	}

	public void setNews(){

		LinearLayout myLayout = (LinearLayout)theRootView.findViewById(R.id.newsFrag);
		String[] newsItems = theNews.split(";");
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,    LinearLayout.LayoutParams.WRAP_CONTENT);
		myLayout.removeAllViews();
		int count=0;
		for (String x : newsItems){
			Log.v("news", "x before replace: " + x);
			x.replace(";", "");
			Log.v("news", "x after replace: " + x);
			TextView rowTextView = new TextView(getActivity());

			// set some properties of rowTextView or something
			rowTextView.setText(x);
			rowTextView.setLayoutParams(lp);
			rowTextView.setTextSize(50);
			rowTextView.setId(count);
			rowTextView.setTextColor(Color.RED);
			
			

			// add the textview to the linearlayout
			myLayout.addView(rowTextView);
			count++;
		}
	}



private class getNews extends AsyncTask<Void, Void, Void> {

	ProgressDialog progressDialog;

	@Override
	protected void onPreExecute(){
		progressDialog= ProgressDialog.show(getActivity(), "Getting News","Please Wait", true);
	}

	@Override
	protected Void doInBackground(Void... params) {
		Log.v("test", "in async");
		System.out.println("Hi");

		HttpClient client = new DefaultHttpClient();

		String urlString = "http://jayyyyrwerewolf.herokuapp.com/games/getnews";
		HttpGet get = new HttpGet(urlString);
		try {

			HttpResponse response = client.execute(get);
			Log.v("test", "afetr response");
			// Get the response
			BufferedReader rd = new BufferedReader
					(new InputStreamReader(response.getEntity().getContent()));

			theNews = rd.readLine();
			Log.v("news", "the news is: "+ theNews);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	protected void onPostExecute(Void result){
		progressDialog.dismiss();
		setNews();
	}

}




}