package com.example.werewolfclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayersPage extends ListFragment{

	ArrayList<String> playerList = new ArrayList<String>();
	ArrayList<Boolean> wolfList = new ArrayList<Boolean>();
	String[] players;
	Boolean[] wolves;
	String userID;
	boolean isWolf;
	boolean isDead;
	boolean voteCheckBool;
	Context con;
	int thePos= 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		con = getActivity();
		Bundle bundle = this.getArguments();
		userID = bundle.getString("login");
		isWolf = bundle.getBoolean("wolf");
		isDead = bundle.getBoolean("dead");
		View rootView = inflater.inflate(R.layout.fragment_playervotepage, container, false);
		getActivity().setTitle("Players");


		//change height so listivew isnt blocked by actionbar
		//		Log.v("player1", "action bar height is " + MainActivity.actionBarHeight);
		//		
		//		ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,rootView.getHeight()-MainActivity.actionBarHeight); 
		//		rootView.setLayoutParams(params);

		GetPlayers task = new GetPlayers();
		task.execute();


		return rootView;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		

		if (!isWolf){
			super.onListItemClick(l, v, position, id);

			thePos = position;
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

			// set title
			alertDialogBuilder.setTitle("Are you sure?");

			// set dialog message
			alertDialogBuilder
			.setMessage("Are you sure  you want to vote for " +  players[position])
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close

					hasVoted voteCheck = new hasVoted();
					voteCheck.execute();


				}
			})
			.setNegativeButton("No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
				}
			});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

		}

	}

	private class GetPlayers extends AsyncTask<String, Void, String>{

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

					String votedOn=jsonobj.getString("votes");

					String dead=jsonobj.getString("dead");
					
					String wolf=jsonobj.getString("werewolf");

					//if player is alive and not you add him to the list 
					if (dead.equals("false") && !id.equals(userID)){
						playerList.add(id);
						wolfList.add(Boolean.parseBoolean(wolf));
					}

					Log.v("player1", "id is: " + id + " voted on is: " + votedOn + " dead is: " + dead);

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
			wolves = wolfList.toArray(new Boolean[wolfList.size()]);
			setListAdapter(new PlayerAdapter(getActivity(), players, isWolf, wolves));
			super.onPostExecute(result);
		}



	}

	private class hasVoted extends AsyncTask<String, Void, String> {

		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute(){
			progressDialog= ProgressDialog.show(getActivity(), "Voting","Please Wait", true);
			Log.v("vote", "CHECKING VOTE FOR: " + userID);

		}

		@Override
		protected String doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();

			String urlString = "http://jayyyyrwerewolf.herokuapp.com/players/hasVoted?id=";
			urlString+=userID;
			HttpGet get = new HttpGet(urlString);
			try {

				HttpResponse response = client.execute(get);
				Log.v("test", "afetr response");
				// Get the response
				BufferedReader rd = new BufferedReader
						(new InputStreamReader(response.getEntity().getContent()));

				String hasVoted = rd.readLine();
				Log.v("test", "."+hasVoted+".");

				if (hasVoted.equals("true")){
					Log.v("vote", "hasvoted was true");
					voteCheckBool = true;
				}
				else{
					Log.v("vote", "hasvoted was false");
					voteCheckBool = false;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}


		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			progressDialog.dismiss();

			if(!voteCheckBool){
				Vote vote = new Vote(players[thePos], getActivity(), userID);
				vote.execute();
			}
			else{
				//dialog.cancel();
				Toast.makeText(getActivity(), "You already voted today", Toast.LENGTH_SHORT).show();;
			}

		}

	}

}
