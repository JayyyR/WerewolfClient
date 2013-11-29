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

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Admin extends Fragment{

	String userID;
	boolean isWolf;
	boolean isDead;
	boolean isDay;
	int maxVotes=0;
	String playerToKill;
	String playerKillWolf;
	String theNews;
	public Admin() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_admin, container, false);
		getActivity().setTitle("Admin");



		Bundle bundle = this.getArguments();
		userID = bundle.getString("login");
		isWolf = bundle.getBoolean("wolf");
		isDead = bundle.getBoolean("dead");
		isDay = bundle.getBoolean("weather");
		
		Log.v("weather", "it is day: " + isDay);

		Button changeDayNight = (Button) rootView.findViewById(R.id.changebut);
		
		changeDayNight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (isDay){
					
					//count votes and kill one werewolf
					
					Toast.makeText(getActivity(), "Changed to night! Check the news!", Toast.LENGTH_SHORT).show();
					
					ResetKill killt= new ResetKill();
					killt.execute();
				}
				else{
					
					//kill the townspeople
					Toast.makeText(getActivity(), "Changed to day! Check the news!", Toast.LENGTH_SHORT).show();
					//temp
					GetKilledLastNight task = new GetKilledLastNight();
					task.execute();
				}
				
				
			}
		});


		return rootView;
	}
	
	private class GetPlayersDay extends AsyncTask<String, Void, String>{

		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute()
		{
			progressDialog= ProgressDialog.show(getActivity(), "Getting Players","Please Wait", true);

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

					String votes=jsonobj.getString("votes");

					String dead=jsonobj.getString("dead");
					
					String wolf=jsonobj.getString("werewolf");

					Log.v("stration", "votes in string is: " + votes);
					int numVotes = Integer.parseInt(votes);
					Log.v("stration", "num votes is: " + numVotes);
					if (numVotes>=maxVotes){
						playerToKill= id;
						maxVotes=numVotes;
						playerKillWolf = wolf;
					}

					Log.v("stration", "player to kill is: " + playerToKill + " maxVotes is: " + maxVotes);

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
			super.onPostExecute(result);
			
			killVoted kill = new killVoted();
			kill.execute();
		}



	}
	
	private class killVoted extends AsyncTask<Void, Void, Void> {

		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute(){
			progressDialog= ProgressDialog.show(getActivity(), "Voting","Please Wait", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			Log.v("stration", "killing player: " + playerToKill);
			HttpPost post = new HttpPost("http://jayyyyrwerewolf.herokuapp.com/players/kill");
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", playerToKill));
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
			
			String news;
			if (playerKillWolf.equals("true"))
				news = "Player " + playerToKill + " has been voted dead;He was a wolf";
			else
				news = "Player " + playerToKill + " has been voted dead;He was not a wolf";
			SetNews task = new SetNews(news);
			task.execute();

		}

	}
	
	private class GetKilledLastNight extends AsyncTask<Void, Void, Void> {

		ProgressDialog progressDialog;
		ArrayList<String> playerList = new ArrayList<String>();
		

		@Override
		protected void onPreExecute(){
			progressDialog= ProgressDialog.show(getActivity(), "Getting Killed","Please Wait", true);
		}

		@Override
		protected Void doInBackground(Void...params) {
			HttpClient client = new DefaultHttpClient();

			String urlString = "http://jayyyyrwerewolf.herokuapp.com/players/dead";
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
					Log.v("player1", "in fodfr");
					JSONObject jsonobj = jsonarr.getJSONObject(i);
					Log.v("player1", "in sadf");

					String id=jsonobj.getString("id");
					Log.v("player1", "in playerid");

					String votedOn=jsonobj.getString("votes");
					
					Log.v("player1", "in votes");

					String dead=jsonobj.getString("dead");
					
					Log.v("player1", "in dead");
					
					String lastNight=jsonobj.getString("killedLastNight");
					
					Log.v("player1", "in killled");
					
					Log.v("player1", "id is: " + id + " lastNight is: " + lastNight);

					//if player is alive and not you add him to the list 
					if (lastNight.equals("true")){
						Log.v("player1", "adding " + id);
						playerList.add(id);
					}

					

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
		protected void onPostExecute(Void result){
			progressDialog.dismiss();
			String news = "No News";
			StringBuilder newsBuilder = new StringBuilder();
			for(int i=0; i<playerList.size(); i++){
				newsBuilder.append(";Player " + playerList.get(i) + " was killed by wolves last night");
			}
			if (playerList.size() > 0)
				news = newsBuilder.toString();
			SetNews task = new SetNews(news);
			task.execute();

		}

	}
	
	private class SetNews extends AsyncTask<Void, Void, Void> {

		ProgressDialog progressDialog;
		String news;
		
		public SetNews(String news){
			this.news = news;
		}

		@Override
		protected void onPreExecute(){
			progressDialog= ProgressDialog.show(getActivity(), "Setting News","Please Wait", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://jayyyyrwerewolf.herokuapp.com/games/changenews");
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("news", news));
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
			
			ResetVotes task = new ResetVotes();
			task.execute();
			//add news
			

		}

	}
	
	private class ResetVotes extends AsyncTask<Void, Void, Void> {

		ProgressDialog progressDialog;
		String news;
	

		@Override
		protected void onPreExecute(){
			progressDialog= ProgressDialog.show(getActivity(), "Resetting Votes","Please Wait", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://jayyyyrwerewolf.herokuapp.com/players/resetvoting");
			HttpGet get2 = new HttpGet("http://jayyyyrwerewolf.herokuapp.com/games/changeday");
			try {
				client.execute(get);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				client.execute(get2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}


		@Override
		protected void onPostExecute(Void result){
			progressDialog.dismiss();
			
			
			//run gamecheck to switch day
			Intent myIntent = new Intent(getActivity(), GameCheck.class);
			Log.v("login", userID);
			Bundle bundle = new Bundle();
			bundle.putString("login", userID);
			myIntent.putExtras(bundle); //Optional parameters
			getActivity().startActivity(myIntent);


		}

	}
	
	private class ResetKill extends AsyncTask<Void, Void, Void> {

		ProgressDialog progressDialog;
		String news;
	

		@Override
		protected void onPreExecute(){
			progressDialog= ProgressDialog.show(getActivity(), "Resetting Kills","Please Wait", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://jayyyyrwerewolf.herokuapp.com/players/resetkill");
			try {
				client.execute(get);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}


		@Override
		protected void onPostExecute(Void result){
			progressDialog.dismiss();
			
		
			GetPlayersDay task = new GetPlayersDay();
			task.execute();
			

		}

	}





}
