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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;



public class Map extends Fragment {

	private class Player{
		String id;
		boolean isWolf;
		double lat;
		double lng;
	}
	// ...
	MapView m;
	ArrayList<Player> playerList = new ArrayList<Player>();

	String userID;
	boolean isWolf;
	boolean isDead;
	boolean voteCheckBool;
	boolean isDay;
	Location myLoc;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		// inflat and return the layout
		Bundle bundle = this.getArguments();
		userID = bundle.getString("login");
		isWolf = bundle.getBoolean("wolf");
		isDead = bundle.getBoolean("dead");
		isDay = bundle.getBoolean("day");

		View v = inflater.inflate(R.layout.fragment_map, container, false);
		m = (MapView) v.findViewById(R.id.mapView);
		m.onCreate(savedInstanceState);

		GetPlayers task = new GetPlayers();
		task.execute();


		return v;
	}

	public void setUpMap(){
		GoogleMap theMap = m.getMap();
		if (theMap == null)
			Log.v("mape", "was null");
		Log.v("mape", "the position: " + theMap.getCameraPosition());

		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE); 
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		myLoc = location;
		CameraUpdate center=
				CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));

		CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
		theMap.moveCamera(center);
		theMap.animateCamera(zoom);
		Log.v("mape", "the position: " + theMap.getCameraPosition());

		for (Player player : playerList){

			MarkerOptions options = new MarkerOptions()
			.position(new LatLng(player.lat, player.lng))
			.title(player.id);

			//if the player who's looking at map is wolf, show who is a villager or not
			if (isWolf){

				if (player.isWolf){
					options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
					options.snippet("Wolf");
				}
				else{
					options.icon(BitmapDescriptorFactory.fromResource(R.drawable.villager_icon)); 
					options.snippet("Villager");
				}
			}


			theMap.addMarker(options);	
		}

		if (isWolf && !isDay){
			theMap.setOnMarkerClickListener(new OnMarkerClickListener(){

				@Override
				public boolean onMarkerClick(Marker marker) {

					Log.v("position", "marker.getPosition.lat = " + marker.getPosition().latitude);
					Log.v("position", "myPos lat = " + myLoc.getLatitude());
					Log.v("position", "marker.getPosition.lng = " + marker.getPosition().longitude);
					Log.v("position", "myPos lng = " + myLoc.getLongitude());
					if (marker.getPosition().latitude <= myLoc.getLatitude()+.01 && 
							marker.getPosition().latitude >= myLoc.getLatitude()-.01 &&
							marker.getPosition().longitude <= myLoc.getLongitude()+.01 &&
							marker.getPosition().longitude >= myLoc.getLongitude()-.01){
						final String playerToKill = marker.getTitle();
						final Marker theMark = marker;
						// TODO Auto-generated method stub
						if (marker.getSnippet().equals("Villager")){

							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

							// set title
							alertDialogBuilder.setTitle("Kill?");

							// set dialog message
							alertDialogBuilder
							.setMessage("Do you want to kill " +  playerToKill + "?")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {

									Kill task = new Kill(playerToKill);
									task.execute();
									theMark.remove();

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
					return false;
				}

			});
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		m.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		m.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		m.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		m.onLowMemory();
	}

	private class Kill extends AsyncTask<Void, Void, Void> {

		ProgressDialog progressDialog;
		String playerToKill;
		public Kill(String id){
			playerToKill= id;
		}

		@Override
		protected void onPreExecute(){
			progressDialog= ProgressDialog.show(getActivity(), "Killing","Please Wait", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();

			HttpPost post = new HttpPost("http://jayyyyrwerewolf.herokuapp.com/players/kill");
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", playerToKill));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				client.execute(post);
			} catch (IOException e) {
				e.printStackTrace();
			}

			HttpPost post2 = new HttpPost("http://jayyyyrwerewolf.herokuapp.com/players/killLastNight");
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", playerToKill));
				post2.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				client.execute(post2);
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

					String wolf=jsonobj.getString("werewolf");

					String lat = jsonobj.getString("lat");

					String lng = jsonobj.getString("lng");

					//if player is alive and not you add him to the list
					if (!id.equals(userID)){
						Player listPlayer = new Player();
						listPlayer.id = id;
						listPlayer.isWolf = Boolean.parseBoolean(wolf);
						listPlayer.lat = Double.parseDouble(lat);
						listPlayer.lng = Double.parseDouble(lng);
						playerList.add(listPlayer);
					}

					Log.v("player1", "id is: " + id + " voted on is: " + votedOn);

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

			setUpMap();
		}



	}
}