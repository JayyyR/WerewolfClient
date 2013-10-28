package com.example.werewolfclient;


import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Profile extends Fragment{
	
	String userID;
	boolean isWolf;
	
	public Profile() {
	}
	
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
         getActivity().setTitle("Profile");
         
         Bundle bundle = this.getArguments();
         userID = bundle.getString("login");
         
         Log.v("inflate", userID); 

         TextView userName = (TextView) rootView.findViewById(R.id.usr);
         if (userName==null)
        	 Log.v("inflate", "user name is null");
         if (userID==null)
        	 Log.v("infalte", "userid is null");
         userName.setText(userID);
         
         TextView were = (TextView) rootView.findViewById(R.id.wereMes);
         if (!isWolf)
        	 were.setText("You're not a Werewolf");
         else{
        	 were.setText("You're a werewolf");
         }
    
         return rootView;
     }
	 
	 
	 

}
