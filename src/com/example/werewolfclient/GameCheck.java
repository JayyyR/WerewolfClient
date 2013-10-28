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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
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
	TextView header ;
	TextView daynite;
	EditText dayField;
	Button createGame;
	Button returnBut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_check);

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
				Intent myIntent = new Intent(GameCheck.this, MainActivity.class);
				//myIntent.putExtra("key", value); //Optional parameters
				GameCheck.this.startActivity(myIntent);
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
			super.onPostExecute(result);
			progressDialog.dismiss();
			//if thers already a game, play it, else make one
			Intent myIntent = new Intent(GameCheck.this, MainActivity.class);
			//myIntent.putExtra("key", value); //Optional parameters
			GameCheck.this.startActivity(myIntent);

		};
	}

}
