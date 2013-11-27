package com.example.werewolfclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameCheck extends Activity {

	String freq;
	boolean gameCheck;
	boolean isWolf;
	TextView header ;
	TextView daynite;
	EditText dayField;
	Button createGame;
	Button returnBut;
	String userID;
	boolean playerCheck;
	double wolfCount;
	double playerCount;
	boolean createdGame = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_check);

		Bundle bundle = getIntent().getExtras();
		userID = bundle.getString("login");
		header = (TextView) findViewById(R.id.create);
		daynite = (TextView) findViewById(R.id.day);
		dayField = (EditText) findViewById(R.id.dayField);
		createGame = (Button) findViewById(R.id.createGame);
		returnBut = (Button) findViewById(R.id.ret);

		Log.v("login", "in game checkk");
		CheckGame checker = new CheckGame();
		checker.execute();

		returnBut.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(GameCheck.this, Login.class);
				//myIntent.putExtra("key", value); //Optional parameters
				GameCheck.this.startActivity(myIntent);
			}


		});

		createGame.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				freq = (dayField.getText().toString());
				if(Integer.parseInt(freq) <0 || Integer.parseInt(freq)>10){
					Toast.makeText(getApplicationContext(), "Please enter number between 1 and 10", Toast.LENGTH_SHORT).show();
				}
				else{
					CreateGame task = new CreateGame();
					task.execute();
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_check, menu);
		return true;
	}

	private class CheckGame extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		//declare other objects as per your need
		@Override
		protected void onPreExecute()
		{
			progressDialog= ProgressDialog.show(GameCheck.this, "Checking for a Game","Please Wait", true);

			//do initialization of required objects objects here                
		};      
		@Override
		protected Void doInBackground(Void... params)
		{   
			Log.v("login", "in check game");
			HttpClient client = new DefaultHttpClient();
			String urlString = "http://jayyyyrwerewolf.herokuapp.com/games/check";
			HttpGet get = new HttpGet(urlString);
			try {

				HttpResponse response = client.execute(get);

				// Get the response
				BufferedReader rd = new BufferedReader
						(new InputStreamReader(response.getEntity().getContent()));

				String result = rd.readLine();

				gameCheck = Boolean.valueOf(result);


			} catch (IOException e) {
				e.printStackTrace();
			}

			//do loading operation here  
			return null;
		}       
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();
			//if thers already a game, play it, else make one
			if (gameCheck){
				CheckPlayer check = new CheckPlayer();
				check.execute();
			}
			else{

				header.setVisibility(View.VISIBLE);
				daynite.setVisibility(View.VISIBLE);
				dayField.setVisibility(View.VISIBLE);
				createGame.setVisibility(View.VISIBLE);
				returnBut.setVisibility(View.VISIBLE);



			}
		};
	}

	private class CreateGame extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		//declare other objects as per your need
		@Override
		protected void onPreExecute()
		{
			progressDialog= ProgressDialog.show(GameCheck.this, "Creating Game","Please Wait", true);

			//do initialization of required objects objects here                
		};      
		@Override
		protected Void doInBackground(Void... params)
		{   
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://jayyyyrwerewolf.herokuapp.com/games/create");
			try {

				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
				String date= sdf.format(cal.getTime());

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("dayNightFreq",
						freq));
				nameValuePairs.add(new BasicNameValuePair("createdDate",
						date));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = client.execute(post);



			} catch (IOException e) {
				e.printStackTrace();
			}

			//do loading operation here  
			return null;
		}       
		@Override
		protected void onPostExecute(Void result)
		{

			createdGame = true;
			super.onPostExecute(result);
			progressDialog.dismiss();
			//if thers already a game, play it, else make one
			CheckPlayer check = new CheckPlayer();
			check.execute();

		};
	}

	private class SetAdmin extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		//declare other objects as per your need
		@Override
		protected void onPreExecute()
		{
			progressDialog= ProgressDialog.show(GameCheck.this, "Setting Admin","Please Wait", true);

			//do initialization of required objects objects here                
		};      
		@Override
		protected Void doInBackground(Void... params)
		{   
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://jayyyyrwerewolf.herokuapp.com/players/setAdmin");
			try {


				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id",
						userID));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = client.execute(post);



			} catch (IOException e) {
				e.printStackTrace();
			}

			//do loading operation here  
			return null;
		}       
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();
			
			Intent myIntent = new Intent(GameCheck.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("login", userID);
			myIntent.putExtras(bundle); //Optional parameters
			GameCheck.this.startActivity(myIntent);
			
		};
	}

	private class CheckPlayer extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		//declare other objects as per your need
		@Override
		protected void onPreExecute()
		{
			progressDialog= ProgressDialog.show(GameCheck.this, "Checking Player","Please Wait", true);

			//do initialization of required objects objects here                
		};      
		@Override
		protected Void doInBackground(Void... params)
		{   
			Log.v("login", userID);
			HttpClient client = new DefaultHttpClient();
			String urlString = "http://jayyyyrwerewolf.herokuapp.com/players/" + userID;
			HttpGet get = new HttpGet(urlString);
			try {

				HttpResponse response = client.execute(get);
				Log.v("login", "http resp " + response);
				// Get the response
				BufferedReader rd = new BufferedReader
						(new InputStreamReader(response.getEntity().getContent()));

				String result = rd.readLine();
				Log.v("login", "http respstring " + result);

				//no player
				if (result.equals("<html>")){
					Log.v("login", "no player");
					playerCheck = false;
				}
				else{	//yes player
					playerCheck = true;
				}


			} catch (IOException e) {
				e.printStackTrace();
			}

			//do loading operation here  
			return null;
		}       
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();

			if (playerCheck){
				Intent myIntent = new Intent(GameCheck.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("login", userID);
				myIntent.putExtras(bundle); //Optional parameters
				GameCheck.this.startActivity(myIntent);
			}
			else{
				GetWolves getwolf = new GetWolves();
				getwolf.execute();
			}

		};
	}

	private class CreatePlayer extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		//declare other objects as per your need
		@Override
		protected void onPreExecute()
		{
			progressDialog= ProgressDialog.show(GameCheck.this, "Checking Player","Please Wait", true);

			//do initialization of required objects objects here                
		};      
		@Override
		protected Void doInBackground(Void... params)
		{   

			Log.v("login", "wolfCount " +  wolfCount);
			Log.v("login", "playcount " + playerCount);
			String isWolf = "false";
			//is Player a werewolf?
			if ((wolfCount/playerCount) < .3){
				Log.v("login", "division: " + (wolfCount/playerCount));
				isWolf = "true";
			}


			final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

			if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

				this.cancel(true);
				makeToast();
			}




			LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
			Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://jayyyyrwerewolf.herokuapp.com/players/insert");
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id",
						userID));
				nameValuePairs.add(new BasicNameValuePair("isDead",
						"false"));
				nameValuePairs.add(new BasicNameValuePair("lat",
						String.valueOf(latitude)));
				nameValuePairs.add(new BasicNameValuePair("lng",
						String.valueOf(longitude)));
				nameValuePairs.add(new BasicNameValuePair("userId",
						userID));
				nameValuePairs.add(new BasicNameValuePair("isWerewolf",
						isWolf));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = client.execute(post);



			} catch (IOException e) {
				e.printStackTrace();
			}

			//do loading operation here  
			return null;
		}       

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (createdGame){
				SetAdmin task = new SetAdmin();
				task.execute();
			}
			else{
				
				
				Intent myIntent = new Intent(GameCheck.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("login", userID);
				myIntent.putExtras(bundle); //Optional parameters
				GameCheck.this.startActivity(myIntent);
			}

		};
	}

	private class GetWolves extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		//declare other objects as per your need
		@Override
		protected void onPreExecute()
		{
			progressDialog= ProgressDialog.show(GameCheck.this, "Checking Player","Please Wait", true);

			//do initialization of required objects objects here                
		};      
		@Override
		protected Void doInBackground(Void... params)
		{   

			HttpClient client = new DefaultHttpClient();
			String urlString = "http://jayyyyrwerewolf.herokuapp.com/players/werewolves";
			HttpGet get = new HttpGet(urlString);
			try {

				HttpResponse response = client.execute(get);

				// Get the response
				BufferedReader rd = new BufferedReader
						(new InputStreamReader(response.getEntity().getContent()));

				String result = rd.readLine();

				Log.v("login", "wolf count is: "+ result);

				wolfCount = Double.parseDouble(result);
			} catch (IOException e) {
				e.printStackTrace();
			}

			HttpClient client2 = new DefaultHttpClient();
			String urlString2 = "http://jayyyyrwerewolf.herokuapp.com/players/all";
			HttpGet get2 = new HttpGet(urlString2);
			try {

				HttpResponse response = client2.execute(get2);

				// Get the response
				BufferedReader rd = new BufferedReader
						(new InputStreamReader(response.getEntity().getContent()));

				String result = rd.readLine();

				Log.v("login", "all count is: "+ result);

				playerCount = Double.parseDouble(result);
			} catch (IOException e) {
				e.printStackTrace();
			}


			//do loading operation here  
			return null;
		}       
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();

			CreatePlayer createPlayer = new CreatePlayer();
			createPlayer.execute();

		};
	}

	private void makeToast() {
		Toast.makeText(getApplicationContext(), "Please turn your gps on", Toast.LENGTH_SHORT).show();

	}


}
