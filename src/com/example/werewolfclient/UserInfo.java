//package com.example.werewolfclient;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.view.View;
//
//
//public class UserInfo extends AsyncTask<Void, Void, Void> {
//
//	String userId;
//	double lat;
//	double lng;
//	boolean votedOn;
//	boolean isDead;
//	boolean isWerewolf;
//	Context context;
//	public UserInfo(String user, Context context2){
//		userId = user;
//		context = context2;
//	}
//	
//	public String getUser(){
//		return userId;
//	}
//	public double getLat(){
//		return lat;
//	}
//	public double getLng(){
//		return lng;
//	}
//	public boolean getVoted(){
//		return votedOn;
//	}
//	public boolean getDead(){
//		return isDead;
//	}
//	public boolean getWolf(){
//		return isWerewolf;
//	}
//	ProgressDialog progressDialog;
//	//declare other objects as per your need
//	@Override
//	protected void onPreExecute()
//	{
//		progressDialog= ProgressDialog.show(context, "Logging in","Please Wait", true);
//
//		//do initialization of required objects objects here                
//	};      
//	@Override
//	protected Void doInBackground(Void... params)
//	{   
//
//		HttpClient client = new DefaultHttpClient();
//		String urlString = "http://jayyyyrwerewolf.herokuapp.com/players/" + userId;
//		HttpGet get = new HttpGet(urlString);
//		try {
//
//			HttpResponse response = client.execute(get);
//
//			// Get the response
//			BufferedReader rd = new BufferedReader
//					(new InputStreamReader(response.getEntity().getContent()));
//
//			String result = rd.readLine();
//
//			Log.v("players", result);
//			
//			result = result.replace("{", "");
//			result = result.replace("}", "");
//			result = result.replace("\"", "");
//			Log.v("players", result);
//			String[] stuff = result.split(",");
//			for (String x:stuff){
//				Log.v("players", "." + x + ".");
//				String[] att = x.split(":");
//				
//				if (att[0].equals("lat")){
//					Log.v("players", "doing lat");
//					lat = Double.parseDouble(att[1]);
//				}
//				else if (att[0].equals("lng")){
//					Log.v("players", "doing lng");
//					lng = Double.parseDouble(att[1]);
//				}
//				else if (att[0].equals("votedOn")){
//					Log.v("players", "doing vote");
//					votedOn = Boolean.parseBoolean(att[1]);
//				}
//				else if (att[0].equals("dead")){
//					Log.v("players", "doing dead");
//					isDead = Boolean.parseBoolean(att[1]);
//				}
//				else if (att[0].equals("werewolf")){
//					Log.v("players", "doing wolf");
//					Log.v("players", "wolf att 1 is ." + att[1]+".");
//					Log.v("players", "wolf bool: " +Boolean.parseBoolean(att[1]));
//					isWerewolf = Boolean.parseBoolean(att[1]);
//				}
//			}
//
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		//do loading operation here  
//		return null;
//	}       
//	@Override
//	protected void onPostExecute(Void result)
//	{
//		super.onPostExecute(result);
//		progressDialog.dismiss();
//		
//		MainActivity.theRest();
//		Log.v("players", "userID: " + userId + "\nlat: " + lat + "\nlng: " + lng + "\nvoted: " + votedOn + "\ndead: " + isDead + "\nwolf: " + isWerewolf);
//		
//	};
//}