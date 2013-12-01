package com.example.werewolfclient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Stats extends ListFragment{
	ArrayList<String> playerList = new ArrayList<String>();
	ArrayList<Long> scoreList = new ArrayList<Long>();
	String[] players;
	Long[] scores;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_playervotepage, container, false);
		getActivity().setTitle("Players");

		GetScores task = new GetScores();
		task.execute();

		return rootView;
	}
	
	private class GetScores extends AsyncTask<String, Void, String>{

		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute()
		{
			progressDialog= ProgressDialog.show(getActivity(), "Getting Scores","Please Wait", true);

			//do initialization of required objects objects here                
		}

		@Override
		protected String doInBackground(String... urls) {
			HttpClient client = new DefaultHttpClient();

			String urlString = "http://jayyyyrwerewolf.herokuapp.com/players/alive";
			HttpGet get = new HttpGet(urlString);
			try {

				HttpResponse response = client.execute(get);
				Log.v("test", "afetr response");
				// Get the response
				BufferedReader rd = new BufferedReader
						(new InputStreamReader(response.getEntity().getContent()));

				String player = rd.readLine();
				Log.v("player1", "rd realine is: " + player);
				//				player.replace("[", "");
				//				player.replace("]", "");
				//				String[] players = player.split("}");
				//				for (int i = 0; i < players.length; i++)
				//					Log.v("player1", ""+players[i]);

				JSONArray jsonarr = new JSONArray(player);


				Log.v("player1", "jsonArr is: " + jsonarr);
				for(int i = 0; i < jsonarr.length(); i++){
					Log.v("player1", "in for");
					JSONObject jsonobj = jsonarr.getJSONObject(i);


					String id=jsonobj.getString("id");

					
					String stringDate=jsonobj.getString("createdDate");
					Log.v("score", "stringdate is " + stringDate);
					Log.v("score", "date long convert: " +  Long.valueOf(stringDate).longValue());
					Log.v("score", "date long:         " +  new Date().getTime());
					long score = (new Date().getTime()) - (Long.valueOf(stringDate).longValue());
					Log.v("score", "score is: " + score);
				
					//if player is alive and not you add him to the list 
					playerList.add(id);
					scoreList.add(score/100);
	

				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			progressDialog.dismiss();
			players = playerList.toArray(new String[playerList.size()]);
			scores = scoreList.toArray(new Long[scoreList.size()]);
			setListAdapter(new StatAdapter(getActivity(), players, scores));
			super.onPostExecute(result);
		}



	}


}
