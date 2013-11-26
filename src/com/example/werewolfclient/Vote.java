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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class Vote extends AsyncTask<Void, Void, Void> {
	
	String id;
	ProgressDialog progressDialog;
	Context mContext;
	public Vote(String theId, Context context){
		Log.v("id", "id is: " + theId);
		id = theId;
		mContext = context;
	}
	
	@Override
	protected void onPreExecute(){
		Log.v("player1", "mcontext is: " + mContext);
		progressDialog= ProgressDialog.show(mContext, "Voting","Please Wait", true);
	
	}

	@Override
	protected Void doInBackground(Void... params) {
		HttpClient client = new DefaultHttpClient();

		HttpPost post = new HttpPost("http://jayyyyrwerewolf.herokuapp.com/players/vote");
		try {
			Log.v("player1", "adding vote to: " + id);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", id));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			client.execute(post);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@Override
	protected void onPostExecute(Void result){
		progressDialog.dismiss();
		
	}

}
