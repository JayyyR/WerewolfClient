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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {

	String firstName;
	String lastName;
	String userName;
	String password;
	String email;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		final EditText firstField = (EditText) findViewById(R.id.firstField);
		final EditText lastField = (EditText) findViewById(R.id.lastField);
		final EditText userField = (EditText) findViewById(R.id.userField);
		final EditText passField = (EditText) findViewById(R.id.passField);
		final EditText emailField = (EditText) findViewById(R.id.emailField);



		Button register = (Button) findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				firstName = firstField.getText().toString();
				lastName = lastField.getText().toString();
				userName = userField.getText().toString();
				password = Integer.toString(passField.getText().toString().hashCode());
				Log.v("password", "pass is " + password);
				email = emailField.getText().toString();

				if (firstName.isEmpty() || lastName.isEmpty() || userName.isEmpty() || password.isEmpty() || email.isEmpty())
					Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
				else{
					registerUser task = new registerUser();
					task.execute();

					Intent myIntent = new Intent(Register.this, Login.class);
					//myIntent.putExtra("key", value); //Optional parameters
					Register.this.startActivity(myIntent);

					Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	private class registerUser extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {

			System.out.println("Hi");

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://jayyyyrwerewolf.herokuapp.com/users/create");
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("firstName",
						firstName));
				nameValuePairs.add(new BasicNameValuePair("lastName",
						lastName));
				nameValuePairs.add(new BasicNameValuePair("id",
						userName));
				nameValuePairs.add(new BasicNameValuePair("hashedPassword",
						password));
				nameValuePairs.add(new BasicNameValuePair("imageURL",
						"null"));
				nameValuePairs.add(new BasicNameValuePair("email",
						email));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = client.execute(post);



			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
