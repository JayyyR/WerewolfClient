package com.example.werewolfclient;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {

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
				
				
				String loginName = userName.getText().toString();
				String loginPass = password.getText().toString();
				
				Intent myIntent = new Intent(Login.this, MainActivity.class);
				//myIntent.putExtra("key", value); //Optional parameters
				Login.this.startActivity(myIntent);
				
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
