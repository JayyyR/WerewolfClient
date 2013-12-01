package com.example.werewolfclient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	String userID;
	double lat;
	double lng;
	boolean isDead;
	boolean isDay;
	boolean isWerewolf;
	boolean isVotedOn;
	boolean isAdmin;
	Context mContext;
	String[] menuItems;
	String[] adminItems;
	UserInfo user;
	TextView count;
	Button fdUp;
	Integer penalty = 5;
	ListView mDrawerList;
	DrawerLayout mDrawerLayout;
	ActionBarDrawerToggle mDrawerToggle;
	ImageView weather;
	public static FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_main);
		getActionBar().setTitle("Werewolf");

		weather = (ImageView) findViewById(R.id.dayNightImage);
	

		mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
		
		
		mContext=this;
		menuItems = getResources().getStringArray(R.array.menu_array);
		adminItems = getResources().getStringArray(R.array.admin_array);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, menuItems));
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


		Bundle bundle = getIntent().getExtras();
		userID = bundle.getString("login");
		
		user = new UserInfo();
		user.execute();
		

		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
				R.string.app_name,  /* "open drawer" description */
				R.string.hello_world  /* "close drawer" description */
				) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle("Werewolf");
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("Werewolf");
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		
	}
	
	
	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//New game
			if(position==0){ 
				Fragment fragment = new Profile();


				Bundle bundle = new Bundle();
				bundle.putString("login", userID);
				bundle.putBoolean("wolf", isWerewolf);
				bundle.putBoolean("vote", isVotedOn);
				bundle.putBoolean("dead", isDead);
				fragment.setArguments(bundle);
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.home_view, fragment).commit();



				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerLayout.closeDrawer(mDrawerList);
			}
			
			if (position == 1){
				Fragment fragment = new Stats();
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.home_view, fragment).commit();
				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerLayout.closeDrawer(mDrawerList);
				
			}
			
			if (position==2){
				Fragment fragment = new PlayersPage();
				Bundle bundle = new Bundle();
				bundle.putString("login", userID);
				bundle.putBoolean("wolf", isWerewolf);
				bundle.putBoolean("vote", isVotedOn);
				bundle.putBoolean("dead", isDead);
				bundle.putBoolean("day", isDay);
				fragment.setArguments(bundle);
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.home_view, fragment).commit();
				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerLayout.closeDrawer(mDrawerList);
			}
			
			if (position == 3){
				Fragment fragment = new Map();


				Bundle bundle = new Bundle();
				bundle.putString("login", userID);
				bundle.putBoolean("wolf", isWerewolf);
				bundle.putBoolean("vote", isVotedOn);
				bundle.putBoolean("dead", isDead);
				fragment.setArguments(bundle);
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.home_view, fragment).commit();



				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerLayout.closeDrawer(mDrawerList);
			
			}
			if (position==4){
				
				Intent myIntent = new Intent(MainActivity.this, Login.class);
				MainActivity.this.startActivity(myIntent);
			}
			
			if (position==5){
				Log.v("frag", "news");
				Fragment fragment = new News();


				Bundle bundle = new Bundle();
				bundle.putString("login", userID);
				bundle.putBoolean("wolf", isWerewolf);
				bundle.putBoolean("vote", isVotedOn);
				bundle.putBoolean("dead", isDead);
				bundle.putBoolean("weather", isDay);
				fragment.setArguments(bundle);
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.home_view, fragment).commit();



				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerLayout.closeDrawer(mDrawerList);
			}
			
			if (position==6){
				Fragment fragment = new Admin();


				Bundle bundle = new Bundle();
				bundle.putString("login", userID);
				bundle.putBoolean("wolf", isWerewolf);
				bundle.putBoolean("vote", isVotedOn);
				bundle.putBoolean("dead", isDead);
				bundle.putBoolean("weather", isDay);
				fragment.setArguments(bundle);
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.home_view, fragment).commit();



				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerLayout.closeDrawer(mDrawerList);
				
			}
		}
	}
	
	private class UserInfo extends AsyncTask<Void, Void, Void> {

		
		public UserInfo(){

		}
	
		ProgressDialog progressDialog;
		//declare other objects as per your need
		@Override
		protected void onPreExecute()
		{
			progressDialog= ProgressDialog.show(MainActivity.this, "Getting Your Information","Please Wait", true);

			//do initialization of required objects objects here                
		};      
		@Override
		protected Void doInBackground(Void... params)
		{   

			HttpClient client = new DefaultHttpClient();
			String urlString = "http://jayyyyrwerewolf.herokuapp.com/players/" + userID;
			HttpGet get = new HttpGet(urlString);
			try {

				HttpResponse response = client.execute(get);

				// Get the response
				BufferedReader rd = new BufferedReader
						(new InputStreamReader(response.getEntity().getContent()));

				String result = rd.readLine();

				Log.v("players", result);
				
				result = result.replace("{", "");
				result = result.replace("}", "");
				result = result.replace("\"", "");
				Log.v("players", result);
				String[] stuff = result.split(",");
				for (String x:stuff){
					Log.v("players", "." + x + ".");
					String[] att = x.split(":");
					
					if (att[0].equals("lat")){
						Log.v("players", "doing lat");
						lat = Double.parseDouble(att[1]);
					}
					else if (att[0].equals("lng")){
						Log.v("players", "doing lng");
						lng = Double.parseDouble(att[1]);
					}
					else if (att[0].equals("votedOn")){
						Log.v("players", "doing vote");
						isVotedOn = Boolean.parseBoolean(att[1]);
					}
					else if (att[0].equals("admin")){
						Log.v("players", "doing admin");
						isAdmin = Boolean.parseBoolean(att[1]);
					}
					else if (att[0].equals("dead")){
						Log.v("players", "doing dead");
						isDead = Boolean.parseBoolean(att[1]);
					}
					else if (att[0].equals("werewolf")){
						Log.v("players", "doing wolf");
						Log.v("players", "wolf att 1 is ." + att[1]+".");
						Log.v("players", "wolf bool: " +Boolean.parseBoolean(att[1]));
						isWerewolf = Boolean.parseBoolean(att[1]);
					}
				}
				
				


			} catch (IOException e) {
				e.printStackTrace();
			}
			//HttpClient client = new DefaultHttpClient();
			String urlString2 = "http://jayyyyrwerewolf.herokuapp.com/games/checkday";
			get = new HttpGet(urlString2);
			try {

				HttpResponse response = client.execute(get);

				// Get the response
				BufferedReader rd = new BufferedReader
						(new InputStreamReader(response.getEntity().getContent()));

				String result = rd.readLine();
				Log.v("bool", result);
				isDay = Boolean.parseBoolean(result);
				Log.v("bool", "after parse " +Boolean.parseBoolean(result));
				
			} catch (IOException e) {
				e.printStackTrace();
			}

			//do loading operation here  
			return null;
		}       
		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (!isDay)
				weather.setBackground(getResources().getDrawable(R.drawable.fullmoon));
			
			weather.setVisibility(View.VISIBLE);
			Log.v("players", "userID: " + userID + "\nlat: " + lat + "\nlng: " + lng + "\nvoted: " + isVotedOn + "\ndead: " + isDead + "\nwolf: " + isWerewolf);
			
			
			if(isAdmin){
				Log.v("admin", "is admin");
				
				mDrawerList.setAdapter(new ArrayAdapter<String>(mContext,
						R.layout.drawer_list_item, adminItems));
				
				
			}
		};
	}
	
	

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}










