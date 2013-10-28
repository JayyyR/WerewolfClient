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
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

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
import android.widget.Toast;

public class Login extends Activity {

	String loginName;
	String loginPass;
	String checkedPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().setTitle("Werewolf");

		final EditText userName = (EditText) findViewById(R.id.userField);
		final EditText password = (EditText) findViewById(R.id.passField);

		Button login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// Add login stuff later
				loginName = userName.getText().toString();
				loginPass = password.getText().toString();

				Log.v("test", "before execute");
				login task = new login();
				task.execute();
				

			}


		});

		Button register = (Button) findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {


				Intent myIntent = new Intent(Login.this, Register.class);
				//myIntent.putExtra("key", value); //Optional parameters
				Login.this.startActivity(myIntent);

			}


		});
	}

	private class login extends AsyncTask<String, Void, String> {
		ProgressDialog progressDialog;
		//declare other objects as per your need
		@Override
		protected void onPreExecute()
		{
			progressDialog= ProgressDialog.show(Login.this, "Logging in","Please Wait", true);

			//do initialization of required objects objects here                
		};      
		@Override
		protected String doInBackground(String... urls) {
			Log.v("test", "in async");
			System.out.println("Hi");

			HttpClient client = new DefaultHttpClient();

			String urlString = "http://jayyyyrwerewolf.herokuapp.com/users/getPass?id=";
			urlString+=loginName;
			Log.v("test", "Url string is : " + urlString);
			HttpGet get = new HttpGet(urlString);
			try {

				HttpResponse response = client.execute(get);
				Log.v("test", "afetr response");
				// Get the response
				BufferedReader rd = new BufferedReader
						(new InputStreamReader(response.getEntity().getContent()));

				checkedPass = rd.readLine();
				Log.v("test", "."+checkedPass+".");

			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			Log.v("test", "login pass is: " + loginPass);
			Log.v("test", "check pass is: " + checkedPass);
			if (!loginPass.equals(checkedPass))
				Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
			else{
				Intent myIntent = new Intent(Login.this, GameCheck.class);
				Log.v("login", loginName);
				Bundle bundle = new Bundle();
				bundle.putString("login", loginName);
				myIntent.putExtras(bundle); //Optional parameters
				Login.this.startActivity(myIntent);
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");

	}

}
