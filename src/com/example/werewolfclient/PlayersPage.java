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

import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class PlayersPage extends ListFragment{

	ArrayList<String> playerList = new ArrayList<String>();
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_playerpage, container, false);
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
		super.onListItemClick(l, v, position, id);
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
				//				player.replace("[", "");
				//				player.replace("]", "");
				//				String[] players = player.split("}");
				//				for (int i = 0; i < players.length; i++)
				//					Log.v("player1", ""+players[i]);

				JSONArray jsonarr = new JSONArray(player);

				for(int i = 0; i < jsonarr.length(); i++){

					JSONObject jsonobj = jsonarr.getJSONObject(i);

					
					String id=jsonobj.getString("id");
					
					String votedOn=jsonobj.getString("votedOn");
					
					String dead=jsonobj.getString("dead");
					
					//if player is alive add him to the list
					if (dead.equals("false"))
							playerList.add(id);
					
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
			String[] players = playerList.toArray(new String[playerList.size()]);
			setListAdapter(new PlayerAdapter(getActivity(), players));
			super.onPostExecute(result);
		}



	}

}
